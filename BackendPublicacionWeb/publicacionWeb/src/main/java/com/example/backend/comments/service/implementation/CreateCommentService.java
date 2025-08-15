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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCommentService implements CreateCommentUsecase {
    private final CommentRepository commentRepository;
    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;


    @Override
    public CommentResponseDTO createComment(CommentRequestDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Provider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> new RuntimeException("Prestador no encontrado"));

        Comment comment = commentMapper.toEntity(request, user, provider);
        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toResponseDTO(savedComment);

    }
}

