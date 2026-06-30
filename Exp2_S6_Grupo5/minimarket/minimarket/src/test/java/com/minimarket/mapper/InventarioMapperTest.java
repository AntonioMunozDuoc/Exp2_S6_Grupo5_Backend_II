package com.minimarket.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.minimarket.dto.InventarioRequestDTO;
import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.mapper.InventarioMapper;

class InventarioMapperTest {

    private final InventarioMapper mapper = new InventarioMapper();

    @Test
    void debeMantenerInformacionMovimiento() {

        // Arrange

        Producto producto = new Producto();

        producto.setId(1L);

        InventarioRequestDTO dto = new InventarioRequestDTO();

        dto.setCantidad(10);

        dto.setTipoMovimiento("Entrada");

        dto.setFechaMovimiento(new Date());

        // Act

        Inventario resultado =
                mapper.toEntity(
                        dto,
                        producto
                );

        // Assert

        assertNotNull(resultado.getCantidad());

        assertNotNull(resultado.getTipoMovimiento());

        assertFalse(
                resultado
                        .getTipoMovimiento()
                        .isBlank()
        );

        assertEquals(
                10,
                resultado.getCantidad()
        );

        assertEquals(
                "Entrada",
                resultado.getTipoMovimiento()
        );

    }

    @Test
    void debeAsociarProductoCorrectamente() {

        // Arrange

        Producto producto = new Producto();

        producto.setId(100L);

        producto.setNombre("Arroz");

        InventarioRequestDTO dto = new InventarioRequestDTO();

        dto.setCantidad(5);

        dto.setTipoMovimiento("Salida");

        dto.setFechaMovimiento(new Date());

        // Act

        Inventario resultado =
                mapper.toEntity(
                        dto,
                        producto
                );

        // Assert

        assertNotNull(resultado.getProducto());

        assertEquals(
                100L,
                resultado
                        .getProducto()
                        .getId()
        );

        assertEquals(
                "Arroz",
                resultado
                        .getProducto()
                        .getNombre()
        );

    }

}    