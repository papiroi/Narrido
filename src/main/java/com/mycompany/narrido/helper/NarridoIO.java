/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author princessmelisa
 */
public final class NarridoIO {
    
    public static class NarridoIOException extends Exception{

        public NarridoIOException() {
        }
        
        public NarridoIOException(String message) {
            super(message);
        }
    }
    
    public static final String DIR = "C:/NarridoFileUploads/";
    
    private NarridoIO(){}
    
    public static void saveToDisk(InputStream istream, String uploadDir, String fileName) throws NarridoIOException {
        java.nio.file.Path path = Paths.get(uploadDir);
        
        if(!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
        
        File theFile = new File(uploadDir + fileName);
        
        if(theFile.exists()) {
            theFile.delete();
            System.out.println("File duplicate; delete!");
        }
        
        try{
            
            InputStreamReader isr = new InputStreamReader(istream);
            
            OutputStream ostream = null;
            int read = 0;
            byte[] bytez = new byte[1024];
            
            ostream = new FileOutputStream(new File(uploadDir + fileName));
            
            while((read = istream.read(bytez)) != -1){
                ostream.write(bytez, 0, read);
            }
            
            ostream.flush();
            ostream.close();
            
            
        }catch(IOException ioe){
            ioe.printStackTrace(System.err);
        }
    }
}
