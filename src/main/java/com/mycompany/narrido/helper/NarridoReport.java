/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.helper;

import com.mycompany.narrido.pojo.NarridoPc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jboss.logging.Logger;

/**
 *
 * @author princessmelisa
 */
public class NarridoReport {
    static final String REPORT_DIR = "C:/NarridoFileUploads/templates/";
    public static void generatePcReport(List<NarridoPc> pcs, File file) throws JRException, FileNotFoundException{
        Map<String, Object> params = new HashMap<>();
        List<NarridoPc> beans = pcs;

        JRBeanCollectionDataSource mrBeanSource = new JRBeanCollectionDataSource(beans);
        params.put("PcDataSource", mrBeanSource);

        JasperPrint jp = JasperFillManager.fillReport(REPORT_DIR + "DarthVader.jasper", params, new JREmptyDataSource());
        JasperExportManager.exportReportToPdfStream(jp, new FileOutputStream(file));
    }
}
