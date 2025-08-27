package com.example.backend.providers.service.implementation;

import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.domain.Search;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.infrastructure.ProviderSearchRepository;
import com.example.backend.providers.service.Mapper.ProviderMapper;
import com.example.backend.ratings.controller.dto.ProviderRatingStats;
import com.example.backend.ratings.infrastructure.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;
    @Mock
    private ProviderSearchRepository providerSearchRepository;
    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private ProviderMapper providerMapper;

    @InjectMocks
    private SearchProviderService service;

    @Test
    void execute_proveedorNoExiste_lanzaExcepcion() {
        Long id = 1L;
        when(providerRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.execute(id));

        assertEquals("Proveedor no encontrado", ex.getMessage());
        verify(providerRepository).findById(id);
        verifyNoInteractions(providerSearchRepository, providerMapper, ratingRepository);
    }

    @Test
    void execute_proveedorExiste_conBusquedaPrevia_incrementaCounterYGuarda() {
        Long id = 1L;
        Provider provider = new Provider();
        provider.setId(id);

        Search search = new Search();
        search.setProvider(provider);
        search.setCounter(BigInteger.valueOf(5));

        ProviderResponseDTO dto = new ProviderResponseDTO();
        when(providerRepository.findById(id)).thenReturn(Optional.of(provider));
        when(providerSearchRepository.findByProvider(provider)).thenReturn(Optional.of(search));
        when(providerMapper.toDTO(provider)).thenReturn(dto);
        when(ratingRepository.getStatsByProviderId(id)).thenReturn(new ProviderRatingStats(4.2, 7L));

        ProviderResponseDTO result = service.execute(id);

        assertNotNull(result);
        assertEquals(7L, result.getTotalRatings());
        assertEquals(4.2, result.getAverageRating());

        assertEquals(BigInteger.valueOf(6), search.getCounter()); // incrementado

        verify(providerSearchRepository).save(search);
        verify(providerMapper).toDTO(provider);
        verify(ratingRepository).getStatsByProviderId(id);
    }

    @Test
    void execute_proveedorExiste_sinBusquedaPrevia_creaNuevaBusquedaConCounterUno() {
        Long id = 2L;
        Provider provider = new Provider();
        provider.setId(id);

        ProviderResponseDTO dto = new ProviderResponseDTO();

        when(providerRepository.findById(id)).thenReturn(Optional.of(provider));
        when(providerSearchRepository.findByProvider(provider)).thenReturn(Optional.empty());
        when(providerMapper.toDTO(provider)).thenReturn(dto);
        when(ratingRepository.getStatsByProviderId(id)).thenReturn(new ProviderRatingStats(3.5, 2L));

        ProviderResponseDTO result = service.execute(id);

        assertNotNull(result);
        assertEquals(2L, result.getTotalRatings());
        assertEquals(3.5, result.getAverageRating());

        ArgumentCaptor<Search> captor = ArgumentCaptor.forClass(Search.class);
        verify(providerSearchRepository).save(captor.capture());

        Search saved = captor.getValue();
        assertEquals(provider, saved.getProvider());
        assertEquals(BigInteger.ONE, saved.getCounter());
    }

    @Test
    void execute_devuelveDTOConRatings() {
        Long id = 3L;
        Provider provider = new Provider();
        provider.setId(id);

        ProviderResponseDTO dto = new ProviderResponseDTO();

        when(providerRepository.findById(id)).thenReturn(Optional.of(provider));
        when(providerSearchRepository.findByProvider(provider)).thenReturn(Optional.empty());
        when(providerMapper.toDTO(provider)).thenReturn(dto);
        when(ratingRepository.getStatsByProviderId(id)).thenReturn(new ProviderRatingStats(5.0, 100L));

        ProviderResponseDTO result = service.execute(id);

        assertEquals(100L, result.getTotalRatings());
        assertEquals(5.0, result.getAverageRating());
    }
}
