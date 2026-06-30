package com.minimarket.security.model;

import jakarta.validation.constraints.NotBlank;

// DTO para la solicitud de refresh token, contiene el token de refresh enviado por el cliente  
public class RefreshTokenRequest {

    // El token de refresh es obligatorio para esta operación
    @NotBlank(message = "El refresh token es obligatorio")
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
