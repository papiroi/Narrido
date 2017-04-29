/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author princessmelisa
 */
@Entity
@Table(name = "jobs")
public class NarridoJob implements Serializable {
    private Integer jobId;
    private Date dateReported;
    private String remarks;
    private String report;
    private String findings; //diagnostics
    private String action; //application
    private NarridoUser handledBy; //in charge technician
    private Date dateResolved;
    private NarridoUser reportedBy;
    private String status; //job status
    private NarridoPc pc;

    public NarridoJob() {
    }

    public NarridoJob(Integer jobId, Date dateReported, String remarks, String report, String findings, String action, NarridoUser handledBy, Date dateResolved, NarridoUser reportedBy, String status) {
        this.jobId = jobId;
        this.dateReported = dateReported;
        this.remarks = remarks;
        this.report = report; 
        this.findings = findings; //diagnostics
        this.action = action; //application
        this.handledBy = handledBy;
        this.dateResolved = dateResolved;
        this.reportedBy = reportedBy;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jobid")
    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_reported")
    public Date getDateReported() {
        return dateReported;
    }

    public void setDateReported(Date dateReported) {
        this.dateReported = dateReported;
    }

    @Column(name = "remarks")
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Column(name = "report")
    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    @Column(name = "findings")
    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }

    @Column(name = "application")
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @ManyToOne
    @JoinColumn(name = "userid_handled_by")
    public NarridoUser getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(NarridoUser handledBy) {
        this.handledBy = handledBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_resolved")
    public Date getDateResolved() {
        return dateResolved;
    }

    public void setDateResolved(Date dateResolved) {
        this.dateResolved = dateResolved;
    }
    
    @ManyToOne
    @JoinColumn(name = "userid_reported_by")
    public NarridoUser getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(NarridoUser reportedBy) {
        this.reportedBy = reportedBy;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "pcid")
    public NarridoPc getPc() {
        return pc;
    }

    public void setPc(NarridoPc pc) {
        this.pc = pc;
    }
    
    
    
}
