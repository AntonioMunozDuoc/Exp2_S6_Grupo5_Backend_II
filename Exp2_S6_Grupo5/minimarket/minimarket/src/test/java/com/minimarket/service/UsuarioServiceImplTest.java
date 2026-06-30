package com.minimarket.service;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.security.service.SanitizerService;
import com.minimarket.service.impl.UsuarioServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SanitizerService sanitizerService;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void debeGuardarUsuarioConDatosObligatorios() {

        Usuario usuario = new Usuario();

        usuario.setUsername("francisca");

        usuario.setPassword("Password123");

        Rol rol = new Rol();

        rol.setNombre("ROLE_CLIENTE");

        when(
                sanitizerService
                        .sanitize(any())
        ).thenReturn("francisca");

        when(
                passwordEncoder
                        .encode(any())
        ).thenReturn("hash");

        when(
                rolRepository
                        .findByNombre(
                                "ROLE_CLIENTE"
                        )
        ).thenReturn(
                Optional.of(rol)
        );

        when(
                usuarioRepository
                        .save(any())
        ).thenAnswer(
                i -> i.getArgument(0)
        );

        Usuario resultado =
                usuarioService
                        .save(usuario);

        assertNotNull(resultado);

        assertEquals(
                "francisca",
                resultado.getUsername()
        );

        assertEquals(
                "hash",
                resultado.getPassword()
        );

        assertTrue(
                resultado
                        .getRoles()
                        .contains(rol)
        );

        ArgumentCaptor<Usuario> captor =
        ArgumentCaptor.forClass(
                Usuario.class
        );

        verify(
                usuarioRepository
        ).save(
                captor.capture()
        );

        Usuario usuarioGuardado =
                captor.getValue();

        assertEquals(
                "francisca",
                usuarioGuardado.getUsername()
        );

        assertEquals(
                "hash",
                usuarioGuardado.getPassword()
        );

        assertTrue(
                usuarioGuardado
                        .getRoles()
                        .contains(rol)
        );

        verify(sanitizerService).sanitize(
                "francisca");

        verify(passwordEncoder).encode(
                "Password123");

    }

    @Test
    void noDebeGuardarUsuarioSiRolNoExiste() {

        Usuario usuario =
                new Usuario();

        usuario.setUsername(
                "francisca"
        );

        usuario.setPassword(
                "Password123"
        );

        when(
                sanitizerService
                        .sanitize(any())
        ).thenReturn(
                "francisca"
        );

        when(
                passwordEncoder
                        .encode(any())
        ).thenReturn(
                "hash"
        );

        when(
                rolRepository
                        .findByNombre(
                                "ROLE_CLIENTE"
                        )
        ).thenReturn(
                Optional.empty()
        );

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () ->
                        usuarioService
                                .save(usuario));

        assertEquals("Rol no encontrado", ex.getMessage());

        verify(usuarioRepository,
                never()).save(any());

        verify(sanitizerService).sanitize(
        "francisca");

        verify(passwordEncoder).encode(
        "Password123");

    }

}
