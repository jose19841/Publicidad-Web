package com.example.backend.ratings.service.implementation;

import com.example.backend.ratings.controller.dto.ProviderRatingDTO;
import com.example.backend.ratings.infrastructure.RatingRepository;
import com.example.backend.ratings.service.usecase.GetProviderRatingsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class GetProviderRatingsService implements GetProviderRatingsUseCase {

    private final RatingRepository ratingRepository;

    @Override
    public List<ProviderRatingDTO> execute() {
        log.info("Ejecutando servicio: obtener calificaciones de proveedores");

        List<ProviderRatingDTO> providers = ratingRepository.getAllProvidersWithRatings();

        if (providers.isEmpty()) {
            log.warn("No se encontraron proveedores con calificaciones registradas");
        } else {
            log.info("Se encontraron {} proveedores con calificaciones", providers.size());
            // Para depuración más detallada, se pueden listar con debug
            providers.forEach(p ->
                    log.debug("Proveedor ID: {}, Nombre: {} {}, Categoría: {}, Calificaciones: {}, Promedio: {}",
                            p.getId(), p.getFirstName(), p.getLastName(),
                            p.getCategory(), p.getRatingsCount(), p.getAverageScore())
            );
        }
        return providers;
    }

}
