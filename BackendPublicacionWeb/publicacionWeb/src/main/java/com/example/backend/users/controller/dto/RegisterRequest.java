package com.example.backend.users.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO para registrar un nuevo usuario.
 * Contiene validaciones de entrada.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "El email no puede estar vacío.")
    @Email(message = "El email debe ser válido.")
    private String email;

    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres.")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    private String password;
}
