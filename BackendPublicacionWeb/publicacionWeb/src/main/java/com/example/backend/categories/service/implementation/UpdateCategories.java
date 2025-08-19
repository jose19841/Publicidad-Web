package com.example.backend.categories.service.implementation;

import com.example.backend.categories.controller.dto.CategoryRequestDTO;
import com.example.backend.categories.controller.dto.CategoryResponseDTO;
import com.example.backend.categories.domain.Category;
import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.categories.service.Mapper.CategoryMapper;
import com.example.backend.categories.service.usecase.UpdateCategoriesUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateCategories implements UpdateCategoriesUsecase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        log.info("→ [UpdateCategories] Solicitud para actualizar categoría id={}", id);
        try {
            Category existing = categoryRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("✗ [UpdateCategories] No se encontró la categoría con id={}", id);
                        return new RuntimeException("Categoría no encontrada");
                    });

            existing.setName(requestDTO.getName());
            existing.setDescription(requestDTO.getDescription());
            existing.setModifiedAt(LocalDateTime.now());

            Category updated = categoryRepository.save(existing);
            CategoryResponseDTO response = categoryMapper.toResponseDTO(updated);

            log.info("✓ [UpdateCategories] Categoría actualizada id={} nuevoNombre={}",
                    response.getId(), response.getName());
            return response;
        } catch (Exception e) {
            log.error("✗ [UpdateCategories] Error al actualizar categoría id={}", id, e);
            throw e;
        }
    }
}
