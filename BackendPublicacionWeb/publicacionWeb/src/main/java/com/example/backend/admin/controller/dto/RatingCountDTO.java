package com.example.backend.admin.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingCountDTO {
    private int stars;
    private long count;
}