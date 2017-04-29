/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.helper;

import com.mycompany.narrido.helper.NarridoGeneric.NarridoOperation;
import com.mycompany.narrido.pojo.NarridoLaboratory;
import com.mycompany.narrido.pojo.NarridoLaboratory_;
import com.mycompany.narrido.pojo.NarridoPc;
import com.mycompany.narrido.pojo.NarridoUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author princessmelisa
 */
public final class SFH {
    
    private static SessionFactory sf;
    
    static{
        sf = new Configuration().configure().buildSessionFactory();
    }
    
    private SFH(){
    }
    
    public static void init(){}
    
    public static SessionFactory getSF(){
        return sf;
    }
    
    public static void shutdown(){
        if(sf != null) sf.close();
    }
    
    public static void main(String[] args) {
        try {
            registerAdmin();
//            NarridoLaboratory laboratory = NarridoGeneric.getSingle(NarridoLaboratory.class, NarridoLaboratory_.labId, 1);
//            NarridoPc pc = new NarridoPc();
//            pc.setDateAcquired(new Date());
//            pc.setLaboratory(laboratory);
//            pc.setMr("Mildred Apostol");
//            pc.setPcDescription("Acer Veriton");
//            pc.setPcName("CL-01 PC-01");
//            pc.setPcNumber("01");
//            pc.setPropertyNumber("54-123");
//            pc.setReMr("Bryan Balaga");
//            pc.setSerialNumber("12345");
//            pc.setStatus("WORKING");
//            pc.setUnitValue(30000D);
//            
//            NarridoGeneric.saveThing(pc);
        } catch(HibernateException he) {
            System.out.println("LOL!!! " + he.getMessage());
            if(he instanceof ConstraintViolationException){
                System.out.println("LOL!!! " + he.getCause().getMessage());
            }
            shutdown();
        } finally {
            shutdown();
        }
    }
    
    public static void registerAdmin() {
        try {
            NarridoUser user = new NarridoUser();
            user.setIdNumber(201412345);
            user.setFirstName("Liane Vina");
            user.setMiddleName("G.");
            user.setLastName("Ocampo");
            user.setType(NarridoType.MIS);
            user.setUsername("liane");
            
            String salt = NarridoAuth.generateSalt();
            user.setPassword(NarridoAuth.sha256("ocampo", salt));
            user.setSalt(salt);
            user.setAddress("debug");
            
            NarridoGeneric.saveThing(user);
            
            user.setIsConfirmed(Boolean.TRUE);
            NarridoGeneric.doThing(NarridoOperation.UPDATE, user);
            
            //create the labs
            List<NarridoLaboratory> labs = new ArrayList<>();
            for(int i = 0; i < 9; i++) {
                NarridoLaboratory lab = new NarridoLaboratory();
                lab.setLabDescription("Computer Laboratory " + Integer.toString(i + 1));
                labs.add(lab);
            }
            
            NarridoGeneric.saveThing(labs.toArray((Object[]) new NarridoLaboratory[labs.size()]));
            
        } catch(HibernateException he) {
            System.out.println("LOL!!! " + he.getMessage());
            if(he instanceof ConstraintViolationException){
                System.out.println("LOL!!! " + he.getCause().getMessage());
            }
            shutdown();
        } finally {
            shutdown();
        }
    }
    
}

class ExperimentalClass {
    private String type;
    private Object content;

    public ExperimentalClass() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
    
}