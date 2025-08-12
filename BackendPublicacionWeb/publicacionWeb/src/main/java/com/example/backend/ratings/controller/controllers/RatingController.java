package com.example.backend.ratings.controller.controllers;

import com.example.backend.config.security.CookieService;
import com.example.backend.ratings.controller.dto.CreateRatingRequest;
import com.example.backend.ratings.controller.dto.RatingResponse;
import com.example.backend.ratings.service.usecase.RateProviderUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/providers/{providerId}/ratings")
@RequiredArgsConstructor
@Tag(name = "Ratings", description = "Endpoints para calificar prestadores")
public class RatingController {

    private final RateProviderUseCase rateProviderUseCase;
    private final CookieService cookieService;

    @Operation(
            summary = "Calificar un prestador",
            description = "Permite a un usuario autenticado calificar con estrellas (1..5) a un prestador.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Puntaje de la calificación (1 a 5).",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateRatingRequest.class),
                            examples = {
                                    @ExampleObject(name = "score-5", value = "{\"score\":5}"),
                                    @ExampleObject(name = "score-3", value = "{\"score\":3}")
                            }
                    )
            )
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<?> rate(
            @PathVariable Long providerId,
            @Valid @RequestBody CreateRatingRequest request,
            HttpServletRequest httpRequest) {

        // 1) Tomar usuario de la cookie JWT
        var userOpt = cookieService.getUserFromCookie(httpRequest);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Sesión no válida o expirada."));
        }

        var user = userOpt.get();

        // 2) Ejecutar el caso de uso (como lo definimos)
        RatingResponse resp = rateProviderUseCase.execute(
                providerId,
                user.getId(),            // <-- id del usuario autenticado
                request.getScore()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}