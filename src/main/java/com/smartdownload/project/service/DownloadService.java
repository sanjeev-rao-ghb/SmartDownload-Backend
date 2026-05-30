package com.smartdownload.project.service;

import java.io.File;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.smartdownload.project.entity.Order;
import com.smartdownload.project.entity.Project;
import com.smartdownload.project.entity.Payment.PaymentStatus;
import com.smartdownload.project.repository.OrderRepository;
import com.smartdownload.project.repository.PaymentRepository;
import com.smartdownload.project.repository.ProjectRepository;

@Service
public class DownloadService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // 🔐 DOWNLOAD WITH PAYMENT CHECK
    public ResponseEntity<Resource> downloadProject(Integer orderId) {

        // 1️⃣ Order exists?
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2️⃣ Payment SUCCESS?
        boolean paid = paymentRepository.existsByOrderIdAndPaymentStatus(
                orderId,
                PaymentStatus.SUCCESS
        );

        if (!paid) {
            throw new RuntimeException("Payment not completed");
        }

        // 3️⃣ Get project
        Project project = projectRepository.findById(order.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // 4️⃣ Load file
        try {
            File file = new File(project.getFilePath());

            if (!file.exists()) {
                throw new RuntimeException("File not found on server");
            }

            byte[] fileBytes = Files.readAllBytes(file.toPath());
            ByteArrayResource resource = new ByteArrayResource(fileBytes);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getName() + "\""
                    )
                    .contentLength(file.length())
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("File download failed");
        }
    }
}
