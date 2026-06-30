package com.minimarket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaRequestDTO {

    @NotBlank(
        message = "El nombre es obligatorio"
    )

    @Size(
        min = 3,
        max = 50,
        message =
            "El nombre debe tener entre 3 y 50 caracteres"
    )

    private String nombre;

}
