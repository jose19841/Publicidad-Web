package com.example.backend.users.service.usecase;

import com.example.backend.users.controller.dto.AuthenticationRequest;
import com.example.backend.users.controller.dto.AuthenticationResponse;

/**
 * Caso de uso para gestionar la autenticación de usuarios.
 */
public interface AuthenticationUseCase {

    /**
     * Realiza el proceso de inicio de sesión validando credenciales
     * y generando un token JWT si el usuario es válido.
     *
     * @param request objeto con email y contraseña
     * @return respuesta con el token JWT generado
     */
    AuthenticationResponse login(AuthenticationRequest request);


}
