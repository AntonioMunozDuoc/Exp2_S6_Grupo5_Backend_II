package com.minimarket.controller;

import com.minimarket.dto.CarritoRequestDTO;
import com.minimarket.dto.CarritoResponseDTO;
import com.minimarket.entity.Carrito;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.mapper.CarritoMapper;
import com.minimarket.service.CarritoService;
import com.minimarket.service.ProductoService;
import com.minimarket.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CarritoMapper carritoMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLEADO', 'GERENTE', 'ADMIN')")
    public List<CarritoResponseDTO> listarCarrito() {

        return carritoService.findAll()
                .stream()
                .map(carritoMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPLEADO', 'GERENTE', 'ADMIN')")
    public ResponseEntity<CarritoResponseDTO> obtenerCarritoPorId(
            @PathVariable Long id) {

        Carrito carrito =
                carritoService.findById(id);

        if (carrito == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                carritoMapper.toResponseDTO(carrito)
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPLEADO', 'GERENTE', 'ADMIN')")
    public ResponseEntity<CarritoResponseDTO> agregarProductoAlCarrito(

            @Valid
            @RequestBody
            CarritoRequestDTO dto

    ) {

        Usuario usuario =
                usuarioService.findById(dto.getUsuarioId())
                        .orElse(null);

        Producto producto =
                productoService.findById(dto.getProductoId());

        if (usuario == null || producto == null) {
            return ResponseEntity.badRequest().build();
        }

        Carrito carrito =
                carritoMapper.toEntity(
                        dto,
                        usuario,
                        producto
                );

        Carrito guardado =
                carritoService.save(carrito);

        return ResponseEntity.ok(
                carritoMapper.toResponseDTO(guardado)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPLEADO', 'GERENTE', 'ADMIN')")
    public ResponseEntity<CarritoResponseDTO> actualizarCarrito(

            @PathVariable Long id,

            @Valid
            @RequestBody
            CarritoRequestDTO dto

    ) {

        Carrito existente =
                carritoService.findById(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario =
                usuarioService.findById(dto.getUsuarioId())
                        .orElse(null);

        Producto producto =
                productoService.findById(dto.getProductoId());

        if (usuario == null || producto == null) {
            return ResponseEntity.badRequest().build();
        }

        Carrito carrito =
                carritoMapper.toEntity(
                        dto,
                        usuario,
                        producto
                );

        carrito.setId(id);

        return ResponseEntity.ok(
                carritoMapper.toResponseDTO(
                        carritoService.save(carrito)
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPLEADO', 'GERENTE', 'ADMIN')")
    public ResponseEntity<Void> eliminarProductoDelCarrito(
            @PathVariable Long id) {

        Carrito carrito =
                carritoService.findById(id);

        if (carrito == null) {
            return ResponseEntity.notFound().build();
        }

        carritoService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}