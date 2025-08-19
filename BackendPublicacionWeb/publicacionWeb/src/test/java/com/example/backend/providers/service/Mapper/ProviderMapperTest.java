package com.example.backend.providers.service.Mapper;

import com.example.backend.categories.domain.Category;
import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.domain.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProviderMapperTest {

    private ProviderMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProviderMapper();
    }

    @Test
    void toEntity_mapeaTodosLosCampos() {
        ProviderRequestDTO dto = new ProviderRequestDTO();
        dto.setName("Juan");
        dto.setLastName("Perez");
        dto.setAddress("Calle Falsa 123");
        dto.setPhone("123456789");
        dto.setDescription("Proveedor de prueba");
        dto.setIsActive(false);

        Provider provider = mapper.toEntity(dto);

        assertEquals("Juan", provider.getName());
        assertEquals("Perez", provider.getLastName());
        assertEquals("Calle Falsa 123", provider.getAddress());
        assertEquals("123456789", provider.getPhone());
        assertEquals("Proveedor de prueba", provider.getDescription());
        assertFalse(provider.getIsActive());
    }

    @Test
    void toEntity_isActiveNull_porDefectoTrue() {
        ProviderRequestDTO dto = new ProviderRequestDTO();
        dto.setName("Maria");
        dto.setIsActive(null);

        Provider provider = mapper.toEntity(dto);

        assertTrue(provider.getIsActive()); // ✅ por defecto true
    }

    @Test
    void toDTO_mapeaTodosLosCamposConCategoria() {
        Provider provider = new Provider();
        provider.setId(1L);
        provider.setName("Carlos");
        provider.setLastName("Lopez");
        provider.setAddress("Av Siempreviva 742");
        provider.setPhone("987654321");
        provider.setDescription("Otro proveedor");
        provider.setPhotoUrl("foto.jpg");
        provider.setIsActive(true);
        provider.setCreatedAt(LocalDateTime.now().minusDays(1));
        provider.setModifiedAt(LocalDateTime.now());

        Category category = new Category();
        category.setName("Electricista");
        provider.setCategory(category);

        ProviderResponseDTO dto = mapper.toDTO(provider);

        assertEquals(1L, dto.getId());
        assertEquals("Carlos", dto.getName());
        assertEquals("Lopez", dto.getLastName());
        assertEquals("Av Siempreviva 742", dto.getAddress());
        assertEquals("987654321", dto.getPhone());
        assertEquals("Otro proveedor", dto.getDescription());
        assertEquals("foto.jpg", dto.getPhotoUrl());
        assertTrue(dto.getIsActive());
        assertEquals("Electricista", dto.getCategoryName());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getUpdatedAt());
    }

    @Test
    void toDTO_sinCategoria_noSeteaCategoryName() {
        Provider provider = new Provider();
        provider.setId(2L);
        provider.setName("Luis");
        provider.setCategory(null);

        ProviderResponseDTO dto = mapper.toDTO(provider);

        assertEquals(2L, dto.getId());
        assertEquals("Luis", dto.getName());
        assertNull(dto.getCategoryName());
    }
}
