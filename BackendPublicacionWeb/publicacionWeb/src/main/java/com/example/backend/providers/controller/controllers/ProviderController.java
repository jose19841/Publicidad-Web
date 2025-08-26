package com.example.backend.providers.controller.controllers;

import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.controller.dto.ProviderUpdateRequestDTO;
import com.example.backend.providers.service.ProviderService;
import com.example.backend.providers.service.usecase.SearchProvidersByNameOrCategoryUsecase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
@Tag(name = "Proveedores", description = "API para administrar prestadores")
public class ProviderController {

    private final ProviderService providerService;
    private final SearchProvidersByNameOrCategoryUsecase searchProvidersByNameOrCategoryUsecase;
    private final ObjectMapper objectMapper;

    //ENDPOINT PARA REGISTRAR UN NUEVO PROVEEDOR CON IMAGEN -admin
    @Operation(
            summary = "Registrar proveedor con imagen",
            description = """
                    Este endpoint permite registrar un nuevo proveedor.
                    El campo `provider` debe enviarse como un String plano con la estructura de ProviderRequestDTO.
                    El campo `image` permite adjuntar una imagen opcional del proveedor.
                    """
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProviderResponseDTO> createProvider(
            @Parameter(
                    name = "provider",
                    description = "JSON como string plano representando un ProviderRequestDTO",
                    required = true
            )
            @RequestPart("provider") String providerJson,
            @Parameter(
                    name = "image",
                    description = "Imagen del proveedor (formatos JPG, PNG o WEBP).",
                    required = false
            )
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws JsonProcessingException {
        log.info("→ [ProviderController] Solicitud para registrar proveedor con datos JSON={} y image={}",
                providerJson, (image != null ? image.getOriginalFilename() : "sin imagen"));
        try {
            ProviderRequestDTO provider = objectMapper.readValue(providerJson, ProviderRequestDTO.class);
            ProviderResponseDTO response = providerService.create(provider, image);
            log.info("✓ [ProviderController] Proveedor creado exitosamente id={} nombre={} apellido={}",
                    response.getId(), response.getName(), response.getLastName());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("✗ [ProviderController] Error al registrar proveedor", e);
            throw e;
        }
    }

    //ENPOINT PARA LISTAR TODOS LOS PRESTADORES - publico
    @Operation(summary = "Listar Prestadores", description = "Recupera una lista de todos los prestadores")
    @ApiResponse(responseCode = "200", description = "List of providers retrieved successfully")
    @GetMapping("/getAll")
    public ResponseEntity<List<ProviderResponseDTO>> getAll() {
        log.info("→ [ProviderController] Solicitud para listar todos los proveedores");
        List<ProviderResponseDTO> providers = providerService.getAll();
        log.info("✓ [ProviderController] Se recuperaron {} proveedores", providers.size());
        return ResponseEntity.ok(providers);
    }

    //ENDPOINT PARA ACTUALIZAR UN PROVEEDOR EXISTENTE
    @Operation(summary = "Actualizar proveedor",
            description = "Actualiza un proveedor existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProviderResponseDTO> update(
            @Parameter(description = "ID del proveedor a actualizar", example = "1")
            @PathVariable Long id,
            @RequestPart("provider") ProviderUpdateRequestDTO request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("imageAction") String imageAction) {
        log.info("→ [ProviderController] Solicitud para actualizar proveedor id={} con imageAction={}", id, imageAction);
        try {
            ProviderResponseDTO updated = providerService.update(id, request, image, imageAction);
            log.info("✓ [ProviderController] Proveedor actualizado id={} nombre={} apellido={}",
                    updated.getId(), updated.getName(), updated.getLastName());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("✗ [ProviderController] Error al actualizar proveedor id={}", id, e);
            throw e;
        }
    }

    //ENDPOINT PARA INHABILITAR UN PROVEEDOR POR SU ID
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Inhabilitar Prestador", description = "Inhabilita un proveedor por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Provider disabled successfully"),
            @ApiResponse(responseCode = "404", description = "Provider not found")
    })
    @DeleteMapping("/disable/{id}")
    public ResponseEntity<Void> disable(@PathVariable Long id) {
        log.info("→ [ProviderController] Solicitud para inhabilitar proveedor id={}", id);
        providerService.disable(id);
        log.info("✓ [ProviderController] Proveedor inhabilitado id={}", id);
        return ResponseEntity.noContent().build();
    }

