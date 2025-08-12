package com.example.backend.ratings.infrastructure;

import com.example.backend.ratings.domain.Rating;
import com.example.backend.ratings.controller.dto.ProviderRatingStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    boolean existsByUserIdAndProviderId(Long userId, Long providerId);

    @Query("""
        select new com.example.backend.ratings.controller.dto.ProviderRatingStats(
            coalesce(avg(r.score), 0.0),
            coalesce(count(r), 0L)
        )
        from Rating r
        where r.provider.id = :providerId
    """)
    ProviderRatingStats getStatsByProviderId(Long providerId);

    @Query("""
    select new com.example.backend.ratings.controller.dto.ProviderRatingStats(
        coalesce(avg(r.score), 0.0),
        coalesce(count(r), 0L)
    )
    from Rating r
    where r.provider.id = :providerId
""")
    ProviderRatingStats getAvgAndCountByProviderId(Long providerId);



}
