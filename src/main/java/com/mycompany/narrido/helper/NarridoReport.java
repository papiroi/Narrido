/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.helper;

import com.mycompany.narrido.dao.PcDaoHb;
import com.mycompany.narrido.dao.ice.PcDao;
import com.mycompany.narrido.pojo.NarridoDailyMonitoring;
import com.mycompany.narrido.pojo.NarridoJob;
import com.mycompany.narrido.pojo.NarridoPc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
    
    public static void generateMonitoringReport(List<NarridoDailyMonitoring> monitorings, String laboratory, File file) throws JRException, FileNotFoundException{
        Map<String, Object> params = new HashMap<>();
        List<NarridoDailyMonitoring> beans = monitorings;

        JRBeanCollectionDataSource mrBeanSource = new JRBeanCollectionDataSource(beans);
        params.put("MonitoringDataSource", mrBeanSource);
        params.put("LaboratoryName", laboratory);

        JasperPrint jp = JasperFillManager.fillReport(REPORT_DIR + "DarthSidious.jasper", params, new JREmptyDataSource());
        JasperExportManager.exportReportToPdfStream(jp, new FileOutputStream(file));
    }
    
    public static void generateJobReport(List<NarridoJob> jobs, File file) throws JRException, FileNotFoundException{
        Map<String, Object> params = new HashMap<>();
        List<JobSummaryData> beans = new ArrayList<>();
        
        for(NarridoJob job : jobs) {
            beans.add(new JobSummaryData(job));
        }

        JRBeanCollectionDataSource mrBeanSource = new JRBeanCollectionDataSource(beans);
        params.put("JobsDataSource", mrBeanSource);

        JasperPrint jp = JasperFillManager.fillReport(REPORT_DIR + "DarthMildred.jasper", params, new JREmptyDataSource());
        JasperExportManager.exportReportToPdfStream(jp, new FileOutputStream(file));
    }
    
    public static void generateReMr(
            String reMr,
            Date date,
            String description,
            String note,
            String receivedFrom,
            String receivedFromPosition,
            String receiverPosition,
            File file) throws JRException, FileNotFoundException {
        
        PcDao dao = PcDaoHb.getInstance();
        Map<String, Object> params = new HashMap<>();
        Integer qty = 0;
        Double totalAmount = 0D;
        
        params.put("Description", description);
        params.put("Note", note);
        params.put("Unit", "pcs");
        params.put("ReceivedFrom", receivedFrom);
        params.put("ReceivedFromPosition", receivedFromPosition);
        params.put("ReceivedBy", reMr);
        params.put("ReceivedByPosition", receiverPosition);
        
        List<NarridoPc> pcs = dao.getMrPc(reMr, date);
        
        qty = pcs.size();
        
        for(NarridoPc pc : pcs) {
            totalAmount += pc.getUnitValue();
        }
        
        params.put("Qty", qty.toString());
        params.put("TotalAmount", totalAmount);
        
        JasperPrint jp = JasperFillManager.fillReport(REPORT_DIR + "DarthMall.jasper", params, new JREmptyDataSource());
        JasperExportManager.exportReportToPdfStream(jp, new FileOutputStream(file));
    }
    
    public static void main(String[] args) throws JRException, FileNotFoundException{
        SFH.init();

        File file = new File("C:/NarridoFileUploads/test_report_job.pdf");
        List<NarridoJob> monitorings = NarridoGeneric.getList(NarridoJob.class);
        
        generateJobReport(monitorings, file);
    }
    
    
}
