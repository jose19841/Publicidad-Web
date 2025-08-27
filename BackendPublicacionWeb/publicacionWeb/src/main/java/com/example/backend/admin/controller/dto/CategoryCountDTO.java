package com.example.backend.admin.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryCountDTO {
    private String category;
    private long count;
}