package com.minimarket.controller;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class InventarioControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BODY_ENTRADA = """
    {
        "productoId":1,
        "cantidad":5,
        "tipoMovimiento":"Entrada",
        "fechaMovimiento":"2026-06-29T00:00:00.000+00:00"
    }
    """;

    private static final String BODY_SALIDA = """
    {
        "productoId":1,
        "cantidad":3,
        "tipoMovimiento":"Salida",
        "fechaMovimiento":"2026-06-29T00:00:00.000+00:00"
    }
    """;

    /**
     * Éxito:
     * Un empleado posee permisos para registrar un movimiento de entrada.
     * La prueba verifica que el acceso sea autorizado, independiente del
     * resultado de la lógica de negocio.
     */
    @Test
    @WithMockUser(
            username = "empleado",
            roles = {"EMPLEADO"}
    )
    void empleadoDebePoderRegistrarMovimientoEntrada() throws Exception {

        MvcResult result = mockMvc.perform(
                post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_ENTRADA)
        ).andReturn();

        int status = result.getResponse().getStatus();

        assertNotEquals(401, status);
        assertNotEquals(403, status);
    }

    /**
     * Éxito:
     * Un empleado también posee permisos para registrar un movimiento de salida.
     * La prueba valida únicamente la autorización del usuario.
     */
    @Test
    @WithMockUser(
            username = "empleado",
            roles = {"EMPLEADO"}
    )
    void empleadoDebePoderRegistrarMovimientoSalida() throws Exception {

        MvcResult result = mockMvc.perform(
                post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_SALIDA)
        ).andReturn();

        int status = result.getResponse().getStatus();

        assertNotEquals(401, status);
        assertNotEquals(403, status);
    }
    
    /**
     * Error:
     * Un cliente autenticado no posee permisos.
     */
    @Test
    @WithMockUser(
            username = "cliente",
            roles = {"CLIENTE"}
    )
    void clienteNoDebeRegistrarMovimiento() throws Exception {

        mockMvc.perform(
                post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_ENTRADA)
        )
        .andExpect(status().isForbidden());

    }

    /**
     * Error:
     * Un usuario sin autenticación recibe 401.
     */
    @Test
    void usuarioNoAutenticadoNoDebeRegistrarMovimiento()
            throws Exception {

        mockMvc.perform(
                post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_ENTRADA)
        )
        .andExpect(status().isUnauthorized());

    }

}
