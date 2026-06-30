package com.minimarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoResponseDTO {

    private Long id;

    private Long usuarioId;

    private String username;

    private Long productoId;

    private String nombreProducto;

    private Integer cantidad;

}
