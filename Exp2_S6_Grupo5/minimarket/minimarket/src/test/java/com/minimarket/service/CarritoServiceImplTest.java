package com.minimarket.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Producto;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.service.impl.CarritoServiceImpl;

@ExtendWith(MockitoExtension.class)
class CarritoServiceImplTest {

    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    @Test
    void noDebeAgregarProductoSiStockEsInsuficiente() {

        // Arrange

        Producto producto = new Producto();

        producto.setStock(5);

        Carrito carrito = new Carrito();

        carrito.setProducto(producto);

        carrito.setCantidad(10);

        // Act

        IllegalStateException ex =
                assertThrows(
                        IllegalStateException.class,
                        () ->
                                carritoService
                                        .save(carrito)
                );

        // Assert

        assertEquals(
                "Stock insuficiente",
                ex.getMessage()
        );

        verify(
                carritoRepository,
                never()
        ).save(any());

    }

    @Test
    void debeAgregarProductoSiHayStock() {

        // Arrange

        Producto producto = new Producto();

        producto.setStock(10);

        Carrito carrito = new Carrito();

        carrito.setProducto(producto);

        carrito.setCantidad(3);

        when(carritoRepository.save(any()))
        .thenReturn(carrito);

        // Act

        Carrito resultado = carritoService.save(carrito);

        // Assert

        assertNotNull(resultado);

        verify(
                carritoRepository
        ).save(any());

    }

}
