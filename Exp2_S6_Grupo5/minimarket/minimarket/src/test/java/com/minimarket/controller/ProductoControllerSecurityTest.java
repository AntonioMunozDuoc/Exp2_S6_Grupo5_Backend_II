package com.minimarket.controller;

import com.minimarket.security.service.monitor.SuspiciousActivityService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BODY = """
    {
        "nombre":"Producto actualizado",
        "precio":1500,
        "stock":20,
        "categoriaId":1
    }
    """;

    @Test
    @WithMockUser(
            username = "admin",
            roles = {
                    "ADMIN"
            }
    )
    void adminDebePoderActualizarProducto()
            throws Exception {

        mockMvc.perform(
                put("/api/productos/1")
                        .contentType(
                                MediaType.APPLICATION_JSON
                        )
                        .content(BODY)
        )
        .andExpect(
                status()
                        .isNotFound()
        );

    }

    @Test
    @WithMockUser(
            username = "gerente",
            roles = {
                    "GERENTE"
            }
    )
    void gerenteDebePoderActualizarProducto()
            throws Exception {

        mockMvc.perform(
                put("/api/productos/1")
                        .contentType(
                                MediaType.APPLICATION_JSON
                        )
                        .content(BODY)
        )
        .andExpect(
                status()
                        .isNotFound()
        );

    }

    @Test
    @WithMockUser(
            username = "empleado",
            roles = {
                    "EMPLEADO"
            }
    )
    void empleadoDebePoderActualizarProducto()
            throws Exception {

        mockMvc.perform(
                put("/api/productos/1")
                        .contentType(
                                MediaType.APPLICATION_JSON
                        )
                        .content(BODY)
        )
        .andExpect(
                status()
                        .isNotFound()
        );

    }

    @Test
    @WithMockUser(
            username = "cliente",
            roles = {
                    "CLIENTE"
            }
    )
    void clienteNoDebePoderActualizarProducto()
            throws Exception {

        mockMvc.perform(
                put("/api/productos/1")
                        .contentType(
                                MediaType.APPLICATION_JSON
                        )
                        .content(BODY)
        )
        .andExpect(
                status()
                        .isForbidden()
        );

    }

    @Test
    void usuarioNoAutenticadoNoDebeActualizarProducto()
            throws Exception {

        mockMvc.perform(
                put("/api/productos/1")
                        .contentType(
                                MediaType.APPLICATION_JSON
                        )
                        .content(BODY)
        )
        .andExpect(
                status()
                        .isUnauthorized()
        );

    }

}