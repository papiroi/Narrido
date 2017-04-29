/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.narrido.helper.SFH;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author princessmelisa
 */
@Entity
@Table(name="pc")
@XmlRootElement
public class NarridoPc {
    private Integer id;
    private String pcNumber;
    private String pcName;
    private String pcDescription;
    private Date dateAcquired;
    private String serialNumber;
    private String propertyNumber;
    private Double unitValue;
    private String status;
    private NarridoLaboratory laboratory;
    private String mr;
    private String reMr;
    
    public NarridoPc() {
    }

    public NarridoPc(Integer id, String pcNumber, String pcName, String pcDescription, Date dateAcquired, String serialNumber, String propertyNumber, Double unitValue, String status) {
        this.id = id;
        this.pcNumber = pcNumber;
        this.pcName = pcName;
        this.pcDescription = pcDescription;
        this.dateAcquired = dateAcquired;
        this.serialNumber = serialNumber;
        this.propertyNumber = propertyNumber;
        this.unitValue = unitValue;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pcid")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "pc_number")
    public String getPcNumber() {
        return pcNumber;
    }

    public void setPcNumber(String pcNumber) {
        this.pcNumber = pcNumber;
    }

    @Column(name = "pc_name")
    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }

    @Column(name = "pc_description")
    public String getPcDescription() {
        return pcDescription;
    }

    public void setPcDescription(String pcDescription) {
        this.pcDescription = pcDescription;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "date_acquired")
    public Date getDateAcquired() {
        return dateAcquired;
    }

    public void setDateAcquired(Date dateAcquired) {
        this.dateAcquired = dateAcquired;
    }

    @Column(name = "serialno")
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Column(name = "property_number")
    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    @Column(name = "unit_value")
    public Double getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(Double unitValue) {
        this.unitValue = unitValue;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "labid")
    public NarridoLaboratory getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(NarridoLaboratory laboratory) {
        this.laboratory = laboratory;
    }

    @Column(name = "mr")
    public String getMr() {
        return mr;
    }

    public void setMr(String mr) {
        this.mr = mr;
    }

    @Column(name = "re_mr")
    public String getReMr() {
        return reMr;
    }

    public void setReMr(String reMr) {
        this.reMr = reMr;
    }
    
    @Override
    public String toString() {
        return "ID: " + id + "\nPC Name :" + pcName + "\nLocation: " + laboratory.getLabDescription() + "\nDescription: " + pcDescription;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.pcNumber);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NarridoPc other = (NarridoPc) obj;
        if (!Objects.equals(this.pcNumber, other.pcNumber)) {
            return false;
        }
        if (!Objects.equals(this.pcName, other.pcName)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
