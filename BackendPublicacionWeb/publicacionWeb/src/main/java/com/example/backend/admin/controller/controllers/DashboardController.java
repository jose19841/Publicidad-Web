package com.example.backend.admin.controller.controllers;

import com.example.backend.admin.controller.dto.DashboardMetricsDTO;
import com.example.backend.admin.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dashboard", description = "Endpoints para obtener métricas del panel de administración")
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(
            summary = "Obtener métricas del dashboard",
            description = "Devuelve estadísticas consolidadas: proveedores, categorías, usuarios, rating promedio, proveedores por categoría y distribución de calificaciones."
    )
    @ApiResponse(responseCode = "200", description = "Métricas obtenidas con éxito",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DashboardMetricsDTO.class)))
    @GetMapping("/metrics")
    @PreAuthorize("hasRole('ADMIN')")
    public DashboardMetricsDTO getDashboardMetrics() {
        log.info("Iniciando consulta de métricas para dashboard");
        DashboardMetricsDTO metrics = dashboardService.getMetrics();
        log.info("Consulta completada -> Proveedores: {}, Categorías: {}, Usuarios: {}, Promedio: {}",
                metrics.getTotalProviders(), metrics.getTotalCategories(),
                metrics.getTotalUsers(), metrics.getAverageRating());
        return metrics;
    }
}
