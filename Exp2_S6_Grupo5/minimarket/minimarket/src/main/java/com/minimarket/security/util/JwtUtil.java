package com.minimarket.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

// Clase utilitaria para manejo de JWT
@Component
public class JwtUtil {

    // Claves de configuración inyectadas desde application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    // Duración de los tokens en milisegundos
    @Value("${jwt.expiration}")
    private long expiration;

    // Duración de los refresh tokens en milisegundos
    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration;

    // Validación de configuración al iniciar la aplicación
    @PostConstruct
    public void init() {

        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException(
                    "jwt.secret no está configurado"
            );
        }
    }

    // Genera un token de acceso para el usuario
    public String generateToken(UserDetails userDetails) {

        return generateToken(
                userDetails.getUsername(),
                expiration,
                "ACCESS"
        );
    }

    // Genera un token de refresh para el usuario
    public String generateRefreshToken(UserDetails userDetails) {

        return generateToken(
                userDetails.getUsername(),
                refreshExpiration,
                "REFRESH"
        );
    }

    // Método privado para generar un token con los parámetros especificados
    private String generateToken(
            String subject,
            long duration,
            String tokenType) {

        return Jwts.builder()
                .subject(subject)
                .claim("tokenType", tokenType)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + duration)
                )
                .signWith(getSigningKey())
                .compact();
    }

    // Extrae el username del token
    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    // Extrae la fecha de expiración del token
    public Date extractExpiration(String token) {

        return extractAllClaims(token)
                .getExpiration();
    }

    // Verifica si el token ha expirado
    public boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());
    }

    // Valida el token de acceso comparando el username y el tipo de token, además de verificar que no haya expirado
    public boolean validateToken(
            String token,
            UserDetails userDetails) {

        String username = extractUsername(token);
        String tokenType = extractTokenType(token);

        return username.equals(userDetails.getUsername())
                && "ACCESS".equals(tokenType)
                && !isTokenExpired(token);
    }

    // Valida el token de refresh comparando el username y el tipo de token, además de verificar que no haya expirado
    public boolean validateRefreshToken(
            String token,
            UserDetails userDetails) {

        String username = extractUsername(token);
        String tokenType = extractTokenType(token);

        return username.equals(userDetails.getUsername())
                && "REFRESH".equals(tokenType)
                && !isTokenExpired(token);
    }

    // Extrae el tipo de token (ACCESS o REFRESH) del token JWT
    public String extractTokenType(String token) {

        Object claim =
                extractAllClaims(token).get("tokenType");

        return claim != null
                ? claim.toString()
                : null;
    }

    /**
     * Método privado para extraer todos los claims del token
     * Manejando el caso de token expirado para permitir la extracción de claims incluso si el token ya no es válido
     */
    private Claims extractAllClaims(String token) {

        try {

            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (ExpiredJwtException e) {

            return e.getClaims();
        }
    }

    // Método privado para obtener la clave de firma a partir de la clave secreta configurada, decodificada desde Base64
    private SecretKey getSigningKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}