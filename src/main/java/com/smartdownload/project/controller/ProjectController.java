package com.smartdownload.project.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.smartdownload.project.entity.Project;
import com.smartdownload.project.entity.User;
import com.smartdownload.project.service.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired private ProjectService projectService;
    @Autowired private OrderService orderService;
    @Autowired private UserService userService;

    // ======================================
    // 🔓 PUBLIC – SHOW ALL ACTIVE PROJECTS
    // ======================================
    @GetMapping("/public")
    public List<Project> publicProjects() {
        return projectService.getActiveProjects();
    }

    // ======================================
    // 🖼 IMAGE VIEW API – SAFE VERSION
    // ======================================
    @GetMapping("/image/{id}")
    public ResponseEntity<?> getImage(
            @PathVariable Integer id) {

        try {
            Project p = projectService.getById(id);

            // 1️⃣ IMAGE UNDA LEKA CHECK
            if (p.getImagePath() == null) {
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No image for this project");
            }

            Path path = Paths.get(p.getImagePath());

            // 2️⃣ FILE EXIST CHECK
            if (!Files.exists(path)) {
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Image file not found");
            }

            Resource resource =
                new UrlResource(path.toUri());

            // 3️⃣ AUTO CONTENT TYPE
            String contentType =
                Files.probeContentType(path);

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);

        } catch (Exception e) {

            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Image load failed");
        }
    }

    // ======================================
    // 🔐 DOWNLOAD – ONLY IF PURCHASED
    // ======================================
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/download/{id}")
    public ResponseEntity<?> download(
            @PathVariable Integer id,
            Principal principal) {

        try {

            // Logged-in user
            User user = userService.getUserByEmail(
                    principal.getName());

            // Check purchased or not
            if (!orderService.hasUserPurchased(
                    user.getUserId(), id)) {

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body("Not purchased this project");
            }

            Project p = projectService.getById(id);

            Path path = Paths.get(p.getFilePath());

            if (!Files.exists(path)) {
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("File not found");
            }

            Resource resource =
                new UrlResource(path.toUri());

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" +
                    resource.getFilename() + "\"")
                .body(resource);

        } catch (Exception e) {

            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Download failed");
        }
    }
}
