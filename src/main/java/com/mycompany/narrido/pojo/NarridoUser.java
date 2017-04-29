/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author princessmelisa
 */
@JsonIgnoreProperties(value = {"ownedGroups", "joinedGroups", "posts", "comments"}, allowGetters = true)
@Entity
@Table(name = "users")
public class NarridoUser implements Serializable {
    private Integer userid;
    private Integer idNumber; //ie. student nr.
    private String username;
    private String password;
    private String salt;
    private String type;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private Boolean isConfirmed;
    private List<NarridoGroup> ownedGroups = new ArrayList<>();
    private List<NarridoMembership> joinedGroups = new ArrayList<>();
    private List<NarridoPost> posts = new ArrayList<>();
    private List<NarridoComment> comments = new ArrayList<>();

    public NarridoUser() {
    }

    public NarridoUser(Integer userid, Integer idNumber, String username, String password, String salt, String type, String firstName, String middleName, String lastName, String address, Boolean isConfirmed, List<NarridoGroup> ownedGroups, List<NarridoMembership> joinedGroups, List<NarridoPost> posts, List<NarridoComment> comments) {
        this.userid = userid;
        this.idNumber = idNumber;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.type = type;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.address = address;
        this.isConfirmed = isConfirmed;
        this.ownedGroups = ownedGroups;
        this.joinedGroups = joinedGroups;
        this.posts = posts;
        this.comments = comments;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
    
    @Column(name = "id_number")
    public Integer getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(Integer idNumber) {
        this.idNumber = idNumber;
    }
    
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnoreProperties(allowSetters = true) //we do not want to show the password hash
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnoreProperties(allowSetters = true) 
    @Column(name = "salt")
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "middle_name")
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "is_confirmed", nullable = true, insertable = false)
    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    @JsonBackReference(value = "user-groups")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    public List<NarridoGroup> getOwnedGroups() {
        return ownedGroups;
    }

    public void setOwnedGroups(List<NarridoGroup> ownedGroups) {
        this.ownedGroups = ownedGroups;
    }

    @JsonBackReference(value = "user-membership")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    public List<NarridoMembership> getJoinedGroups() {
        return joinedGroups;
    }

    public void setJoinedGroups(List<NarridoMembership> joinedGroups) {
        this.joinedGroups = joinedGroups;
    }

    @JsonBackReference(value = "user-posts")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    public List<NarridoPost> getPosts() {
        return posts;
    }
    
    public void setPosts(List<NarridoPost> posts) {
        this.posts = posts;
    }    
    
    @JsonBackReference(value = "user-comments")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    public List<NarridoComment> getComments() {
        return comments;
    }

    public void setComments(List<NarridoComment> comments) {
        this.comments = comments;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.userid);
        hash = 97 * hash + Objects.hashCode(this.username);
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
        final NarridoUser other = (NarridoUser) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.userid, other.userid)) {
            return false;
        }
        return true;
    }
    
}
