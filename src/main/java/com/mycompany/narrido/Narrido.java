/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.mycompany.narrido.helper.SFH;
import java.util.Set;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/**
 * The REST resource config.
 * NB: All Resources and Helper classes shall have a prefix of Narrido- eg. "NarridoSecurity, NarridoIO"
 * @author princessmelisa
 */
@javax.ws.rs.ApplicationPath("api")
public class Narrido extends ResourceConfig{

//    @Override
//    public Set<Class<?>> getClasses() {
//        Set<Class<?>> resources = new java.util.HashSet<>();
//        addRestResourceClasses(resources);
//        return resources;
//    }

    public Narrido() {
        super();
        packages("com.mycompany.narrido");
        register(RolesAllowedDynamicFeature.class);
        register(MultiPartFeature.class);
        register(NarridoObjectMapper.class);
        register(JacksonFeature.class);
        
        SFH.init();
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.mycompany.narrido.NarridoAccountResource.class);
        resources.add(com.mycompany.narrido.NarridoFileIOResource.class);
        resources.add(com.mycompany.narrido.NarridoGroupResource.class);
        resources.add(com.mycompany.narrido.NarridoITResource.class);
        resources.add(com.mycompany.narrido.NarridoObjectMapper.class);
        resources.add(com.mycompany.narrido.NarridoPcResource.class);
        resources.add(com.mycompany.narrido.NarridoPushResource.class);
        resources.add(com.mycompany.narrido.NarridoSecurity.class);
        resources.add(com.mycompany.narrido.filters.NarridoLoginFilter.class);
    }
}
