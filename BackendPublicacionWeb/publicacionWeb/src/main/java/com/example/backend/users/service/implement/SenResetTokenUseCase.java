package com.example.backend.users.service.implement;

import com.example.backend.config.security.CookieService;
import com.example.backend.users.domain.ResetToken;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.ResetTokenRepository;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.EmailService;
import com.example.backend.users.service.usecase.SendResetTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SenResetTokenUseCase implements SendResetTokenUseCase {

    private final UserRepository userRepository;

    private final ResetTokenRepository resetTokenRepository;
    private final EmailService emailService;

    /**
     * Envía un token de recuperación de contraseña al correo del usuario.
     *
     * @param email email del usuario
     */
    @Override
    public void execute(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return;

        User user = userOpt.get();
        String token = UUID.randomUUID().toString();
        ResetToken resetToken = new ResetToken(token, user, LocalDateTime.now().plusMinutes(30));
        resetTokenRepository.save(resetToken);

        String link = "http://localhost:3000/reset-password?token=" + token;
        emailService.send(
                email,
                "Recuperación de contraseña",
                "Haz clic en el siguiente enlace para restablecer tu contraseña:\n" + link + "\n\nEste enlace expirará en 30 minutos."
        );
    }
}
