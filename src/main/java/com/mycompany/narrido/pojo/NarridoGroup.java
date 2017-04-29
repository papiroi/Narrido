/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author princessmelisa
 */
@JsonIgnoreProperties(value = {"owner", "membership", "posts", "codes"}, allowGetters = true)
@Entity
@Table(name = "user_group")
public class NarridoGroup implements Serializable {

    private Integer groupId;
    private String type;
    private String groupName;
    private NarridoUser owner;
    private List<NarridoMembership> membership;
    private List<NarridoPost> posts;
    private List<NarridoAccessCode> codes;
    private List<NarridoFile> files;

    public NarridoGroup() {
    }

    public NarridoGroup(Integer groupId, String type, String groupName, NarridoUser owner, List<NarridoMembership> membership, List<NarridoPost> posts) {
        this.groupId = groupId;
        this.type = type;
        this.groupName = groupName;
        this.owner = owner;
        this.membership = membership;
        this.posts = posts;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "group_name")
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @JsonManagedReference(value = "user-groups")
    @ManyToOne
    @JoinColumn(name = "userid_owner")
    public NarridoUser getOwner() {
        return owner;
    }

    public void setOwner(NarridoUser owner) {
        this.owner = owner;
    }

    @JsonBackReference(value = "group-membership")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "group")
    public List<NarridoMembership> getMembership() {
        return membership;
    }

    public void setMembership(List<NarridoMembership> membership) {
        this.membership = membership;
    }

    @JsonBackReference(value = "group-posts")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "group")
    public List<NarridoPost> getPosts() {
        return posts;
    }

    public void setPosts(List<NarridoPost> posts) {
        this.posts = posts;
    }

    @JsonBackReference(value = "group-codes")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "group")
    public List<NarridoAccessCode> getCodes() {
        return codes;
    }

    public void setCodes(List<NarridoAccessCode> codes) {
        this.codes = codes;
    }

    @JsonBackReference(value = "group-files")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "group")
    public List<NarridoFile> getFiles() {
        return files;
    }

    public void setFiles(List<NarridoFile> files) {
        this.files = files;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.groupId);
        hash = 29 * hash + Objects.hashCode(this.type);
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
        final NarridoGroup other = (NarridoGroup) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.groupId, other.groupId)) {
            return false;
        }
        return true;
    }

    
    
}
