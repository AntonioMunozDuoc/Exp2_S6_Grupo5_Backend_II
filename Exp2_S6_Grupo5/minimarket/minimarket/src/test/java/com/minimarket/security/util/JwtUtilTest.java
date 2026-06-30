package com.minimarket.security.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    // Clave Base64 válida de 256 bits para tests
    private static final String SECRET =
            "dGVzdFNlY3JldEtleVBhcmFQcnVlYmFzVW5pdGFyaWFzMTIzNDU2Nzg5MA==";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expiration", 3600000L);
        ReflectionTestUtils.setField(jwtUtil, "refreshExpiration", 86400000L);
    }

    private UserDetails buildUser(String username) {
        return new User(
                username,
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_CLIENTE"))
        );
    }

    // Éxito: genera token y extrae el username correctamente
    @Test
    void debeGenerarTokenYExtraerUsername() {

        UserDetails user = buildUser("antonio");
        String token = jwtUtil.generateToken(user);

        assertNotNull(token);
        assertEquals("antonio", jwtUtil.extractUsername(token));
    }

    // Éxito: token recién generado no está expirado
    @Test
    void tokenRecienGeneradoNoDebeEstarExpirado() {

        UserDetails user = buildUser("antonio");
        String token = jwtUtil.generateToken(user);

        assertFalse(jwtUtil.isTokenExpired(token));
    }

    // Éxito: validateToken retorna true con usuario correcto
    @Test
    void debeValidarTokenCorrectamente() {

        UserDetails user = buildUser("antonio");
        String token = jwtUtil.generateToken(user);

        assertTrue(jwtUtil.validateToken(token, user));
    }

    // Error: validateToken retorna false con usuario distinto
    @Test
    void debeRechazarTokenDeOtroUsuario() {

        UserDetails user1 = buildUser("antonio");
        UserDetails user2 = buildUser("pedro");

        String token = jwtUtil.generateToken(user1);

        assertFalse(jwtUtil.validateToken(token, user2));
    }

    // Éxito: genera refresh token y lo valida correctamente
    @Test
    void debeGenerarYValidarRefreshToken() {

        UserDetails user = buildUser("antonio");
        String refreshToken = jwtUtil.generateRefreshToken(user);

        assertNotNull(refreshToken);
        assertTrue(jwtUtil.validateRefreshToken(refreshToken, user));
    }

    // Error: access token no es válido como refresh token
    @Test
    void accessTokenNoDebeSerValidoComoRefreshToken() {

        UserDetails user = buildUser("antonio");
        String accessToken = jwtUtil.generateToken(user);

        assertFalse(jwtUtil.validateRefreshToken(accessToken, user));
    }

    // Error: refresh token no es válido como access token
    @Test
    void refreshTokenNoDebeSerValidoComoAccessToken() {

        UserDetails user = buildUser("antonio");
        String refreshToken = jwtUtil.generateRefreshToken(user);

        assertFalse(jwtUtil.validateToken(refreshToken, user));
    }

    // Éxito: extrae tipo de token ACCESS correctamente
    @Test
    void debeExtraerTipoDeTokenAccess() {

        UserDetails user = buildUser("antonio");
        String token = jwtUtil.generateToken(user);

        assertEquals("ACCESS", jwtUtil.extractTokenType(token));
    }

    // Éxito: extrae tipo de token REFRESH correctamente
    @Test
    void debeExtraerTipoDeTokenRefresh() {

        UserDetails user = buildUser("antonio");
        String refreshToken = jwtUtil.generateRefreshToken(user);

        assertEquals("REFRESH", jwtUtil.extractTokenType(refreshToken));
    }
}