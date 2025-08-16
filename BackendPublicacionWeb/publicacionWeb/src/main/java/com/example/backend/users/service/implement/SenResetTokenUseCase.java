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

        String htmlContent =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "  <meta charset='UTF-8'>" +
                        "  <style>" +
                        "    .container { font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 8px; background: #fafafa; }" +
                        "    h2 { color: #2c3e50; }" +
                        "    p { color: #555; font-size: 15px; }" +
                        "    .button { display: inline-block; margin-top: 20px; padding: 12px 20px; background: #1976d2; color: white; text-decoration: none; border-radius: 6px; font-weight: bold; }" +
                        "    .footer { margin-top: 30px; font-size: 12px; color: #888; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class='container'>" +
                        "    <h2>🔑 Recuperación de contraseña</h2>" +
                        "    <p>Recibimos una solicitud para restablecer tu contraseña. Si fuiste vos, hacé clic en el siguiente botón:</p>" +
                        "    <a href='" + link + "' class='button'>Restablecer contraseña</a>" +
                        "    <p>Este enlace expirará en <b>30 minutos</b>. Si no solicitaste este cambio, podés ignorar este mensaje.</p>" +
                        "    <div class='footer'>Imperial-Net © 2025</div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";


        emailService.sendHtml(
                email,
                "Recuperación de contraseña",
                htmlContent
        );
    }
}
