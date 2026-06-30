package com.minimarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVentaResponseDTO {

    private Long id;

    private Long ventaId;

    private Long productoId;

    private String nombreProducto;

    private Integer cantidad;

    private Double precio;

}
