/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author princessmelisa
 */
@Entity
@Table(name = "access_codes")
public class NarridoAccessCode implements Serializable {
    
    private Integer codeId;
    private String accessCode;
    private String type;
    private NarridoUser user;
    private NarridoGroup group;

    public NarridoAccessCode() {
    }

    public NarridoAccessCode(Integer codeId, String accessCode, String type, NarridoUser user, NarridoGroup group) {
        this.codeId = codeId;
        this.accessCode = accessCode;
        this.type = type;
        this.user = user;
        this.group = group;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "codeid")
    public Integer getCodeId() {
        return codeId;
    }

    public void setCodeId(Integer codeId) {
        this.codeId = codeId;
    }

    @Column(name = "access_code")
    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @OneToOne
    @JoinColumn(name = "userid")
    public NarridoUser getUser() {
        return user;
    }

    public void setUser(NarridoUser user) {
        this.user = user;
    }

    @JsonIgnoreProperties(allowGetters = true)
    @JsonManagedReference(value = "group-codes")
    @ManyToOne
    @JoinColumn(name = "group_id")
    public NarridoGroup getGroup() {
        return group;
    }

    public void setGroup(NarridoGroup group) {
        this.group = group;
    }
    
}
