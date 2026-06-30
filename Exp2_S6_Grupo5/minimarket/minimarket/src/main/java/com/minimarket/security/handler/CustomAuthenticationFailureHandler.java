package com.minimarket.security.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.minimarket.repository.UsuarioRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;

// Manejador personalizado para el fallo de autenticación, utilizado para incrementar los intentos fallidos
// Bloquea la cuenta si se alcanzan los intentos máximos permitidos
@Component
public class CustomAuthenticationFailureHandler
        extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    CustomAuthenticationFailureHandler.class
            );

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        String username = request.getParameter("username");

        usuarioRepository.findByUsername(username)
                .ifPresent(usuario -> {

                    int failedAttempts =
                            usuario.getFailedAttempt() + 1;

                    usuario.setFailedAttempt(failedAttempts);

                    if (failedAttempts >= 3) {

                        usuario.setAccountLocked(true);

                        logger.warn(
                                "Cuenta bloqueada por múltiples intentos fallidos: {}",
                                username
                        );
                    }

                    usuarioRepository.save(usuario);
                });

        logger.warn(
                "Intento login fallido para usuario: {}",
                username
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().write(
                "Credenciales inválidas"
        );
    }
}