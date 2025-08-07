package com.example.backend.categories.service.Mapper;

import com.example.backend.categories.controller.dto.CategoryRequestDTO;
import com.example.backend.categories.controller.dto.CategoryResponseDTO;
import com.example.backend.categories.domain.Category;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {

    public static Category toEntity(CategoryRequestDTO dto){
        if(dto==null) return null;
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        return category;
    }

    public static CategoryResponseDTO toResponseDTO(Category category){
        if(category==null) return null;
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt()
        );
    }
}
