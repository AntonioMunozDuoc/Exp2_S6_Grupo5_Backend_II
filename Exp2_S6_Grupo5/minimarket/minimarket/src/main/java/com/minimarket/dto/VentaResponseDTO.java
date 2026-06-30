package com.minimarket.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaResponseDTO {

    private Long id;

    private Long usuarioId;

    private String username;

    private Date fecha;

    private Integer cantidadDetalles;

    private List<Long> detalleIds;

}
