package com.example.backend.providers.service.Mapper;

import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.domain.Provider;
import org.springframework.stereotype.Component;

@Component
public class ProviderMapper {
    public Provider toEntity(ProviderRequestDTO dto){
        Provider provider = new Provider();
        provider.setName(dto.getName());
        provider.setLastName(dto.getLastName());
        provider.setAddress(dto.getAddress());
        provider.setPhone(dto.getPhone());
        provider.setDescription(dto.getDescription());
        provider.setPhotoUrl(dto.getPhotoUrl());
        provider.setIsActive(dto.getIsActive());
        return provider;
    }
    public ProviderResponseDTO toDTO(Provider entity) {
        ProviderResponseDTO dto = new ProviderResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        dto.setPhotoUrl(entity.getPhotoUrl());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }
    }

