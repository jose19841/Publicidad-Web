package com.example.backend.providers.service.implementation;

import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.domain.Search;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.infrastructure.ProviderSearchRepository;
import com.example.backend.providers.service.Mapper.ProviderMapper;
import com.example.backend.providers.service.usecase.SearchProviderUsecase;
import com.example.backend.ratings.controller.dto.ProviderRatingStats;
import com.example.backend.ratings.infrastructure.RatingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchProviderService implements SearchProviderUsecase {

    private final ProviderRepository providerRepository;
    private final ProviderSearchRepository providerSearchRepository;
    private final RatingRepository ratingRepository;
    private final ProviderMapper providerMapper;

    @Override
    public ProviderResponseDTO execute(Long providerId) {
        log.info("Ejecutando búsqueda de proveedor con id: {}", providerId);

        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> {
                    log.error("Proveedor con id {} no encontrado", providerId);
                    return new RuntimeException("Proveedor no encontrado");
                });

        log.debug("Proveedor encontrado: {}", provider.getName());

        providerSearchRepository.findByProvider(provider)
                .ifPresentOrElse(
                        search -> {
                            log.debug("Incrementando contador de búsqueda para proveedor id {}", providerId);
                            search.setCounter(search.getCounter().add(BigInteger.ONE));
                            providerSearchRepository.save(search);
                        },
                        () -> {
                            log.debug("No existía registro de búsqueda para el proveedor id {}, creando nuevo", providerId);
                            Search newSearch = new Search();
                            newSearch.setProvider(provider);
                            newSearch.setCounter(BigInteger.ONE);
                            providerSearchRepository.save(newSearch);
                        }
                );

        ProviderResponseDTO response = providerMapper.toDTO(provider);

        ProviderRatingStats providerRatingStats = ratingRepository.getStatsByProviderId(provider.getId());
        response.setTotalRatings(providerRatingStats.getCount());
        response.setAverageRating(providerRatingStats.getAverage());

        log.info("Se retornó la información del proveedor id {} con {} calificaciones y promedio {}",
                providerId,
                response.getTotalRatings(),
                response.getAverageRating());

        return response;
    }
}
