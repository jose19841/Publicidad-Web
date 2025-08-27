package com.example.backend.ratings.infrastructure;

import com.example.backend.admin.controller.dto.DashboardMetricsDTO;
import com.example.backend.admin.controller.dto.RatingCountDTO;
import com.example.backend.ratings.controller.dto.ProviderRatingDTO;
import com.example.backend.ratings.domain.Rating;
import com.example.backend.ratings.controller.dto.ProviderRatingStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Query("""
                select new com.example.backend.ratings.controller.dto.ProviderRatingDTO(
                    p.id,
                    p.name,
                    p.lastName,
                    p.category.name,
                    coalesce(count(r), 0L),
                    coalesce(avg(r.score), 0.0)
                )
                from Provider p
                left join Rating r on r.provider.id = p.id
                group by p.id, p.name, p.lastName, p.category.name
            """)
    List<ProviderRatingDTO> getAllProvidersWithRatings();

    @Query("SELECT COALESCE(AVG(r.score),0.0) FROM Rating r")
    double getAverageScore();

    @Query("SELECT new com.example.backend.admin.controller.dto.RatingCountDTO(r.score, COUNT(r)) " +
            "FROM Rating r GROUP BY r.score ORDER BY r.score")
    List<RatingCountDTO> getRatingsDistribution();

}
