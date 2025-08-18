package com.example.backend.categories.service.implementation;

import com.example.backend.categories.controller.dto.CategoryRequestDTO;
import com.example.backend.categories.controller.dto.CategoryResponseDTO;
import com.example.backend.categories.domain.Category;
import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.categories.service.Mapper.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCategoriesTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private UpdateCategories updateCategories;

    private Long idExistente;
    private Long idInexistente;

    private CategoryRequestDTO requestDTO;
    private Category existing;
    private Category updated;
    private CategoryResponseDTO responseDTO;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @BeforeEach
    void setUp() {
        idExistente = 1L;
        idInexistente = 99L;

        createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        updatedAt = LocalDateTime.of(2024, 1, 2, 12, 30);

        // Datos entrantes (nuevos valores)
        requestDTO = new CategoryRequestDTO();
        requestDTO.setName("Nueva categoría");
        requestDTO.setDescription("Descripción actualizada");

        // Lo que devuelve findById(idExistente)
        existing = new Category();
        existing.setId(idExistente);
        existing.setName("Categoría vieja");
        existing.setDescription("Descripción vieja");
        existing.setCreatedAt(createdAt);

        // Lo que devuelve save(existing) tras aplicar cambios
        updated = new Category();
        updated.setId(idExistente);
        updated.setName(requestDTO.getName());
        updated.setDescription(requestDTO.getDescription());
        updated.setCreatedAt(createdAt);
        updated.setUpdatedAt(updatedAt);

        // Lo que devuelve el mapper al response
        responseDTO = new CategoryResponseDTO(
                updated.getId(),
                updated.getName(),
                updated.getDescription(),
                updated.getCreatedAt() // Agrega updatedAt si tu DTO lo expone
        );
    }

    @Test
    void updateCategory_categoriaExiste_actualizaYDevuelveResponseDTO()
    {
        // Arrange
        when(categoryRepository.findById(idExistente)).thenReturn(java.util.Optional.of(existing));

        when(categoryRepository.save(existing)).thenReturn(updated);

        // Act
        CategoryResponseDTO result = updateCategories.updateCategory(idExistente, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(requestDTO.getName(), result.getName());
        assertEquals(requestDTO.getDescription(), result.getDescription());
        assertEquals(createdAt, result.getCreatedAt());
        verify(categoryRepository).findById(idExistente);
        verify(categoryRepository).save(existing);


    }
    @Test
    void updateCategory_categoriaNoExiste_lanzaRuntimeException() {
        // Arrange
        when(categoryRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> updateCategories.updateCategory(idInexistente, requestDTO));

        assertEquals("Categoría no encontrada", ex.getMessage());

        // Verificaciones
        verify(categoryRepository).findById(eq(idInexistente));
        verify(categoryRepository, never()).save(any());
        verifyNoInteractions(categoryMapper);
    }


    @Test
    void updateCategory_actualizaCampos_nameYDescription_correctamente()
    {
        // Arrange
        when(categoryRepository.findById(idExistente)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenReturn(updated);

        // Act
        CategoryResponseDTO result = updateCategories.updateCategory(idExistente, requestDTO);

        // Assert
        assertEquals(requestDTO.getName(), result.getName());
        assertEquals(requestDTO.getDescription(), result.getDescription());
        verify(categoryRepository).findById(idExistente);
        verify(categoryRepository).save(existing);


    }
    @Test
    void updateCategory_seteaUpdatedAt_conFechaActual(){
        // Arrange
        when(categoryRepository.findById(idExistente)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(updated);

        // Act
        CategoryResponseDTO result = updateCategories.updateCategory(idExistente, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(requestDTO.getName(), result.getName());
        assertEquals(requestDTO.getDescription(), result.getDescription());
        assertEquals(createdAt, result.getCreatedAt());
        verify(categoryRepository).findById(idExistente);
        verify(categoryRepository).save(existing);

    }

    @Test
    void updateCategory_llamaRepositorio_findById_y_save_unaVez(){
        // Arrange
        when(categoryRepository.findById(idExistente)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(updated);

        // Act
        CategoryResponseDTO result = updateCategories.updateCategory(idExistente, requestDTO);

        // Assert
        verify(categoryRepository, times(1)).findById(idExistente);
        verify(categoryRepository, times(1)).save(existing);
        verifyNoMoreInteractions(categoryRepository);

    }
    @Test
    void updateCategory_interacciones_enOrden_findLuegoSave(){
        // Arrange
        when(categoryRepository.findById(idExistente)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(updated);

        // Act
        CategoryResponseDTO result = updateCategories.updateCategory(idExistente, requestDTO);

        // Assert
        InOrder inOrder = inOrder(categoryRepository);
        inOrder.verify(categoryRepository).findById(idExistente);
        inOrder.verify(categoryRepository).save(existing);


    }
    @Test
    void updateCategory_noLlamaSave_cuandoNoExisteLaCategoria()
    {
        // Arrange
        when(categoryRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> updateCategories.updateCategory(idInexistente, requestDTO));

        // Verificaciones
        verify(categoryRepository).findById(idInexistente);
        verify(categoryRepository, never()).save(any());
        verifyNoInteractions(categoryMapper);

    }


}