package com.minimarket.controller;

import com.minimarket.dto.ProductoRequestDTO;
import com.minimarket.dto.ProductoResponseDTO;
import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.mapper.ProductoMapper;
import com.minimarket.service.CategoriaService;
import com.minimarket.service.ProductoService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProductoMapper productoMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPLEADO', 'GERENTE', 'ADMIN')")
    public List<ProductoResponseDTO> listarProductos() {

        return productoService.findAll()
                .stream()
                .map(productoMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPLEADO', 'GERENTE', 'ADMIN')")
    public ResponseEntity<ProductoResponseDTO> obtenerProductoPorId(
            @PathVariable Long id) {

        Producto producto =
                productoService.findById(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                productoMapper.toResponseDTO(producto)
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GERENTE', 'EMPLEADO', 'ADMIN')")
    public ResponseEntity<ProductoResponseDTO> guardarProducto(

            @Valid
            @RequestBody
            ProductoRequestDTO dto

    ) {

        Categoria categoria =
                categoriaService.findById(
                        dto.getCategoriaId()
                );

        if (categoria == null) {
            return ResponseEntity.badRequest().build();
        }

        Producto producto =
                productoMapper.toEntity(
                        dto,
                        categoria
                );

        Producto guardado =
                productoService.save(
                        producto
                );

        return ResponseEntity.ok(
                productoMapper.toResponseDTO(
                        guardado
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'EMPLEADO', 'ADMIN')")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(

            @PathVariable Long id,

            @Valid
            @RequestBody
            ProductoRequestDTO dto

    ) {

        Producto existente =
                productoService.findById(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        Categoria categoria =
                categoriaService.findById(
                        dto.getCategoriaId()
                );

        if (categoria == null) {
            return ResponseEntity.badRequest().build();
        }

        Producto producto =
                productoMapper.toEntity(
                        dto,
                        categoria
                );

        producto.setId(id);

        Producto actualizado =
                productoService.save(
                        producto
                );

        return ResponseEntity.ok(
                productoMapper.toResponseDTO(
                        actualizado
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Long id) {

        Producto producto =
                productoService.findById(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        productoService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}