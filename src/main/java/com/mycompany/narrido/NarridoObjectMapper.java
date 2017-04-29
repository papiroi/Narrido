/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author princessmelisa
 */
@Provider
public class NarridoObjectMapper implements ContextResolver<ObjectMapper> {

    final ObjectMapper defaultObjectMapper;

    public NarridoObjectMapper() {
        this.defaultObjectMapper = createDefaultMapper();
    }
    
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return defaultObjectMapper;
    }

    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper result = new ObjectMapper();
        Hibernate5Module module = new Hibernate5Module();
        
        module.disable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
        result.registerModule(module);
        
        return result;
    }
    
}
