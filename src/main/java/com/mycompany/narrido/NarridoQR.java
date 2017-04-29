/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
 
import javax.imageio.ImageIO;
 
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mycompany.narrido.dao.PcDaoHb;
import com.mycompany.narrido.dao.ice.PcDao;
import com.mycompany.narrido.helper.NarridoGeneric;
import com.mycompany.narrido.helper.NarridoIO;
import com.mycompany.narrido.helper.SFH;
import com.mycompany.narrido.pojo.NarridoPc;
import com.mycompany.narrido.pojo.NarridoPc_;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
 
/**
 * @author Crunchify.com
 * Updated: 03/20/2016 - added code to narrow border size 
 */
 
public class NarridoQR {
    
    private PcDao pcDao;
    
    public NarridoQR(){
        pcDao = PcDaoHb.getInstance();
    }

    public PcDao getPcDao() {
        return pcDao;
    }

    public void setPcDao(PcDao pcDao) {
        this.pcDao = pcDao;
    }
    
    public static String getQr(NarridoPc pc) {
        String myCodeText = pc.toString();
        String uploadDir = NarridoIO.DIR + "qr/";
        String fileName = pc.getPcName() + " " + pc.getLaboratory().getLabDescription() + ".png";
        String filePath = uploadDir + fileName;
        int size = 250;
        String fileType = "png";
        
        java.nio.file.Path path = Paths.get(uploadDir);
        
        if(!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
        
        File file = new File(filePath);
        try {

                Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
                hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

                // Now with zxing version 3.2.1 you could change border size (white border size to just 1)
                hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
                hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                hintMap.put(EncodeHintType.QR_VERSION, 10);
                
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix byteMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size,
                                size, hintMap);
                int CrunchifyWidth = byteMatrix.getWidth();
                BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth,
                                BufferedImage.TYPE_INT_RGB);
                image.createGraphics();

                Graphics2D graphics = (Graphics2D) image.getGraphics();
                graphics.setColor(Color.WHITE);
                graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
                graphics.setColor(Color.BLACK);

                for (int i = 0; i < CrunchifyWidth; i++) {
                    for (int j = 0; j < CrunchifyWidth; j++) {
                        if (byteMatrix.get(i, j)) {
                                graphics.fillRect(i, j, 1, 1);
                        }
                    }
                }
            ImageIO.write(image, fileType, file);
        } catch (WriterException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
        System.out.println("\n\nYou have successfully created QR Code.");
        return fileName;
    }
    
    public static void main(String[] args) {
        SFH.getSF();
        NarridoPc pc = NarridoGeneric.getSingle(NarridoPc.class, NarridoPc_.id, 1);
        getQr(pc);
        System.out.println("OK!");
    }
}
