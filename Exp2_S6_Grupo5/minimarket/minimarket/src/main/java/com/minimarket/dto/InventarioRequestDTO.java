package com.minimarket.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventarioRequestDTO {

    @NotNull(
        message = "El producto es obligatorio"
    )
    private Long productoId;

    @NotNull(
        message = "La cantidad es obligatoria"
    )

    @Min(
        value = 1,
        message =
            "La cantidad debe ser mayor a cero"
    )
    private Integer cantidad;

    @NotBlank
    @Pattern(
    regexp = "^(Entrada|Salida)$",
    message =
        "Tipo de movimiento inválido"
    )
    private String tipoMovimiento;

    @NotNull(
        message =
            "La fecha es obligatoria"
    )
    private Date fechaMovimiento;

}