package com.example.backend.comments.service.usecase;

import com.example.backend.comments.controller.dto.CommentRequestDTO;
import com.example.backend.comments.controller.dto.CommentResponseDTO;

public interface CreateCommentUsecase {
    CommentResponseDTO createComment(CommentRequestDTO request);
}
