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
import com.mycompany.narrido.helper.NarridoGeneric.NarridoOperation;
import com.mycompany.narrido.helper.NarridoIO;
import com.mycompany.narrido.helper.NarridoReport;
import com.mycompany.narrido.helper.NarridoType;
import com.mycompany.narrido.pojo.NarridoAuditTrail;
import com.mycompany.narrido.pojo.NarridoDailyMonitoring;
import com.mycompany.narrido.pojo.NarridoDailyMonitoring_;
import com.mycompany.narrido.pojo.NarridoFile;
import com.mycompany.narrido.pojo.NarridoJob;
import com.mycompany.narrido.pojo.NarridoJob_;
import com.mycompany.narrido.pojo.NarridoLaboratory;
import com.mycompany.narrido.pojo.NarridoLaboratory_;
import com.mycompany.narrido.pojo.NarridoPc;
import com.mycompany.narrido.pojo.NarridoPc_;
import com.mycompany.narrido.pojo.NarridoUser;
import com.mycompany.narrido.pojo.NarridoUser_;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sf.jasperreports.engine.JRException;
import org.hibernate.HibernateException;

/**
 *
 * @author princessmelisa
 */
@Path("/it")
public class NarridoITResource {
    
    @Context
    ContainerRequestContext context;
    
    @Context
    ResourceContext rcontext;
    
    private UserDao dao = UserDaoHb.getInstance();
    
