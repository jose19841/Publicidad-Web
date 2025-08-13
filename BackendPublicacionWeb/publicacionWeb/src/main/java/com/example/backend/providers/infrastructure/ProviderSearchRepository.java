package com.example.backend.providers.infrastructure;


import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.domain.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderSearchRepository extends JpaRepository<Search, Long> {
 Optional<Search> findByProvider(Provider provider);
}
