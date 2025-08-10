package com.example.backend.providers.controller.controllers;

import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.service.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
@Tag(name = "Providers", description = "API for managing providers")
public class ProviderController {

    private final ProviderService providerService;

    @Operation(
            summary = "Crear Proveedor",
            description = "Crea un nuevo proveedor utilizando la información proporcionada"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/create")
    public ResponseEntity<ProviderResponseDTO> create(
            @Valid @RequestBody ProviderRequestDTO providerRequest) {
        return ResponseEntity.ok(providerService.create(providerRequest));
    }

    @Operation(
            summary = "Listar Proveedores",
            description = "Recupera una lista de todos los proveedores"
    )
    @ApiResponse(responseCode = "200", description = "List of providers retrieved successfully")
    @GetMapping("/getAll")
    public ResponseEntity<List<ProviderResponseDTO>> getAll() {
        return ResponseEntity.ok(providerService.getAll());
    }

    @Operation(
            summary = "Actualizar Proveedor",
            description = "Actualiza un proveedor existente por su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider updated successfully"),
            @ApiResponse(responseCode = "404", description = "Provider not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<ProviderResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProviderRequestDTO providerRequest) {
        return ResponseEntity.ok(providerService.update(id, providerRequest));
    }

    @Operation(
            summary = "Inhabilitar Proveedor",
            description = "Inhabilita un proveedor por su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Provider disabled successfully"),
            @ApiResponse(responseCode = "404", description = "Provider not found")
    })
    @DeleteMapping("/disable/{id}")
    public ResponseEntity<Void> disable(@PathVariable Long id) {
        providerService.disable(id);
        return ResponseEntity.noContent().build();
    }
}
