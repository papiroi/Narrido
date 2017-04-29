/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido;

import java.security.Principal;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author princessmelisa
 */
@Provider
public class NarridoSecurity implements SecurityContext{

    Principal principal;
    String scheme;

    public NarridoSecurity(Principal principal, String scheme) {
        this.principal = principal;
        this.scheme = scheme;
    }
    
    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }

    @Override
    public boolean isUserInRole(String string) {
        return "AICOLA".equals(string);
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isSecure() {
        return "https".equals(scheme);
    }
    
}
