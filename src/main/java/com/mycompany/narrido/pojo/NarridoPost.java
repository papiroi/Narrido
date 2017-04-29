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
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author princessmelisa
 */
@JsonIgnoreProperties(value = {"comments", "user", "group"}, allowGetters = true)
@Entity
@Table(name = "posts")
public class NarridoPost implements Serializable {
    
    private Integer postId;
    private String postContent;
    private NarridoUser user;
    private NarridoGroup group;
    private List<NarridoComment> comments;
    private NarridoFile files;
    private Date date;

    public NarridoPost() {
    }

    public NarridoPost(Integer postId, String postContent, NarridoUser user, NarridoGroup group) {
        this.postId = postId;
        this.postContent = postContent;
        this.user = user;
        this.group = group;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postid")
    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    @Column(name = "post_content")
    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    @JsonManagedReference(value = "user-posts")
    @ManyToOne
    @JoinColumn(name = "userid")
    public NarridoUser getUser() {
        return user;
    }

    public void setUser(NarridoUser user) {
        this.user = user;
    }

    @JsonManagedReference(value = "group-posts")
    @ManyToOne
    @JoinColumn(name = "group_id")
    public NarridoGroup getGroup() {
        return group;
    }

    public void setGroup(NarridoGroup group) {
        this.group = group;
    }

    @JsonBackReference(value = "post-comments")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    public List<NarridoComment> getComments() {
        return comments;
    }

    public void setComments(List<NarridoComment> comments) {
        this.comments = comments;
    }

    @JsonManagedReference(value = "file-post")
    //@JsonBackReference(value = "post-file")
    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public NarridoFile getFiles() {
        return files;
    }

    public void setFiles(NarridoFile files) {
        this.files = files;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_posted")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}
