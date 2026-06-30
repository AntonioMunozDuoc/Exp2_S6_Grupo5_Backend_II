package com.minimarket.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.repository.DetalleVentaRepository;
import com.minimarket.service.impl.DetalleVentaServiceImpl;

@ExtendWith(MockitoExtension.class)
class DetalleVentaServiceImplTest {

    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    @InjectMocks
    private DetalleVentaServiceImpl service;

    @Test
    void debeLanzarExcepcionSiStockEsInsuficiente() {

        Producto producto = new Producto();
        producto.setStock(5);

        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto);
        detalle.setCantidad(10);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.save(detalle)
        );

        assertEquals("Stock insuficiente", ex.getMessage());
    }

    @Test
    void debeGuardarCuandoHayStock() {

        Producto producto = new Producto();
        producto.setStock(10);

        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto);
        detalle.setCantidad(5);

        when(detalleVentaRepository.save(any()))
                .thenReturn(detalle);

        DetalleVenta resultado = service.save(detalle);

        assertNotNull(resultado);
    }
}