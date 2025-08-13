package com.example.backend.providers.service.implementation;

import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.service.Mapper.ProviderMapper;
import com.example.backend.providers.service.usecase.SearchProvidersByNameOrCategoryUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchProvidersByNameOrCategoryService implements SearchProvidersByNameOrCategoryUsecase {
    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;

    @Override
    public List<ProviderResponseDTO> execute (String name, String category){
        List<Provider> providers;
        if(name !=null && category !=null){
            providers = providerRepository.findByNameAndCategoryName(name, category);

        }else if (name !=null){
            providers = providerRepository.findByName(name);
        } else if (category !=null) {
            providers = providerRepository.findByCategoryName(category);
        }else{
            providers = providerRepository.findAll();
        }
        return providers.stream()
                .map(providerMapper::toDTO)
                .toList();
    }
}
