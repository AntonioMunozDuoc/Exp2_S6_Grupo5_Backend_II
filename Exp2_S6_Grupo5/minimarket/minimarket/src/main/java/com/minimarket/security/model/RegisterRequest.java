package com.minimarket.security.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    /**
     * Username: entre 4 y 50 caracteres alfanuméricos (sin espacios ni caracteres especiales).
     * Se normaliza con trim() antes de usarlo en AuthController.
     */
    @NotBlank(message = "El username es obligatorio")
    @Size(min = 4, max = 50, message = "El username debe tener entre 4 y 50 caracteres")
    @Pattern(
        regexp = "^[a-zA-Z0-9_.-]+$",
        message = "El username solo puede contener letras, números, puntos, guiones y guiones bajos"
    )
    private String username;

    /**
     * Contraseña: mínimo 8 caracteres, máximo 100.
     * Debe incluir al menos una mayúscula, una minúscula y un número.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
        message = "La contraseña debe contener al menos una mayúscula, una minúscula y un número"
    )
    private String password;

    /**
     * Rol opcional. Si se omite, se asigna ROLE_CLIENTE por defecto en AuthController.
     * Solo se aceptan valores alfanuméricos simples (sin prefijo ROLE_).
     */
    @Size(max = 30, message = "El nombre del rol no puede exceder 30 caracteres")
    @Pattern(
        regexp = "^[a-zA-Z_]*$",
        message = "El rol solo puede contener letras y guiones bajos"
    )
    private String rol;

    public String getUsername() { return username != null ? username.trim() : null; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol != null ? rol.trim() : null; }
    public void setRol(String rol) { this.rol = rol; }
}
