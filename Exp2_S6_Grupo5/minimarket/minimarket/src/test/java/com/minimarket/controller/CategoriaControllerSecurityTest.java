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
class CategoriaControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    // Éxito: CLIENTE puede listar categorías
    @Test
    @WithMockUser(username = "cliente", roles = {"CLIENTE"})
    void clientePuedeListarCategorias() throws Exception {
        mockMvc.perform(get("/api/categorias"))
               .andExpect(status().isOk());
    }

    // Éxito: EMPLEADO puede listar categorías
    @Test
    @WithMockUser(username = "empleado", roles = {"EMPLEADO"})
    void empleadoPuedeListarCategorias() throws Exception {
        mockMvc.perform(get("/api/categorias"))
               .andExpect(status().isOk());
    }

    // Éxito: GERENTE puede listar categorías
    @Test
    @WithMockUser(username = "gerente", roles = {"GERENTE"})
    void gerentePuedeListarCategorias() throws Exception {
        mockMvc.perform(get("/api/categorias"))
               .andExpect(status().isOk());
    }

    // Éxito: ADMIN puede listar categorías
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminPuedeListarCategorias() throws Exception {
        mockMvc.perform(get("/api/categorias"))
               .andExpect(status().isOk());
    }

    // Error: sin autenticación no puede listar categorías
    @Test
    void sinAutenticacionNoPuedeListarCategorias() throws Exception {
        mockMvc.perform(get("/api/categorias"))
               .andExpect(status().isUnauthorized());
    }

    // Error: CLIENTE no puede eliminar categorías
    @Test
    @WithMockUser(username = "cliente", roles = {"CLIENTE"})
    void clienteNoPuedeEliminarCategoria() throws Exception {
        mockMvc.perform(delete("/api/categorias/1"))
               .andExpect(status().isForbidden());
    }

    // Error: EMPLEADO no puede eliminar categorías
    @Test
    @WithMockUser(username = "empleado", roles = {"EMPLEADO"})
    void empleadoNoPuedeEliminarCategoria() throws Exception {
        mockMvc.perform(delete("/api/categorias/1"))
               .andExpect(status().isForbidden());
    }

    // Éxito: GERENTE puede eliminar (404 porque no existe en BD de test)
    @Test
    @WithMockUser(username = "gerente", roles = {"GERENTE"})
    void gerentePuedeEliminarCategoria() throws Exception {
        mockMvc.perform(delete("/api/categorias/1"))
               .andExpect(status().isNotFound());
    }

    // Éxito: ADMIN puede eliminar (404 porque no existe en BD de test)
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminPuedeEliminarCategoria() throws Exception {
        mockMvc.perform(delete("/api/categorias/1"))
               .andExpect(status().isNotFound());
    }
}