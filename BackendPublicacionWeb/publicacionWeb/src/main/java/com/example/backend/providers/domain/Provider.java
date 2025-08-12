    package com.example.backend.providers.domain;
    
    import com.example.backend.categories.domain.Category;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    
    import java.time.LocalDateTime;
    
    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name="provider")
    public class Provider {
    
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
        @Column(nullable = false, length = 100)
        private String name;
    
        @Column(nullable = false, length = 100)
        private String lastName;
    
        @Column(nullable = false, length = 255)
        private String address;
    
        @Column(nullable = false, unique = true, length = 25)
        private String phone;
    
        @Column(nullable = true, length = 255)
        private String description;
    
        @Column(nullable = true, length = 255)
        private String photoUrl;
    
        @Column(name = "is_active", nullable = false)
        private Boolean isActive=true;
    
        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;
    
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;
    
        @ManyToOne(optional = false)
        @JoinColumn(name = "category_id", nullable = false)
        Category category;
    
        @PrePersist
        protected void onCreate() {
            this.createdAt = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
        }
    
        @PreUpdate
        protected void onUpdate() {
            this.updatedAt = LocalDateTime.now();
        }
    
    }