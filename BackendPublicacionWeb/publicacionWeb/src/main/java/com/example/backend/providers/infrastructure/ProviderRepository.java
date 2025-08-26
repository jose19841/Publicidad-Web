package com.example.backend.providers.infrastructure;


import com.example.backend.providers.domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
List<Provider> findByName(String name);
List<Provider> findByCategoryName(String category);
List<Provider> findByNameAndCategoryName(String name, String category);
boolean existsByNameAndLastName(String name, String lastName);
boolean existsByPhone(String phone);
Optional<Provider> findByPhone(String phone);

Optional<Provider> findByNameAndLastName(String name, String lastname);

    @Query("""
           SELECT p
           FROM Provider p
           LEFT JOIN Rating r ON r.provider = p
           WHERE p.isActive = true
           GROUP BY p.id
           ORDER BY COALESCE(AVG(r.score), 0) DESC, COUNT(r) DESC
           """)
    List<Provider> findActiveProvidersOrderByAvgRatingDesc();


    List<Provider> findByNameContainingIgnoreCase(String name);

    List<Provider> findByCategoryNameContainingIgnoreCase(String category);

    List<Provider> findByNameContainingIgnoreCaseAndCategoryNameContainingIgnoreCase(String name, String category);
}
