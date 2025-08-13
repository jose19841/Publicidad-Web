package com.example.backend.ratings.domain;

import com.example.backend.providers.domain.Provider;
import com.example.backend.users.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "rating",
        uniqueConstraints = @UniqueConstraint(name = "uk_rating_user_provider", columnNames = {"user_id","provider_id"}),
        indexes = {
                @Index(name = "ix_rating_provider", columnList = "provider_id"),
                @Index(name = "ix_rating_user", columnList = "user_id")
        }
)
@Getter @Setter
public class Rating {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="provider_id", nullable=false)
    private Provider provider;

    @Column(nullable=false)
    private Integer score; // 1..5

    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
