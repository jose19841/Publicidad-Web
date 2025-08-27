package com.example.backend.comments.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {
    private Long providerId;
    private String content;
}
