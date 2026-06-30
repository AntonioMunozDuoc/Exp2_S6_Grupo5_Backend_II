package com.minimarket.mapper;

import org.springframework.stereotype.Component;

import com.minimarket.dto.DetalleVentaRequestDTO;
import com.minimarket.dto.DetalleVentaResponseDTO;
import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Venta;

@Component
public class DetalleVentaMapper {

    public DetalleVenta toEntity(
            DetalleVentaRequestDTO dto,
            Venta venta,
            Producto producto
    ) {

        DetalleVenta detalle =
                new DetalleVenta();

        detalle.setVenta(
                venta
        );

        detalle.setProducto(
                producto
        );

        detalle.setCantidad(
                dto.getCantidad()
        );

        detalle.setPrecio(
                dto.getPrecio()
        );

        return detalle;

    }

    public DetalleVentaResponseDTO toResponseDTO(
            DetalleVenta detalle
    ) {

        return new DetalleVentaResponseDTO(

                detalle.getId(),

                detalle.getVenta().getId(),

                detalle.getProducto().getId(),

                detalle.getProducto()
                        .getNombre(),

                detalle.getCantidad(),

                detalle.getPrecio()

        );

    }

}