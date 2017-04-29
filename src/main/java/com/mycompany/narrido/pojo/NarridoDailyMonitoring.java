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
@JsonIgnoreProperties(value = {"laboratory", "user"}, allowGetters = true)
@Entity
@Table(name = "daily_monitoring")
public class NarridoDailyMonitoring implements Serializable {
    private Integer monitoringId;
    private NarridoLaboratory laboratory;
    private Date date;
    private String instructor;
    private String courseSection;
    private String systemUnit;
    private String monitor;
    private String keyboard;
    private String mouse;
    private String avr;
    private String remarks;
    private String comment;
    private NarridoUser user;

    public NarridoDailyMonitoring() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monitoringid")
    public Integer getMonitoringId() {
        return monitoringId;
    }

    public void setMonitoringId(Integer monitoringId) {
        this.monitoringId = monitoringId;
    }

    @JsonManagedReference("laboratory-daily")
    @ManyToOne
    @JoinColumn(name = "labid")
    public NarridoLaboratory getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(NarridoLaboratory laboratory) {
        this.laboratory = laboratory;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "instructor")
    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    @Column(name = "course_section")
    public String getCourseSection() {
        return courseSection;
    }

    public void setCourseSection(String courseSection) {
        this.courseSection = courseSection;
    }

    @Column(name = "system_unit")
    public String getSystemUnit() {
        return systemUnit;
    }

    public void setSystemUnit(String systemUnit) {
        this.systemUnit = systemUnit;
    }

    @Column(name = "monitor")
    public String getMonitor() {
        return monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    @Column(name = "keyboard")
    public String getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(String keyboard) {
        this.keyboard = keyboard;
    }

    @Column(name = "mouse")
    public String getMouse() {
        return mouse;
    }

    public void setMouse(String mouse) {
        this.mouse = mouse;
    }

    @Column(name = "avr")
    public String getAvr() {
        return avr;
    }

    public void setAvr(String avr) {
        this.avr = avr;
    }

    @Column(name = "remarks")
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @JsonManagedReference("user-daily")
    @ManyToOne
    @JoinColumn(name = "userid")
    public NarridoUser getUser() {
        return user;
    }

    public void setUser(NarridoUser user) {
        this.user = user;
    }
    
    
}
