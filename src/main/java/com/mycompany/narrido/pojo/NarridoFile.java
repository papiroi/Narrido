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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author princessmelisa
 */
@JsonIgnoreProperties(value = {"uploader", "group"}, allowGetters = true)
@Entity
@Table(name = "file")
public class NarridoFile implements Serializable {
    private Integer fileid;
    private String fileUrl;
    private NarridoUser uploader;
    private NarridoPost post;
    private NarridoGroup group;
    private Date dateUploaded;
    private String fileName;
    private String fileType;

    public NarridoFile() {
    }

    public NarridoFile(Integer fileid, String fileUrl, NarridoUser uploader, NarridoPost post, Date dateUploaded) {
        this.fileid = fileid;
        this.fileUrl = fileUrl;
        this.uploader = uploader;
        this.post = post;
        this.dateUploaded = dateUploaded;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fileid")
    public Integer getFileid() {
        return fileid;
    }

    public void setFileid(Integer fileid) {
        this.fileid = fileid;
    }

    @Column(name = "file_url")
    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @ManyToOne
    @JoinColumn(name = "userid")
    public NarridoUser getUploader() {
        return uploader;
    }

    public void setUploader(NarridoUser uploader) {
        this.uploader = uploader;
    }

    @JsonBackReference(value = "file-post")
    //@JsonManagedReference(value = "post-file")
    @OneToOne
    @JoinColumn(name = "postid")
    public NarridoPost getPost() {
        return post;
    }

    public void setPost(NarridoPost post) {
        this.post = post;
    }

    @JsonManagedReference(value = "group-files")
    @ManyToOne
    @JoinColumn(name = "group_id")
    public NarridoGroup getGroup() {
        return group;
    }

    public void setGroup(NarridoGroup group) {
        this.group = group;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_uploaded")
    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    @Column(name = "file_name")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "file_type")
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
}
