package com.minimarket.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoRequestDTO {

    @NotBlank(
        message =
            "El nombre es obligatorio"
    )

    @Size(
        min = 2,
        max = 100,
        message =
            "El nombre debe tener entre 2 y 100 caracteres"
    )

    private String nombre;

    @NotNull(
        message =
            "El precio es obligatorio"
    )

    @Min(
        value = 0,
        message =
            "El precio no puede ser negativo"
    )

    private Double precio;

    @NotNull(
        message =
            "El stock es obligatorio"
    )

    @Min(
        value = 0,
        message =
            "El stock no puede ser negativo"
    )

    private Integer stock;

    @NotNull(
        message =
            "La categoría es obligatoria"
    )

    private Long categoriaId;

}
