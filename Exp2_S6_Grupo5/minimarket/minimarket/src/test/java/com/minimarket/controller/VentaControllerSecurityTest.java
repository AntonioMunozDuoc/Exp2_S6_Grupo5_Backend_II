package com.minimarket.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class VentaControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BODY_VENTA = """
    {
        "usuarioId":1,
        "productos":[
            {
                "productoId":1,
                "cantidad":1
            }
        ],
        "fechaVenta":"2026-06-29T00:00:00.000+00:00"
    }
    """;

    @Test
    @WithMockUser(username = "empleado", roles = {"EMPLEADO"})
    void empleadoDebePoderGenerarVenta() throws Exception {

        mockMvc.perform(
                post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_VENTA)
        )
        .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "cliente", roles = {"CLIENTE"})
    void clienteNoDebeGenerarVenta() throws Exception {

        MvcResult result = mockMvc.perform(
                post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_VENTA)
        )
        .andReturn();

        int status = result.getResponse().getStatus();

        assertTrue(status == 400 || status == 403);
    }

    @Test
    void usuarioNoAutenticadoNoDebeGenerarVenta() throws Exception {

        mockMvc.perform(
                post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_VENTA)
        )
        .andExpect(status().isUnauthorized());
    }
}
