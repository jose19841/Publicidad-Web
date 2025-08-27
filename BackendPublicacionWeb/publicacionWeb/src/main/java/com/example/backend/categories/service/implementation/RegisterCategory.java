package com.example.backend.categories.service.implementation;

import com.example.backend.categories.controller.dto.CategoryRequestDTO;
import com.example.backend.categories.controller.dto.CategoryResponseDTO;
import com.example.backend.categories.domain.Category;
import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.categories.service.Mapper.CategoryMapper;
import com.example.backend.categories.service.usecase.RegisterCategoryUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterCategory implements RegisterCategoryUsecase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTo) {
        log.info("→ [RegisterCategory] Creando nueva categoría nombre={}", requestDTo.getName());
        try {
            Category category = CategoryMapper.toEntity(requestDTo);
            category.setCreatedAt(LocalDateTime.now());

            Category saved = categoryRepository.save(category);
            CategoryResponseDTO response = CategoryMapper.toResponseDTO(saved);

            log.info("✓ [RegisterCategory] Categoría creada exitosamente id={} nombre={}",
                    response.getId(), response.getName());
            return response;
        } catch (Exception e) {
            log.error("✗ [RegisterCategory] Error al crear categoría nombre={}", requestDTo.getName(), e);
            throw e;
        }
    }
}
