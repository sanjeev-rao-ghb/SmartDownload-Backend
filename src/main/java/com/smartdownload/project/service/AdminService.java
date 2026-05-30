package com.smartdownload.project.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smartdownload.project.dto.ProjectRequest;
import com.smartdownload.project.entity.Project;
import com.smartdownload.project.repository.DownloadRepository;
import com.smartdownload.project.repository.OrderRepository;
import com.smartdownload.project.repository.ProjectRepository;

@Service
public class AdminService {

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private DownloadRepository downloadRepo;

    // ==========================================
    // GET ALL PROJECTS
    // ==========================================
    public List<Project> getAllProjects() {
        return projectRepo.findAll();
    }

    // ==========================================
    // 🔥 ADD PROJECT WITH ZIP + IMAGE
    // ==========================================
    public Project addProject(
            ProjectRequest data,
            MultipartFile file,
            MultipartFile image
    ) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("ZIP file required");
        }

        if (!file.getOriginalFilename().endsWith(".zip")) {
            throw new RuntimeException("Only ZIP files allowed");
        }

        try {

            // ===== ZIP FILE SAVE =====
            String zipDir = "uploads/projects";
            new File(zipDir).mkdirs();

            String zipName =
                UUID.randomUUID() + "_" +
                file.getOriginalFilename()
                    .replaceAll("[^a-zA-Z0-9.]", "_");

            Path zipPath = Paths.get(zipDir, zipName);

            Files.write(zipPath, file.getBytes());

            // ===== IMAGE SAVE =====
            String imagePath = null;

            if (image != null && !image.isEmpty()) {

                String imgDir = "uploads/images";
                new File(imgDir).mkdirs();

                String imgName =
                    UUID.randomUUID() + "_" +
                    image.getOriginalFilename()
                        .replaceAll("[^a-zA-Z0-9.]", "_");

                Path imgPath = Paths.get(imgDir, imgName);

                Files.write(imgPath, image.getBytes());

                imagePath = imgPath.toString();
            }

            // ===== CREATE ENTITY =====
            Project p = new Project();

            p.setTitle(data.getTitle());
            p.setDescription(data.getDescription());
            p.setPrice(data.getPrice());

            p.setLanguage(
                Project.Language.valueOf(
                    data.getLanguage().toUpperCase()
                )
            );

            p.setLevel(
                Project.Level.valueOf(
                    data.getLevel().toUpperCase()
                )
            );

            p.setFilePath(zipPath.toString());
            p.setImagePath(imagePath);   // 🔥 image save
            p.setIsActive(true);

            return projectRepo.save(p);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid language or level value");

        } catch (Exception e) {
            throw new RuntimeException(
                "Upload failed: " + e.getMessage()
            );
        }
    }

    // ==========================================
    // TOGGLE STATUS
    // ==========================================
    public Project updateProjectStatus(
            Integer id,
            Boolean active
    ) {

        Project p = projectRepo.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Project not found")
            );

        p.setIsActive(active);

        return projectRepo.save(p);
    }

    // ==========================================
    // 🔥 DELETE PROJECT (FK SAFE)
    // ==========================================
    public void deleteProject(Integer id) {

        Project p = projectRepo.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Project not found")
            );

        // 1️⃣ DELETE CHILD TABLE DATA FIRST
        downloadRepo.deleteAll(
            downloadRepo.findByProjectId(id)
        );

        orderRepo.deleteAll(
            orderRepo.findByUserId(id)
        );

        // 2️⃣ DELETE FILES FROM STORAGE
        try {

            if (p.getFilePath() != null) {
                Files.deleteIfExists(
                    Paths.get(p.getFilePath())
                );
            }

            if (p.getImagePath() != null) {
                Files.deleteIfExists(
                    Paths.get(p.getImagePath())
                );
            }

        } catch (Exception e) {
            System.out.println(
                "File delete issue: " + e.getMessage()
            );
        }

        // 3️⃣ DELETE PROJECT RECORD
        projectRepo.deleteById(id);
    }

    // ==========================================
    // UPDATE DETAILS ONLY
    // ==========================================
    public Project updateProject(
            Integer id,
            ProjectRequest req
    ) {

        Project p = projectRepo.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Project not found")
            );

        p.setTitle(req.getTitle());
        p.setPrice(req.getPrice());
        p.setDescription(req.getDescription());

        return projectRepo.save(p);
    }
}
