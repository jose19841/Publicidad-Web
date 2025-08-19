package com.example.backend.providers.domain;

import com.example.backend.categories.domain.Category;
import com.example.backend.shared.auduting.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name = "provider")
public class Provider  extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del prestador es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "El apellido del prestador es obligatorio")
    @Size(max = 100, message = "El apellido no puede tener más de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String lastName;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255, message = "La dirección no puede tener más de 255 caracteres")
    @Column(nullable = false, length = 255)
    private String address;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{6,25}", message = "El teléfono debe contener solo números y tener entre 6 y 25 dígitos")
    @Column(nullable = false, unique = true, length = 25)
    private String phone;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    @Column(nullable = true, length = 255)
    private String description;

    @Size(max = 255, message = "La URL de la foto no puede tener más de 255 caracteres")
    @Column(nullable = true, length = 255)
    private String photoUrl;

    @NotNull(message = "El estado activo/inactivo es obligatorio")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;



    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;



    @Override
    public String toString() {
        return "Proveedor {" +
                "id=" + id +
                ", nombre='" + name + '\'' +
                ", apellido='" + lastName + '\'' +
                ", dirección='" + address + '\'' +
                ", teléfono='" + phone + '\'' +
                ", descripción='" + description + '\'' +
                ", URL foto='" + photoUrl + '\'' +
                ", activo=" + isActive +
                ", categoría=" + (category != null ? category.getName() : "Sin categoría") +
                ", creado=" + getCreatedAt() +
                ", actualizado=" + getModifiedAt() +
                '}';
    }
}
