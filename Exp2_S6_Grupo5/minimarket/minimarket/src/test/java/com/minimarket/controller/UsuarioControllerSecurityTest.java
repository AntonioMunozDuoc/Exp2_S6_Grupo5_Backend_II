package com.minimarket.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    // Éxito: GERENTE puede listar usuarios
    @Test
    @WithMockUser(username = "gerente", authorities = {"ROLE_GERENTE"})
    void gerentePuedeListarUsuarios() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
               .andExpect(status().isOk());
    }

    // Éxito: ADMIN puede listar usuarios
    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void adminPuedeListarUsuarios() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
               .andExpect(status().isOk());
    }

    // Error: CLIENTE no puede listar usuarios
    @Test
    @WithMockUser(username = "cliente", authorities = {"ROLE_CLIENTE"})
    void clienteNoPuedeListarUsuarios() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
               .andExpect(status().isForbidden());
    }

    // Error: EMPLEADO no puede listar usuarios
    @Test
    @WithMockUser(username = "empleado", authorities = {"ROLE_EMPLEADO"})
    void empleadoNoPuedeListarUsuarios() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
               .andExpect(status().isForbidden());
    }

    // Error: sin autenticación no puede listar usuarios
    @Test
    void sinAutenticacionNoPuedeListarUsuarios() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
               .andExpect(status().isUnauthorized());
    }

    // Error: CLIENTE no puede eliminar usuario
    @Test
    @WithMockUser(username = "cliente", authorities = {"ROLE_CLIENTE"})
    void clienteNoPuedeEliminarUsuario() throws Exception {
        mockMvc.perform(delete("/api/usuarios/1"))
               .andExpect(status().isForbidden());
    }

    // GERENTE intenta eliminar usuario con id inexistente -> 404
    @Test
    @WithMockUser(username = "gerente", authorities = {"ROLE_GERENTE"})
    void gerentePuedeEliminarUsuario() throws Exception {
        mockMvc.perform(delete("/api/usuarios/99999"))
               .andExpect(status().isNotFound());
    }

    // ADMIN intenta eliminar usuario con id inexistente -> 404
    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void adminPuedeEliminarUsuario() throws Exception {
        mockMvc.perform(delete("/api/usuarios/99999"))
               .andExpect(status().isNotFound());
    }
}