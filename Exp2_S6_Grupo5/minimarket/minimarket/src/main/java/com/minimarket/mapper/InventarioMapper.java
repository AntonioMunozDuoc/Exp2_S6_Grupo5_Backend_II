package com.minimarket.mapper;

import org.springframework.stereotype.Component;

import com.minimarket.dto.InventarioRequestDTO;
import com.minimarket.dto.InventarioResponseDTO;
import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;

@Component
public class InventarioMapper {

    public Inventario toEntity(
            InventarioRequestDTO dto,
            Producto producto
    ) {

        Inventario inventario =
                new Inventario();

        inventario.setProducto(
                producto
        );

        inventario.setCantidad(
                dto.getCantidad()
        );

        inventario.setTipoMovimiento(
                dto.getTipoMovimiento()
        );

        inventario.setFechaMovimiento(
                dto.getFechaMovimiento()
        );

        return inventario;

    }

    public InventarioResponseDTO toResponseDTO(
            Inventario inventario
    ) {

        return new InventarioResponseDTO(

                inventario.getId(),

                inventario.getProducto()
                        .getId(),

                inventario.getProducto()
                        .getNombre(),

                inventario.getCantidad(),

                inventario.getTipoMovimiento(),

                inventario.getFechaMovimiento()

        );

    }

}
