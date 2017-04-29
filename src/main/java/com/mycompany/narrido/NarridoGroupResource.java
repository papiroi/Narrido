/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido;

import com.mycompany.narrido.dao.GroupDaoHb;
import com.mycompany.narrido.dao.UserDaoHb;
import com.mycompany.narrido.dao.ice.GroupDao;
import com.mycompany.narrido.dao.ice.UserDao;
import com.mycompany.narrido.helper.NarridoAuth;
import com.mycompany.narrido.helper.NarridoCode;
import com.mycompany.narrido.helper.NarridoGeneric;
import com.mycompany.narrido.helper.NarridoGeneric.NarridoOperation;
import static com.mycompany.narrido.helper.NarridoIO.NarridoIOException;
import com.mycompany.narrido.helper.NarridoIO;
import com.mycompany.narrido.helper.NarridoType;
import com.mycompany.narrido.pojo.NarridoAccessCode;
import com.mycompany.narrido.pojo.NarridoAccessCode_;
import com.mycompany.narrido.pojo.NarridoFile;
import com.mycompany.narrido.pojo.NarridoFile_;
import com.mycompany.narrido.pojo.NarridoGroup;
import com.mycompany.narrido.pojo.NarridoGroup_;
import com.mycompany.narrido.pojo.NarridoMembership;
import com.mycompany.narrido.pojo.NarridoMembership_;
import com.mycompany.narrido.pojo.NarridoPost;
import com.mycompany.narrido.pojo.NarridoUser;
import com.mycompany.narrido.pojo.NarridoUser_;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author princessmelisa
 */
@Path("/groups")
public class NarridoGroupResource {

    @Context
    ContainerRequestContext context;
    
    @Context
    ResourceContext resourceContext;

    final UserDao dao = UserDaoHb.getInstance();
    final GroupDao gdao = GroupDaoHb.getInstance();

    @GET
    @Path("/user/{set}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response groups(@PathParam("set") String set) {
        NarridoUser user = null;
        List<NarridoGroup> groups = null;

        if (!("owned".equals(set) || "joined".equals(set))) {
            return Response.status(Status.METHOD_NOT_ALLOWED).build();
        }

        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());

