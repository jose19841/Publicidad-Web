package com.example.backend.comments.controller.controllers;

import com.example.backend.comments.controller.dto.CommentRequestDTO;
import com.example.backend.comments.controller.dto.CommentResponseDTO;
import com.example.backend.comments.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "Comentarios", description = "API para gestionar comentarios de proveedores")
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "Crear un nuevo comentario",
            description = "Crea un comentario asociado a un proveedor usando el usuario autenticado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comentario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario o proveedor no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CommentResponseDTO> createComment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para crear un nuevo comentario",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de request",
                                    value = """
                                            {
                                              "providerId": 1,
                                              "content": "Muy buen servicio, volveré a contratar."
                                            }
                                            """
                            )
                    )
            )
            @RequestBody CommentRequestDTO request
    ) {
        log.info("→ [CommentController] Solicitud para crear comentario sobre providerId={} con contenido='{}'",
                request.getProviderId(), request.getContent());
        try {
            CommentResponseDTO response = commentService.createComment(request);
            log.info("✓ [CommentController] Comentario creado id={} userId={} providerId={}",
                    response.getId(), response.getUserId(), request.getProviderId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("✗ [CommentController] Error al crear comentario para providerId={}", request.getProviderId(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Obtener comentarios de un proveedor",
            description = "Devuelve la lista de comentarios asociados a un proveedor identificado por su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de comentarios obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getAllByProvider")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByProvider(
            @RequestParam Long providerId
    ) {
        log.info("→ [CommentController] Solicitud para obtener comentarios del providerId={}", providerId);
        try {
            List<CommentResponseDTO> comments = commentService.getCommentByProvider(providerId);
            log.info("✓ [CommentController] Se recuperaron {} comentarios para providerId={}",
                    comments.size(), providerId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            log.error("✗ [CommentController] Error al recuperar comentarios para providerId={}", providerId, e);
            throw e;
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Eliminar un comentario",
            description = "Elimina un comentario identificado por su ID. Solo accesible para administradores."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comentario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Comentario no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        log.info("→ [CommentController] Solicitud para eliminar comentario id={}", id);
        try {
            commentService.deleteComment(id);
            log.info("✓ [CommentController] Comentario id={} eliminado", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("✗ [CommentController] Error al eliminar comentario id={}", id, e);
            throw e;
        }
    }
}
