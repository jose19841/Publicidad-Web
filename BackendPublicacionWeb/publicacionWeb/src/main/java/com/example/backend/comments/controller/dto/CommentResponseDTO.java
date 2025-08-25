package com.example.backend.comments.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDTO {
    private Long id;
    private Long userId;
    private Long providerId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
}
