package com.minimarket.mapper;

import org.springframework.stereotype.Component;

import com.minimarket.dto.CategoriaRequestDTO;
import com.minimarket.dto.CategoriaResponseDTO;
import com.minimarket.entity.Categoria;

@Component
public class CategoriaMapper {

    public Categoria toEntity(
            CategoriaRequestDTO dto
    ) {

        Categoria categoria =
                new Categoria();

        categoria.setNombre(
                dto.getNombre()
        );

        return categoria;

    }

    public CategoriaResponseDTO toResponseDTO(
            Categoria categoria
    ) {

        return new CategoriaResponseDTO(

                categoria.getId(),

                categoria.getNombre()

        );

    }

}
