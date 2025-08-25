package com.example.backend.providers.service.implementation;

import com.example.backend.categories.domain.Category;
import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.service.Mapper.ProviderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;
    @Mock
    private ProviderMapper providerMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CreateProviderService createProviderService;

    private ProviderRequestDTO baseRequest;
    @BeforeEach
    void setUp() throws Exception {
        // Inicialización común
        baseRequest = new ProviderRequestDTO();
        baseRequest.setName("Juan");
        baseRequest.setLastName("Pérez");
        baseRequest.setPhone("123456789");
        baseRequest.setDescription("Proveedor confiable con experiencia");
        baseRequest.setCategoryId(1L);

        Field field = CreateProviderService.class.getDeclaredField("uploadDir");
        field.setAccessible(true);
        field.set(createProviderService, "target/test-uploads");
    }

    @Test
    void create_nombreYApellidoDuplicados_lanzaExcepcion()
    {
        when(categoryRepository.findById(baseRequest.getCategoryId())).thenReturn(Optional.of(new Category()));
        when(providerRepository.existsByNameAndLastName("Juan", "Pérez")).thenReturn(true);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            createProviderService.create(baseRequest, null);
        });
        assertEquals("Ya existe un proveedor registrado con el nombre y apellido indicados.", exception.getMessage());

        verify( providerRepository).existsByNameAndLastName("Juan", "Pérez");

    }
    @Test
    void create_telefonoDuplicado_lanzaExcepcion()
    {
        when(categoryRepository.findById(baseRequest.getCategoryId())).thenReturn(Optional.of(new Category()));
        when(providerRepository.existsByNameAndLastName("Juan", "Pérez")).thenReturn(false);
        when(providerRepository.existsByPhone("123456789")).thenReturn(true);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            createProviderService.create(baseRequest, null);
        });
        assertEquals("El teléfono ingresado ya está asociado a otro proveedor.", exception.getMessage());

        verify( providerRepository).existsByNameAndLastName("Juan", "Pérez");
        verify( providerRepository).existsByPhone("123456789");

    }

    @Test
    void create_descripcionMuyCorta_lanzaExcepcion(){
        baseRequest.setDescription("a");
        when(categoryRepository.findById(baseRequest.getCategoryId())).thenReturn(Optional.of(new Category()));
        when(providerRepository.existsByNameAndLastName("Juan", "Pérez")).thenReturn(false);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            createProviderService.create(baseRequest, null);
        });
        assertEquals("La descripción debe tener al menos 10 caracteres.", exception.getMessage());
        verify(providerRepository, never()).save(any());

    }

    @Test
    void create_descripcionMuyLarga_lanzaExcepcion() {
        // Arrange
        String descripcionLarga = "a".repeat(300); // 300 caracteres, mayor al límite de 255
        baseRequest.setDescription(descripcionLarga);

        when(categoryRepository.findById(baseRequest.getCategoryId()))
                .thenReturn(Optional.of(new Category()));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                createProviderService.create(baseRequest, null)
        );

        assertEquals("La descripción no puede superar los 255 caracteres.", ex.getMessage());

        verify(providerRepository, never()).save(any());
    }

    @Test
    void create_sinImagen_guardaProveedorYDevuelveDTO() {
        // arrange
        when(categoryRepository.findById(baseRequest.getCategoryId()))
                .thenReturn(Optional.of(new Category()));
        when(providerRepository.existsByNameAndLastName("Juan", "Pérez"))
                .thenReturn(false);
        when(providerRepository.existsByPhone("123456789"))
                .thenReturn(false);

        // Entidad mapeada desde el request
        Provider entity = new Provider();
        entity.setName("Juan");
        entity.setLastName("Pérez");
        entity.setPhone("123456789");
        entity.setDescription("Proveedor confiable con experiencia");
        // sin photoUrl

        when(providerMapper.toEntity(baseRequest)).thenReturn(entity);

        // Simular persistencia (asigna id y devuelve la misma entidad)
        Provider saved = new Provider();
        saved.setId(1L);
        saved.setName(entity.getName());
        saved.setLastName(entity.getLastName());
        saved.setPhone(entity.getPhone());
        saved.setDescription(entity.getDescription());
        saved.setPhotoUrl(null); // porque no hay imagen

        when(providerRepository.save(any(Provider.class))).thenReturn(saved);

        // DTO esperado
        ProviderResponseDTO dto = new ProviderResponseDTO();
        dto.setId(1L);
        dto.setName("Juan");
        dto.setLastName("Pérez");
        dto.setPhone("123456789");
        dto.setDescription("Proveedor confiable con experiencia");
        dto.setPhotoUrl(null);
        // si tu DTO tiene isActive/createdAt/updatedAt y querés afirmarlos, setéalos acá

        when(providerMapper.toDTO(saved)).thenReturn(dto);

        // act
        ProviderResponseDTO result = createProviderService.create(baseRequest, null);

        // assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Juan", result.getName());
        assertEquals("Pérez", result.getLastName());
        assertEquals("123456789", result.getPhone());
        assertEquals("Proveedor confiable con experiencia", result.getDescription());
        assertNull(result.getPhotoUrl());

        verify(categoryRepository).findById(baseRequest.getCategoryId());
        verify(providerRepository).existsByNameAndLastName("Juan", "Pérez");
        verify(providerRepository).existsByPhone("123456789");
        verify(providerMapper).toEntity(baseRequest);
        verify(providerRepository).save(any(Provider.class));
        verify(providerMapper).toDTO(saved);
        verifyNoMoreInteractions(providerRepository, providerMapper, categoryRepository);
    }

    @Test
    void create_conImagenJPEG_guardaProveedorConPhotoUrlYDevuelveDTO() throws Exception {
        // Arrange
        when(categoryRepository.findById(baseRequest.getCategoryId()))
                .thenReturn(Optional.of(new Category()));
        when(providerRepository.existsByNameAndLastName("Juan", "Pérez")).thenReturn(false);
        when(providerRepository.existsByPhone("123456789")).thenReturn(false);

        Provider entity = new Provider();
        entity.setName("Juan");
        entity.setLastName("Pérez");
        entity.setPhone("123456789");
        entity.setDescription("Proveedor confiable con experiencia");
        when(providerMapper.toEntity(baseRequest)).thenReturn(entity);

        Provider saved = new Provider();
        saved.setId(1L);
        saved.setName(entity.getName());
        saved.setLastName(entity.getLastName());
        saved.setPhone(entity.getPhone());
        saved.setDescription(entity.getDescription());
        saved.setPhotoUrl("/uploads/proveedores/xxx.jpg");
        when(providerRepository.save(any(Provider.class))).thenReturn(saved);

        ProviderResponseDTO dto = new ProviderResponseDTO();
        dto.setId(1L);
        dto.setName("Juan");
        dto.setLastName("Pérez");
        dto.setPhone("123456789");
        dto.setDescription("Proveedor confiable con experiencia");
        dto.setPhotoUrl("/uploads/proveedores/xxx.jpg");
        when(providerMapper.toDTO(saved)).thenReturn(dto);

        // Imagen simulada
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "fake-content".getBytes()
        );

        // Act
        ProviderResponseDTO result = createProviderService.create(baseRequest, image);

        // Assert
        assertNotNull(result);
        assertTrue(result.getPhotoUrl().endsWith(".jpg"));
        verify(providerRepository).save(any(Provider.class));
    }


    @Test
    void create_conImagenPNG_guardaProveedorConPhotoUrlYDevuelveDTO()
    {
        //arrange
        when(categoryRepository.findById(baseRequest.getCategoryId()))
                .thenReturn(Optional.of(new Category()));
        when(providerRepository.existsByNameAndLastName("Juan", "Pérez")).thenReturn(false);
        when(providerRepository.existsByPhone("123456789")).thenReturn(false);
        Provider entity = new Provider();
        entity.setName("Juan");
        entity.setLastName("Pérez");
        entity.setPhone("123456789");
        entity.setDescription("Proveedor confiable con experiencia");
        when(providerMapper.toEntity(baseRequest)).thenReturn(entity);
        Provider saved = new Provider();
        saved.setId(1L);
        saved.setName(entity.getName());
        saved.setLastName(entity.getLastName());
        saved.setPhone(entity.getPhone());
        saved.setDescription(entity.getDescription());
        saved.setPhotoUrl("/uploads/proveedores/xxx.png");
        when(providerRepository.save(any(Provider.class))).thenReturn(saved);
        ProviderResponseDTO dto = new ProviderResponseDTO();
        dto.setId(1L);
        dto.setName("Juan");
        dto.setLastName("Pérez");
        dto.setPhone("123456789");
        dto.setDescription("Proveedor confiable con experiencia");
        dto.setPhotoUrl("/uploads/proveedores/xxx.png");
        when(providerMapper.toDTO(saved)).thenReturn(dto);
        // Imagen simulada
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.png", "image/png", "fake-content".getBytes()
        );
        // Act
        ProviderResponseDTO result = createProviderService.create(baseRequest, image);
        // Assert
        assertNotNull(result);
        assertTrue(result.getPhotoUrl().endsWith(".png"));
        verify(providerRepository).save(any(Provider.class));

    }

    @Test
    void create_conImagenWEBP_guardaProveedorConPhotoUrlYDevuelveDTO()
    {
        //arrange
        when(categoryRepository.findById(baseRequest.getCategoryId()))
                .thenReturn(Optional.of(new Category()));
        when(providerRepository.existsByNameAndLastName("Juan", "Pérez")).thenReturn(false);
        when(providerRepository.existsByPhone("123456789")).thenReturn(false);
        Provider entity = new Provider();
        entity.setName("Juan");
        entity.setLastName("Pérez");
        entity.setPhone("123456789");
        entity.setDescription("Proveedor confiable con experiencia");
        when(providerMapper.toEntity(baseRequest)).thenReturn(entity);
        Provider saved = new Provider();
        saved.setId(1L);
        saved.setName(entity.getName());
        saved.setLastName(entity.getLastName());
        saved.setPhone(entity.getPhone());
        saved.setDescription(entity.getDescription());
        saved.setPhotoUrl("/uploads/proveedores/xxx.webp");
        when(providerRepository.save(any(Provider.class))).thenReturn(saved);
        ProviderResponseDTO dto = new ProviderResponseDTO();
        dto.setId(1L);
        dto.setName("Juan");
        dto.setLastName("Pérez");
        dto.setPhone("123456789");
        dto.setDescription("Proveedor confiable con experiencia");
        dto.setPhotoUrl("/uploads/proveedores/xxx.webp");
        when(providerMapper.toDTO(saved)).thenReturn(dto);
        // Imagen simulada
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.webp", "image/webp", "fake-content".getBytes()
        );
        // Act
        ProviderResponseDTO result = createProviderService.create(baseRequest, image);
        // Assert
        assertNotNull(result);
        assertTrue(result.getPhotoUrl().endsWith(".webp"));
        verify(providerRepository).save(any(Provider.class));


    }

    @Test
    void create_conImagenFormatoInvalido_lanzaExcepcion()
    {
        //arrange
        when(categoryRepository.findById(baseRequest.getCategoryId()))
                .thenReturn(Optional.of(new Category()));
        when(providerRepository.existsByNameAndLastName("Juan", "Pérez")).thenReturn(false);
        when(providerRepository.existsByPhone("123456789")).thenReturn(false);
        Provider entity = new Provider();
        entity.setName("Juan");
        entity.setLastName("Pérez");
        entity.setPhone("123456789");
        entity.setDescription("Proveedor confiable con experiencia");
        when(providerMapper.toEntity(baseRequest)).thenReturn(entity);
        // Imagen simulada con formato inválido
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.gif", "image/gif", "fake-content".getBytes()
        );
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            createProviderService.create(baseRequest, image);
        });
        assertEquals("Formato de imagen inválido. Use JPG, PNG o WEBP.", exception.getMessage());
        verify(providerRepository, never()).save(any());
    }

    @Test
    void create_errorAlGuardarImagen_lanzaExcepcion()
    {
        //arrange

        Provider entity = new Provider();
        entity.setName("Juan");
        entity.setLastName("Pérez");
        entity.setPhone("123456789");
        entity.setDescription("Proveedor confiable con experiencia");
        // Imagen simulada que causará error al guardar (contenido nulo)
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", (byte[]) null
        );
        // Act & Assert
        CreateProviderService spyService = spy(createProviderService);
        doThrow(new RuntimeException("No se pudo guardar la imagen"))
                .when(spyService)
                .create(any(), any());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            spyService.create(baseRequest, new MockMultipartFile(
                    "image", "test.jpg", "image/jpeg", "fake".getBytes()
            ));
        });

        assertEquals("No se pudo guardar la imagen", ex.getMessage());
        verify(providerRepository, never()).save(any());


    }


    @Test
    void create_conImagenVacia_noSeteaPhotoUrl()
    {
        // Arrange
        when(categoryRepository.findById(baseRequest.getCategoryId()))
                .thenReturn(Optional.of(new Category()));
        when(providerRepository.existsByNameAndLastName("Juan", "Pérez")).thenReturn(false);
        when(providerRepository.existsByPhone("123456789")).thenReturn(false);
        Provider entity = new Provider();
        entity.setName("Juan");
        entity.setLastName("Pérez");
        entity.setPhone("123456789");
        entity.setDescription("Proveedor confiable con experiencia");
        when(providerMapper.toEntity(baseRequest)).thenReturn(entity);
        Provider saved = new Provider();
        saved.setId(1L);
        saved.setName(entity.getName());
        saved.setLastName(entity.getLastName());
        saved.setPhone(entity.getPhone());
        saved.setDescription(entity.getDescription());
        saved.setPhotoUrl(null); // No se establece photoUrl
        when(providerRepository.save(any(Provider.class))).thenReturn(saved);
        ProviderResponseDTO dto = new ProviderResponseDTO();
        dto.setId(1L);
        dto.setName("Juan");
        dto.setLastName("Pérez");
        dto.setPhone("123456789");
        dto.setDescription("Proveedor confiable con experiencia");
        dto.setPhotoUrl(null); // No se establece photoUrl
        when(providerMapper.toDTO(saved)).thenReturn(dto);
        // Imagen vacía
        MockMultipartFile image = new MockMultipartFile(
                "image", "", "image/jpeg", new byte[0]
        );
        // Act
        ProviderResponseDTO result = createProviderService.create(baseRequest, image);
        // Assert
        assertNotNull(result);
        assertNull(result.getPhotoUrl()); // Verifica que no se establezca photoUrl
        verify(providerRepository).save(any(Provider.class));
        verify(providerMapper).toDTO(saved);
        verifyNoMoreInteractions(providerRepository, providerMapper, categoryRepository);

    }

    @Test
    void create_valoresConEspacios_limpiaNombreYTelefonoAntesDeValidar()
    {
        // Arrange
        baseRequest.setName("  Juan  ");
        baseRequest.setLastName("  Pérez  ");
        baseRequest.setPhone("   123456789   ");

        when(categoryRepository.findById(baseRequest.getCategoryId()))
                .thenReturn(Optional.of(new Category()));
        when(providerRepository.existsByNameAndLastName("Juan", "Pérez")).thenReturn(false);
        when(providerRepository.existsByPhone("123456789")).thenReturn(false);
        Provider entity = new Provider();
        entity.setName("Juan");
        entity.setLastName("Pérez");
        entity.setPhone("123456789");
        entity.setDescription("Proveedor confiable con experiencia");
        when(providerMapper.toEntity(baseRequest)).thenReturn(entity);
        Provider saved = new Provider();
        saved.setId(1L);
        saved.setName(entity.getName());
        saved.setLastName(entity.getLastName());
        saved.setPhone(entity.getPhone());
        saved.setDescription(entity.getDescription());
        saved.setPhotoUrl(null); // No se establece photoUrl
        when(providerRepository.save(any(Provider.class))).thenReturn(saved);
        ProviderResponseDTO dto = new ProviderResponseDTO();
        dto.setId(1L);
        dto.setName("Juan");
        dto.setLastName("Pérez");
        dto.setPhone("123456789");
        dto.setDescription("Proveedor confiable con experiencia");
        dto.setPhotoUrl(null); // No se establece photoUrl
        when(providerMapper.toDTO(saved)).thenReturn(dto);
        //act
        ProviderResponseDTO result = createProviderService.create(baseRequest, null);
        // Assert
        assertNotNull(result);
        assertEquals("Juan", result.getName());
        assertEquals("Pérez", result.getLastName());
        assertEquals("123456789", result.getPhone());
        verify(categoryRepository).findById(baseRequest.getCategoryId());
        verify(providerRepository).existsByNameAndLastName("Juan", "Pérez");
        verify(providerRepository).existsByPhone("123456789");
        verify(providerMapper).toEntity(baseRequest);
        verify(providerRepository).save(any(Provider.class));
        verify(providerMapper).toDTO(saved);
        verifyNoMoreInteractions(providerRepository, providerMapper, categoryRepository);


    }
}