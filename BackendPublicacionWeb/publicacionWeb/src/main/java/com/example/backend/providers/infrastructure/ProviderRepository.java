package com.example.backend.providers.infrastructure;


import com.example.backend.providers.domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
List<Provider> findByName(String name);
List<Provider> findByCategoryName(String category);
List<Provider> findByNameAndCategoryName(String name, String category);
boolean existsByNameAndLastName(String name, String lastName);
boolean existsByPhone(String phone);
}
