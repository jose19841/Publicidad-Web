package com.example.backend.comments.controller.controllers;

import com.example.backend.comments.controller.dto.CommentRequestDTO;
import com.example.backend.comments.controller.dto.CommentResponseDTO;
import com.example.backend.comments.service.CommentService;
import com.example.backend.comments.service.usecase.CreateCommentUsecase;
import com.example.backend.comments.service.usecase.GetCommentsByProviderUsecase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "Crear un nuevo comentario",
            description = "Crea un comentario asociado a un proveedor usando el usuario autenticado"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Comentario creado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de respuesta",
                                    value = """
                                            {
                                              "id": 10,
                                              "userId": 3,
                                              "username": "juanperez",
                                              "content": "Muy buen servicio, volveré a contratar.",
                                              "createdAt": "2025-08-13T16:45:23.123"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario o proveedor no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
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
        CommentResponseDTO response = commentService.createComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Obtener comentarios de un proveedor",
            description = "Devuelve la lista de comentarios asociados a un proveedor identificado por su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de comentarios obtenida correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de respuesta",
                                    value = """
                                            [
                                              {
                                                "id": 10,
                                                "userId": 3,
                                                "username": "juanperez",
                                                "content": "Muy buen servicio, volveré a contratar.",
                                                "createdAt": "2025-08-13T16:45:23.123"
                                              },
                                              {
                                                "id": 11,
                                                "userId": 4,
                                                "username": "maria",
                                                "content": "Excelente atención y rapidez.",
                                                "createdAt": "2025-08-13T17:02:10.456"
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByProvider(
            @RequestParam Long providerId
    ) {
        List<CommentResponseDTO> comments = commentService.getCommentByProvider(providerId);
        return ResponseEntity.ok(comments);
    }
}
