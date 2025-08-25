package com.example.backend.categories.service.usecase;

import com.example.backend.categories.controller.dto.CategoryRequestDTO;
import com.example.backend.categories.controller.dto.CategoryResponseDTO;

public interface RegisterCategoryUsecase {
    CategoryResponseDTO createCategory(CategoryRequestDTO requestDTo);

}
