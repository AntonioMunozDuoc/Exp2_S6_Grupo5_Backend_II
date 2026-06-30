package com.minimarket.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaRequestDTO {

    @NotNull(
        message =
            "El usuario es obligatorio"
    )
    private Long usuarioId;

    @NotNull(
        message =
            "La fecha es obligatoria"
    )
    private Date fecha;

    private List<Long> detalleIds;

}
