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
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class SearchProviderService implements SearchProviderUsecase {

    private final ProviderRepository providerRepository;
    private final ProviderSearchRepository providerSearchRepository;
    private final RatingRepository ratingRepository;
    private final ProviderMapper providerMapper;

    @Override
    public ProviderResponseDTO execute(Long providerId) {

        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(()-> new RuntimeException("proveedor no encontrado"));

        providerSearchRepository.findByProvider(provider)
                .ifPresentOrElse(
                        search -> {
                            search.setCounter(search.getCounter().add(BigInteger.ONE));
                            providerSearchRepository.save(search);
                        },
                        () ->{
                            Search newSearch = new Search();
                            newSearch.setProvider(provider);
                            newSearch.setCounter(BigInteger.ONE);
                            providerSearchRepository.save(newSearch);
                        }
                );

        ProviderResponseDTO response= providerMapper.toDTO(     provider);
        ProviderRatingStats providerRatingStats= ratingRepository.getStatsByProviderId(provider.getId());
        response.setTotalRatings(providerRatingStats.getCount());
        response.setAverageRating(providerRatingStats.getAverage());

        return response;
    }
}
