package com.example.backend.categories.service;

import com.example.backend.categories.controller.dto.CategoryRequestDTO;
import com.example.backend.categories.controller.dto.CategoryResponseDTO;
import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.categories.service.usecase.DeleteCategoryUsecase;
import com.example.backend.categories.service.usecase.GetAllCategoriesUsecase;
import com.example.backend.categories.service.usecase.RegisterCategoryUsecase;
import com.example.backend.categories.service.usecase.UpdateCategoriesUsecase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RegisterCategoryUsecase registerService;

    @Autowired
    private GetAllCategoriesUsecase getAllService;

    @Autowired
    private UpdateCategoriesUsecase categoriesUsecase;

    @Autowired
    private DeleteCategoryUsecase deleteCategoryUsecase;

    // crear Categoria
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        log.info("Iniciando creación de categoría con nombre={}", requestDTO.getName());
        try {
            CategoryResponseDTO response = registerService.createCategory(requestDTO);
            log.info("Categoría creada exitosamente id={} nombre={}", response.getId(), response.getName());
            return response;
        } catch (Exception e) {
            log.error("Error al crear categoría con nombre={}", requestDTO.getName(), e);
            throw e;
        }
    }

    // listar todas las categorias
    public List<CategoryResponseDTO> getAllCategories() {
        log.info("Recuperando todas las categorías...");
        try {
            List<CategoryResponseDTO> categories = getAllService.getAllCategories();
            log.info("Se recuperaron {} categorías", categories.size());
            return categories;
        } catch (Exception e) {
            log.error("Error al listar categorías", e);
            throw e;
        }
    }

    // actualizar categorias
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        log.info("Iniciando actualización de categoría id={}", id);
        try {
            CategoryResponseDTO updated = categoriesUsecase.updateCategory(id, requestDTO);
            log.info("Categoría actualizada id={} nuevoNombre={}", updated.getId(), updated.getName());
            return updated;
        } catch (Exception e) {
            log.error("Error al actualizar categoría id={}", id, e);
            throw e;
        }
    }

    // eliminar categoria
    public void deleteCategory(Long id) {
        log.info("Iniciando eliminación de categoría id={}", id);
        try {
            deleteCategoryUsecase.deleteCategory(id);
            log.info("Categoría eliminada id={}", id);
        } catch (Exception e) {
            log.error("Error al eliminar categoría id={}", id, e);
            throw e;
        }
    }
}
