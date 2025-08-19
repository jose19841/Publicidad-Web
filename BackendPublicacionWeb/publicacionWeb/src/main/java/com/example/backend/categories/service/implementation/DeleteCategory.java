package com.example.backend.categories.service.implementation;

import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.categories.service.usecase.DeleteCategoryUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteCategory implements DeleteCategoryUsecase {

    private final CategoryRepository categoryRepository;

    @Override
    public void deleteCategory(Long id) {
        log.info("→ [DeleteCategory] Solicitud para eliminar categoría id={}", id);
        try {
            if (!categoryRepository.existsById(id)) {
                log.warn("✗ [DeleteCategory] No se encontró la categoría con id={}", id);
                throw new RuntimeException("Categoría no encontrada");
            }
            categoryRepository.deleteById(id);
            log.info("✓ [DeleteCategory] Categoría eliminada exitosamente id={}", id);
        } catch (Exception e) {
            log.error("✗ [DeleteCategory] Error al eliminar la categoría id={}", id, e);
            throw e;
        }
    }
}
