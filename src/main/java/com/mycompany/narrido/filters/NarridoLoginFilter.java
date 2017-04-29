/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.filters;

import com.mycompany.narrido.helper.NarridoAuth;
import com.mycompany.narrido.helper.NarridoGeneric;
import com.mycompany.narrido.pojo.NarridoUser;
import com.mycompany.narrido.pojo.NarridoUser_;
import com.mycompany.narrido.restannotation.LoginRequired;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import java.io.IOException;
import java.net.URI;
import javax.servlet.ServletContext;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author princessmelisa
 */
@LoginRequired
@Provider
public class NarridoLoginFilter implements ContainerRequestFilter{

    @Context
    ServletContext context;
    
    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        //TODO
        URI loginPath = URI.create(context.getContextPath() + "/login.html");
        Response redirect = Response.status(Status.FORBIDDEN)
                .entity("Login is required for this operation.")
                .location(loginPath).build();
        
        String token = crc.getHeaderString(HttpHeaders.AUTHORIZATION);
        
        if(token == null) {
            crc.abortWith(redirect);
        } else {
            try {
                token = token.substring("Bearer".length());
                Jws<Claims> claims = NarridoAuth.authenticate(token);
            } catch (JwtException e) { //invalid jwt
                e.printStackTrace(System.err);
                
                crc.abortWith(Response
                        .status(Status.FORBIDDEN)
                        .cookie()
                        .location(loginPath)
                        .entity(e.getMessage())
                        .build());
            }
        }
        
    }
    
}
