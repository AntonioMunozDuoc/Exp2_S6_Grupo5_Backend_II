package com.minimarket.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.service.impl.CarritoServiceImpl;

@ExtendWith(MockitoExtension.class)
class CarritoServiceImplExtraTest {

    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    // Éxito: cantidad exactamente igual al stock (borde)
    @Test
    void debeAceptarCantidadExactamenteIgualAlStock() {

        // Arrange
        Producto producto = new Producto();
        producto.setStock(5);

        Carrito carrito = new Carrito();
        carrito.setProducto(producto);
        carrito.setCantidad(5);

        when(carritoRepository.save(carrito)).thenReturn(carrito);

        // Act
        Carrito resultado = carritoService.save(carrito);

        // Assert
        assertNotNull(resultado);
        verify(carritoRepository).save(carrito);
    }

    // Éxito: busca carrito por id existente
    @Test
    void debeRetornarCarritoPorIdExistente() {

        // Arrange
        Carrito carrito = new Carrito();
        carrito.setId(1L);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        // Act
        Carrito resultado = carritoService.findById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    // Error: retorna null si el id no existe
    @Test
    void debeRetornarNullSiIdNoExiste() {

        // Arrange
        when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Carrito resultado = carritoService.findById(99L);

        // Assert
        assertNull(resultado);
    }

    // Éxito: retorna carritos de un usuario
    @Test
    void debeRetornarCarritosPorUsuarioId() {

        // Arrange
        Carrito carrito = new Carrito();
        when(carritoRepository.findByUsuarioId(1L)).thenReturn(List.of(carrito));

        // Act
        List<Carrito> resultado = carritoService.findByUsuarioId(1L);

        // Assert
        assertEquals(1, resultado.size());
        verify(carritoRepository).findByUsuarioId(1L);
    }

    // Éxito: retorna lista vacía si usuario no tiene carritos
    @Test
    void debeRetornarListaVaciaSiUsuarioNoTieneCarritos() {

        // Arrange
        when(carritoRepository.findByUsuarioId(2L)).thenReturn(List.of());

        // Act
        List<Carrito> resultado = carritoService.findByUsuarioId(2L);

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }
}