package com.example.backend.users.service.usecase;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Caso de uso para cerrar la sesión de un usuario.
 */
public interface LogoutUseCase {
    void execute(HttpServletResponse response);
}