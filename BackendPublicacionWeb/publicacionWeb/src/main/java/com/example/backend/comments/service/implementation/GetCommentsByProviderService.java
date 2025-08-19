package com.example.backend.comments.service.implementation;

import com.example.backend.comments.controller.dto.CommentResponseDTO;
import com.example.backend.comments.infrastructure.CommentRepository;
import com.example.backend.comments.service.mapper.CommentMapper;
import com.example.backend.comments.service.usecase.GetCommentsByProviderUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetCommentsByProviderService implements GetCommentsByProviderUsecase {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentResponseDTO> getCommentsByProvider(Long providerId) {
        log.info("→ [GetCommentsByProviderService] Solicitando comentarios para providerId={}", providerId);
        try {
            List<CommentResponseDTO> comments = commentRepository.findByProviderId(providerId)
                    .stream()
                    .map(commentMapper::toResponseDTO)
                    .toList();

            log.info("✓ [GetCommentsByProviderService] Se recuperaron {} comentarios para providerId={}",
                    comments.size(), providerId);
            return comments;
        } catch (Exception e) {
            log.error("✗ [GetCommentsByProviderService] Error al obtener comentarios para providerId={}", providerId, e);
            throw e;
        }
    }
}
