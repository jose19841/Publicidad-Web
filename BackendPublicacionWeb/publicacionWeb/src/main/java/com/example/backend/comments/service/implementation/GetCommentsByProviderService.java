package com.example.backend.comments.service.implementation;


import com.example.backend.comments.controller.dto.CommentResponseDTO;
import com.example.backend.comments.infrastructure.CommentRepository;
import com.example.backend.comments.service.mapper.CommentMapper;
import com.example.backend.comments.service.usecase.GetCommentsByProviderUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCommentsByProviderService implements GetCommentsByProviderUsecase {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentResponseDTO> getCommentsByProvider(Long providerId) {
        return commentRepository.findByProviderId(providerId)
                .stream()
                .map(commentMapper::toResponseDTO)
                .toList();
    }
}
