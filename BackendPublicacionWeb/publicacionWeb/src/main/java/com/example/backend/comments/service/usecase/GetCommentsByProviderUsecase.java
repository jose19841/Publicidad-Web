package com.example.backend.comments.service.usecase;

import com.example.backend.comments.controller.dto.CommentResponseDTO;

import java.util.List;

public interface GetCommentsByProviderUsecase {

    List<CommentResponseDTO> getCommentsByProvider(Long providerId);
}
