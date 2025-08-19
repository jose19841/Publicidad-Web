package com.example.backend.providers.service.implementation;

import com.example.backend.categories.domain.Category;
import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.service.Mapper.ProviderMapper;
import com.example.backend.providers.service.usecase.CreateProviderUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CreateProviderService implements CreateProviderUsecase {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final CategoryRepository categoryRepository;

    @Value("${file.upload.dir:./uploads}")
    private String uploadDir;

    @Value("${app.base-url:}")
    private String baseUrl;

    @Override
    public ProviderResponseDTO create(ProviderRequestDTO request, MultipartFile image) {
        log.info("Iniciando creación de proveedor con nombre: {} {}", request.getName(), request.getLastName());

        // 1) Validar categoría
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> {
                    log.error("Categoría con id {} no encontrada", request.getCategoryId());
                    return new RuntimeException("Categoría con id " + request.getCategoryId() + " no encontrada");
                });

        if (request.getDescription() != null && request.getDescription().length() > 255) {
            log.error("Descripción excede los 255 caracteres: {}", request.getDescription().length());
            throw new RuntimeException("La descripción no puede superar los 255 caracteres.");
        }

        // 2) Mapear DTO -> Entidad
        validateProviderRequest(request);
        Provider provider = providerMapper.toEntity(request);
        provider.setCategory(category);
        log.debug("Proveedor mapeado: {}", provider);

        // 3) Guardar imagen si existe
        if (image != null && !image.isEmpty()) {
            log.info("Procesando imagen para proveedor...");
            String photoUrl = storeImage(image, "proveedores");
            provider.setPhotoUrl(photoUrl);
            log.debug("Imagen guardada en: {}", photoUrl);
        }

        // 4) Persistir
        Provider saved = providerRepository.save(provider);
        log.info("Proveedor creado con id: {}", saved.getId());

        return providerMapper.toDTO(saved);
    }

    private String storeImage(MultipartFile file, String subfolder) {
        log.debug("Almacenando imagen en subcarpeta: {}", subfolder);

        // Validar tipo
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/jpeg")
                        || contentType.equals("image/png")
                        || contentType.equals("image/webp"))) {
            log.error("Formato de imagen inválido: {}", contentType);
            throw new RuntimeException("Formato de imagen inválido. Use JPG, PNG o WEBP.");
        }

        // Determinar extensión
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
            log.info("Imagen guardada en ruta local: {}", target);
        } catch (IOException e) {
            log.error("Error al guardar la imagen", e);
            throw new RuntimeException("No se pudo guardar la imagen", e);
        }

        String publicPath = "/uploads/" + subfolder + "/" + filename;
        if (baseUrl != null && !baseUrl.isBlank()) {
            return baseUrl + publicPath;
        }
        return publicPath;
    }

    private void validateProviderRequest(ProviderRequestDTO request) {
        log.debug("Validando datos del proveedor...");

        if (providerRepository.existsByNameAndLastName(
                request.getName().trim(),
                request.getLastName().trim()
        )) {
            log.error("Duplicado: nombre={} apellido={}", request.getName(), request.getLastName());
            throw new RuntimeException("Ya existe un proveedor registrado con el nombre y apellido indicados.");
        }

        if (providerRepository.existsByPhone(request.getPhone().trim())) {
            log.error("Teléfono duplicado: {}", request.getPhone());
            throw new RuntimeException("El teléfono ingresado ya está asociado a otro proveedor.");
        }

        if (request.getDescription() == null || request.getDescription().length() < 10) {
            log.error("Descripción inválida: {}", request.getDescription());
            throw new RuntimeException("La descripción debe tener al menos 10 caracteres.");
        }
        log.debug("Validación de proveedor exitosa");
    }
}
