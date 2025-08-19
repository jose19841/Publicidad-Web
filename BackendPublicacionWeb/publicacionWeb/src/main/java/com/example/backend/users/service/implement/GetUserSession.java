package com.example.backend.users.service.implement;

import com.example.backend.config.security.CookieService;
import com.example.backend.users.controller.dto.UserResponseDTO;
import com.example.backend.users.domain.User;
import com.example.backend.users.service.mapper.UserMapper;
import com.example.backend.users.service.usecase.GetUserSessionUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
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
        log.debug("Buscando usuario autenticado en la cookie de sesión...");

        Optional<User> optionalUser = cookieService.getUserFromCookie(request);

        if (optionalUser.isEmpty()) {
            log.warn("No se encontró usuario autenticado en la cookie de sesión.");
            throw new AuthenticationCredentialsNotFoundException("Usuario no autenticado");
        }

        UserResponseDTO dto = userMapper.toDTO(optionalUser.get());
        log.info("Sesión válida encontrada. Usuario autenticado: id={}, email={}",
                dto.getId(), dto.getEmail());

        return dto;
    }
}
