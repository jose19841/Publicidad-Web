package com.example.backend.providers.controller.controllers;

import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.service.Mapper.ProviderMapper;
import com.example.backend.providers.service.ProviderService;
import com.example.backend.providers.service.implementation.SearchProviderService;
import com.example.backend.providers.service.usecase.SearchProvidersByNameOrCategoryUsecase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
@Tag(name = "Proveedor", description = "API para administrar prestadores")
public class ProviderController {

    private final ProviderService providerService;
    private final SearchProvidersByNameOrCategoryUsecase searchProvidersByNameOrCategoryUsecase;
    private final ObjectMapper objectMapper;



    @Operation(
            summary = "Registrar proveedor con imagen",
            description = """
        Este endpoint permite registrar un nuevo proveedor.
        <br><br>
        El campo <code>provider</code> debe enviarse como un <b>String plano</b> que contenga un JSON con la estructura de <code>ProviderRequestDTO</code>.
        <br>
        El campo <code>image</code> permite adjuntar una imagen opcional del proveedor.
        """
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProviderResponseDTO> createProvider(

            @Parameter(
                    name = "provider",
                    description = "JSON como string plano representando un ProviderRequestDTO",
                    required = true,
                    schema = @Schema(implementation = ProviderRequestDTO.class),
                    examples = {
                            @ExampleObject(
                                    name = "Ejemplo ProviderRequestDTO",
                                    value = """
                    {
                      "name": "Juan",
                      "lastName": "Pérez",
                      "address": "Roca 601",
                      "phone": "2923530179",
                      "description": "Albañil las 24 hs",
                      "isActive": true,
                      "categoryId": 1
                    }
                """
                            )
                    }
            )
            @RequestPart("provider") String providerJson,

            @Parameter(
                    name = "image",
                    description = "Imagen del proveedor (formatos JPG, PNG o WEBP).",
                    required = false
            )
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws JsonProcessingException {

        ProviderRequestDTO provider = objectMapper.readValue(providerJson, ProviderRequestDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(providerService.create(provider, image));
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

