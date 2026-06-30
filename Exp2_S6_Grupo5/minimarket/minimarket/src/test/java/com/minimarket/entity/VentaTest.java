package com.minimarket.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Venta;

class VentaTest {

    @Test
    void debeCalcularTotalCorrectamente() {

        // Arrange
        DetalleVenta detalle1 = new DetalleVenta();
        detalle1.setCantidad(2);
        detalle1.setPrecio(1000.0);

        DetalleVenta detalle2 = new DetalleVenta();
        detalle2.setCantidad(3);
        detalle2.setPrecio(500.0);

        Venta venta = new Venta();
        venta.setDetalles(List.of(detalle1, detalle2));

        // Act
        Double total = venta.calcularTotal();

        // Assert
        assertEquals(3500.0, total);
    }

    @Test
    void debeRetornarCeroSiDetallesEsNull() {

        // Arrange
        Venta venta = new Venta();
        venta.setDetalles(null);

        // Act
        Double total = venta.calcularTotal();

        // Assert
        assertEquals(0.0, total);
    }

    @Test
    void debeRetornarCeroSiDetallesEstaVacio() {

        // Arrange
        Venta venta = new Venta();
        venta.setDetalles(new ArrayList<>());

        // Act
        Double total = venta.calcularTotal();

        // Assert
        assertEquals(0.0, total);
    }

    @Test
    void debeTratarValoresNullEnDetalleComoCero() {

        // Arrange
        DetalleVenta detalle = new DetalleVenta();
        detalle.setCantidad(null);
        detalle.setPrecio(null);

        Venta venta = new Venta();
        venta.setDetalles(List.of(detalle));

        // Act
        Double total = venta.calcularTotal();

        // Assert
        assertEquals(0.0, total);
    }

    @Test
    void noDebeAceptarValoresNegativos() {

        DetalleVenta detalle =
                new DetalleVenta();

        detalle.setPrecio(-100.0);

        detalle.setCantidad(2);

        Venta venta =
                new Venta();

        venta.setDetalles(
                List.of(detalle)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> venta.calcularTotal()
        );

    }
}
