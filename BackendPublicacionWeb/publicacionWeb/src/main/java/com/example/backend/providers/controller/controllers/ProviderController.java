package com.example.backend.providers.controller.controllers;

import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.service.Mapper.ProviderMapper;
import com.example.backend.providers.service.ProviderService;
import com.example.backend.providers.service.implementation.SearchProviderService;
import com.example.backend.providers.service.usecase.SearchProvidersByNameOrCategoryUsecase;
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
@Tag(name = "Proveedor", description = "API para administrar prestadores")
public class ProviderController {

    private final ProviderService providerService;
    private final SearchProvidersByNameOrCategoryUsecase searchProvidersByNameOrCategoryUsecase;


    @Operation(
            summary = "Crear Prestador",
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
            summary = "Listar Prestadores",
            description = "Recupera una lista de todos los prestadores"
    )
    @ApiResponse(responseCode = "200", description = "List of providers retrieved successfully")
    @GetMapping("/getAll")
    public ResponseEntity<List<ProviderResponseDTO>> getAll() {
        return ResponseEntity.ok(providerService.getAll());
    }

    @Operation(
            summary = "Actualizar Prestador",
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
            summary = "Inhabilitar Prestador",
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

    @Operation(
            summary = "Buscar Prestador por ID y aumentar contador de búsquedas",
            description = "Obtiene un prestador por su ID y registra la búsqueda"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider found successfully"),
            @ApiResponse(responseCode = "404", description = "Provider not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProviderResponseDTO> getProviderById(@PathVariable Long id) {
        return ResponseEntity.ok(providerService.search(id));
    }

    @Operation(
            summary = "Buscar prestadores por nombre o rubro",
            description = "Permite buscar prestadores filtrando por nombre, rubro o ambos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de prestadores obtenida correctamente"),
            @ApiResponse(responseCode = "204", description = "No se encontraron prestadores")
    })
    @GetMapping("/search")
    public ResponseEntity<List<ProviderResponseDTO>> searchProviders(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {
        List<ProviderResponseDTO> results = searchProvidersByNameOrCategoryUsecase.execute(name, category);

        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }
}