    //ENDPOINT PARA BUSCAR UN PRESTADOR POR SU ID Y AUMENTAR EL CONTADOR DE BÚSQUEDAS
    @Operation(summary = "Buscar Prestador por ID y aumentar contador de búsquedas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider found successfully"),
            @ApiResponse(responseCode = "404", description = "Provider not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProviderResponseDTO> getProviderById(@PathVariable Long id) {
        log.info("→ [ProviderController] Solicitud para obtener proveedor id={}", id);
        ProviderResponseDTO provider = providerService.search(id);
        log.info("✓ [ProviderController] Proveedor recuperado id={} nombre={} apellido={}",
                provider.getId(), provider.getName(), provider.getLastName());
        return ResponseEntity.ok(provider);
    }

    //ENDPOINT PARA BUSCAR PRESTADORES POR NOMBRE O RUBRO- publico
    @Operation(summary = "Buscar prestadores por nombre o rubro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de prestadores obtenida correctamente"),
            @ApiResponse(responseCode = "204", description = "No se encontraron prestadores")
    })
    @GetMapping("/search")
    public ResponseEntity<List<ProviderResponseDTO>> searchProviders(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {
        log.info("→ [ProviderController] Búsqueda de proveedores por nombre='{}' categoría='{}'", name, category);
        List<ProviderResponseDTO> results = searchProvidersByNameOrCategoryUsecase.execute(name, category);
        if (results.isEmpty()) {
            log.warn("✗ [ProviderController] No se encontraron proveedores para name='{}' category='{}'", name, category);
            return ResponseEntity.noContent().build();
        }
        log.info("✓ [ProviderController] Se encontraron {} proveedores para la búsqueda", results.size());
        return ResponseEntity.ok(results);
    }

    //ENDPOINT PARA HABILITAR UN PRESTADOR POR SU ID
    @Operation(summary = "Habilitar Prestador", description = "Habilita un prestador por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prestador habilitado correctamente"),
            @ApiResponse(responseCode = "404", description = "Prestador no encontrado")
    })
    @PutMapping("/enable/{id}")
    public ResponseEntity<Void> enable(@PathVariable Long id) {
        log.info("→ [ProviderController] Solicitud para habilitar proveedor id={}", id);
        providerService.enable(id);
        log.info("✓ [ProviderController] Proveedor habilitado id={}", id);
        return ResponseEntity.noContent().build();
    }


    //ENDPOINT PARA OBTENER PRESTADORES PAGINADOS - publico
    @Operation(
            summary = "Listar prestadores paginados",
            description = """
    Recupera una lista de prestadores en formato paginado. <br><br>
    Parámetros:
    - <code>page</code>: Número de página (empezando en 0).
    - <code>size</code>: Cantidad de registros por página.
    
    La respuesta incluye:
    - <code>content</code>: Lista de prestadores.
    - <code>totalElements</code>: Cantidad total de registros.
    - <code>totalPages</code>: Cantidad total de páginas.
    - <code>size</code>: Tamaño de la página.
    - <code>number</code>: Página actual.
    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista paginada de prestadores obtenida correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProviderResponseDTO.class)
                    )
            )
    })
    @GetMapping("/paged")
    public ResponseEntity<Page<ProviderResponseDTO>> getAllPaged(
            @Parameter(
                    description = "Número de página (comienza en 0).",
                    example = "0"
            )
            @RequestParam(defaultValue = "0") int page,

            @Parameter(
                    description = "Cantidad de elementos por página.",
                    example = "5"
            )
            @RequestParam(defaultValue = "5") int size
    ) {
        log.info("→ [ProviderController] Solicitud para listar proveedores paginados | page={} | size={}", page, size);
        try {
            Page<ProviderResponseDTO> result = providerService.getAllPaged(page, size);
            log.info("✓ [ProviderController] Se recuperaron {} proveedores en la página {} de {} (total elementos={})",
                    result.getContent().size(),
                    result.getNumber(),
                    result.getTotalPages(),
                    result.getTotalElements()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("✗ [ProviderController] Error al obtener proveedores paginados | page={} | size={}", page, size, e);
            throw e;
        }
    }


}