    @GET
    @Path("/monitoring/{labId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewMonitoring(@PathParam("labId") Integer labId) {
        NarridoUser user = null;
        NarridoLaboratory laboratory = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
            
            laboratory = NarridoGeneric.getSingle(NarridoLaboratory.class, NarridoLaboratory_.labId, labId);
        } catch (JwtException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        } catch (NoResultException ne) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No laboratory found")
                    .build();
        }
        
        List<NarridoDailyMonitoring> nm = NarridoGeneric.getList(NarridoDailyMonitoring.class, NarridoDailyMonitoring_.laboratory, laboratory);
        
        return Response.ok(nm).build();
    }
    
    @POST
    @Path("/monitoring/{labId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMonitoring(@PathParam("labId") Integer labId, final NarridoDailyMonitoring monitoring) {
        NarridoUser user = null;
        NarridoLaboratory laboratory = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
            
            laboratory = NarridoGeneric.getSingle(NarridoLaboratory.class, NarridoLaboratory_.labId, labId);
            monitoring.setUser(user);
            monitoring.setLaboratory(laboratory);
            
            NarridoGeneric.saveThing(monitoring);
            
            //super epic efficient getting of users
            NarridoPushResource npr = rcontext.getResource(NarridoPushResource.class);
            npr.sendToEveryone(monitoring);
            
            //put it to log
            npr.sendToEveryone(
                    NarridoGeneric.logTrail(user, "Added monitoring entry for " + laboratory.getLabDescription())
            );
            
            return Response.ok("Monitoring saved!").build();
        } catch (JwtException | HibernateException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(NarridoGeneric.getStackTrace(e))
                    .build();
        } catch (NoResultException ne) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Laboratory not found")
                    .build();
        }
        
    }
    
    @GET
    @Path("/pc/{labId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewPc(@PathParam("labId") Integer labId) {
        NarridoUser user = null;
        NarridoLaboratory laboratory = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
            
            laboratory = NarridoGeneric.getSingle(NarridoLaboratory.class, NarridoLaboratory_.labId, labId);
        } catch (JwtException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        } catch (NoResultException ne) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No laboratory found")
                    .build();
        }
        
        List<NarridoPc> pcs = NarridoGeneric.getList(NarridoPc.class, NarridoPc_.laboratory, laboratory);
        
        return Response.ok(pcs).build();
    }
    
    @POST
    @Path("/pc/{labId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPc(@PathParam("labId") Integer labId, final NarridoPc pc) {
        NarridoUser user = null;
        NarridoLaboratory laboratory = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
            
            laboratory = NarridoGeneric.getSingle(NarridoLaboratory.class, NarridoLaboratory_.labId, labId);
            pc.setLaboratory(laboratory);
            
            //super epic efficient getting of users
            
            pc.setId(NarridoGeneric.saveThing(pc));
            
            String url = "http://localhost:8080/files/qr/" + NarridoQR.getQr(pc);
            NarridoFile nf = new NarridoFile();
            nf.setDateUploaded(new Date());
            nf.setFileName(pc.getPcName() + " " + pc.getLaboratory().getLabDescription() + ".png");
            nf.setFileUrl(url);
            nf.setUploader(user);
            nf.setFileType("qr");
            
            NarridoGeneric.saveThing(nf);
            
            NarridoPushResource npr = rcontext.getResource(NarridoPushResource.class);
            npr.sendToEveryone(pc);
            npr.sendToEveryone(nf);
            npr.sendToEveryone(
                    NarridoGeneric.logTrail(user, "Added new PC: " + pc.getPcNumber() + " at " + laboratory.getLabDescription())
            );
            
            return Response.ok("PC saved!").build();
        } catch (JwtException | HibernateException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(NarridoGeneric.getStackTrace(e))
                    .build();
        } catch (NoResultException ne) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Laboratory not found")
                    .build();
        }
    }
    
    @PUT
    @Path("/pc")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePc(final PcUpdateData pud) {
        NarridoUser user = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
            
            
            //super epic efficient getting of users
            
            NarridoPc pc = pud.getPc();
            
            NarridoGeneric.doThing(NarridoOperation.UPDATE, pud.getPc());
            
            String url = "http://localhost:8080/files/qr/" + NarridoQR.getQr(pc);
            NarridoFile nf = new NarridoFile();
            nf.setDateUploaded(new Date());
            nf.setFileName(pc.getPcName() + " " + pc.getLaboratory().getLabDescription() + "(UPDATED).png");
            nf.setFileUrl(url);
            nf.setUploader(user);
            nf.setFileType("qr");
            
            NarridoGeneric.saveThing(nf);
            
            NarridoPushResource npr = rcontext.getResource(NarridoPushResource.class);
            npr.sendToEveryone(nf);
            npr.sendToEveryone(
                    NarridoGeneric.logTrail(user, "Updated PC: " + pc.getPcNumber() + "(" + pud.getRemarks() + ")")
            );
            
            return Response.ok("PC info updated!").build();
        } catch (JwtException | HibernateException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(NarridoGeneric.getStackTrace(e))
                    .build();
        } catch (NoResultException ne) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ne.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/pc/{labId}/report")
    public Response getReport(@PathParam("labId") Integer labId) {
        NarridoUser user = null;
        NarridoLaboratory laboratory = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
            
            laboratory = NarridoGeneric.getSingle(NarridoLaboratory.class, NarridoLaboratory_.labId, labId);
            List<NarridoPc> pcs = NarridoGeneric.getList(NarridoPc.class, NarridoPc_.laboratory, laboratory);
            
            DateFormat df = new SimpleDateFormat("MMM d y hhmm");
            String fileName = "PC Report " + laboratory.getLabDescription() + " " + df.format(new Date()) + ".pdf";
            String url = NarridoIO.DIR + "reports/" + fileName;
            String siteUrl = "http://localhost:8080/files/reports/" + fileName;
            
            
            java.nio.file.Path path = Paths.get(NarridoIO.DIR + "reports/");
        
            if(!Files.exists(path)) {
                Files.createDirectories(path);
            }

            File theFile = new File(url);

            if(theFile.exists()) {
                theFile.delete();
                System.out.println("File duplicate; delete!");
            }
            
            theFile = new File(url);
            NarridoReport.generatePcReport(pcs, theFile);
            
            NarridoFile file = new NarridoFile();
            file.setFileType("report");
            file.setFileName(fileName);
            file.setFileUrl(siteUrl);
            file.setUploader(user);
            file.setDateUploaded(new Date());
            
            NarridoGeneric.saveThing(file);
            NarridoPushResource npr = rcontext.getResource(NarridoPushResource.class);
            npr.sendToEveryone(file);
            
            return Response.ok("Report generated!").build();
        } catch (JwtException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        } catch (NoResultException ne) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No laboratory found")
                    .build();
        } catch (JRException | IOException je) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(je.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/monitoring/{labId}/report")
    public Response getMonitoringReport(@PathParam("labId") Integer labId) {
        NarridoUser user = null;
        NarridoLaboratory laboratory = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
            
            laboratory = NarridoGeneric.getSingle(NarridoLaboratory.class, NarridoLaboratory_.labId, labId);
            List<NarridoDailyMonitoring> monitorings = NarridoGeneric.getList(NarridoDailyMonitoring.class, NarridoDailyMonitoring_.laboratory, laboratory);
            
            DateFormat df = new SimpleDateFormat("MMM d y hhmm");
            String fileName = "Monitoring Report " + laboratory.getLabDescription() + " " + df.format(new Date()) + ".pdf";
            String url = NarridoIO.DIR + "reports/" + fileName;
            String siteUrl = "http://localhost:8080/files/reports/" + fileName;
            
            
            java.nio.file.Path path = Paths.get(NarridoIO.DIR + "reports/");
        
            if(!Files.exists(path)) {
                Files.createDirectories(path);
            }

            File theFile = new File(url);

            if(theFile.exists()) {
                theFile.delete();
                System.out.println("File duplicate; delete!");
            }
            
            theFile = new File(url);
            NarridoReport.generateMonitoringReport(monitorings, laboratory.getLabDescription(), theFile);
            
            NarridoFile file = new NarridoFile();
            file.setFileType("report");
            file.setFileName(fileName);
            file.setFileUrl(siteUrl);
            file.setUploader(user);
            file.setDateUploaded(new Date());
            
            NarridoGeneric.saveThing(file);
            NarridoPushResource npr = rcontext.getResource(NarridoPushResource.class);
            npr.sendToEveryone(file);
            
            return Response.ok("Report generated!").build();
        } catch (JwtException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        } catch (NoResultException ne) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No laboratory found")
                    .build();
        } catch (JRException | IOException je) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(je.getMessage())
                    .build();
        }
    }
    
    @POST
    @Path("/support")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newTicket(NarridoJob job) {
        NarridoUser user = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
            
            job.setReportedBy(user);
            
            NarridoGeneric.saveThing(job);
            
            NarridoPushResource npr = rcontext.getResource(NarridoPushResource.class);
            npr.sendToEveryone(job);
            return Response.ok("Report submitted!").build();
        } catch (JwtException | HibernateException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(NarridoGeneric.getStackTrace(e))
                    .build();
        } catch (NoResultException ne) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Laboratory not found")
                    .build();
        }
    }
    
    @PUT
    @Path("/support")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTicket(NarridoJob job) {
        NarridoUser user = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
            
            job.setHandledBy(user);
            
            if("resolved".equals(job.getStatus())) {
                job.setDateResolved(new Date());
            } else {
                job.setDateResolved(null);
            }
            
            NarridoGeneric.doThing(NarridoOperation.UPDATE, job);
            
            NarridoPushResource npr = rcontext.getResource(NarridoPushResource.class);
            npr.sendToEveryone(job);
            npr.sendToEveryone(
                    NarridoGeneric.logTrail(user, "Updated issue # " + job.getJobId() + ": " + job.getReport() + "(" + job.getStatus() + ")")
            );
            return Response.ok("Report updated!").build();
        } catch (JwtException | HibernateException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(NarridoGeneric.getStackTrace(e))
                    .build();
        } catch (NoResultException ne) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Laboratory not found")
                    .build();
        }
    }
    
    @GET
    @Path("/support")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTickets(@QueryParam("status") String status) {
        NarridoUser user = null;
        List<NarridoJob> jobs;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
            
            boolean isFaculty = NarridoType.FACULTY.equals(user.getType());
            
            if("all".equals(status)) {
                jobs = !isFaculty ?
                            NarridoGeneric.getList(NarridoJob.class) :
                            dao.mySubmittedJobs(user, status);
            } else {
                jobs = !isFaculty ?
                            NarridoGeneric.getList(NarridoJob.class, NarridoJob_.status, status) :
                            dao.mySubmittedJobs(user, status);
            }
            
            return Response.ok(jobs).build();
        } catch (JwtException | HibernateException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(NarridoGeneric.getStackTrace(e))
                    .build();
        } catch (NoResultException ne) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Laboratory not found")
                    .build();
        }
    }
    
    @GET
    @Path("/support/pc/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTickets(@PathParam("id") Integer pcId) {
        NarridoUser user = null;
        List<NarridoJob> jobs;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
            
            NarridoPc pc = NarridoGeneric.getSingle(NarridoPc.class, NarridoPc_.id, pcId);
            jobs = NarridoGeneric.getList(NarridoJob.class, NarridoJob_.pc, pc);
            
            return Response.ok(jobs).build();
        } catch (JwtException | HibernateException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(NarridoGeneric.getStackTrace(e))
                    .build();
        } catch (NoResultException ne) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("PC not found")
                    .build();
        }
    }
    
    @GET
    @Path("/support/report")
    public Response reportTickets() {
        NarridoUser user = null;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
            
            List<NarridoJob> jobs = NarridoGeneric.getList(NarridoJob.class);
            
            DateFormat df = new SimpleDateFormat("MMM d y hhmm");
            String fileName = "Job Summary " + df.format(new Date()) + ".pdf";
            String url = NarridoIO.DIR + "reports/" + fileName;
            String siteUrl = "http://localhost:8080/files/reports/" + fileName;
            
            
            java.nio.file.Path path = Paths.get(NarridoIO.DIR + "reports/");
        
            if(!Files.exists(path)) {
                Files.createDirectories(path);
            }

            File theFile = new File(url);

            if(theFile.exists()) {
                theFile.delete();
                System.out.println("File duplicate; delete!");
            }
            
            theFile = new File(url);
            NarridoReport.generateJobReport(jobs, theFile);
            
            NarridoFile file = new NarridoFile();
            file.setFileType("report");
            file.setFileName(fileName);
            file.setFileUrl(siteUrl);
            file.setUploader(user);
            file.setDateUploaded(new Date());
            
            NarridoGeneric.saveThing(file);
            NarridoPushResource npr = rcontext.getResource(NarridoPushResource.class);
            npr.sendToEveryone(file);
            
            return Response.ok("Report generated!").build();
        } catch (JwtException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        } catch (NoResultException ne) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No laboratory found")
                    .build();
        } catch (JRException | IOException je) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(je.getMessage())
                    .build();
        }
    } //TODO refactor report generation endpoints
    
    /**
     * Fake JAX RS response to get installed software list on a machine.
     * @return a string of installed programs
     */
    @GET
    @Path("/pc/software")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInstalledSoftware(/*I could use a pc number...*/) {
        String[] apps = {
            "NetBeans IDE 8.1",
            "Visual Studio Ultimate 2012",
            "Adobe Photoshop CS6",
            "Dev-C++ 5.9.9.2",
            "Java(TM) Runtime Environment 1.8.0",
            "USB Defender",
            "DOTA 2",
            "Steam", //of course steam is required
            "Chrome"
        };
        
        List<String> installed = new ArrayList<>();
        for(String app : apps) {
            installed.add(app);
        }
        return Response.ok(installed).build();
    }
    
    /**
     * Yet another Fake JAX RS response that gives fake running program list
     * For the sake of demonstration and testing
     * @return a string of running programs
     */
    @GET
    @Path("/pc/running")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRunningPrograms(/*I could use a pc number...*/) {
        String[] apps = {
            "DOTA 2",
            "Steam", //of course steam is required
            "Docu for thesis - Microsoft Word",
            "Untitled - Chrome"
        };
        
        List<String> runnings = new ArrayList<>();
        for(String app : apps) {
            runnings.add(app);
        }
        
        return Response.ok(runnings).build();
    }
    
    @GET
    @Path("/pc/logs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLog(/*I could use a pc number...*/) {
        final List<LogData> logs = new ArrayList<>();
        
        logs.add(new LogData("Machine start", new Date()));
        logs.add(new LogData("Logged in (Princess Meliza Narrido)", new Date()));
        logs.add(new LogData("Logged out", new Date()));
        logs.add(new LogData("Machine shutdown", new Date()));
        logs.add(new LogData("Machine startup", new Date()));
        logs.add(new LogData("Logged in (Marie Jane Herbas)", new Date()));
        logs.add(new LogData("Logged out", new Date()));
        logs.add(new LogData("Logged in (Rocky Axtor)", new Date()));
        logs.add(new LogData("Logged out", new Date()));
        logs.add(new LogData("Machine shutdown", new Date()));
        logs.add(new LogData("Machine startup", new Date()));
        logs.add(new LogData("Logged in (Jay Dominguez)", new Date()));
        
        return Response.ok(logs).build();
    }
    
    @GET
    @Path("/log")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuditTrail() {
        NarridoUser user = null;
        List<NarridoAuditTrail> trails;
        try {
            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                Jws<Claims> claims = NarridoAuth.authenticate(token.substring("Bearer".length()));
                user = NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
            } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Login required")
                        .build();
            }
            
            trails = NarridoGeneric.getList(NarridoAuditTrail.class);
            
            return Response.ok(trails).build();
        } catch (JwtException | HibernateException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(NarridoGeneric.getStackTrace(e))
                    .build();
        } catch (NoResultException ne) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Laboratory not found")
                    .build();
        }
    }
}


class LogData {
    private String log;
    private Date date;

    public LogData() {
    }

    public LogData(String log, Date date) {
        this.log = log;
        this.date = date;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

class PcUpdateData {
    private NarridoPc pc;
    private String remarks;

    public PcUpdateData() {
    }

    public NarridoPc getPc() {
        return pc;
    }

    public void setPc(NarridoPc pc) {
        this.pc = pc;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    
}