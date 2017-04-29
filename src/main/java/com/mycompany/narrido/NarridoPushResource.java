/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido;

import com.mycompany.narrido.helper.NarridoAuth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author princessmelisa
 */
@Path("/push")
public class NarridoPushResource {
    
    final static Map<String, AsyncResponse> WAITERS = new ConcurrentHashMap<>();
    final static ExecutorService EX = Executors.newSingleThreadExecutor();
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void wait(@Context ContainerRequestContext rc, @Suspended AsyncResponse async) {
        String username = "";
        
        String token = rc.getHeaderString(HttpHeaders.AUTHORIZATION);
        
        try {
            if(token != null) {
                token = token.substring("Bearer".length());
                Jws<Claims> claims = NarridoAuth.authenticate(token);
                username = claims.getBody().getSubject();
            } else {
                async.resume(Response.status(Status.FORBIDDEN).entity("Login Required.").build());
                return;
            }
        } catch (JwtException je) {
            async.resume(Response.status(Status.FORBIDDEN).entity(je.getMessage()).build());
            return;
        }
        
        async.setTimeoutHandler((AsyncResponse asyncResponse) -> {
            asyncResponse.resume(Response.status(Status.SERVICE_UNAVAILABLE).entity("Timed out.").build());
        });
        async.setTimeout(30, TimeUnit.SECONDS);
        WAITERS.put(username, async);
    }
    
    public void send(String username, Object object) {
        AsyncResponse resp = WAITERS.get(username);
        if(resp != null) resp.resume(object);
    }
    
    public void sendToEveryone(Object object) {
        Collection<AsyncResponse> asyncs = WAITERS.values();
        
        for(AsyncResponse async : asyncs) {
            async.resume(object);
        }
    }
    
}
