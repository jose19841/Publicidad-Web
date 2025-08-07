package com.example.backend.categories.service;

import com.example.backend.categories.controller.dto.CategoryRequestDTO;
import com.example.backend.categories.controller.dto.CategoryResponseDTO;
import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.categories.service.usecase.DeleteCategoryUsecase;
import com.example.backend.categories.service.usecase.GetAllCategoriesUsecase;
import com.example.backend.categories.service.usecase.RegisterCategoryUsecase;
import com.example.backend.categories.service.usecase.UpdateCategoriesUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RegisterCategoryUsecase registerService;

    @Autowired
    private GetAllCategoriesUsecase getAllService;

    @Autowired
    private UpdateCategoriesUsecase categoriesUsecase;

    @Autowired
    DeleteCategoryUsecase deleteCategoryUsecase;

    // crear Categoria
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTo) {return registerService.createCategory(requestDTo);}

    // listar todas las categorias

    public List<CategoryResponseDTO> getAllCategories() {return getAllService.getAllCategories();}


    // actualizar categorias

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {return categoriesUsecase.updateCategory(id, requestDTO);}

    // elimianr categoria
    public void deleteCategory(Long id) {deleteCategoryUsecase.deleteCategory(id);}
}