package com.example.backend.categories.controller.controllers;

import com.example.backend.categories.service.CategoryService;
import com.example.backend.categories.controller.dto.CategoryRequestDTO;
import com.example.backend.categories.controller.dto.CategoryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/categories")
@Tag(name = "Categorías", description = "API para administrar categorías")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(
            summary = "Crear Categoria",
            description = "Crea una nueva categoría utilizando la información proporcionada"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CategoryRequestDTO requestDTO) {
        log.info("Petición recibida para crear categoría con nombre={}", requestDTO.getName());
        CategoryResponseDTO response = categoryService.createCategory(requestDTO);
        log.info("Categoría creada con id={} y nombre={}", response.getId(), response.getName());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Ver Categorias",
            description = "Recupera una lista de todas las categorías"
    )
    @ApiResponse(responseCode = "200", description = "List of categories retrieved successfully")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        log.info("Petición recibida para listar todas las categorías");
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        log.info("Se recuperaron {} categorías", categories.size());
        return ResponseEntity.ok(categories);
    }

    @Operation(
            summary = "Editar Categorias",
            description = "Actualiza una categoría existente por su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO requestDTO) {
        log.info("Petición recibida para actualizar categoría id={} con nuevos datos", id);
        CategoryResponseDTO updated = categoryService.updateCategory(id, requestDTO);
        log.info("Categoría actualizada id={} nuevoNombre={}", updated.getId(), updated.getName());
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Eliminar Categoria",
            description = "Elimina una categoría por su id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("Petición recibida para eliminar categoría id={}", id);
        categoryService.deleteCategory(id);
        log.info("Categoría eliminada id={}", id);
        return ResponseEntity.noContent().build();
    }
}
