package com.minimarket.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.minimarket.entity.Producto;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.security.service.SanitizerService;
import com.minimarket.service.impl.ProductoServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private SanitizerService sanitizerService;

    @InjectMocks
    private ProductoServiceImpl productoService;

    // Escenario de éxito: guarda correctamente y sanitiza el nombre
    @Test
    void debeGuardarProductoYSanitizarNombre() {

        // Arrange
        Producto producto = new Producto();
        producto.setNombre("<script>Arroz</script>");
        producto.setPrecio(1500.0);
        producto.setStock(100);

        when(sanitizerService.sanitize("<script>Arroz</script>"))
                .thenReturn("Arroz");

        when(productoRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        // Act
        Producto resultado = productoService.save(producto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Arroz", resultado.getNombre());
        verify(sanitizerService).sanitize("<script>Arroz</script>");
        verify(productoRepository).save(any());
    }

    // Escenario de éxito: nombre normal sin caracteres peligrosos
    @Test
    void debeGuardarProductoConNombreLimpio() {

        // Arrange
        Producto producto = new Producto();
        producto.setNombre("Leche");
        producto.setPrecio(900.0);
        producto.setStock(50);

        when(sanitizerService.sanitize("Leche"))
                .thenReturn("Leche");

        when(productoRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        // Act
        Producto resultado = productoService.save(producto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Leche", resultado.getNombre());
        verify(productoRepository).save(any());
    }

    // Escenario de error: el repositorio falla al guardar
    @Test
    void debeLanzarExcepcionSiRepositorioFalla() {

        // Arrange
        Producto producto = new Producto();
        producto.setNombre("Pan");
        producto.setPrecio(500.0);
        producto.setStock(20);

        when(sanitizerService.sanitize("Pan"))
                .thenReturn("Pan");

        when(productoRepository.save(any()))
                .thenThrow(new RuntimeException("Error en base de datos"));

        // Act & Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> productoService.save(producto)
        );

        assertEquals("Error en base de datos", ex.getMessage());
        verify(productoRepository).save(any());
    }

    // Escenario de error: el sanitizer lanza excepción con entrada maliciosa
    @Test
    void debeLanzarExcepcionSiNombreEsNulo() {

        // Arrange
        Producto producto = new Producto();
        producto.setNombre(null);

        when(sanitizerService.sanitize(null))
                .thenThrow(new IllegalArgumentException("El nombre no puede ser nulo"));

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productoService.save(producto)
        );

        assertEquals("El nombre no puede ser nulo", ex.getMessage());
        verify(productoRepository, never()).save(any());
    }
}