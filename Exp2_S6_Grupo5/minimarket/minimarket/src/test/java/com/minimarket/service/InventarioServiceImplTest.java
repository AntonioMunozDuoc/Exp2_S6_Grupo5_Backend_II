package com.minimarket.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.service.impl.InventarioServiceImpl;

@ExtendWith(MockitoExtension.class)
class InventarioServiceImplTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    // Escenario de éxito: movimiento de entrada válido
    @Test
    void debeRegistrarMovimientoDeEntradaCorrectamente() {

        // Arrange
        Producto producto = new Producto();
        producto.setId(1L);

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidad(10);
        inventario.setTipoMovimiento("Entrada");
        inventario.setFechaMovimiento(new Date());

        when(inventarioRepository.save(any()))
                .thenReturn(inventario);

        // Act
        Inventario resultado = inventarioService.save(inventario);

        // Assert
        assertNotNull(resultado);
        assertEquals("Entrada", resultado.getTipoMovimiento());
        assertEquals(10, resultado.getCantidad());
        verify(inventarioRepository).save(any());
    }

    // Escenario de éxito: movimiento de salida válido
    @Test
    void debeRegistrarMovimientoDeSalidaCorrectamente() {

        // Arrange
        Producto producto = new Producto();
        producto.setId(2L);

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidad(5);
        inventario.setTipoMovimiento("Salida");
        inventario.setFechaMovimiento(new Date());

        when(inventarioRepository.save(any()))
                .thenReturn(inventario);

        // Act
        Inventario resultado = inventarioService.save(inventario);

        // Assert
        assertNotNull(resultado);
        assertEquals("Salida", resultado.getTipoMovimiento());
        verify(inventarioRepository).save(any());
    }

    // Escenario de error: cantidad igual a cero
    @Test
    void debeLanzarExcepcionSiCantidadEsCero() {

        // Arrange
        Inventario inventario = new Inventario();
        inventario.setCantidad(0);
        inventario.setTipoMovimiento("Entrada");
        inventario.setFechaMovimiento(new Date());

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> inventarioService.save(inventario)
        );

        assertEquals("La cantidad debe ser mayor a 0", ex.getMessage());
        verify(inventarioRepository, never()).save(any());
    }

    // Escenario de error: cantidad negativa
    @Test
    void debeLanzarExcepcionSiCantidadEsNegativa() {

        // Arrange
        Inventario inventario = new Inventario();
        inventario.setCantidad(-3);
        inventario.setTipoMovimiento("Salida");
        inventario.setFechaMovimiento(new Date());

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> inventarioService.save(inventario)
        );

        assertEquals("La cantidad debe ser mayor a 0", ex.getMessage());
        verify(inventarioRepository, never()).save(any());
    }

    // Escenario de error: tipo de movimiento inválido
    @Test
    void debeLanzarExcepcionSiTipoMovimientoEsInvalido() {

        // Arrange
        Inventario inventario = new Inventario();
        inventario.setCantidad(5);
        inventario.setTipoMovimiento("Transferencia");
        inventario.setFechaMovimiento(new Date());

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> inventarioService.save(inventario)
        );

        assertEquals(
            "Tipo de movimiento inválido. Use 'Entrada' o 'Salida'",
            ex.getMessage()
        );
        verify(inventarioRepository, never()).save(any());
    }

    // Escenario de error: tipo de movimiento nulo
    @Test
    void debeLanzarExcepcionSiTipoMovimientoEsNulo() {

        // Arrange
        Inventario inventario = new Inventario();
        inventario.setCantidad(5);
        inventario.setTipoMovimiento(null);
        inventario.setFechaMovimiento(new Date());

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> inventarioService.save(inventario)
        );

        assertEquals(
            "Tipo de movimiento inválido. Use 'Entrada' o 'Salida'",
            ex.getMessage()
        );
        verify(inventarioRepository, never()).save(any());
    }
}