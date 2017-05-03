/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.helper;

import com.mycompany.narrido.pojo.NarridoDailyMonitoring;
import com.mycompany.narrido.pojo.NarridoJob;
import com.mycompany.narrido.pojo.NarridoPc;
import com.mycompany.narrido.pojo.NarridoUser;
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
    
    public static void main(String[] args) throws JRException, FileNotFoundException{
        SFH.init();

        File file = new File("C:/NarridoFileUploads/test_report_job.pdf");
        List<NarridoJob> monitorings = NarridoGeneric.getList(NarridoJob.class);
        
        generateJobReport(monitorings, file);
    }
    
    
}


class JobSummaryData {
    private String inCharge;
    private String pcNumber;
    private String reports;
    private Date dateReported;
    private String diagnostics;
    private String status;
    private String application;

    public JobSummaryData() {
    }
    
    public JobSummaryData(NarridoJob job) {
        NarridoUser user = job.getHandledBy();
        this.inCharge = user != null 
                ? (user.getFirstName() + " " + user.getLastName())
                : "N/A";
        
        this.pcNumber = job.getPc().getPcNumber();
        this.reports = job.getReport();
        this.dateReported = job.getDateReported();
        this.diagnostics = job.getFindings();
        this.status = job.getStatus();
        this.application = job.getAction();
    }

    public String getInCharge() {
        return inCharge;
    }

    public void setInCharge(String inCharge) {
        this.inCharge = inCharge;
    }

    public String getPcNumber() {
        return pcNumber;
    }

    public void setPcNumber(String pcNumber) {
        this.pcNumber = pcNumber;
    }

    public String getReports() {
        return reports;
    }

    public void setReports(String reports) {
        this.reports = reports;
    }

    public Date getDateReported() {
        return dateReported;
    }

    public void setDateReported(Date dateReported) {
        this.dateReported = dateReported;
    }

    public String getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(String diagnostics) {
        this.diagnostics = diagnostics;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
    
    
}