package com.example.backend.comments.service.implementation;

import com.example.backend.comments.controller.dto.CommentRequestDTO;
import com.example.backend.comments.controller.dto.CommentResponseDTO;
import com.example.backend.comments.domain.Comment;
import com.example.backend.comments.infrastructure.CommentRepository;
import com.example.backend.comments.service.mapper.CommentMapper;
import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCommentServiceTest {

    @Mock
    private  CommentRepository commentRepository;
    @Mock
    private  ProviderRepository providerRepository;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  CommentMapper commentMapper;
    @InjectMocks
    private CreateCommentService createCommentService;


    private String email;
    private User user;
    private Provider provider;
    private CommentRequestDTO request;
    private Comment comment;
    private Comment savedComment;
    private CommentResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // Email del usuario autenticado
        String email = "user@test.com";

        // User de prueba
        user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setUsername("Test User");

        // Provider de prueba
        provider = new Provider();
        provider.setId(10L);
        provider.setName("Test Provider");

        // Request de comentario
        request = new CommentRequestDTO();
        request.setProviderId(provider.getId());
        request.setContent("Muy buen servicio!");

        // Comment antes de guardar
        comment = new Comment();
        comment.setContent(request.getContent());
        comment.setUser(user);
        comment.setProvider(provider);

        // Comment guardado (simulando ID generado)
        savedComment = new Comment();
        savedComment.setId(100L);
        savedComment.setContent(request.getContent());
        savedComment.setUser(user);
        savedComment.setProvider(provider);

        // Response DTO esperado
        responseDTO = new CommentResponseDTO();
        responseDTO.setId(savedComment.getId());
        responseDTO.setContent(savedComment.getContent());
        responseDTO.setUsername(user.getName());

        //  Mockear el contexto de seguridad
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    void createComment_usuarioYProveedorExisten_guardaYDevuelveResponseDTO() {
        // Stub 1: cuando el servicio pida el usuario por email (cualquier String), devolvé Optional.of(user)
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Stub 2: cuando pida el proveedor por id (cualquier long), devolvé Optional.of(provider)
        when(providerRepository.findById(anyLong())).thenReturn(Optional.of(provider));

        // Stub 3: cuando el mapper arme la entidad desde (request, user, provider), devolvé la entidad comment
        when(commentMapper.toEntity(any(CommentRequestDTO.class), any(User.class), any(Provider.class))).thenReturn(comment);

        // Stub 4: cuando el repo guarde el comment, simulá que devuelve savedComment (con ID)
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        // Stub 5: cuando el mapper convierta la entidad guardada a DTO, devolvé responseDTO
        when(commentMapper.toResponseDTO(any(Comment.class))).thenReturn(responseDTO);

        // Act: ejecutá el método bajo prueba con el request preparado en @BeforeEach
        CommentResponseDTO result = createCommentService.createComment(request);

        // Assert 1: el resultado no debe ser null
        assertNotNull(result);

        // Assert 2: el ID del DTO debe ser el ID del comment guardado (simulado)
        assertEquals(savedComment.getId(), result.getId());

        // Assert 3: el contenido del DTO debe ser el contenido del comment guardado
        assertEquals(savedComment.getContent(), result.getContent());

        // Assert 4: el username del DTO debe coincidir con el nombre del usuario de prueba
        assertEquals(user.getName(), result.getUsername());
    }


    @Test
    void createComment_usuarioNoEncontrado_lanzaExcepcion() {
        // Stub 1: cuando el servicio pida el usuario por email (cualquier String), devolvé Optional.empty()
        // Esto simula que en la base de datos no existe un usuario con ese email.
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert: al ejecutar el método, debe lanzar RuntimeException con mensaje "Usuario no encontrado"
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            // Act: llamás al servicio con el request preparado en setUp().
            createCommentService.createComment(request);
        });

        // Assert: verificás que el mensaje de la excepción sea exactamente "Usuario no encontrado".
        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void createComment_proveedorNoEncontrado_lanzaExcepcion() {
        // Stub 1: simulás que el usuario sí existe en la base.
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Stub 2: simulás que el proveedor NO existe (se devuelve Optional.empty()).
        when(providerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert: al ejecutar createComment con el request, debe lanzar una RuntimeException.
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            createCommentService.createComment(request);
        });

        // Assert: verificás que el mensaje de la excepción sea "Prestador no encontrado".
        assertEquals("Prestador no encontrado", exception.getMessage());
    }

    @Test
    void createComment_llamaRepositoriosYMapperCorrectamente() {
        // 🟢 Stub 1: simulás que cuando se busque un usuario por email, devuelva el usuario de prueba.
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // 🟢 Stub 2: simulás que cuando se busque un proveedor por ID, devuelva el proveedor de prueba.
        when(providerRepository.findById(anyLong())).thenReturn(Optional.of(provider));

        // 🟢 Stub 3: simulás que cuando el mapper arme la entidad Comment desde el request + user + provider,
        // devuelva la entidad comment preparada en setUp().
        when(commentMapper.toEntity(any(CommentRequestDTO.class), any(User.class), any(Provider.class))).thenReturn(comment);

        // 🟢 Stub 4: simulás que cuando se guarde el comment en el repositorio, devuelva la versión con ID (savedComment).
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        // 🟢 Stub 5: simulás que cuando el mapper convierta la entidad guardada a DTO, devuelva el responseDTO esperado.
        when(commentMapper.toResponseDTO(any(Comment.class))).thenReturn(responseDTO);

        // 🚀 Act: ejecutás el método que querés probar con el request de prueba.
        createCommentService.createComment(request);

        // 🔎 Assert 1: verificás que el servicio consultó al repositorio de usuarios una sola vez.
        verify(userRepository, times(1)).findByEmail(anyString());

        // 🔎 Assert 2: verificás que el servicio consultó al repositorio de proveedores una sola vez.
        verify(providerRepository, times(1)).findById(anyLong());

        // 🔎 Assert 3: verificás que el mapper fue llamado para crear la entidad con los objetos correctos.
        verify(commentMapper, times(1)).toEntity(request, user, provider);

        // 🔎 Assert 4: verificás que el repositorio guardó el comentario una sola vez.
        verify(commentRepository, times(1)).save(comment);

        // 🔎 Assert 5: verificás que el mapper transformó el comentario guardado en un DTO de respuesta.
        verify(commentMapper, times(1)).toResponseDTO(savedComment);
    }


}