                groups = "owned".equals(set) ? dao.ownedGroups(user) : dao.joinedGroups(user);
            } else {
                return Response.status(Status.FORBIDDEN).entity("Login required").build();
            }
        } catch (JwtException e) {
            return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
        }
        return Response.ok().entity(groups).build();
    }

    @POST
    @Path("/join")
    public Response joinGroup(@FormParam("accessCode") String code) {
        NarridoUser user = null;
        NarridoAccessCode codeObj = null;

        try {
            codeObj = NarridoGeneric.getSingle(
                    NarridoAccessCode.class,
                    NarridoAccessCode_.accessCode,
                    code);
        } catch (NoResultException nre) {
            return Response.status(Status.NOT_FOUND)
                    .entity("No such access code")
                    .build();
        }

        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
        } catch (JwtException e) {
            return Response.status(Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        }

        if (Objects.equals(user.getUserid(), codeObj.getGroup().getOwner().getUserid())) {
            return Response.status(Status.FORBIDDEN)
                    .entity("You own this group; you cannot join as a member.")
                    .build();
        } else if (!user.getType().equals(codeObj.getType())) {
            return Response.status(Status.FORBIDDEN)
                    .entity("You are not allowed to use this access code.")
                    .build();
        }

        NarridoMembership membership = new NarridoMembership();
        membership.setGroup(codeObj.getGroup());
        membership.setUser(user);
        membership.setCode(codeObj);
        
        try {
            NarridoGeneric.doThing(NarridoOperation.SAVE, membership);
            if (!"student".equals(user.getType())) {
                codeObj.setUser(user);
                NarridoGeneric.doThing(NarridoOperation.UPDATE, codeObj);
            }
        } catch (HibernateException he) {
            if (he instanceof ConstraintViolationException) {
                return Response
                        .status(Status.FORBIDDEN)
                        .entity("Cannot join: You are already a member of this group.")
                        .build();
            }
        }

        return Response
                .ok("Join successful. Please await the confirmation of your membership by the group admin.")
                .build();
    }

    @POST
    public Response newGroup(@FormParam("groupTitle") String groupName, @FormParam("groupType") String groupType) {
        NarridoUser user = null;
        String[] acceptableTypes = {
            NarridoType.STUDENT,
            NarridoType.FACULTY,
            NarridoType.IT_STAFF,
            NarridoType.D_HEAD
        };

        List<String> types = Arrays.asList(acceptableTypes);

        if (!types.contains(groupType)) {
            return Response.status(Status.FORBIDDEN)
                    .entity("Invalid type!")
                    .build();
        } else if (groupName.length() < 4) {
            return Response.status(Status.FORBIDDEN)
                    .entity("Group name must be atleast 5 characters!")
                    .build();
        }

        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
        } catch (JwtException e) {
            return Response.status(Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        }

        NarridoGroup group = new NarridoGroup();
        group.setGroupName(groupName);
        group.setType(groupType);
        group.setOwner(user);

        NarridoAccessCode code = new NarridoAccessCode();
        code.setAccessCode(NarridoCode.getCode());
        code.setGroup(group);
        code.setType(groupType);

        NarridoGeneric.saveThing(group, code);

        return Response
                .ok("Group created! The joining code is: " + code.getAccessCode())
                .build();
    }

    //@MembersOnly
    @GET
    @Path("/members/{gid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response membership(@PathParam("gid") Integer groupId) {
        NarridoUser user = null;
        List<NarridoMembership> memz = new ArrayList<>();
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
        } catch (JwtException e) {
            return Response.status(Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        }

        NarridoGroup group = NarridoGeneric.getSingle(NarridoGroup.class, NarridoGroup_.groupId, groupId);
        memz = gdao.membership(group);
        boolean isMember = false;

        for (NarridoMembership mem : memz) {
            if (Objects.equals(mem.getUser().getUserid(), user.getUserid()) && mem.getConfirmed()) {
                isMember = true;
                break;
            }
        }

        if (!(Objects.equals(user.getUserid(), group.getOwner().getUserid()) || isMember)) {
            return Response.status(Status.FORBIDDEN)
                    .entity("Only members are allowed to this resource.")
                    .build();
        }

        return Response.ok().entity(memz).build();
    }

    @GET
    @Path("/codes/{gid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response accessCodes(@PathParam("gid") Integer groupId) {
        NarridoUser user = null;
        NarridoGroup group = null;
        List<NarridoAccessCode> codez = new ArrayList<>();
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
        } catch (JwtException e) {
            return Response.status(Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        }

        try {
            group = NarridoGeneric.getSingle(NarridoGroup.class, NarridoGroup_.groupId, groupId);
        } catch (NoResultException e) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Group does not exist.")
                    .build();
        }

        if (!Objects.equals(user.getUserid(), group.getOwner().getUserid())) {
            return Response.status(Status.UNAUTHORIZED)
                    .entity("You are not allowed to access this resource.")
                    .build();
        } else {
            codez = gdao.accessCode(group);
        }

        return Response.ok(codez).build();
    }

    @POST
    @Path("/codes/{gid}")
    public Response createCode(@PathParam("gid") Integer groupId, @FormParam("type") String type) {
        NarridoUser user = null;
        NarridoGroup group = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
        } catch (JwtException e) {
            return Response.status(Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        }

        try {
            group = NarridoGeneric.getSingle(NarridoGroup.class, NarridoGroup_.groupId, groupId);
        } catch (NoResultException e) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Group does not exist.")
                    .build();
        }

        String theCode = null;
        if (!Objects.equals(user.getUserid(), group.getOwner().getUserid())) {
            return Response.status(Status.UNAUTHORIZED)
                    .entity("You are not allowed to access this resource.")
                    .build();
        } else {
            theCode = NarridoCode.getCode();

            NarridoAccessCode accessCode = new NarridoAccessCode();
            accessCode.setGroup(group);
            accessCode.setType(type);
            accessCode.setAccessCode(theCode);

            NarridoGeneric.saveThing(accessCode);
        }

        return Response.ok("Code created: " + theCode).build();
    }

    @PUT
    @Path("/members/reject")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response rejectMembership(MembershipData data) {
        NarridoUser user = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
        } catch (JwtException e) {
            return Response.status(Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        }

        try {
            NarridoMembership membership = NarridoGeneric.getSingle(
                    NarridoMembership.class,
                    NarridoMembership_.membershipId,
                    data.getMembershipId());
            NarridoAccessCode code = membership.getCode();
            code.setUser(null);

            NarridoGroup group = membership.getGroup();

            boolean isOwner = Objects.equals(user.getUserid(), group.getOwner().getUserid());

            if (!isOwner) {
                return Response.status(Status.UNAUTHORIZED)
                        .entity("You cannot perform this operation.")
                        .build();
            }

            NarridoGeneric.doThing(NarridoOperation.UPDATE, code);
            NarridoGeneric.doThing(NarridoOperation.DELETE, membership);
            if (!membership.getUser().getIsConfirmed()) {
                NarridoGeneric.doThing(NarridoOperation.DELETE, membership.getUser());
            }
        } catch (NoResultException e) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Data does not exist.")
                    .build();
        }

        return Response.ok("Reject operation successful.").build();
    }

    @PUT
    @Path("/members/accept")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response acceptMembership(MembershipData data) {
        NarridoUser user = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
        } catch (JwtException e) {
            return Response.status(Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        }

        try {
            NarridoMembership membership = NarridoGeneric.getSingle(
                    NarridoMembership.class,
                    NarridoMembership_.membershipId,
                    data.getMembershipId());
            NarridoAccessCode code = membership.getCode();
            NarridoGroup group = membership.getGroup();

            boolean isOwner = Objects.equals(user.getUserid(), group.getOwner().getUserid());

            if (!isOwner) {
                return Response.status(Status.UNAUTHORIZED)
                        .entity("You cannot perform this operation.")
                        .build();
            }

            membership.setConfirmed(Boolean.TRUE);

            if (!"student".equals(user.getType())) {
                NarridoGeneric.doThing(NarridoOperation.UPDATE, code);
            }
            NarridoGeneric.doThing(NarridoOperation.UPDATE, membership);
            if (!membership.getUser().getIsConfirmed()) {
                membership.getUser().setIsConfirmed(Boolean.TRUE);
                NarridoGeneric.doThing(NarridoOperation.UPDATE, membership.getUser());
            }
        } catch (NoResultException e) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Data does not exist.")
                    .build();
        }

        return Response.ok("User successfully accepted into group!").build();
    }

    @POST
    @Path("/posts/{gid}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response submitPost(
            @PathParam("gid") Integer groupId,
            @FormDataParam("postContent") String postContent,
            @FormDataParam("file") InputStream file,
            @FormDataParam("file") FormDataContentDisposition fileDescription) {

        //super epic lengthy convoluted authentication process 
        NarridoUser user = null;
        NarridoGroup group = null;
        List<NarridoGroup> groupzUser = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
                group = NarridoGeneric.getSingle(NarridoGroup.class, NarridoGroup_.groupId, groupId);
                groupzUser = dao.joinedGroups(user);
            } else {
                return Response.status(Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
        } catch (JwtException | NoResultException e) {
            return Response.status(Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        }

        boolean isMember = false;
        boolean isOwner = Objects.equals(user.getUserid(), group.getOwner().getUserid());

        for (NarridoGroup gp : groupzUser) {
            if (Objects.equals(gp.getGroupId(), group.getGroupId())) {
                isMember = true;
                break;
            }
        }

        if (!(isMember || isOwner)) {
            return Response.status(Status.UNAUTHORIZED)
                    .entity("Operation not allowed; Members only")
                    .build();
        }
        //convoluted mess ends here

        NarridoPost post = new NarridoPost();
        post.setUser(user);
        post.setGroup(group);
        post.setPostContent(postContent);
        post.setDate(new Date());

        NarridoFile narridoFile = null;

        try {
            String url = "group/" + groupId.toString() + "/";
            String dir = NarridoIO.DIR + url;
            NarridoIO.saveToDisk(file, dir, fileDescription.getFileName());

            narridoFile = new NarridoFile();
            narridoFile.setDateUploaded(new Date());
            narridoFile.setFileUrl("http://localhost:8080/files/" + url + fileDescription.getFileName());
            narridoFile.setGroup(group);
            narridoFile.setUploader(user);
            narridoFile.setPost(post);
            narridoFile.setFileName(fileDescription.getFileName());
            narridoFile.setFileType("file");
            
        } catch (NarridoIOException nioe) {
            System.err.println(nioe.getMessage());
        }
        
        //save the post unceremoniously
        if(narridoFile != null) {
            NarridoGeneric.saveThing(post, narridoFile);
            post.setFiles(narridoFile);
        } else {
            NarridoGeneric.saveThing(post);
        }

        //broadcast to members...
        NarridoPushResource npr = resourceContext.getResource(NarridoPushResource.class);
        List<NarridoMembership> members = gdao.membership(group);
        for(NarridoMembership mem : members) {
            npr.send(mem.getUser().getUsername(), post);
        }
        //...and to the owner too!
        npr.send(group.getOwner().getUsername(), post);

        //return ok response
        //...unceremoniously
        return Response.ok().build();
    }

    @GET
    @Path("/posts/{gid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response posts(@PathParam("gid") Integer groupId) {
        
        //extra epic convoluted authentication operation begins here
        NarridoUser user = null;
        NarridoGroup group = null;
        List<NarridoGroup> groupzUser = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
                group = NarridoGeneric.getSingle(NarridoGroup.class, NarridoGroup_.groupId, groupId);
                groupzUser = dao.joinedGroups(user);
            } else {
                return Response.status(Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
        } catch (JwtException | NoResultException e) {
            return Response.status(Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        }

        boolean isMember = false;
        boolean isOwner = Objects.equals(user.getUserid(), group.getOwner().getUserid());

        for (NarridoGroup gp : groupzUser) {
            if (Objects.equals(gp.getGroupId(), group.getGroupId())) {
                isMember = true;
                break;
            }
        }

        if (!(isMember || isOwner)) {
            return Response.status(Status.UNAUTHORIZED)
                    .entity("Operation not allowed; Members only")
                    .build();
        }
        //extra epic convoluted mess ends here... start assembling the posts
        
        List<NarridoPost> posts = gdao.posts(group);
        return Response.ok(posts).build();
    }
    
    @GET
    @Path("/files/{gid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response files(@PathParam("gid") Integer groupId) {
        //extra epic convoluted authentication operation begins here
        NarridoUser user = null;
        NarridoGroup group = null;
        List<NarridoGroup> groupzUser = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
                group = NarridoGeneric.getSingle(NarridoGroup.class, NarridoGroup_.groupId, groupId);
                groupzUser = dao.joinedGroups(user);
            } else {
                return Response.status(Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
        } catch (JwtException | NoResultException e) {
            return Response.status(Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        }

        boolean isMember = false;
        boolean isOwner = Objects.equals(user.getUserid(), group.getOwner().getUserid());

        for (NarridoGroup gp : groupzUser) {
            if (Objects.equals(gp.getGroupId(), group.getGroupId())) {
                isMember = true;
                break;
            }
        }

        if (!(isMember || isOwner)) {
            return Response.status(Status.UNAUTHORIZED)
                    .entity("Operation not allowed; Members only")
                    .build();
        }
        //extra epic convoluted mess ends here... start assembling the posts
        
        List<NarridoFile> files = NarridoGeneric.getList(NarridoFile.class, NarridoFile_.group, group);
        return Response.ok(files).build();
    }

}

class MembershipData {

    private Integer membershipId;

    public MembershipData() {
    }

    public Integer getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Integer membershipId) {
        this.membershipId = membershipId;
    }

}
