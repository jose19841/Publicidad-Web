package com.example.backend.categories.service.Mapper;

import com.example.backend.categories.controller.dto.CategoryRequestDTO;
import com.example.backend.categories.controller.dto.CategoryResponseDTO;
import com.example.backend.categories.domain.Category;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    // ---------- toEntity ----------

    @Test
    void toEntity_conDTOValido_mapeaNombreYDescripcion_yNoSeteaIdNiCreatedAt() {
        // Arrange
        CategoryRequestDTO dto = new CategoryRequestDTO();
        dto.setName("Tecnología");
        dto.setDescription("Todo sobre tech");

        // Act
        Category entity = CategoryMapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals("Tecnología", entity.getName());
        assertEquals("Todo sobre tech", entity.getDescription());
        assertNull(entity.getId(), "No debería setear id en toEntity");
        assertNull(entity.getCreatedAt(), "No debería setear createdAt en toEntity");
    }

    @Test
    void toEntity_conDTOConCamposNull_mapeaNulls() {
        // Arrange
        CategoryRequestDTO dto = new CategoryRequestDTO();
        dto.setName(null);
        dto.setDescription(null);

        // Act
        Category entity = CategoryMapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertNull(entity.getName());
        assertNull(entity.getDescription());
        assertNull(entity.getId());
        assertNull(entity.getCreatedAt());
    }

    @Test
    void toEntity_conNull_devuelveNull() {
        // Act
        Category entity = CategoryMapper.toEntity(null);

        // Assert
        assertNull(entity);
    }

    // ---------- toResponseDTO ----------

    @Test
    void toResponseDTO_conCategoriaValida_mapeaCampos() {
        // Arrange
        LocalDateTime created = LocalDateTime.of(2024, 1, 1, 10, 0);
        Category category = new Category();
        category.setId(1L);
        category.setName("Hogar");
        category.setDescription("Cosas para la casa");
        category.setCreatedAt(created);

        // Act
        CategoryResponseDTO dto = CategoryMapper.toResponseDTO(category);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Hogar", dto.getName());
        assertEquals("Cosas para la casa", dto.getDescription());
        assertEquals(created, dto.getCreatedAt());
    }

    @Test
    void toResponseDTO_conCategoriaConCamposNull_mapeaNulls() {
        // Arrange
        Category category = new Category();
        // id, name, description, createdAt quedan null por defecto

        // Act
        CategoryResponseDTO dto = CategoryMapper.toResponseDTO(category);

        // Assert
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getDescription());
        assertNull(dto.getCreatedAt());
    }

    @Test
    void toResponseDTO_conNull_devuelveNull() {
        // Act
        CategoryResponseDTO dto = CategoryMapper.toResponseDTO(null);

        // Assert
        assertNull(dto);
    }
}
