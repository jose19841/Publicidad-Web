package com.example.backend.comments.service;

import com.example.backend.comments.controller.dto.CommentRequestDTO;
import com.example.backend.comments.controller.dto.CommentResponseDTO;
import com.example.backend.comments.infrastructure.CommentRepository;
import com.example.backend.comments.service.usecase.CreateCommentUsecase;
import com.example.backend.comments.service.usecase.GetCommentsByProviderUsecase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
        log.info("→ [CommentService] Creando comentario para providerId={} con contenido='{}'",
                request.getProviderId(), request.getContent());
        try {
            CommentResponseDTO response = createCommentUsecase.createComment(request);
            log.info("✓ [CommentService] Comentario creado id={} userId={} providerId={}",
                    response.getId(), response.getUserId(), response.getProviderId());
            return response;
        } catch (Exception e) {
            log.error("✗ [CommentService] Error al crear comentario para providerId={}", request.getProviderId(), e);
            throw e;
        }
    }

    // Listar comentarios por Prestador
    public List<CommentResponseDTO> getCommentByProvider(Long providerId) {
        log.info("→ [CommentService] Obteniendo comentarios para providerId={}", providerId);
        try {
            List<CommentResponseDTO> comments = getCommentsByProviderUsecase.getCommentsByProvider(providerId);
            log.info("✓ [CommentService] Se recuperaron {} comentarios para providerId={}", comments.size(), providerId);
            return comments;
        } catch (Exception e) {
            log.error("✗ [CommentService] Error al obtener comentarios para providerId={}", providerId, e);
            throw e;
        }
    }
}
