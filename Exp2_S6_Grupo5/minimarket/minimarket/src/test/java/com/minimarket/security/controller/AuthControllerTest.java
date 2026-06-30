package com.minimarket.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.security.model.LoginRequest;
import com.minimarket.security.model.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Éxito: registro de usuario con rol CLIENTE por defecto
    @Test
    void debeRegistrarUsuarioConRolClientePorDefecto() throws Exception {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser_" + System.currentTimeMillis());
        request.setPassword("Password123!");
        request.setRol(null);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.mensaje").value(
                       "Usuario registrado correctamente con rol: ROLE_CLIENTE"
               ));
    }

    // Éxito: registro de usuario con rol EMPLEADO explícito
    @Test
    void debeRegistrarUsuarioConRolEmpleado() throws Exception {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("empleado_" + System.currentTimeMillis());
        request.setPassword("Password123!");
        request.setRol("EMPLEADO");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.mensaje").value(
                       "Usuario registrado correctamente con rol: ROLE_EMPLEADO"
               ));
    }

    // Error: registro con username duplicado retorna 409
    @Test
    void debeRetornar409SiUsernameYaExiste() throws Exception {

        String username = "duplicado_" + System.currentTimeMillis();

        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setPassword("Password123!");
        request.setRol("CLIENTE");

        // Primer registro
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated());

        // Segundo registro con el mismo username
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isConflict())
               .andExpect(jsonPath("$.error").value("El username ya está en uso"));
    }

    // Error: registro con rol no permitido retorna 400
    @Test
    void debeRetornar400SiRolNoEstaPermitido() throws Exception {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("user_" + System.currentTimeMillis());
        request.setPassword("Password123!");
        request.setRol("SUPERUSUARIO");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.error").value("Rol no permitido"));
    }

    // Error: login con credenciales incorrectas retorna 401
    @Test
    void debeRetornar401ConCredencialesIncorrectas() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setUsername("usuarioInexistente");
        request.setPassword("claveErronea");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isUnauthorized());
    }

    // Éxito: login con credenciales válidas retorna token
    @Test
    void debeRetornarTokenAlLoginCorrecto() throws Exception {

        // Primero registramos el usuario
        String username = "logintest_" + System.currentTimeMillis();

        RegisterRequest register = new RegisterRequest();
        register.setUsername(username);
        register.setPassword("Password123!");
        register.setRol("CLIENTE");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
               .andExpect(status().isCreated());

        // Luego hacemos login
        LoginRequest login = new LoginRequest();
        login.setUsername(username);
        login.setPassword("Password123!");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.token").exists())
               .andExpect(jsonPath("$.refreshToken").exists());
    }
}