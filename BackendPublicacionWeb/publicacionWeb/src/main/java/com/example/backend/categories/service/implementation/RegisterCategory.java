package com.example.backend.categories.service.implementation;

import com.example.backend.categories.controller.dto.CategoryRequestDTO;
import com.example.backend.categories.controller.dto.CategoryResponseDTO;
import com.example.backend.categories.domain.Category;
import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.categories.service.Mapper.CategoryMapper;
import com.example.backend.categories.service.usecase.RegisterCategoryUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegisterCategory implements RegisterCategoryUsecase {

private final CategoryRepository categoryRepository;
private final CategoryMapper categoryMapper;
    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTo) {
        Category category = CategoryMapper.toEntity(requestDTo);
        category.setCreatedAt(LocalDateTime.now());
        Category saved = categoryRepository.save(category);
        return CategoryMapper.toResponseDTO(saved);
    }
}
