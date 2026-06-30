package com.minimarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

// DTO para la respuesta de información de un usuario, contiene los campos que se devolverán al cliente al solicitar información de un usuario 
// Incluyendo su ID, nombre de usuario y roles asociados
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String username;
    private Set<String> roles;
}
