package com.smartdownload.project.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smartdownload.project.entity.PasswordResetToken;
import com.smartdownload.project.entity.User;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    // 🔥 ADD THIS
    Optional<PasswordResetToken> findByUser(User user);
}
