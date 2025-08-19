package com.example.backend.users.service.implement;

import com.example.backend.config.security.CookieService;
import com.example.backend.users.controller.dto.ChangePasswordDTO;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.usecase.ChangePasswordUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangePassword implements ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CookieService cookieService;

    /**
     * Cambia la contraseña del usuario autenticado.
     *
     * @param request            petición HTTP con la cookie de autenticación
     * @param changePasswordDTO  datos de la contraseña actual y nueva
     * @throws RuntimeException si la contraseña actual es incorrecta, o las nuevas contraseñas no coinciden
     */
    @Override
    public void execute(HttpServletRequest request, ChangePasswordDTO changePasswordDTO) {
        log.debug("Iniciando proceso de cambio de contraseña");

        Optional<User> optionalUser = cookieService.getUserFromCookie(request);
        if (optionalUser.isEmpty()) {
            log.warn("Intento de cambio de contraseña sin usuario autenticado");
            throw new RuntimeException("Usuario no autenticado");
        }

        User user = optionalUser.get();
        log.debug("Usuario autenticado: id={}, email={}", user.getId(), user.getEmail());

        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            log.error("Contraseña actual incorrecta para usuario id={}", user.getId());
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getRepeatPassword())) {
            log.error("Las nuevas contraseñas no coinciden para usuario id={}", user.getId());
            throw new RuntimeException("Las nuevas contraseñas no coinciden");
        }

        if (passwordEncoder.matches(changePasswordDTO.getNewPassword(), user.getPassword())) {
            log.error("La nueva contraseña es igual a la actual para usuario id={}", user.getId());
            throw new RuntimeException("La nueva contraseña no puede ser igual a la actual");
        }

        log.info("Cambio de contraseña válido para usuario id={}, procediendo a actualizar", user.getId());
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);

        log.info("Contraseña actualizada correctamente para usuario id={}", user.getId());
    }
}
