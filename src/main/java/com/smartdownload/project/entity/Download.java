package com.smartdownload.project.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "downloads")
public class Download {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "download_id")
    private Integer downloadId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "project_id", nullable = false)
    private Integer projectId;

    @Column(name = "download_time", updatable = false)
    private LocalDateTime downloadTime;

    // -------- Constructors --------
    public Download() {
        // default constructor
    }

    // -------- Lifecycle Callback --------
    @PrePersist
    protected void onCreate() {
        this.downloadTime = LocalDateTime.now();
    }

    // -------- Getters & Setters --------
    public Integer getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(Integer downloadId) {
        this.downloadId = downloadId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public LocalDateTime getDownloadTime() {
        return downloadTime;
    }
}
