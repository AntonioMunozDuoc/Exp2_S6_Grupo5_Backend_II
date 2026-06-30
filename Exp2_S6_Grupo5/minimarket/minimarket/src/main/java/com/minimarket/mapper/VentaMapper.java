package com.minimarket.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.minimarket.dto.VentaRequestDTO;
import com.minimarket.dto.VentaResponseDTO;
import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;

@Component
public class VentaMapper {

    public Venta toEntity(
            VentaRequestDTO dto,
            Usuario usuario,
            List<DetalleVenta> detalles
    ) {

        Venta venta =
                new Venta();

        venta.setUsuario(
                usuario
        );

        venta.setFecha(
                dto.getFecha()
        );

        venta.setDetalles(
                detalles
        );

        return venta;

    }

    public VentaResponseDTO toResponseDTO(
            Venta venta
    ) {

        List<Long> detalleIds =
                venta.getDetalles() == null

                ? Collections.emptyList()

                : venta.getDetalles()

                .stream()

                .map(
                    DetalleVenta::getId
                )

                .collect(
                    Collectors.toList()
                );

        return new VentaResponseDTO(

                venta.getId(),

                venta.getUsuario()
                        .getId(),

                venta.getUsuario()
                        .getUsername(),

                venta.getFecha(),

                detalleIds.size(),

                detalleIds

        );

    }

}
