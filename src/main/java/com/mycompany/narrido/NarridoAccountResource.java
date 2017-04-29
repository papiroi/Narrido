/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido;

import com.mycompany.narrido.helper.NarridoGeneric;
import com.mycompany.narrido.helper.NarridoGeneric.NarridoOperation;
import com.mycompany.narrido.helper.NarridoAuth;
import com.mycompany.narrido.pojo.NarridoAccessCode;
import com.mycompany.narrido.pojo.NarridoAccessCode_;
import com.mycompany.narrido.pojo.NarridoGroup;
import com.mycompany.narrido.pojo.NarridoMembership;
import com.mycompany.narrido.pojo.NarridoUser;
import com.mycompany.narrido.pojo.NarridoUser_;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.net.URI;
import javax.persistence.NoResultException;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author princessmelisa
 */
@Path("/account")
public class NarridoAccountResource {
    
    @Context
    ServletContext serv;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response user(@Context ContainerRequestContext crc) {
        String token = crc.getHeaderString(HttpHeaders.AUTHORIZATION);
        URI loginPath = URI.create(serv.getContextPath() + "/login.html");
        Jws<Claims> claims;
        String username = "";
        
        try {
            claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
        } catch (JwtException e) {
            return Response
                    .status(Status.FORBIDDEN)
                    .cookie()
                    .entity(e.getMessage())
                    .build();
            //should be 302
        }
        
        if (claims != null) username = claims.getBody().getSubject();
        
        NarridoUser user = NarridoGeneric
                .getSingle(NarridoUser.class, NarridoUser_.username, username);
        
        return Response.ok().entity(user).build();
    }

    @Path("/register")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response isValid(@QueryParam("code") String code) {

        NarridoAccessCode theCode = NarridoGeneric.getSingle(
                NarridoAccessCode.class,
                NarridoAccessCode_.accessCode,
                code
        );

        if (theCode == null) {
            return Response
                    .status(Status.NOT_FOUND)
                    .entity("Code does not exist.").build();
        }
        if (theCode.getUser() != null) {
            return Response
                    .status(Status.FORBIDDEN)
                    .entity("Code is already used.").build();
        }

        return Response.ok().entity(theCode).build();
    }

    
    /**
     * A very expensive registration process
     * @param code the code used by the user for registration
     * @return something...
     */
    @Path("/register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(RegisterData code) {
        NarridoUser user = code.getUser();
        NarridoGroup group = code.getGroup();
        if (user == null || group == null) {
            return Response.status(Status.FORBIDDEN).entity("Invalid request").build();
        }

        //Hash the password and generate hash salt
        String salt = NarridoAuth.generateSalt();
        String hashPass = NarridoAuth.sha256(user.getPassword(), salt);

        user.setSalt(salt);
        user.setPassword(hashPass);

        NarridoMembership membership = new NarridoMembership();
        membership.setGroup(group);
        membership.setUser(user);

        try {
            
            //get the code on db and check if code is already taken
            //a well meaning person can take over your registration code...
            NarridoAccessCode oldCode = NarridoGeneric.getSingle(NarridoAccessCode.class, NarridoAccessCode_.codeId, code.getCodeId());
            membership.setCode(oldCode);
            NarridoGeneric.saveThing(user, membership);
            if(oldCode.getUser() == null){ //make sure code on system is not taken
                if(!"student".equals(code.getType())) { //non-student codes are one-time use and therefore user data for code must be occupied
                    oldCode.setUser(user);
                    NarridoGeneric.doThing(NarridoOperation.UPDATE, oldCode);
                }
            }else{
                return Response.status(Status.FORBIDDEN)
                        .entity("Access code is already taken")
                        .build();
            }
        } catch (HibernateException e) {
            if (e instanceof ConstraintViolationException) {
                String message = e.getCause().getMessage();
                if (message.contains("id_number_UNIQUE")) {
                    return Response.status(Status.FORBIDDEN)
                            .entity("ID Number is already registered.")
                            .build();
                }
                if (message.contains("username_UNIQUE")) {
                    return Response.status(Status.FORBIDDEN)
                            .entity("Username is already registered.")
                            .build();
                }
            }
        }
        return Response.status(Status.OK).entity("Registration success!").build();
    }

    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginData login) {
        final Response INVALID = Response.status(Status.FORBIDDEN).entity("Username or password invalid!").build();
        
        NarridoUser user;
        
        try {
            user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, login.getUsername());
        } catch(NoResultException nre) {
            return INVALID;
        }
        
        //if user exists
        if (user != null) {
            String password = NarridoAuth.sha256(login.getPassword(), user.getSalt());
            if (password.equals(user.getPassword())) { //password matches
                
                if(!user.getIsConfirmed()) {
                    return Response.status(Status.FORBIDDEN)
                        .entity("Your account is not yet confirmed. Please contact your administrator for processing of your registration.")
                        .build();
                }
                
                String token = Jwts.builder()
                        .setSubject(user.getUsername())
                        .claim("name", user.getFirstName() + " " + user.getLastName())
                        .claim("role", user.getType())
                        .signWith (
                                SignatureAlgorithm.HS256,
                                user.getPassword().getBytes()
                        )
                        .compact();
                NewCookie cookie = new NewCookie("token", token, "/Narrido-1.0-SNAPSHOT", null, null, 60 * 60 * 3, false, false);
                return Response.ok()
                        .cookie(cookie)
                        .build();
            } else {
                return INVALID;
            }
        } else {
            return INVALID;
        }
    }

}

class RegisterData {
    private Integer codeId;
    private String accessCode;
    private String type;
    private NarridoUser user;
    private NarridoGroup group;

    public RegisterData() {
    }

    public Integer getCodeId() {
        return codeId;
    }

    public void setCodeId(Integer codeId) {
        this.codeId = codeId;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NarridoUser getUser() {
        return user;
    }

    public void setUser(NarridoUser user) {
        this.user = user;
    }

    public NarridoGroup getGroup() {
        return group;
    }

    public void setGroup(NarridoGroup group) {
        this.group = group;
    }
    
    
}

class LoginData {

    private String username;
    private String password;

    public LoginData() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
