package com.example.backend.providers.service.implementation;

import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.controller.dto.ProviderUpdateRequestDTO;
import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.service.Mapper.ProviderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private ProviderMapper providerMapper;

    @InjectMocks
    private UpdateProviderService updateProviderService;

    private Provider provider;
    private ProviderUpdateRequestDTO request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        provider = new Provider();
        provider.setId(1L);
        provider.setPhotoUrl("old.jpg");

        request = new ProviderUpdateRequestDTO();
        request.setName("Nuevo");
        request.setLastName("Apellido");
        request.setPhone("999");
        request.setDescription("Descripcion más larga");

        // ✅ setear el uploadDir que tu servicio necesita
        ReflectionTestUtils.setField(updateProviderService, "uploadDir", "./uploads-test");
    }

    // ✅ 1. Actualiza todos los campos básicos
    @Test
    void update_actualizaCamposBasicos() {
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(providerRepository.save(any())).thenReturn(provider);
        when(providerMapper.toDTO(any())).thenReturn(new ProviderResponseDTO());

        ProviderResponseDTO result = updateProviderService.update(1L, request, null, "keep");

        assertNotNull(result);
        assertEquals("Nuevo", provider.getName());
        assertEquals("Apellido", provider.getLastName());
        assertEquals("999", provider.getPhone());
        verify(providerRepository).save(provider);
    }

    // ✅ 2. ID inexistente → lanza excepción
    @Test
    void update_idNoExistente_lanzaExcepcion() {
        when(providerRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> updateProviderService.update(1L, request, null, "keep"));

        assertTrue(ex.getMessage().contains("Proveedor con id 1 no existe"));
    }

    // ✅ 3. Imagen nueva con action=replace
    @Test
    void update_replaceImage_conImagenValida() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "img.jpg", "image/jpeg", "dummy".getBytes());

        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(providerRepository.save(any())).thenReturn(provider);
        when(providerMapper.toDTO(any())).thenReturn(new ProviderResponseDTO());

        updateProviderService.update(1L, request, file, "replace");

        assertNotNull(provider.getPhotoUrl());
        assertTrue(provider.getPhotoUrl().endsWith(".jpg"));
        verify(providerRepository).save(provider);
    }

    // ✅ 4. Eliminar imagen con action=remove
    @Test
    void update_removeImage() {
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(providerRepository.save(any())).thenReturn(provider);
        when(providerMapper.toDTO(any())).thenReturn(new ProviderResponseDTO());

        updateProviderService.update(1L, request, null, "remove");

        assertEquals("", provider.getPhotoUrl());
        verify(providerRepository).save(provider);
    }

    // ✅ 5. Mantener imagen con action=keep
    @Test
    void update_keepImage() {
        String oldUrl = provider.getPhotoUrl();

        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(providerRepository.save(any())).thenReturn(provider);
        when(providerMapper.toDTO(any())).thenReturn(new ProviderResponseDTO());

        updateProviderService.update(1L, request, null, "keep");

        assertEquals(oldUrl, provider.getPhotoUrl());
        verify(providerRepository).save(provider);
    }

    // ✅ 6. Imagen inválida (PDF en vez de imagen)
    @Test
    void update_imagenInvalida_lanzaExcepcion() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "doc.pdf", "application/pdf", "dummy".getBytes());

        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> updateProviderService.update(1L, request, file, "replace"));

        assertTrue(ex.getMessage().contains("Formato de imagen inválido"));
    }

    // 👉 Opcionales: tests para validateProviderRequest (duplicados, descripción corta, etc.)
}
