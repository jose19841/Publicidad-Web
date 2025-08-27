package com.example.backend.comments.service.implementation;

import com.example.backend.comments.infrastructure.CommentRepository;
import com.example.backend.comments.service.usecase.DeleteCommentUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteCommentService implements DeleteCommentUsecase {

    private final CommentRepository commentRepository;
    @Override
    public void execute(Long id) {
        log.info("→ [DeleteCommentService] Deleting comment with id={}", id);
        commentRepository.deleteById(id);
    }
}
