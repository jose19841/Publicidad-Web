package com.example.backend.users.service.implement;

import com.example.backend.config.security.CookieService;
import com.example.backend.users.domain.ResetToken;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.ResetTokenRepository;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.EmailService;
import com.example.backend.users.service.usecase.ResetPasswordUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ResetPassword implements ResetPasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResetTokenRepository resetTokenRepository;



    @Override
    public void execute(String token, String newPassword) {
        ResetToken resetToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El enlace de recuperación ha expirado.");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetTokenRepository.delete(resetToken);
    }

}
