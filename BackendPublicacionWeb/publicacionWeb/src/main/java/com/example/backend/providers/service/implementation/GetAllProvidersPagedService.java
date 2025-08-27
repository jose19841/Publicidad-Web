package com.example.backend.providers.service.implementation;

import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.service.Mapper.ProviderMapper;
import com.example.backend.providers.service.usecase.GetAllProvidersPagedUsecase;
import com.example.backend.ratings.infrastructure.RatingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetAllProvidersPagedService implements GetAllProvidersPagedUsecase {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final RatingRepository ratingRepository;

    @Override
    public Page<ProviderResponseDTO> execute(int page, int size) {
        log.debug("→ [GetAllProvidersPagedService] Ejecutando paginación | página={} | size={}", page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<ProviderResponseDTO> result = providerRepository.findAll(pageable)
                .map(provider -> {
                    ProviderResponseDTO dto = providerMapper.toDTO(provider);
                    var stats = ratingRepository.getAvgAndCountByProviderId(provider.getId());
                    dto.setAverageRating(stats.getAverage());
                    dto.setTotalRatings(stats.getCount());
                    return dto;
                });

        log.debug("✓ [GetAllProvidersPagedService] Resultado obtenido | registrosDevueltos={} | páginaActual={} | totalPáginas={} | totalElementos={}",
                result.getContent().size(),
                result.getNumber(),
                result.getTotalPages(),
                result.getTotalElements()
        );

        return result;
    }
}
