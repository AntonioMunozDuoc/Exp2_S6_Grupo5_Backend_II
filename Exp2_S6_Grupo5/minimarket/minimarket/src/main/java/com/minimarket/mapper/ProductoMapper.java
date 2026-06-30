package com.minimarket.mapper;

import org.springframework.stereotype.Component;

import com.minimarket.dto.ProductoRequestDTO;
import com.minimarket.dto.ProductoResponseDTO;
import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;

@Component
public class ProductoMapper {

    public Producto toEntity(
            ProductoRequestDTO dto,
            Categoria categoria
    ) {

        Producto producto =
                new Producto();

        producto.setNombre(
                dto.getNombre()
        );

        producto.setPrecio(
                dto.getPrecio()
        );

        producto.setStock(
                dto.getStock()
        );

        producto.setCategoria(
                categoria
        );

        return producto;

    }

    public ProductoResponseDTO toResponseDTO(
            Producto producto
    ) {

        return new ProductoResponseDTO(

                producto.getId(),

                producto.getNombre(),

                producto.getPrecio(),

                producto.getStock(),

                producto.getCategoria()
                        .getId(),

                producto.getCategoria()
                        .getNombre()

        );

    }

}
