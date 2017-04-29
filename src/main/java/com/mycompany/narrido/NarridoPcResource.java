/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido;

import com.mycompany.narrido.dao.PcDaoHb;
import com.mycompany.narrido.dao.ice.PcDao;
import com.mycompany.narrido.pojo.NarridoPc;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

/**
 * REST Web Service
 *
 * @author princessmelisa
 */
@Deprecated
@Path("/pc")
public class NarridoPcResource {

    PcDao pcDao; //will be Spring'd soon
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ServiceResource
     */
    public NarridoPcResource() {
        pcDao = PcDaoHb.getInstance();
    }

    public void setPcDao(PcDao pcDao) {
        this.pcDao = pcDao;
    }

    /**
     * Retrieves representation of an instance of com.narrido.ServiceResource
     * @return an instance of java.lang.String
     */

    @GET
    @Path("/allpc")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPcs(){
        List<NarridoPc> pcs = pcDao.getAllPcs();
        GenericEntity<List<NarridoPc>> ge = new GenericEntity<List<NarridoPc>>(pcs){};
        return Response.ok(ge).build();
    }
    
//    @POST
//    @Path("/qr")
//    @Produces({"image/png"})
//    public Response getQrByPcNumber(@FormParam("pc-number")final String pcNumber){
//        return Response.ok().entity(new StreamingOutput(){
//            @Override
//            public void write(OutputStream output) throws IOException, WebApplicationException {
//                output.write(NarridoQR.getQr(pcNumber));
//                output.flush();
//            }
//        }).build();
//    }

//    @GET
//    @Path("/pdf/all")
//    @Produces("application/pdf")
//    public Response getPdfAllPcs(){
//        ResponseBuilder rb = Response.ok().entity((Object) new File(this.getClass().getResource("DarthVader.pdf").getPath()));
//        rb.header("Content-Disposition", "attachment; filename=report.pdf");
//        return rb.build();
//    }

    /**
     * PUT method for updating or creating an instance of ServiceResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.TEXT_HTML)
    public void putHtml(String content) {
    }
}
