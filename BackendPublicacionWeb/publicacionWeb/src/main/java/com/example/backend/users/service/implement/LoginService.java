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
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final AuthenticationUseCase authentication;
    private final CookieService cookieService;
    private final UserMapper userMapper;
    private final GetUserByEmailUseCase getUserByEmail;

    @Override
    public UserResponseDTO execute(AuthenticationRequest request, HttpServletResponse response) {
        // Autenticación y generación del token
        AuthenticationResponse authResponse = authentication.login(request);

        // Crear cookie con el token
        ResponseCookie cookie = cookieService.createAuthCookie(authResponse.getToken());
        response.addHeader("Set-Cookie", cookie.toString());

        // Retornar DTO del usuario autenticado
        return userMapper
                .toDTO(getUserByEmail.execute(request.getEmail()).get());
    }
}
