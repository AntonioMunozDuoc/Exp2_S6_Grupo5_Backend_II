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
class CarritoControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    // Éxito: CLIENTE puede ver un ítem del carrito por id
    @Test
    @WithMockUser(username = "cliente", roles = {"CLIENTE"})
    void clientePuedeVerCarritoPorId() throws Exception {
        mockMvc.perform(get("/api/carrito/1"))
               .andExpect(status().isNotFound()); // 404 porque no existe en BD de test
    }

    // Éxito: EMPLEADO puede listar todo el carrito
    @Test
    @WithMockUser(username = "empleado", roles = {"EMPLEADO"})
    void empleadoPuedeListarCarrito() throws Exception {
        mockMvc.perform(get("/api/carrito"))
               .andExpect(status().isOk());
    }

    // Éxito: GERENTE puede listar todo el carrito
    @Test
    @WithMockUser(username = "gerente", roles = {"GERENTE"})
    void gerentePuedeListarCarrito() throws Exception {
        mockMvc.perform(get("/api/carrito"))
               .andExpect(status().isOk());
    }

    // Éxito: ADMIN puede listar todo el carrito
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminPuedeListarCarrito() throws Exception {
        mockMvc.perform(get("/api/carrito"))
               .andExpect(status().isOk());
    }

    // Error: CLIENTE no puede listar todo el carrito (solo EMPLEADO, GERENTE, ADMIN)
    @Test
    @WithMockUser(username = "cliente", roles = {"CLIENTE"})
    void clienteNoPuedeListarTodoElCarrito() throws Exception {
        mockMvc.perform(get("/api/carrito"))
               .andExpect(status().isForbidden());
    }

    // Error: sin autenticación no puede acceder al carrito
    @Test
    void sinAutenticacionNoPuedeAccederAlCarrito() throws Exception {
        mockMvc.perform(get("/api/carrito"))
               .andExpect(status().isUnauthorized());
    }

    // Éxito: CLIENTE puede eliminar su propio ítem del carrito
    @Test
    @WithMockUser(username = "cliente", roles = {"CLIENTE"})
    void clientePuedeEliminarItemDelCarrito() throws Exception {
        mockMvc.perform(delete("/api/carrito/1"))
               .andExpect(status().isNotFound()); // 404 porque no existe en BD de test
    }

    // Error: sin autenticación no puede eliminar del carrito
    @Test
    void sinAutenticacionNoPuedeEliminarDelCarrito() throws Exception {
        mockMvc.perform(delete("/api/carrito/1"))
               .andExpect(status().isUnauthorized());
    }
}