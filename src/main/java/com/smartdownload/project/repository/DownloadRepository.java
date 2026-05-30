package com.smartdownload.project.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.smartdownload.project.entity.Download;

public interface DownloadRepository extends JpaRepository<Download, Integer> {

    // 🔍 Get all downloads by a user
	List<Download> findByProjectId(Integer projectId);


    // 🔍 Check if user already downloaded a project
    boolean existsByUserIdAndProjectId(Integer userId, Integer projectId);
}
