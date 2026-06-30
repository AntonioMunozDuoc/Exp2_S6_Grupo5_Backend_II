package com.minimarket.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVentaRequestDTO {

    @NotNull(
        message = "La venta es obligatoria"
    )
    private Long ventaId;

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

    @NotNull(
        message = "El precio es obligatorio"
    )
    @Min(
        value = 0,
        message =
            "El precio no puede ser negativo"
    )
    private Double precio;

}
