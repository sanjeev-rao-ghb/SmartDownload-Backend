package com.smartdownload.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.smartdownload.project.dto.ProjectRequest;
import com.smartdownload.project.entity.Project;
import com.smartdownload.project.service.AdminService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ===== ALL PROJECTS =====
    @GetMapping("/projects")
    public List<Project> projects() {
        return adminService.getAllProjects();
    }

    // =======================================
    // 🔥 ADD PROJECT WITH ZIP + IMAGE
    // =======================================
    @PostMapping(
        value = "/projects",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Project addProject(
        @RequestPart("data") ProjectRequest data,

        // ZIP FILE
        @RequestPart("file") MultipartFile file,

        // IMAGE FILE (NEW)
        @RequestPart(value = "image", required = false)
        MultipartFile image
    ) {

        return adminService.addProject(data, file, image);
    }

    // ===== TOGGLE STATUS =====
    @PutMapping("/projects/{id}/status")
    public Project toggle(
        @PathVariable Integer id,
        @RequestParam Boolean isActive) {

        return adminService.updateProjectStatus(id, isActive);
    }

    // ===== DELETE PROJECT =====
    @DeleteMapping("/projects/{id}")
    public String deleteProject(@PathVariable Integer id) {

        adminService.deleteProject(id);

        return "Project Deleted";
    }

    // ===== UPDATE DETAILS =====
    @PutMapping("/projects/{id}")
    public Project updateProject(
            @PathVariable Integer id,
            @RequestBody ProjectRequest req) {

        return adminService.updateProject(id, req);
    }
}
