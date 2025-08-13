package com.example.backend.ratings.controller.dto;

import jakarta.annotation.security.DenyAll;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CreateRatingRequest {
    @NotNull
    @Min(1) @Max(5)
    private Integer score;
}