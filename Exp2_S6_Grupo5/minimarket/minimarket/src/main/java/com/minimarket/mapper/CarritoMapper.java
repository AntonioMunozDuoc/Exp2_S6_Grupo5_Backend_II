package com.minimarket.mapper;

import org.springframework.stereotype.Component;

import com.minimarket.dto.CarritoRequestDTO;
import com.minimarket.dto.CarritoResponseDTO;
import com.minimarket.entity.Carrito;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;

@Component
public class CarritoMapper {

    public Carrito toEntity(
            CarritoRequestDTO dto,
            Usuario usuario,
            Producto producto
    ) {

        Carrito carrito = new Carrito();

        carrito.setUsuario(usuario);

        carrito.setProducto(producto);

        carrito.setCantidad(
                dto.getCantidad()
        );

        return carrito;
    }

    public CarritoResponseDTO toResponseDTO(
            Carrito carrito
    ) {

        return new CarritoResponseDTO(

                carrito.getId(),

                carrito.getUsuario().getId(),

                carrito.getUsuario().getUsername(),

                carrito.getProducto().getId(),

                carrito.getProducto().getNombre(),

                carrito.getCantidad()
        );

    }

}