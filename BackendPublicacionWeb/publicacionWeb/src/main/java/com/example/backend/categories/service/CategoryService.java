package com.example.backend.categories.service;

import com.example.backend.categories.controller.CategoryRequestDTO;
import com.example.backend.categories.controller.CategoryResponseDTO;
import com.example.backend.categories.domain.Category;
import com.example.backend.categories.infrastructure.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // crear Categoria
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTo) {
        Category category = CategoryMapper.toEntity(requestDTo);
        category.setCreatedAt(LocalDateTime.now());
        Category saved = categoryRepository.save(category);
        return CategoryMapper.toResponseDTO(saved);

    }

    // listar todas las categorias

    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAllByOrderByNameAsc();
        return categories.stream()
                .map(CategoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        existing.setName(requestDTO.getName());
        existing.setDescription(requestDTO.getDescription());
        existing.setUpdatedAt(LocalDateTime.now());

        Category updated = categoryRepository.save(existing);
        return CategoryMapper.toResponseDTO(updated);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoryRepository.deleteById(id);
    }
}