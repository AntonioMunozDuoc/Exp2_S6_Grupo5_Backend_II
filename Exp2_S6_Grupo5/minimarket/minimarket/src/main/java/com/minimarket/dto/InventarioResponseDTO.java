package com.minimarket.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventarioResponseDTO {

    private Long id;

    private Long productoId;

    private String nombreProducto;

    private Integer cantidad;

    private String tipoMovimiento;

    private Date fechaMovimiento;

}
