package com.minimarket.controller;

import com.minimarket.dto.CategoriaRequestDTO;
import com.minimarket.dto.CategoriaResponseDTO;
import com.minimarket.entity.Categoria;
import com.minimarket.mapper.CategoriaMapper;
import com.minimarket.service.CategoriaService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaMapper categoriaMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPLEADO', 'GERENTE', 'ADMIN')")
    public List<CategoriaResponseDTO> listarCategorias() {

        return categoriaService.findAll()
                .stream()
                .map(categoriaMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPLEADO', 'GERENTE', 'ADMIN')")
    public ResponseEntity<CategoriaResponseDTO> obtenerCategoriaPorId(
            @PathVariable Long id) {

        Categoria categoria =
                categoriaService.findById(id);

        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                categoriaMapper.toResponseDTO(categoria)
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    public ResponseEntity<CategoriaResponseDTO> guardarCategoria(

            @Valid

            @RequestBody
            CategoriaRequestDTO dto

    ) {

        Categoria categoria =
                categoriaMapper.toEntity(dto);

        Categoria guardada =
                categoriaService.save(categoria);

        return ResponseEntity.ok(
                categoriaMapper.toResponseDTO(guardada)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    public ResponseEntity<CategoriaResponseDTO> actualizarCategoria(

            @PathVariable Long id,

            @Valid

            @RequestBody
            CategoriaRequestDTO dto

    ) {

        Categoria existente =
                categoriaService.findById(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        Categoria categoria =
                categoriaMapper.toEntity(dto);

        categoria.setId(id);

        Categoria actualizada =
                categoriaService.save(categoria);

        return ResponseEntity.ok(
                categoriaMapper.toResponseDTO(actualizada)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    public ResponseEntity<Void> eliminarCategoria(
            @PathVariable Long id) {

        Categoria categoria =
                categoriaService.findById(id);

        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }

        categoriaService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}