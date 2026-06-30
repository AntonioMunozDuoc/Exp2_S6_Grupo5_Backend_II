package com.minimarket.controller;

import com.minimarket.dto.InventarioRequestDTO;
import com.minimarket.dto.InventarioResponseDTO;
import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.mapper.InventarioMapper;
import com.minimarket.service.InventarioService;
import com.minimarket.service.ProductoService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private InventarioMapper inventarioMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE', 'EMPLEADO', 'ADMIN')")
    public List<InventarioResponseDTO> listarMovimientosDeInventario() {

        return inventarioService.findAll()
                .stream()
                .map(inventarioMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'EMPLEADO', 'ADMIN')")
    public ResponseEntity<InventarioResponseDTO> obtenerMovimientoPorId(
            @PathVariable Long id) {

        Inventario inventario =
                inventarioService.findById(id);

        if (inventario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                inventarioMapper.toResponseDTO(
                        inventario
                )
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GERENTE', 'EMPLEADO', 'ADMIN')")
    public ResponseEntity<InventarioResponseDTO> registrarMovimiento(

            @Valid
            @RequestBody
            InventarioRequestDTO dto

    ) {

        Producto producto =
                productoService.findById(
                        dto.getProductoId()
                );

        if (producto == null) {
            return ResponseEntity.badRequest().build();
        }

        Inventario inventario =
                inventarioMapper.toEntity(
                        dto,
                        producto
                );

        Inventario guardado =
                inventarioService.save(
                        inventario
                );

        return ResponseEntity.ok(
                inventarioMapper.toResponseDTO(
                        guardado
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'EMPLEADO', 'ADMIN')")
    public ResponseEntity<InventarioResponseDTO> actualizarMovimiento(

            @PathVariable Long id,

            @Valid
            @RequestBody
            InventarioRequestDTO dto

    ) {

        Inventario existente =
                inventarioService.findById(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        Producto producto =
                productoService.findById(
                        dto.getProductoId()
                );

        if (producto == null) {
            return ResponseEntity.badRequest().build();
        }

        Inventario inventario =
                inventarioMapper.toEntity(
                        dto,
                        producto
                );

        inventario.setId(id);

        Inventario actualizado =
                inventarioService.save(
                        inventario
                );

        return ResponseEntity.ok(
                inventarioMapper.toResponseDTO(
                        actualizado
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    public ResponseEntity<Void> eliminarMovimiento(
            @PathVariable Long id) {

        Inventario inventario =
                inventarioService.findById(id);

        if (inventario == null) {
            return ResponseEntity.notFound().build();
        }

        inventarioService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}