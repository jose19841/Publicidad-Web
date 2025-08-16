package com.example.backend.users.service.implement;

import com.example.backend.config.security.CookieService;
import com.example.backend.users.domain.ResetToken;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.ResetTokenRepository;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.EmailService;
import com.example.backend.users.service.usecase.SendResetTokenUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${frontend.base.url}")   // 👈 inyecta la URL base del frontend
    private String frontendBaseUrl;

    /**
     * Envía un token de recuperación de contraseña al correo del usuario.
     *
     * @param email email del usuario
     */
    @Override
    @Transactional  // 👈 clave para que deleteByUser y save estén en la misma transacción
    public void execute(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        //  Buscar token existente
        resetTokenRepository.findByUser(user).ifPresent(existingToken -> {
            if (existingToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                resetTokenRepository.delete(existingToken); // está vencido, lo borro
            } else {
                throw new IllegalStateException("Ya se ha enviado el correo de recuperación correctamente. Verifique su casilla de correo o en la sección de Spam.");
            }
        });

        String token = UUID.randomUUID().toString();
        ResetToken resetToken = new ResetToken(token, user, LocalDateTime.now().plusMinutes(30));
        resetTokenRepository.save(resetToken);

        String link = frontendBaseUrl + "/reset-password?token=" + token;

        emailService.send(
                email,
                "Recuperación de contraseña",
                "Haz clic en el siguiente enlace para restablecer tu contraseña:\n" + link +
                        "\n\nEste enlace expirará en 30 minutos."
        );
    }
}
