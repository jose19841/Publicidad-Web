package com.example.backend.categories.service.implementation;

import com.example.backend.categories.controller.dto.CategoryRequestDTO;
import com.example.backend.categories.controller.dto.CategoryResponseDTO;
import com.example.backend.categories.domain.Category;
import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.categories.service.Mapper.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterCategoryTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private RegisterCategory registerCategory;

    CategoryRequestDTO requestDTO;
    Category category;
    Category savedCategory;

    @BeforeEach
    void setUp() {
        requestDTO = new CategoryRequestDTO();
        requestDTO.setName("Tecnología");

        category = new Category();
        category.setName(requestDTO.getName());

        savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName(requestDTO.getName());
        savedCategory.setDescription("Categoría de tecnología");
        savedCategory.setCreatedAt(LocalDateTime.now());

    }
    @Test
    void createCategory_categoriaValida_devuelveResponseDTO() {

        // Arrange: preparamos el escenario del test
        //  Stub: cuando se llame a categoryRepository.save con cualquier objeto,
        // en lugar de ir a la base de datos, devolverá el objeto savedCategory que definimos en setUp().
        when(categoryRepository.save(any())).thenReturn(savedCategory);

        // Act: ejecutamos el método que queremos probar
        //  Llamamos al servicio registerCategory.createCategory pasándole el requestDTO preparado en setUp().
        CategoryResponseDTO response = registerCategory.createCategory(requestDTO);

        // Assert: verificamos que el resultado sea el esperado
        //  Chequeamos que la respuesta no sea null
        assertNotNull(response);
        //  Comprobamos que el id en la respuesta coincide con el id del savedCategory simulado
        assertEquals(savedCategory.getId(), response.getId());
        //  Comprobamos que el nombre en la respuesta coincide con el nombre del savedCategory
        assertEquals(savedCategory.getName(), response.getName());
        //  Comprobamos que la fecha de creación en la respuesta coincide con la del savedCategory
        assertEquals(savedCategory.getCreatedAt(), response.getCreatedAt());
    }


    @Test
    void createCategory_llamaRepositorioCorrectamente() {
        // Arrange
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        // Act
        CategoryResponseDTO response = registerCategory.createCategory(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(savedCategory.getId(), response.getId());
        assertEquals(savedCategory.getName(), response.getName());
        assertEquals(savedCategory.getCreatedAt(), response.getCreatedAt());

        // Verificamos que se llamó al repositorio exactamente una vez
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void createCategory_requestNull_lanzaExcepcion() {
        // Arrange
        requestDTO = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> registerCategory.createCategory(requestDTO));

        // Verificamos que no se llama al repositorio cuando el request es null
        verify(categoryRepository, never()).save(any(Category.class));

    }

}