package com.smartdownload.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.smartdownload.project.service.DownloadService;

@RestController
@RequestMapping("/api/downloads")
public class DownloadController {

    @Autowired
    private DownloadService downloadService;

    // 🔐 USER ONLY + PAYMENT CHECK
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}")
    public ResponseEntity<Resource> downloadProject(
            @PathVariable Integer orderId
    ) {
        return downloadService.downloadProject(orderId);
    }
}
