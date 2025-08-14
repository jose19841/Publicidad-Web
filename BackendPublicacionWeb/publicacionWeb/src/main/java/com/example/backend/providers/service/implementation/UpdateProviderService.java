package com.example.backend.providers.service.implementation;

import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.service.Mapper.ProviderMapper;
import com.example.backend.providers.service.usecase.UpdateProviderUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProviderService implements UpdateProviderUsecase {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;

    @Override
    public ProviderResponseDTO update(Long id, ProviderRequestDTO request) {
//        Provider provider = providerRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("El proveedor con el id: " + id + "no existe"));

//        provider.setName(request.getName());
//        provider.setLastName(request.getLastName());
//        provider.setAddress(request.getAddress());
//        provider.setPhone(request.getPhone());
//        provider.setDescription(request.getDescription());
//        provider.setPhotoUrl(request.getPhotoUrl());
//        provider.setIsActive(request.getIsActive());

        ProviderResponseDTO updateProvider = new ProviderResponseDTO();
     //           providerRepository.save(provider);

    //    providerMapper.toDTO(updateProvider);
        return updateProvider;
    }
}
