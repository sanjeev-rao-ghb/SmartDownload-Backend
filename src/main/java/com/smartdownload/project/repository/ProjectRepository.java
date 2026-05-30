package com.smartdownload.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartdownload.project.entity.Project;

public interface ProjectRepository
        extends JpaRepository<Project, Integer> {
}
