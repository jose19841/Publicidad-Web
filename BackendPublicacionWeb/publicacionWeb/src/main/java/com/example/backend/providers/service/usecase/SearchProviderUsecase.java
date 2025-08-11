package com.example.backend.providers.service.usecase;

import com.example.backend.providers.domain.Provider;

public interface SearchProviderUsecase {
    Provider execute(Long providerId);
}
