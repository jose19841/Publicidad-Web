package com.example.backend.categories.service.implementation;

import com.example.backend.categories.controller.dto.CategoryResponseDTO;
import com.example.backend.categories.domain.Category;
import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.categories.service.Mapper.CategoryMapper;
import com.example.backend.categories.service.usecase.GetAllCategoriesUsecase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GetAllCategories implements GetAllCategoriesUsecase {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        log.info("→ [GetAllCategories] Solicitud para obtener todas las categorías ordenadas por nombre");
        try {
            List<Category> categories = categoryRepository.findAllByOrderByNameAsc();
            List<CategoryResponseDTO> response = categories.stream()
                    .map(CategoryMapper::toResponseDTO)
                    .collect(Collectors.toList());
            log.info("✓ [GetAllCategories] Se recuperaron {} categorías", response.size());
            return response;
        } catch (Exception e) {
            log.error("✗ [GetAllCategories] Error al recuperar categorías", e);
            throw e;
        }
    }
}
