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
        log.info("Iniciando actualización de proveedor con id {}", id);

        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Proveedor con id {} no existe", id);
                    return new RuntimeException("Proveedor con id " + id + " no existe");
                });

        log.debug("Proveedor encontrado: {} {}", provider.getName(), provider.getLastName());

        // actualizar campos básicos
        provider.setName(request.getName());
        provider.setLastName(request.getLastName());
        provider.setAddress(request.getAddress());
        provider.setPhone(request.getPhone());
        provider.setDescription(request.getDescription());
        provider.setIsActive(request.getIsActive());

        log.debug("Campos básicos actualizados para proveedor id {}", id);

        // decidir qué hacer con la imagen
        if ("replace".equalsIgnoreCase(imageAction) && image != null && !image.isEmpty()) {
            log.info("Reemplazando imagen del proveedor id {}", id);
            String photoUrl = storeImage(image, "proveedores");
            provider.setPhotoUrl(photoUrl);
        } else if ("remove".equalsIgnoreCase(imageAction)) {
            log.info("Eliminando imagen del proveedor id {}", id);
            provider.setPhotoUrl("");
        } else {
            log.debug("Manteniendo imagen actual del proveedor id {}", id);
        }

        Provider saved = providerRepository.save(provider);
        log.info("Proveedor con id {} actualizado correctamente", id);

        return providerMapper.toDTO(saved);
    }

    /**
     * Guarda la imagen en: {uploadDir}/{subfolder}/{uuid}.{ext}
     */
    private String storeImage(MultipartFile file, String subfolder) {
        log.debug("Guardando imagen para proveedor en subcarpeta {}", subfolder);

        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/jpeg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/webp"))) {
            log.error("Formato de imagen inválido: {}", contentType);
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
            log.info("Imagen guardada exitosamente en {}", target);
        } catch (IOException e) {
            log.error("No se pudo guardar la imagen para proveedor: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo guardar la imagen", e);
        }

        String publicPath = "/uploads/" + subfolder + "/" + filename;
        String fullPath = (baseUrl != null && !baseUrl.isBlank()) ? baseUrl + publicPath : publicPath;

        log.debug("URL pública generada para imagen: {}", fullPath);
        return fullPath;
    }

    /**
     * Valida que los datos del proveedor sean correctos.
     * Permite que el nombre/teléfono actuales se mantengan sin error.
     */
    private void validateProviderRequest(ProviderUpdateRequestDTO request, Long idActual) {
        log.debug("Validando datos del proveedor con id {}", idActual);

        providerRepository.findByNameAndLastName(
                request.getName().trim(),
                request.getLastName().trim()
        ).ifPresent(existing -> {
            if (!existing.getId().equals(idActual)) {
                log.error("Ya existe proveedor con el mismo nombre y apellido: {} {}", request.getName(), request.getLastName());
                throw new RuntimeException(
                        "Ya existe un proveedor registrado con el nombre y apellido indicados."
                );
            }
        });

        providerRepository.findByPhone(request.getPhone().trim())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(idActual)) {
                        log.error("El teléfono {} ya está asociado a otro proveedor", request.getPhone());
                        throw new RuntimeException(
                                "El teléfono ingresado ya está asociado a otro proveedor."
                        );
                    }
                });

        if (request.getDescription() == null || request.getDescription().length() < 10) {
            log.error("Descripción inválida para proveedor id {}. Longitud: {}", idActual,
                    request.getDescription() != null ? request.getDescription().length() : 0);
            throw new RuntimeException(
                    "La descripción debe tener al menos 10 caracteres."
            );
        }

        log.debug("Validación completada correctamente para proveedor id {}", idActual);
    }
}
