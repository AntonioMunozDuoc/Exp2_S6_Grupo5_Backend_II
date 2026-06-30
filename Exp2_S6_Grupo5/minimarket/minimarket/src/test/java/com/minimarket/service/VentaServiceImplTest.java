package com.minimarket.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.impl.VentaServiceImpl;

@ExtendWith(MockitoExtension.class)
class VentaServiceImplTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    // Éxito: guarda una venta correctamente
    @Test
    void debeGuardarVentaCorrectamente() {

        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setFecha(new Date());

        when(ventaRepository.save(any())).thenReturn(venta);

        // Act
        Venta resultado = ventaService.save(venta);

        // Assert
        assertNotNull(resultado);
        assertEquals(usuario, resultado.getUsuario());
        verify(ventaRepository).save(any());
    }

    // Éxito: retorna venta al buscar por id existente
    @Test
    void debeRetornarVentaPorIdExistente() {

        // Arrange
        Venta venta = new Venta();
        venta.setId(1L);
        venta.setFecha(new Date());

        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        // Act
        Venta resultado = ventaService.findById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(ventaRepository).findById(1L);
    }

    // Error: retorna null si el id no existe
    @Test
    void debeRetornarNullSiIdNoExiste() {

        // Arrange
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Venta resultado = ventaService.findById(99L);

        // Assert
        assertNull(resultado);
        verify(ventaRepository).findById(99L);
    }

    // Éxito: retorna lista de todas las ventas
    @Test
    void debeRetornarTodasLasVentas() {

        // Arrange
        Venta v1 = new Venta();
        Venta v2 = new Venta();

        when(ventaRepository.findAll()).thenReturn(List.of(v1, v2));

        // Act
        List<Venta> resultado = ventaService.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(ventaRepository).findAll();
    }

    // Éxito: retorna ventas filtradas por usuario
    @Test
    void debeRetornarVentasPorUsuarioId() {

        // Arrange
        Venta venta = new Venta();
        when(ventaRepository.findByUsuarioId(1L)).thenReturn(List.of(venta));

        // Act
        List<Venta> resultado = ventaService.findByUsuarioId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(ventaRepository).findByUsuarioId(1L);
    }

    // Error: el repositorio falla al guardar
    @Test
    void debeLanzarExcepcionSiRepositorioFallaAlGuardar() {

        // Arrange
        Venta venta = new Venta();
        venta.setFecha(new Date());

        when(ventaRepository.save(any()))
                .thenThrow(new RuntimeException("Error de base de datos"));

        // Act & Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> ventaService.save(venta)
        );

        assertEquals("Error de base de datos", ex.getMessage());
        verify(ventaRepository).save(any());
    }
}