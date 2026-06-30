package com.minimarket.security.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// DTO para la solicitud de login, contiene el username y la contraseña enviados por el cliente
public class LoginRequest {

    @NotBlank(message = "El username es obligatorio")
    @Size(max = 50, message = "Username demasiado largo")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 100, message = "Contraseña demasiado larga")
    private String password;

    public String getUsername() { return username != null ? username.trim() : null; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
