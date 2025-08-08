package com.example.backend.categories.service.implementation;

import com.example.backend.categories.controller.dto.CategoryResponseDTO;
import com.example.backend.categories.domain.Category;
import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.categories.service.Mapper.CategoryMapper;
import com.example.backend.categories.service.usecase.GetAllCategoriesUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetAllCategories implements GetAllCategoriesUsecase {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List< Category > categories = categoryRepository.findAllByOrderByNameAsc();
        return categories.stream()
                .map(CategoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
