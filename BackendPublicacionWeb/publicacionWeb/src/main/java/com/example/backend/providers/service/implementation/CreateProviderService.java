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
public class CreateProviderService implements CreateProviderUsecase {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final CategoryRepository categoryRepository;

    // Carpeta base (configúrala en application.properties: file.upload.dir=./uploads)
    @Value("${file.upload.dir:./uploads}")
    private String uploadDir;

    // Opcional, si querés devolver URL absoluta: app.base-url=https://tusitio.com
    @Value("${app.base-url:}")
    private String baseUrl;

    /**
     * Crea un proveedor y opcionalmente guarda su imagen en disco.
     * La imagen debe llegar por @RequestPart("image") como MultipartFile.
     */
    @Override
    public ProviderResponseDTO create(ProviderRequestDTO request, MultipartFile image) {
        // 1) Validar categoría
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException(
                        "Categoría con id " + request.getCategoryId() + " no encontrada"
                ));

        if (request.getDescription() != null && request.getDescription().length() > 255) {
            throw new RuntimeException(
                    "La descripción no puede superar los 255 caracteres."
            );
        }

        // 2) Mapear DTO -> Entidad
        validateProviderRequest(request);
        Provider provider = providerMapper.toEntity(request);
        provider.setCategory(category);

        // 3) Si hay imagen, guardarla y setear photoUrl
        if (image != null && !image.isEmpty()) {
            String photoUrl = storeImage(image, "proveedores");
            provider.setPhotoUrl(photoUrl);
        }

        // 4) Persistir
        Provider saved = providerRepository.save(provider);
        return providerMapper.toDTO(saved);
    }

    /**
     * Guarda la imagen en: {uploadDir}/{subfolder}/{uuid}.{ext}
     * Devuelve la URL pública: /uploads/{subfolder}/{filename} (o absoluta si app.base-url está definido)
     */
    private String storeImage(MultipartFile file, String subfolder) {
        // Validar tipo
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/jpeg")
                        || contentType.equals("image/png")
                        || contentType.equals("image/webp"))) {
            throw new RuntimeException("Formato de imagen inválido. Use JPG, PNG o WEBP.");
        }

        // Determinar extensión por contentType
        String ext = switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> "";
        };

        // Generar nombre seguro
        String filename = UUID.randomUUID() + ext;

        // Crear carpeta si no existe
        Path targetFolder = Paths.get(uploadDir, subfolder).toAbsolutePath().normalize();
        try {
            Files.createDirectories(targetFolder);
            Path target = targetFolder.resolve(filename);
            file.transferTo(target.toFile());
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar la imagen", e);
        }

        // Construir URL pública
        String publicPath = "/uploads/" + subfolder + "/" + filename;
        if (baseUrl != null && !baseUrl.isBlank()) {
            return baseUrl + publicPath;
        }
        return publicPath;
    }

    /**
     * Valida que los datos del proveedor cumplan las reglas del negocio.
     * Lanza RuntimeException (o una excepción custom) con mensajes claros.
     */
    private void validateProviderRequest(ProviderRequestDTO request) {
        // Validar nombre único
        if (providerRepository.existsByNameAndLastName(
                request.getName().trim(),
                request.getLastName().trim()
        )) {
            throw new RuntimeException(
                    "Ya existe un proveedor registrado con el nombre y apellido indicados."
            );
        }

        // Validar teléfono único
        if (providerRepository.existsByPhone(request.getPhone().trim())) {
            throw new RuntimeException(
                    "El teléfono ingresado ya está asociado a otro proveedor."
            );
        }

        // Validar descripción mínima
        if (request.getDescription() == null || request.getDescription().length() < 10) {
            throw new RuntimeException(
                    "La descripción debe tener al menos 10 caracteres."
            );
        }
    }
}
