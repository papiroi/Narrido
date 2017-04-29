/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.helper;

import com.mycompany.narrido.pojo.NarridoUser;
import com.mycompany.narrido.pojo.NarridoUser_;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import javax.persistence.NoResultException;

/**
 *
 * @author princessmelisa
 */
public final class NarridoAuth {
    
    private static final Random RANDOM = new SecureRandom();
    
    private NarridoAuth(){}
    
    public static String charArrayToString(char[] theChar){
        return new String(theChar);
    }
    
    public static String byteToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(Integer.toHexString(b & 0xff));
        }
        return sb.toString();
    }
    
    public static String generateSalt(){
        byte[] salt = new byte[32];
        RANDOM.nextBytes(salt);
        return byteToHexString(salt);
    }
    
    /**
     * SHA-256 encryption function
     * @param password the password
     * @param salt the salt
     * @return the encrypted string in hex
     */
    public static String sha256(String password, String salt){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((password + salt).getBytes());
            
            byte[] passwordbytes = md.digest();
            
            StringBuilder sb = new StringBuilder();
            for(byte b : passwordbytes){
                sb.append(Integer.toHexString(b & 0xff));
            }
            return sb.toString();
        }catch(NoSuchAlgorithmException nsae){
            nsae.printStackTrace(System.err);
            return null;
        }
    }
    
    public static Jws<Claims> authenticate(String token) throws JwtException {
        Jws<Claims> claims = Jwts.parser()
                .setSigningKeyResolver(new SigningKeyResolverAdapter(){
                    @Override
                    public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
                        final NarridoUser user = NarridoGeneric
                                .getSingle(NarridoUser.class, NarridoUser_.username, claims.getSubject());
                        return user.getPassword().getBytes();
                    }
                })
                .parseClaimsJws(token);
       return claims;
    }
    
    public static NarridoUser authenticateAndGetUser(String token) throws JwtException, NoResultException{
        Jws<Claims> claims = Jwts.parser()
                .setSigningKeyResolver(new SigningKeyResolverAdapter(){
                    @Override
                    public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
                        final NarridoUser user = NarridoGeneric
                                .getSingle(NarridoUser.class, NarridoUser_.username, claims.getSubject());
                        return user.getPassword().getBytes();
                    }
                })
                .parseClaimsJws(token);
       return NarridoGeneric.getSingle(NarridoUser.class, NarridoUser_.username, claims.getBody().getSubject());
    }
    
}
