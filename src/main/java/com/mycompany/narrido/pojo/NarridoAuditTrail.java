/*
 * Copyright 2017 princessmelisa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mycompany.narrido.pojo;

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
 * A.K.A. "Activity Log"
 * @author princessmelisa
 */
@Entity
@Table(name = "audit_trail")
public class NarridoAuditTrail implements Serializable{
    private Integer trailId;
    private NarridoUser who;
    private String description;
    private Date dateDone;

    public NarridoAuditTrail() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trailid")
    public Integer getTrailId() {
        return trailId;
    }

    public void setTrailId(Integer trailId) {
        this.trailId = trailId;
    }

    @ManyToOne
    @JoinColumn(name = "who")
    public NarridoUser getWho() {
        return who;
    }

    public void setWho(NarridoUser who) {
        this.who = who;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_done")
    public Date getDateDone() {
        return dateDone;
    }

    public void setDateDone(Date dateDone) {
        this.dateDone = dateDone;
    }
    
    
}
