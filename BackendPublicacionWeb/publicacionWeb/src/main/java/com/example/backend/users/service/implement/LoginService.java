package com.example.backend.users.service.implement;

import com.example.backend.config.security.CookieService;
import com.example.backend.users.controller.dto.AuthenticationRequest;
import com.example.backend.users.controller.dto.AuthenticationResponse;
import com.example.backend.users.controller.dto.UserResponseDTO;
import com.example.backend.users.service.mapper.UserMapper;
import com.example.backend.users.service.usecase.AuthenticationUseCase;
import com.example.backend.users.service.usecase.GetUserByEmailUseCase;
import com.example.backend.users.service.usecase.LoginUseCase;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final AuthenticationUseCase authentication;
    private final CookieService cookieService;
    private final UserMapper userMapper;
    private final GetUserByEmailUseCase getUserByEmail;

    @Override
    public UserResponseDTO execute(AuthenticationRequest request, HttpServletResponse response) {
        log.info("Intentando autenticar usuario con email: {}", request.getEmail());

        // Autenticación y generación del token
        AuthenticationResponse authResponse = authentication.login(request);
        log.debug("Token JWT generado para usuario {}", request.getEmail());

        // Crear cookie con el token
        ResponseCookie cookie = cookieService.createAuthCookie(authResponse.getToken());
        response.addHeader("Set-Cookie", cookie.toString());
        log.info("Cookie de autenticación creada y añadida a la respuesta para usuario {}", request.getEmail());

        // Retornar DTO del usuario autenticado
        UserResponseDTO userDTO = userMapper.toDTO(
                getUserByEmail.execute(request.getEmail()).orElseThrow(() -> {
                    log.error("No se encontró el usuario con email {} después de autenticar.", request.getEmail());
                    return new RuntimeException("Usuario no encontrado tras autenticación.");
                })
        );

        log.info("Usuario {} autenticado exitosamente con ID {}", userDTO.getEmail(), userDTO.getId());
        return userDTO;
    }
}
