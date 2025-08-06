package com.example.backend.users.service.usecase;

import com.example.backend.users.controller.dto.AuthenticationRequest;
import com.example.backend.users.controller.dto.UserResponseDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Caso de uso para autenticar a un usuario e iniciar sesión.
 */
public interface LoginUseCase {
    UserResponseDTO execute(AuthenticationRequest request, HttpServletResponse response);
}
