/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author princessmelisa
 */
@JsonIgnoreProperties(value = {"monitoring"}, allowGetters = true)
@Entity
@Table(name = "labs")
public class NarridoLaboratory implements Serializable {
    private Integer labId;
    private String labDescription;
    private List<NarridoDailyMonitoring> monitoring;
    private List<NarridoPc> pcs;

    public NarridoLaboratory() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "labid")
    public Integer getLabId() {
        return labId;
    }

    public void setLabId(Integer labId) {
        this.labId = labId;
    }

    @Column(name = "lab_description")
    public String getLabDescription() {
        return labDescription;
    }

    public void setLabDescription(String labDescription) {
        this.labDescription = labDescription;
    }

    @JsonBackReference("laboratory-daily")
    @OneToMany(mappedBy = "laboratory")
    public List<NarridoDailyMonitoring> getMonitoring() {
        return monitoring;
    }

    public void setMonitoring(List<NarridoDailyMonitoring> monitoring) {
        this.monitoring = monitoring;
    }

    @OneToMany
    public List<NarridoPc> getPcs() {
        return pcs;
    }

    public void setPcs(List<NarridoPc> pcs) {
        this.pcs = pcs;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.labId);
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
        final NarridoLaboratory other = (NarridoLaboratory) obj;
        return Objects.equals(this.labId, other.labId);
    }

    
}
