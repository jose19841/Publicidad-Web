package com.example.backend.users.service.implement;

import com.example.backend.config.security.CookieService;
import com.example.backend.users.controller.dto.UserResponseDTO;
import com.example.backend.users.domain.User;
import com.example.backend.users.service.mapper.UserMapper;
import com.example.backend.users.service.usecase.GetUserSessionUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetUserSession implements GetUserSessionUseCase {

    private final CookieService cookieService;
    private final UserMapper userMapper;

    /**
     * Obtiene los datos del usuario autenticado desde la cookie.
     *
     * @param request petición HTTP con cookie de sesión
     * @return datos del usuario en formato UserResponseDTO
     */
    @Override
    public UserResponseDTO execute(HttpServletRequest request) {
        Optional<User> optionalUser = cookieService.getUserFromCookie(request);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Usuario no autenticado");
        }
        return userMapper.toDTO(optionalUser.get());
    }
}
