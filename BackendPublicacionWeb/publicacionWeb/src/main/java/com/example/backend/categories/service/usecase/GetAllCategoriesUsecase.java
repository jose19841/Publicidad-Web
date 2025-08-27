package com.example.backend.categories.service.usecase;

import com.example.backend.categories.controller.dto.CategoryResponseDTO;

import java.util.List;

public interface GetAllCategoriesUsecase {
    List<CategoryResponseDTO> getAllCategories();
}
