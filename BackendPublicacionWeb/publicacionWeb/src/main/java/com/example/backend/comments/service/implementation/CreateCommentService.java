package com.example.backend.comments.service.implementation;

import com.example.backend.comments.controller.dto.CommentRequestDTO;
import com.example.backend.comments.controller.dto.CommentResponseDTO;
import com.example.backend.comments.domain.Comment;
import com.example.backend.comments.infrastructure.CommentRepository;
import com.example.backend.comments.service.mapper.CommentMapper;
import com.example.backend.comments.service.usecase.CreateCommentUsecase;
import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateCommentService implements CreateCommentUsecase {
    private final CommentRepository commentRepository;
    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponseDTO createComment(CommentRequestDTO request) {
        log.info("→ [CreateCommentService] Creando comentario para providerId={} con contenido='{}'",
                request.getProviderId(), request.getContent());
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            log.debug("[CreateCommentService] Usuario autenticado email={}", email);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        log.warn("✗ [CreateCommentService] Usuario no encontrado email={}", email);
                        return new RuntimeException("Usuario no encontrado");
                    });

            Provider provider = providerRepository.findById(request.getProviderId())
                    .orElseThrow(() -> {
                        log.warn("✗ [CreateCommentService] Prestador no encontrado providerId={}", request.getProviderId());
                        return new RuntimeException("Prestador no encontrado");
                    });

            Comment comment = commentMapper.toEntity(request, user, provider);
            Comment savedComment = commentRepository.save(comment);
            CommentResponseDTO response = commentMapper.toResponseDTO(savedComment);

            log.info("✓ [CreateCommentService] Comentario creado id={} userId={} providerId={}",
                    response.getId(), response.getUserId(), response.getProviderId());
            return response;
        } catch (Exception e) {
            log.error("✗ [CreateCommentService] Error al crear comentario para providerId={}", request.getProviderId(), e);
            throw e;
        }
    }
}
