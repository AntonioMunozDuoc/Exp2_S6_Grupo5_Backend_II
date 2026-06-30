package com.minimarket.security.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.minimarket.repository.UsuarioRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Manejador personalizado para el éxito de autenticación, utilizado para resetear los intentos fallidos y desbloquear la cuenta si es necesario
@Component
public class CustomAuthenticationSuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    CustomAuthenticationSuccessHandler.class
            );

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        String username = authentication.getName();

        usuarioRepository.findByUsername(username)
                .ifPresent(usuario -> {

                    usuario.setFailedAttempt(0);
                    usuario.setAccountLocked(false);

                    usuarioRepository.save(usuario);

                    logger.info(
                            "Login exitoso para usuario: {}",
                            username
                    );
                });

        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().write(
                "Login exitoso"
        );
    }
}