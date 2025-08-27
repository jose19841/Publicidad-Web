/**
 * DTO que representa la respuesta de autenticación enviada al cliente.
 * Contiene el token JWT generado tras una autenticación exitosa.
 */
package com.example.backend.users.controller.dto;

import lombok.*;

/**
 * DTO de respuesta para la autenticación (login).
 * Devuelve la información necesaria para mantener la sesión.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {

    private String token;       // JWT
    private String username;    // para mostrar en frontend
    private String email;
    private String role;
}
