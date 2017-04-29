/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mycompany.narrido.helper.NarridoGeneric;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author princessmelisa
 */
@Entity
@Table(name = "user_group_members")
public class NarridoMembership {
    private Integer membershipId;
    private NarridoUser user;
    private NarridoGroup group;
    private Boolean confirmed;
    private NarridoAccessCode code;

    public NarridoMembership() {
    }

    public NarridoMembership(Integer membershipId, NarridoUser user, NarridoGroup group, Boolean confirmed) {
        this.membershipId = membershipId;
        this.user = user;
        this.group = group;
        this.confirmed = confirmed;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membershipid")
    public Integer getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Integer membershipId) {
        this.membershipId = membershipId;
    }

    @JsonManagedReference(value = "user-membership")
    @ManyToOne
    @JoinColumn(name = "userid")
    public NarridoUser getUser() {
        return user;
    }

    public void setUser(NarridoUser user) {
        this.user = user;
    }

    @JsonManagedReference(value = "group-membership")
    @ManyToOne
    @JoinColumn(name = "group_id")
    public NarridoGroup getGroup() {
        return group;
    }

    public void setGroup(NarridoGroup group) {
        this.group = group;
    }

    @Column(name = "confirmed", insertable = false)
    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    @ManyToOne
    @JoinColumn(name = "codeid")
    public NarridoAccessCode getCode() {
        return code;
    }

    public void setCode(NarridoAccessCode code) {
        this.code = code;
    }
    
    
}
