package com.example.backend.users.service.implement;

import com.example.backend.config.security.CookieService;
import com.example.backend.users.controller.dto.ChangePasswordDTO;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.ResetTokenRepository;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.EmailService;
import com.example.backend.users.service.usecase.ChangePasswordUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Optional<User> optionalUser = cookieService.getUserFromCookie(request);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Usuario no autenticado");
        }
        User user = optionalUser.get();
        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getRepeatPassword())) {
            throw new RuntimeException("Las nuevas contraseñas no coinciden");
        }
        if (passwordEncoder.matches(changePasswordDTO.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("La nueva contraseña no puede ser igual a la actual");
        }
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);

    }
}
