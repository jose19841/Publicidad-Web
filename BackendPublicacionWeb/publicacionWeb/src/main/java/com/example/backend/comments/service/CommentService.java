package com.example.backend.comments.service;

import com.example.backend.comments.controller.dto.CommentRequestDTO;
import com.example.backend.comments.controller.dto.CommentResponseDTO;
import com.example.backend.comments.infrastructure.CommentRepository;
import com.example.backend.comments.service.usecase.CreateCommentUsecase;
import com.example.backend.comments.service.usecase.GetCommentsByProviderUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CreateCommentUsecase createCommentUsecase;

    @Autowired
    private GetCommentsByProviderUsecase getCommentsByProviderUsecase;

    // crear comentario
    public CommentResponseDTO createComment(CommentRequestDTO request) {
        return createCommentUsecase.createComment(request);
    }
    // Listar comentarios por Prestador
    public List<CommentResponseDTO> getCommentByProvider(Long providerId) {
        return getCommentsByProviderUsecase.getCommentsByProvider(providerId);
    }
}
