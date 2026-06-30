package com.minimarket.controller;

import com.minimarket.dto.UsuarioRequestDTO;
import com.minimarket.dto.UsuarioResponseDTO;
import com.minimarket.entity.Usuario;
import com.minimarket.mapper.UsuarioMapper;
import com.minimarket.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioMapper usuarioMapper;

    // Usamos hasAuthority('ROLE_X') para alinearlo con el nombre almacenado en la BD
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_GERENTE') or hasAuthority('ROLE_ADMIN')")
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioService.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_GERENTE') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioService.findById(id)
                .map(usuarioMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_GERENTE') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> guardarUsuario(
            @Valid @RequestBody UsuarioRequestDTO dto) {

        Usuario usuario = usuarioMapper.toEntity(dto);
        Usuario guardado = usuarioService.save(usuario);

        return ResponseEntity.ok(usuarioMapper.toResponseDTO(guardado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_GERENTE') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO dto) {

        Optional<Usuario> usuarioExistente = usuarioService.findById(id);

        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioMapper.toEntity(dto);
            usuario.setId(id);
            Usuario actualizado = usuarioService.save(usuario);
            return ResponseEntity.ok(usuarioMapper.toResponseDTO(actualizado));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_GERENTE') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {

        Optional<Usuario> usuario = usuarioService.findById(id);

        if (usuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                        "error", "No se puede eliminar el usuario porque tiene registros asociados (ventas, carritos, etc.)"
                    ));
        }
    }
}