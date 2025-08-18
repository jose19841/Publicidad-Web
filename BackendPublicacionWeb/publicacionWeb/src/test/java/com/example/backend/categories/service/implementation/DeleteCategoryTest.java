package com.example.backend.categories.service.implementation;

import com.example.backend.categories.infrastructure.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryTest {

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private DeleteCategory deleteCategory;

    private Long idExistente;
    private Long idInexistente;

    @BeforeEach
    void setUp() {

        idExistente = 1L;
        idInexistente = 99L;
    }
    @Test
    void deleteCategory_categoriaExiste_eliminaCategoria(){
        when(categoryRepository.existsById(idExistente)).thenReturn(true);
        deleteCategory.deleteCategory(idExistente);
        verify(categoryRepository, times(1)).existsById(idExistente);
        verify(categoryRepository, times(1)).deleteById(idExistente);
        verifyNoMoreInteractions(categoryRepository);

    }

    @Test
    void deleteCategory_categoriaNoExiste_lanzaRuntimeException(){
        when(categoryRepository.existsById(idInexistente)).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deleteCategory.deleteCategory(idInexistente);
        });
        assertEquals("Categoría no encontrada", exception.getMessage());
        verify( categoryRepository, times(1)).existsById(idInexistente);
        verify(categoryRepository, never()).deleteById(idInexistente);
    }


    @Test
    void deleteCategory_interacciones_enOrden(){
        when(categoryRepository.existsById(idExistente)).thenReturn(true);
        deleteCategory.deleteCategory(idExistente);

        verify(categoryRepository, times(1)).existsById(idExistente);
        verify(categoryRepository, times(1)).deleteById(idExistente);
        verifyNoMoreInteractions(categoryRepository);

    }
}