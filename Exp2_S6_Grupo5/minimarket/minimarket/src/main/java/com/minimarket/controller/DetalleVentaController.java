package com.minimarket.controller;

import com.minimarket.dto.DetalleVentaRequestDTO;
import com.minimarket.dto.DetalleVentaResponseDTO;
import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Venta;
import com.minimarket.mapper.DetalleVentaMapper;
import com.minimarket.service.DetalleVentaService;
import com.minimarket.service.ProductoService;
import com.minimarket.service.VentaService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle-ventas")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private DetalleVentaMapper detalleVentaMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE', 'EMPLEADO', 'ADMIN')")
    public List<DetalleVentaResponseDTO> listarDetalleVentas() {

        return detalleVentaService.findAll()
                .stream()
                .map(detalleVentaMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'EMPLEADO', 'ADMIN')")
    public ResponseEntity<DetalleVentaResponseDTO> obtenerDetalleVentaPorId(
            @PathVariable Long id) {

        DetalleVenta detalle =
                detalleVentaService.findById(id);

        if (detalle == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                detalleVentaMapper.toResponseDTO(detalle)
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPLEADO', 'GERENTE', 'ADMIN')")
    public ResponseEntity<DetalleVentaResponseDTO> guardarDetalleVenta(

            @Valid
            @RequestBody
            DetalleVentaRequestDTO dto

    ) {

        Venta venta =
                ventaService.findById(dto.getVentaId());

        Producto producto =
                productoService.findById(dto.getProductoId());

        if (venta == null || producto == null) {
            return ResponseEntity.badRequest().build();
        }

        DetalleVenta detalle =
                detalleVentaMapper.toEntity(
                        dto,
                        venta,
                        producto
                );

        DetalleVenta guardado =
                detalleVentaService.save(detalle);

        return ResponseEntity.ok(
                detalleVentaMapper.toResponseDTO(
                        guardado
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'EMPLEADO', 'ADMIN')")
    public ResponseEntity<DetalleVentaResponseDTO> actualizarDetalleVenta(

            @PathVariable Long id,

            @Valid
            @RequestBody
            DetalleVentaRequestDTO dto

    ) {

        DetalleVenta existente =
                detalleVentaService.findById(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        Venta venta =
                ventaService.findById(dto.getVentaId());

        Producto producto =
                productoService.findById(dto.getProductoId());

        if (venta == null || producto == null) {
            return ResponseEntity.badRequest().build();
        }

        DetalleVenta detalle =
                detalleVentaMapper.toEntity(
                        dto,
                        venta,
                        producto
                );

        detalle.setId(id);

        return ResponseEntity.ok(
                detalleVentaMapper.toResponseDTO(
                        detalleVentaService.save(
                                detalle
                        )
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    public ResponseEntity<Void> eliminarDetalleVenta(
            @PathVariable Long id) {

        DetalleVenta detalle =
                detalleVentaService.findById(id);

        if (detalle == null) {
            return ResponseEntity.notFound().build();
        }

        detalleVentaService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}