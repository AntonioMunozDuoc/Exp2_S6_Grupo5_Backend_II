package com.minimarket.security.model;

// DTO para la respuesta de login, contiene el token de acceso y opcionalmente el token de refresh si se implementa esa funcionalidad
public class LoginResponse {

    private String token;
    private String refreshToken;

    public LoginResponse(String token) {
        this.token = token;
    }

    public LoginResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
