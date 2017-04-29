/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido;

import com.mycompany.narrido.dao.UserDaoHb;
import com.mycompany.narrido.dao.ice.UserDao;
import com.mycompany.narrido.helper.NarridoAuth;
import com.mycompany.narrido.helper.NarridoGeneric;
import com.mycompany.narrido.helper.NarridoIO;
import com.mycompany.narrido.pojo.NarridoFile;
import com.mycompany.narrido.pojo.NarridoFile_;
import com.mycompany.narrido.pojo.NarridoUser;
import com.mycompany.narrido.pojo.NarridoUser_;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.HibernateException;

/**
 *
 * @author princessmelisa
 */
@Path("/files")
public class NarridoFileIOResource {
    
    @Context
    ContainerRequestContext context;
    
    private UserDao dao = UserDaoHb.getInstance();
    
    /**
     * Upload files to group
     * @param gid the group id
     * @param istream the file itself
     * @param fileDescription the file meta-data
    */
    @POST
    @Path("/group/{gid}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadToGroup(@PathParam("gid")String gid,
            @FormDataParam("file") InputStream istream,
            @FormDataParam("file") FormDataContentDisposition fileDescription) {
        String dir = NarridoIO.DIR + "group/" + gid + "/";
        
        
        //TODO check if member of group
        try {
            save(istream, fileDescription, dir);
        } catch(NarridoIOException ne) {
            return Response
                    .status(Status.FORBIDDEN)
                    .entity(ne.getMessage())
                    .build();
        }
        
        return Response.ok().entity("Upload OK!").build();
    }
    
    @POST
    @Path("/user")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadToPersonal(
            @FormDataParam("file") FormDataContentDisposition fileDescription,
            @FormDataParam("file") InputStream file){
        
        NarridoUser user = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if(token != null) {
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
        
        NarridoFile narridoFile = new NarridoFile();
        try {
            String url = "user/" + user.getUserid().toString() + "/";
            String dir = NarridoIO.DIR + url;
            NarridoIO.saveToDisk(file, dir, fileDescription.getFileName());

            narridoFile = new NarridoFile();
            narridoFile.setDateUploaded(new Date());
            narridoFile.setFileUrl("http://localhost:8080/files/" + url + fileDescription.getFileName());
            narridoFile.setUploader(user);
            narridoFile.setFileName(fileDescription.getFileName());
            narridoFile.setFileType("file");
            
            NarridoGeneric.saveThing(narridoFile);
        } catch(NarridoIO.NarridoIOException | HibernateException ne) {
            return Response
                    .status(Status.FORBIDDEN)
                    .entity(ne.getMessage())
                    .build();
        }
        
        return Response.ok("File upload successful!").build();
    }
    
    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonal(){
        NarridoUser user = null;
        List<NarridoFile> files = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if(token != null) {
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
        
        files = dao.myFiles(user);
        
        return Response.ok(files).build();
    }
    
    @GET
    @Path("/qr")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQR(){
        NarridoUser user = null;
        List<NarridoFile> files = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if(token != null) {
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
        
        files = NarridoGeneric.getList(NarridoFile.class, NarridoFile_.fileType, "qr");
        
        return Response.ok(files).build();
    }
    
    @GET
    @Path("/reports")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReports(){
        NarridoUser user = null;
        List<NarridoFile> files = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if(token != null) {
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
        
        files = NarridoGeneric.getList(NarridoFile.class, NarridoFile_.fileType, "report");
        
        return Response.ok(files).build();
    }
    
    private void save(InputStream istream, FormDataContentDisposition fdcp, String dir) throws NarridoIOException{
        String fileName = fdcp.getFileName();
        String groupDir = dir; 
        
        java.nio.file.Path path = Paths.get(groupDir);
        
        if(!Files.exists(path)){
            try{
                Files.createDirectories(path);
            }catch(IOException ioe){
                ioe.printStackTrace(System.err);
            }
        }
        

        //overwrite duplicate file
        File theFile = new File(groupDir + fileName);
        
        if(theFile.exists()){
            theFile.delete();
            System.out.println("Duplicate; deleted!");
        }
        
        //begin Save
        //NarridoIO.saveToDisk(istream, groupDir + fileName);
    }
        
}

class NarridoIOException extends Exception {

    public NarridoIOException() {
    }
    
    public NarridoIOException(String message) {
        super(message);
    }
    
}