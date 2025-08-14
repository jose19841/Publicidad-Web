package com.example.backend.providers.service.implementation;

import com.example.backend.categories.domain.Category;
import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.controller.dto.ProviderUpdateRequestDTO;
import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.service.Mapper.ProviderMapper;
import com.example.backend.providers.service.usecase.UpdateProviderUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateProviderService implements UpdateProviderUsecase {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final CategoryRepository categoryRepository;

    @Value("${file.upload.dir:./uploads}")
    private String uploadDir;

    @Value("${app.base-url:}")
    private String baseUrl;

    @Override

    public ProviderResponseDTO update(Long id, ProviderUpdateRequestDTO request, MultipartFile image, String imageAction) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor con id " + id + " no existe"));

        // actualizar campos básicos
        provider.setName(request.getName());
        provider.setLastName(request.getLastName());
        provider.setAddress(request.getAddress());
        provider.setPhone(request.getPhone());
        provider.setDescription(request.getDescription());
        provider.setIsActive(request.getIsActive());

        // decidir qué hacer con la imagen
        if ("replace".equalsIgnoreCase(imageAction) && image != null && !image.isEmpty()) {
            String photoUrl = storeImage(image, "proveedores");
            provider.setPhotoUrl(photoUrl);
        } else if ("remove".equalsIgnoreCase(imageAction)) {
            provider.setPhotoUrl("");
        }
        // si es keep → no tocar la imagen

        Provider saved = providerRepository.save(provider);
        return providerMapper.toDTO(saved);
    }

    /**
     * Guarda la imagen en: {uploadDir}/{subfolder}/{uuid}.{ext}
     */
    private String storeImage(MultipartFile file, String subfolder) {
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/jpeg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/webp"))) {
            throw new RuntimeException("Formato de imagen inválido. Use JPG, PNG o WEBP.");
        }

        String ext = switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> "";
        };

        String filename = UUID.randomUUID() + ext;

        Path targetFolder = Paths.get(uploadDir, subfolder).toAbsolutePath().normalize();
        try {
            Files.createDirectories(targetFolder);
            Path target = targetFolder.resolve(filename);
            file.transferTo(target.toFile());
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar la imagen", e);
        }

        String publicPath = "/uploads/" + subfolder + "/" + filename;
        if (baseUrl != null && !baseUrl.isBlank()) {
            return baseUrl + publicPath;
        }
        return publicPath;
    }

    /**
     * Valida que los datos del proveedor sean correctos.
     * Permite que el nombre/teléfono actuales se mantengan sin error.
     */
    private void validateProviderRequest(ProviderUpdateRequestDTO request, Long idActual) {
        providerRepository.findByNameAndLastName(
                request.getName().trim(),
                request.getLastName().trim()
        ).ifPresent(existing -> {
            if (!existing.getId().equals(idActual)) {
                throw new RuntimeException(
                        "Ya existe un proveedor registrado con el nombre y apellido indicados."
                );
            }
        });

        providerRepository.findByPhone(request.getPhone().trim())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(idActual)) {
                        throw new RuntimeException(
                                "El teléfono ingresado ya está asociado a otro proveedor."
                        );
                    }
                });

        if (request.getDescription() == null || request.getDescription().length() < 10) {
            throw new RuntimeException(
                    "La descripción debe tener al menos 10 caracteres."
            );
        }
    }
}
