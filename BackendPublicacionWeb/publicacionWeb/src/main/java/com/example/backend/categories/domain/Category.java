package com.example.backend.categories.domain;

import com.example.backend.shared.auduting.Auditable;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "category",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Category extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description;


    public Category() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
