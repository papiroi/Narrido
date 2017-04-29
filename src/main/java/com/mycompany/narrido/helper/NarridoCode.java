/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.helper;

import java.security.SecureRandom;

/**
 *
 * @author princessmelisa
 */
public final class NarridoCode {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static SecureRandom rnd = new SecureRandom();
    
    private NarridoCode() {}
    
    public static String getCode(){
        return randomString(5) + "-" + randomString(5);
    }
    
    private static String randomString( int len ){
       StringBuilder sb = new StringBuilder( len );
       for( int i = 0; i < len; i++ ) 
          sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
       return sb.toString();
    }
}
