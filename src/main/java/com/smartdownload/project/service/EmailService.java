package com.smartdownload.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetEmail(String toEmail, String resetLink) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Vendora - Reset Your Password");
        message.setText(
            "Hello,\n\n" +
            "You requested to reset your password.\n\n" +
            "Click the link below to reset your password:\n" +
            resetLink + "\n\n" +
            "This link is valid for 15 minutes.\n\n" +
            "If you did not request this, please ignore this email.\n\n" +
            "Regards,\nVendora Team"
        );

        mailSender.send(message);
    }
}
