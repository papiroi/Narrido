/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "comments")
public class NarridoComment {
    
    private Integer commentId;
    private String commentContent;
    private NarridoUser user;
    private NarridoPost post;

    public NarridoComment() {
    }

    public NarridoComment(Integer commentId, String commentContent, NarridoUser user, NarridoPost post) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.user = user;
        this.post = post;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentid")
    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    @Column(name = "comment_content")
    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    @JsonManagedReference(value = "user-comments")
    @ManyToOne
    @JoinColumn(name = "userid")
    public NarridoUser getUser() {
        return user;
    }

    public void setUser(NarridoUser user) {
        this.user = user;
    }

    @JsonManagedReference(value = "post-comments")
    @ManyToOne
    @JoinColumn(name = "postid")
    public NarridoPost getPost() {
        return post;
    }

    public void setPost(NarridoPost post) {
        this.post = post;
    }
    
    
}
