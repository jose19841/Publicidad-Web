package com.example.backend.providers.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderUpdateRequestDTO {
    private String name;
    private String lastName;
    private String address;
    private String phone;
    private String description;
    private Boolean isActive;
    private Long categoryId;
    private Boolean removeImage;
}
