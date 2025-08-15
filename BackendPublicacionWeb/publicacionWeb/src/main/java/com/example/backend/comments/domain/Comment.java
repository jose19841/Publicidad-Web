package com.example.backend.comments.domain;


import com.example.backend.providers.domain.Provider;
import com.example.backend.users.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    // relacion con usuario
    @ManyToOne(optional=false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // relacion con prestamista
    @ManyToOne(optional = false)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @PrePersist
    public void prepersist(){
        this.createdAt= LocalDateTime.now();
    }
}
