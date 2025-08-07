package com.example.backend.categories.service.implementation;

import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.categories.service.usecase.DeleteCategoryUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategory implements DeleteCategoryUsecase {
    private final CategoryRepository categoryRepository;


    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoryRepository.deleteById(id);
    }

}
