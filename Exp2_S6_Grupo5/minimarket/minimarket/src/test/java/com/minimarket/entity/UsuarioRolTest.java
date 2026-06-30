package com.minimarket.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

class UsuarioRolTest {

    // Éxito: usuario recién creado no está bloqueado
    @Test
    void usuarioNuevoNoDebeEstarBloqueado() {

        Usuario usuario = new Usuario();

        assertFalse(usuario.isAccountLocked());
        assertEquals(0, usuario.getFailedAttempt());
    }

    // Éxito: se puede asignar rol ADMIN al usuario
    @Test
    void debeAsignarRolAdmin() {

        Rol rol = new Rol();
        rol.setNombre("ROLE_ADMIN");

        Usuario usuario = new Usuario();
        usuario.setRoles(Set.of(rol));

        assertTrue(
            usuario.getRoles()
                   .stream()
                   .anyMatch(r -> r.getNombre().equals("ROLE_ADMIN"))
        );
    }

    // Éxito: se puede asignar rol GERENTE al usuario
    @Test
    void debeAsignarRolGerente() {

        Rol rol = new Rol();
        rol.setNombre("ROLE_GERENTE");

        Usuario usuario = new Usuario();
        usuario.setRoles(Set.of(rol));

        assertTrue(
            usuario.getRoles()
                   .stream()
                   .anyMatch(r -> r.getNombre().equals("ROLE_GERENTE"))
        );
    }

    // Éxito: se puede asignar rol CLIENTE al usuario
    @Test
    void debeAsignarRolCliente() {

        Rol rol = new Rol();
        rol.setNombre("ROLE_CLIENTE");

        Usuario usuario = new Usuario();
        usuario.setRoles(Set.of(rol));

        assertTrue(
            usuario.getRoles()
                   .stream()
                   .anyMatch(r -> r.getNombre().equals("ROLE_CLIENTE"))
        );
    }

    // Éxito: usuario con múltiples roles los conserva todos
    @Test
    void debeConservarMultiplesRoles() {

        Rol rolAdmin = new Rol();
        rolAdmin.setNombre("ROLE_ADMIN");

        Rol rolGerente = new Rol();
        rolGerente.setNombre("ROLE_GERENTE");

        Usuario usuario = new Usuario();
        usuario.setRoles(Set.of(rolAdmin, rolGerente));

        assertEquals(2, usuario.getRoles().size());
    }

    // Éxito: cuenta se bloquea correctamente al setear accountLocked
    @Test
    void debeBloquearseCuandoSeEstableceLocked() {

        Usuario usuario = new Usuario();
        usuario.setAccountLocked(true);

        assertTrue(usuario.isAccountLocked());
    }

    // Éxito: contador de intentos fallidos aumenta correctamente
    @Test
    void debeIncrementarIntentosFailidos() {

        Usuario usuario = new Usuario();
        usuario.setFailedAttempt(1);
        assertEquals(1, usuario.getFailedAttempt());

        usuario.setFailedAttempt(2);
        assertEquals(2, usuario.getFailedAttempt());

        usuario.setFailedAttempt(3);
        assertEquals(3, usuario.getFailedAttempt());
    }

    // Error: usuario bloqueado con 3 intentos fallidos
    @Test
    void usuarioConTresIntentosFallidosDebeEstarBloqueado() {

        Usuario usuario = new Usuario();
        usuario.setFailedAttempt(3);
        usuario.setAccountLocked(true);

        assertTrue(usuario.isAccountLocked());
        assertEquals(3, usuario.getFailedAttempt());
    }

    // Éxito: se puede desbloquear un usuario bloqueado
    @Test
    void debeDesbloquearseCuandoSeRestablece() {

        Usuario usuario = new Usuario();
        usuario.setAccountLocked(true);
        usuario.setFailedAttempt(3);

        // Restablecimiento
        usuario.setAccountLocked(false);
        usuario.setFailedAttempt(0);

        assertFalse(usuario.isAccountLocked());
        assertEquals(0, usuario.getFailedAttempt());
    }

    // Error: usuario sin roles tiene set vacío o nulo
    @Test
    void usuarioSinRolesNoDebeAccederARolEspecifico() {

        Usuario usuario = new Usuario();
        usuario.setRoles(Set.of());

        assertFalse(
            usuario.getRoles()
                   .stream()
                   .anyMatch(r -> r.getNombre().equals("ROLE_ADMIN"))
        );
    }
}