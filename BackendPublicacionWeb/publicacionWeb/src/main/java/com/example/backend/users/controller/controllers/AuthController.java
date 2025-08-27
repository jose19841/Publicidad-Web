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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Operaciones de inicio de sesión, cierre y gestión de contraseñas")
@Slf4j
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final GetUserSessionUseCase getUserSession;
    private final LogoutUseCase logout;
    private final LoginUseCase login;

    /**
     * Iniciar sesión
     */
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
        log.info("Intento de login para usuario: {}", request.getEmail());
        UserResponseDTO dto = login.execute(request, response);
        log.info("Login exitoso para usuario: {}", dto.getEmail());
        return ResponseEntity.ok(dto);
    }

    /**
     * Cerrar sesión
     */
    @Operation(
            summary = "Cerrar sesión",
            description = "Elimina la cookie con el JWT para cerrar sesión.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logout exitoso")
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        log.info("Logout solicitado");
        logout.execute(response);
        log.info("Logout exitoso");
        return ResponseEntity.ok("Logout exitoso");
    }

    /**
     * Obtener sesión actual
     */
    @Operation(
            summary = "Obtener sesión actual",
            description = "Obtiene información del usuario autenticado desde la cookie."
    )
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getSession(HttpServletRequest request) {
        log.info("Obteniendo sesión actual");
        UserResponseDTO user = getUserSession.execute(request);
        log.info("Sesión obtenida para usuario: {}", user.getEmail());
        return ResponseEntity.ok(user);
    }

    /**
     * Solicitar token de recuperación de contraseña
     */
    @Operation(
            summary = "Solicitar token de recuperación de contraseña",
            description = "Envía un token de restablecimiento de contraseña al correo del usuario.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Correo enviado correctamente")
            }
    )
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailRequest request) {
        log.info("Solicitud de recuperación de contraseña para: {}", request.getEmail());
        userService.sendResetToken(request.getEmail());
        log.info("Token de recuperación enviado (si existe el correo en el sistema)");
        return ResponseEntity.ok("Si el correo está registrado, se envió el enlace.");
    }

    /**
     * Restablecer contraseña
     */
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
        log.info("Solicitud de reseteo de contraseña con token: {}", request.getToken());
        userService.resetPassword(request.getToken(), request.getNewPassword());
        log.info("Contraseña actualizada con éxito para token: {}", request.getToken());
        return ResponseEntity.ok("Contraseña actualizada con éxito.");
    }
}
