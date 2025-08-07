package com.example.backend.users.controller.controllers;

import com.example.backend.users.controller.dto.*;
import com.example.backend.users.service.UserService;
import com.example.backend.users.service.mapper.UserMapper;
import com.example.backend.users.service.usecase.GetUserSessionUseCase;
import com.example.backend.users.service.usecase.LoginUseCase;
import com.example.backend.users.service.usecase.LogoutUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Operaciones de inicio de sesión, cierre y gestión de contraseñas")
public class AuthController {


    private final UserService userService;
    private final UserMapper userMapper;
    private final GetUserSessionUseCase getUserSession;
    private final LogoutUseCase logout;
    private final LoginUseCase login;

    //PRIMER ENDPOINT
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario con email y contraseña. Genera un JWT y lo guarda en una cookie.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login exitoso"),
                    @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
                    @ApiResponse(responseCode = "403", description = "Usuario deshabilitado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(
            @Valid @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(login.execute(request, response));
    }
    //SEGUNDO ENDPOINT
    @Operation(
            summary = "Cerrar sesión",
            description = "Elimina la cookie con el JWT para cerrar sesión.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logout exitoso")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        logout.execute(response);
        return ResponseEntity.ok("Logout exitoso");
    }
    //TERCER ENDPOINT
    @Operation(
            summary = "Obtener sesión actual",
            description = "Obtiene información del usuario autenticado desde la cookie."
    )
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getSession(HttpServletRequest request) {
        UserResponseDTO user = getUserSession.execute(request);
        return ResponseEntity.ok(user);
    }

    //CUARTO ENDPOINT
    @Operation(
            summary = "Solicitar token de recuperación de contraseña",
            description = "Envía un token de restablecimiento de contraseña al correo del usuario.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Correo enviado correctamente")
            }
    )
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailRequest request) {
        userService.sendResetToken(request.getEmail());
        return ResponseEntity.ok("Si el correo está registrado, se envió el enlace.");
    }
    //QUINTO ENDPOINT
    @Operation(
            summary = "Restablecer contraseña",
            description = "Permite restablecer la contraseña de un usuario mediante un token válido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contraseña restablecida correctamente"),
                    @ApiResponse(responseCode = "400", description = "Token inválido o expirado")
            }
    )
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Contraseña actualizada con éxito.");
    }
}
