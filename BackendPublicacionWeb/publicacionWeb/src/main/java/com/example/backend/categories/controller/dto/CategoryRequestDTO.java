package com.example.backend.categories.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryRequestDTO {
    @NotBlank(message = "El Nombre es Obligatorio")
    @Size(max = 100, message = "el nombre no puede superar los 100 caracteres")
    private String name;

    @Size(max = 255, message = "la descripcion no puede superrar los 255 caracteres")
    private String description;

    public CategoryRequestDTO(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
