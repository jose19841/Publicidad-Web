package com.example.backend.providers.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderRequestDTO {
    private String name;
    private String lastName;
    private String address;
    private String phone;
    private String description;
    private String photoUrl;
    private Boolean isActive;
    private Long categoryId;

}
