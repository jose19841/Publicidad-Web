package com.example.backend.providers.service.implementation;

import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.service.Mapper.ProviderMapper;
import com.example.backend.providers.service.usecase.GetAllProviderUsecase;
import com.example.backend.ratings.infrastructure.RatingRepository;
import com.example.backend.ratings.controller.dto.ProviderRatingStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllProviderService implements GetAllProviderUsecase {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final RatingRepository ratingRepository;

    @Override
    public List<ProviderResponseDTO> getAll() {
        log.info("Iniciando proceso de obtención de todos los proveedores");

        List<ProviderResponseDTO> providers = providerRepository.findAll()
                .stream()
                .map(provider -> {
                    log.debug("Procesando proveedor id={}, nombre={} {}",
                            provider.getId(), provider.getName(), provider.getLastName());

                    ProviderResponseDTO dto = providerMapper.toDTO(provider);

                    ProviderRatingStats stats =
                            ratingRepository.getAvgAndCountByProviderId(provider.getId());

                    log.debug("Estadísticas de rating para proveedor id={}: promedio={}, cantidad={}",
                            provider.getId(), stats.getAverage(), stats.getCount());

                    dto.setAverageRating(stats.getAverage());
                    dto.setTotalRatings(stats.getCount());
                    return dto;
                })
                .collect(Collectors.toList());

        log.info("Se obtuvieron {} proveedores en total", providers.size());
        return providers;
    }
}
