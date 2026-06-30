package com.minimarket.security.controller;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.security.model.LoginRequest;
import com.minimarket.security.model.LoginResponse;
import com.minimarket.security.model.RefreshTokenRequest;
import com.minimarket.security.model.RegisterRequest;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.security.service.monitor.SuspiciousActivityService;
import com.minimarket.security.util.JwtUtil;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.JwtException;


import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SuspiciousActivityService suspiciousActivityService;

    // Endpoint para el login de usuarios, autentica las credenciales y devuelve un token JWT y un refresh token si son válidas
        @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(
                @Valid
                @RequestBody
                LoginRequest request
        ) {
        try {
                Authentication authentication =
                        authenticationManager
                                .authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                request.getUsername(),
                                                request.getPassword()
                                        )
                                );

                suspiciousActivityService
                        .registerSuccessfulLogin(
                                request.getUsername()
                        );
                UserDetails userDetails =
                        (UserDetails)
                                authentication
                                        .getPrincipal();
                String token =
                        jwtUtil.generateToken(
                                userDetails
                        );
                String refreshToken =
                        jwtUtil.generateRefreshToken(
                                userDetails
                        );
                return ResponseEntity.ok(
                        new LoginResponse(
                                token,
                                refreshToken
                        )
                );
        }

        catch (AuthenticationException e) {
                        suspiciousActivityService
                                .registerFailedLogin(
                                        request.getUsername()
                                );
                        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build();
        }
   }

    // Endpoint para renovar el token de acceso utilizando un refresh token válido
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        try {
            String username = jwtUtil.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!jwtUtil.validateRefreshToken(refreshToken, userDetails)) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Refresh token inválido o expirado"));
            }

            String token = jwtUtil.generateToken(userDetails);
            String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

            return ResponseEntity.ok(new LoginResponse(token, newRefreshToken));
        } catch (JwtException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "No se pudo renovar el token"));
        }
    }

    // Endpoint para registrar un nuevo usuario, asignándole un rol específico o el rol CLIENTE por defecto si no se especifica
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {

        // Verificamos que el username no esté ya en uso
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El username ya está en uso"));
        }


        // Definimos los roles permitidos para el registro, solo se permiten estos roles específicos para evitar asignaciones no autorizadas
        Set<String> rolesPermitidos = Set.of(
                "ROLE_CLIENTE",
                "ROLE_EMPLEADO",
                "ROLE_GERENTE",
                "ROLE_ADMIN"
        );

        // Si no envían rol, asignamos CLIENTE por defecto
        String nombreRol = (request.getRol() != null && !request.getRol().isBlank())
                ? "ROLE_" + request.getRol().toUpperCase()
                : "ROLE_CLIENTE";

        // Validamos que el rol enviado esté dentro de los roles permitidos, si no es así respondemos con un error indicando los roles válidos
        if (!rolesPermitidos.contains(nombreRol)) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "error", "Rol no permitido",
                        "rolesPermitidos", rolesPermitidos
                ));
        }

        // Buscamos el rol en la base de datos, si no existe lo creamos automáticamente
        Rol rol = rolRepository.findByNombre(nombreRol)
                .orElseGet(() -> {
                Rol nuevoRol = new Rol();
                nuevoRol.setNombre(nombreRol);
                return rolRepository.save(nuevoRol);
                });

        // Creamos el usuario con la contraseña hasheada con BCrypt
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRoles(Set.of(rol));

        usuarioRepository.save(usuario);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Usuario registrado correctamente con rol: " + nombreRol));
    }
}