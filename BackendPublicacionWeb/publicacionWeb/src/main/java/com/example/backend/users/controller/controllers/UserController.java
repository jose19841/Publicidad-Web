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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class UserController {

    private final UserService userService;

    //PRIMER ENDPOINT
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
        userService.save(userRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario registrado correctamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //SEGUNDO ENDPOINT
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
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //TERCER ENDPOINT
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
        userService.updateUser(id, userRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario actualizado correctamente");
    }

    //CUARTO ENDPOINT
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
        userService.changePassword(request, changePasswordDTO);
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    //QUINTO ENDPOINT
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
        return ResponseEntity.ok(userService.getUserSession(request));
    }
}
