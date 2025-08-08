package com.example.backend.categories.service.implementation;

import com.example.backend.categories.controller.dto.CategoryRequestDTO;
import com.example.backend.categories.controller.dto.CategoryResponseDTO;
import com.example.backend.categories.domain.Category;
import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.categories.service.Mapper.CategoryMapper;
import com.example.backend.categories.service.usecase.UpdateCategoriesUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateCategories implements UpdateCategoriesUsecase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        existing.setName(requestDTO.getName());
        existing.setDescription(requestDTO.getDescription());
        existing.setUpdatedAt(LocalDateTime.now());
        Category updated = categoryRepository.save(existing);
        return categoryMapper.toResponseDTO(updated);
    }
}



