package com.example.backend.ratings.service.implementation;

import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.ratings.domain.Rating;
import com.example.backend.ratings.controller.dto.ProviderRatingStats;
import com.example.backend.ratings.controller.dto.RatingResponse;
import com.example.backend.ratings.infrastructure.RatingRepository;
import com.example.backend.ratings.service.usecase.RateProviderUseCase;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateProviderService implements RateProviderUseCase {

    private final RatingRepository ratingRepository;
    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RatingResponse execute(Long providerId, Long currentUserId, int score) {
        log.info("Iniciando calificación -> providerId={}, userId={}, score={}", providerId, currentUserId, score);

        if (score < 1 || score > 5) {
            log.warn("Score inválido: {} (userId={}, providerId={})", score, currentUserId, providerId);
            throw new IllegalArgumentException("La calificación debe ser entre 1 y 5.");
        }

        if (ratingRepository.existsByUserIdAndProviderId(currentUserId, providerId)) {
            log.warn("Usuario {} ya calificó al proveedor {}", currentUserId, providerId);
            throw new IllegalStateException("Ya calificaste a este prestador.");
        }

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado: {}", currentUserId);
                    return new IllegalArgumentException("Usuario no encontrado.");
                });

        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> {
                    log.error("Proveedor no encontrado: {}", providerId);
                    return new IllegalArgumentException("Prestador no encontrado.");
                });

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setProvider(provider);
        rating.setScore(score);
        ratingRepository.save(rating);

        log.debug("Rating guardado con id={} para providerId={}, userId={}", rating.getId(), providerId, currentUserId);

        // Cálculo "inmediato" post-guardado (lectura agregada)
        ProviderRatingStats stats = ratingRepository.getStatsByProviderId(providerId);
        log.info("Estadísticas actualizadas para providerId={} -> promedio={}, cantidad={}",
                providerId, stats.getAverage(), stats.getCount());

        return RatingResponse.builder()
                .ratingId(rating.getId())
                .providerId(providerId)
                .score(score)
                .providerAverage(stats.getAverage())
                .providerRatingCount(stats.getCount())
                .build();
    }
}
