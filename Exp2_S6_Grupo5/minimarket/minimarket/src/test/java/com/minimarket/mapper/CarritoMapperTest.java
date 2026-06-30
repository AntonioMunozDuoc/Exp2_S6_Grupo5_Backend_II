package com.minimarket.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.minimarket.dto.CarritoRequestDTO;
import com.minimarket.entity.Carrito;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;

class CarritoMapperTest {

    private final CarritoMapper mapper =
            new CarritoMapper();

    @Test
    void debeAsociarUsuarioCorrectoAlCarrito() {

        // Arrange

        Usuario usuario = new Usuario();

        usuario.setId(1L);

        Producto producto = new Producto();

        producto.setId(100L);

        CarritoRequestDTO dto = new CarritoRequestDTO(1L, 100L, 2);

        // Act

        Carrito carrito = mapper.toEntity(dto, usuario, producto);

        // Assert

        assertEquals(usuario, carrito.getUsuario());

        assertEquals(producto, carrito.getProducto());

        assertEquals(2, carrito.getCantidad());

    }

}
