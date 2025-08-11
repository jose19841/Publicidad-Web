package com.example.backend.providers.service.implementation;

import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.domain.Search;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.infrastructure.ProviderSearchRepository;
import com.example.backend.providers.service.usecase.SearchProviderUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class SearchProviderService implements SearchProviderUsecase {
    private final ProviderRepository providerRepository;
    private final ProviderSearchRepository providerSearchRepository;

    @Override
    public Provider execute(Long providerId) {

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
        return provider;
    }
}
