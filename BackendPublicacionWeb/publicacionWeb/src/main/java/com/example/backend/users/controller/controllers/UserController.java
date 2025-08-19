package com.example.backend.users.controller.controllers;

import com.example.backend.users.controller.dto.ChangePasswordDTO;
import com.example.backend.users.controller.dto.UserRequestDTO;
import com.example.backend.users.controller.dto.UserResponseDTO;
import com.example.backend.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class UserController {

    private final UserService userService;

    // PRIMER ENDPOINT
    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Registra un nuevo usuario con los datos proporcionados.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/userRegister")
    public ResponseEntity<Map<String, String>> userRegister(
            @Valid @RequestBody UserRequestDTO userRequest
    ) {
        log.info("Intentando registrar un nuevo usuario con email: {}", userRequest.getEmail());
        userService.save(userRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario registrado correctamente");
        log.info("Usuario registrado correctamente con email: {}", userRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // SEGUNDO ENDPOINT
    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Devuelve una lista de todos los usuarios registrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsers() {
        log.info("Obteniendo lista de todos los usuarios");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // TERCER ENDPOINT
    @Operation(
            summary = "Actualizar un usuario",
            description = "Actualiza los datos del usuario con el ID proporcionado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id,
            @RequestBody UserRequestDTO userRequest
    ) {
        log.info("Actualizando usuario con ID: {}", id);
        userService.updateUser(id, userRequest);
        log.info("Usuario actualizado correctamente con ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario actualizado correctamente");
    }

    // CUARTO ENDPOINT
    @Operation(
            summary = "Cambiar contraseña",
            description = "Permite al usuario cambiar su contraseña actual.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contraseña actualizada correctamente"),
                    @ApiResponse(responseCode = "400", description = "Error en los datos proporcionados")
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            HttpServletRequest request,
            @RequestBody ChangePasswordDTO changePasswordDTO
    ) {
        log.info("Usuario intentando cambiar su contraseña");
        userService.changePassword(request, changePasswordDTO);
        log.info("Contraseña actualizada correctamente");
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    // QUINTO ENDPOINT
    @Operation(
            summary = "Obtener usuario en sesión",
            description = "Obtiene los datos del usuario actualmente autenticado mediante cookie JWT.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Datos del usuario obtenidos correctamente"),
                    @ApiResponse(responseCode = "401", description = "No autorizado")
            }
    )
    @GetMapping("/getUserSession")
    public ResponseEntity<UserResponseDTO> getUserSession(HttpServletRequest request) {
        log.info("Obteniendo usuario en sesión desde la cookie JWT");
        return ResponseEntity.ok(userService.getUserSession(request));
    }

    // SEXTO ENDPOINT
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")  // Solo ADMIN puede acceder
    @Operation(
            summary = "Cambiar estado del usuario",
            description = "Permite a un administrador habilitar o inhabilitar un usuario enviando solo su ID."
    )
    public ResponseEntity<UserResponseDTO> changeStatus(@PathVariable Long id) {
        log.info("Cambiando estado del usuario con ID: {}", id);
        UserResponseDTO updated = userService.changeUserStatus(id);
        log.info("Estado del usuario con ID {} cambiado correctamente", id);
        return ResponseEntity.ok(updated);
    }
}
