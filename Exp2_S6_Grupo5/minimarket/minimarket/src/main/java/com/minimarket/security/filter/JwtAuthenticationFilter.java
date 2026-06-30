package com.minimarket.security.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.security.service.monitor.SuspiciousActivityService;
import com.minimarket.security.util.JwtUtil;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Filtro de autenticación JWT que se ejecuta una vez por cada solicitud, encargado de validar el token JWT
// Establece la autenticación en el contexto de seguridad si el token es válido
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private SuspiciousActivityService suspiciousActivityService;

    private static final Logger logger =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    // El método principal del filtro que intercepta cada solicitud, extrae el token JWT del header Authorization, lo valida y establece la autenticación en el contexto de seguridad si es válido
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        
        String  ip = request.getRemoteAddr();
                suspiciousActivityService
                .registerRequest(ip);

        String authHeader = request.getHeader("Authorization");

        // Sin header Bearer: continuar sin autenticar (rutas públicas pasarán igual)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            String username = jwtUtil.extractUsername(token);

            // Solo intentar autenticar si no hay una autenticación ya establecida en el contexto de seguridad
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(token, userDetails)) {

                    setAuthentication(request, userDetails);
                    logger.debug("Autenticación exitosa para usuario: {}", username);

                } else {
                    suspiciousActivityService.registerExpiredToken(username);
                    logger.warn("Token inválido o expirado para usuario: {}", username);
                    sendUnauthorized(response, "Token inválido o expirado");
                    return;
                }
            }
        } catch (JwtException e) {
            suspiciousActivityService.registerCrudFailure("JWT inválido");
            logger.warn("Error al procesar token JWT: {}", e.getMessage());
            sendUnauthorized(response, "Token malformado o inválido");
            return;
        } catch (UsernameNotFoundException e) {
            logger.warn("Usuario del token no encontrado: {}", e.getMessage());
            sendUnauthorized(response, "Credenciales inválidas");
            return;
        }

        filterChain.doFilter(request, response);
    }

    // Método privado para establecer la autenticación en el contexto de seguridad a partir de los detalles del usuario
    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    //  Método privado para enviar una respuesta de error 401 Unauthorized con un mensaje personalizado en caso de token inválido o expirado
    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
