package com.example.backend.comments.service.mapper;

import com.example.backend.comments.domain.Comment;
import com.example.backend.comments.controller.dto.CommentRequestDTO;
import com.example.backend.comments.controller.dto.CommentResponseDTO;
import com.example.backend.providers.domain.Provider;
import com.example.backend.users.domain.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toEntity(CommentRequestDTO dto, User user, Provider provider) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setUser(user);
        comment.setProvider(provider);
        return comment;

    }

    public CommentResponseDTO toResponseDTO(Comment comment){
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setUserId(comment.getUser().getId());
        dto.setUsername(comment.getUser().getUsername());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }

}
