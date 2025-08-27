package com.example.backend.users.controller.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateLoggedUserDTO {


    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres.")
    private String username;
}
