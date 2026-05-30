package com.smartdownload.project.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartdownload.project.entity.PasswordResetToken;
import com.smartdownload.project.entity.User;
import com.smartdownload.project.repository.PasswordResetTokenRepository;
import com.smartdownload.project.repository.UserRepository;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordResetTokenRepository tokenRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ===============================
    // SEND RESET LINK
    // ===============================
    public void sendResetLinkSafely(String email) {

        userRepo.findByEmail(email).ifPresent(user -> {

            tokenRepo.findByUser(user)
                    .ifPresent(tokenRepo::delete);

            String token = UUID.randomUUID().toString();

            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUser(user);
            resetToken.setExpiryTime(
                    LocalDateTime.now().plusMinutes(15));
            resetToken.setUsed(false);

            tokenRepo.save(resetToken);

            String link =
                "http://localhost:5173/reset-password?token=" + token;

            emailService.sendResetEmail(email, link);
        });
    }

    // ===============================
    // RESET PASSWORD
    // ===============================
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken = tokenRepo.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Invalid token"));

        if (resetToken.isUsed()) {
            throw new RuntimeException("Token already used");
        }

        if (resetToken.getExpiryTime()
                .isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        resetToken.setUsed(true);
        tokenRepo.save(resetToken);
    }
}
