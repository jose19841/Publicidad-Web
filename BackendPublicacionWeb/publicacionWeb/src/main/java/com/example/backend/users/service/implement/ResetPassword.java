package com.example.backend.users.service.implement;

import com.example.backend.users.domain.ResetToken;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.ResetTokenRepository;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.usecase.ResetPasswordUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResetPassword implements ResetPasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResetTokenRepository resetTokenRepository;

    /**
     * Ejecuta el proceso de reseteo de contraseña a partir de un token válido.
     *
     * @param token       Token de recuperación
     * @param newPassword Nueva contraseña en texto plano
     */
    @Override
    public void execute(String token, String newPassword) {
        log.info("Iniciando proceso de reseteo de contraseña para token: {}", token);

        ResetToken resetToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("Intento fallido de reseteo: token inválido -> {}", token);
                    return new RuntimeException("Token inválido");
                });

        log.debug("Token encontrado en BD con expiración: {}", resetToken.getExpiryDate());

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.warn("Token expirado para usuario con email: {}", resetToken.getUser().getEmail());
            throw new RuntimeException("El enlace de recuperación ha expirado.");
        }

        User user = resetToken.getUser();
        log.info("Reseteando contraseña para el usuario con ID: {} y email: {}", user.getId(), user.getEmail());

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.debug("Contraseña actualizada en BD para usuario con ID: {}", user.getId());

        resetTokenRepository.delete(resetToken);
        log.info("Token de reseteo eliminado exitosamente para usuario con ID: {}", user.getId());
    }
}
