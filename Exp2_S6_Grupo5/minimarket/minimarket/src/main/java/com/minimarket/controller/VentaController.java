package com.minimarket.controller;

import com.minimarket.dto.VentaRequestDTO;
import com.minimarket.dto.VentaResponseDTO;
import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import com.minimarket.mapper.VentaMapper;
import com.minimarket.service.DetalleVentaService;
import com.minimarket.service.UsuarioService;
import com.minimarket.service.VentaService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DetalleVentaService detalleVentaService;

    @Autowired
    private VentaMapper ventaMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE', 'EMPLEADO', 'ADMIN')")
    public List<VentaResponseDTO> listarVentas() {

        return ventaService.findAll()
                .stream()
                .map(ventaMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'EMPLEADO', 'ADMIN')")
    public ResponseEntity<VentaResponseDTO> obtenerVentaPorId(
            @PathVariable Long id) {

        Venta venta =
                ventaService.findById(id);

        if (venta == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                ventaMapper.toResponseDTO(
                        venta
                )
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPLEADO', 'GERENTE', 'ADMIN')")
    public ResponseEntity<VentaResponseDTO> guardarVenta(

            @Valid
            @RequestBody
            VentaRequestDTO dto

    ) {

        Optional<Usuario> usuarioOpt =
                usuarioService.findById(
                        dto.getUsuarioId()
                );

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<DetalleVenta> detalles =
                dto.getDetalleIds() == null

                ? Collections.emptyList()

                : dto.getDetalleIds()
                        .stream()
                        .map(
                            detalleVentaService::findById
                        )
                        .filter(
                            Objects::nonNull
                        )
                        .toList();

        Venta venta =
                ventaMapper.toEntity(
                        dto,
                        usuarioOpt.get(),
                        detalles
                );

        Venta guardada =
                ventaService.save(
                        venta
                );

        return ResponseEntity.ok(
                ventaMapper.toResponseDTO(
                        guardada
                )
        );
    }
}