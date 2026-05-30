package com.smartdownload.project.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smartdownload.project.dto.ProjectRequest;
import com.smartdownload.project.entity.Project;
import com.smartdownload.project.repository.ProjectRepository;

@Service
public class ProjectService {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private ProjectRepository projectRepository;

    // ===== ADMIN : ADD PROJECT =====
    public Project addProject(ProjectRequest req, MultipartFile file) {

        try {
            // 1. Create uploads folder if not exists
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 2. Create unique file name
            String fileName =
                System.currentTimeMillis() + "_" + file.getOriginalFilename();

            String filePath = UPLOAD_DIR + fileName;

            // 3. Save file
            file.transferTo(new File(filePath));

            // 4. Map DTO → Entity
            Project p = new Project();

            p.setTitle(req.getTitle());
            p.setDescription(req.getDescription());
            p.setPrice(req.getPrice());

            // 🔥 STRING → ENUM CONVERSION
            p.setLanguage(
                Project.Language.valueOf(
                    req.getLanguage().toUpperCase()
                )
            );

            p.setLevel(
                Project.Level.valueOf(
                    req.getLevel().toUpperCase()
                )
            );

            p.setFilePath(filePath);
            p.setIsActive(true);

            // 5. Save to DB
            return projectRepository.save(p);

        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }

    // ===== PUBLIC : ACTIVE PROJECTS =====
    public List<Project> getActiveProjects() {
        return projectRepository.findAll()
                .stream()
                .filter(p -> Boolean.TRUE.equals(p.getIsActive()))
                .toList();
    }

    // ===== GET BY ID =====
    public Project getById(Integer id) {
        return projectRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Project not found with id: " + id)
                );
    }

    // ===== DELETE (optional) =====
    public void delete(Integer id) {
        projectRepository.deleteById(id);
    }
}
