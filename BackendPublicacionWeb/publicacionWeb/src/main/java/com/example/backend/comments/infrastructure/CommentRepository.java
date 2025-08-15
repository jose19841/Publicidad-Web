package com.example.backend.comments.infrastructure;

import com.example.backend.comments.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
      List<Comment> findByProviderId(Long providerId);
}
