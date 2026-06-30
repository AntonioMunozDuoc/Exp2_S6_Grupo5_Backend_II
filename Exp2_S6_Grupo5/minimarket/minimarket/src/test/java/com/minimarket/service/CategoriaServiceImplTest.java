package com.minimarket.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.minimarket.entity.Categoria;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.security.service.SanitizerService;
import com.minimarket.service.impl.CategoriaServiceImpl;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private SanitizerService sanitizerService;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    // Éxito: guarda categoría y sanitiza el nombre
    @Test
    void debeGuardarCategoriaYSanitizarNombre() {

        // Arrange
        Categoria categoria = new Categoria();
        categoria.setNombre("<b>Lácteos</b>");

        when(sanitizerService.sanitize("<b>Lácteos</b>")).thenReturn("Lácteos");
        when(categoriaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        Categoria resultado = categoriaService.save(categoria);

        // Assert
        assertNotNull(resultado);
        assertEquals("Lácteos", resultado.getNombre());
        verify(sanitizerService).sanitize("<b>Lácteos</b>");
        verify(categoriaRepository).save(any());
    }

    // Éxito: nombre limpio sin caracteres peligrosos
    @Test
    void debeGuardarCategoriaConNombreLimpio() {

        // Arrange
        Categoria categoria = new Categoria();
        categoria.setNombre("Bebidas");

        when(sanitizerService.sanitize("Bebidas")).thenReturn("Bebidas");
        when(categoriaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        Categoria resultado = categoriaService.save(categoria);

        // Assert
        assertEquals("Bebidas", resultado.getNombre());
        verify(categoriaRepository).save(any());
    }

    // Éxito: retorna categoría por id existente
    @Test
    void debeRetornarCategoriaPorIdExistente() {

        // Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Snacks");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        // Act
        Categoria resultado = categoriaService.findById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Snacks", resultado.getNombre());
        verify(categoriaRepository).findById(1L);
    }

    // Error: retorna null si el id no existe
    @Test
    void debeRetornarNullSiIdNoExiste() {

        // Arrange
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Categoria resultado = categoriaService.findById(99L);

        // Assert
        assertNull(resultado);
        verify(categoriaRepository).findById(99L);
    }

    // Éxito: retorna lista de todas las categorías
    @Test
    void debeRetornarTodasLasCategorias() {

        // Arrange
        Categoria c1 = new Categoria();
        Categoria c2 = new Categoria();

        when(categoriaRepository.findAll()).thenReturn(List.of(c1, c2));

        // Act
        List<Categoria> resultado = categoriaService.findAll();

        // Assert
        assertEquals(2, resultado.size());
        verify(categoriaRepository).findAll();
    }

    // Error: el sanitizer lanza excepción con nombre nulo
    @Test
    void debeLanzarExcepcionSiNombreEsNulo() {

        // Arrange
        Categoria categoria = new Categoria();
        categoria.setNombre(null);

        when(sanitizerService.sanitize(null))
                .thenThrow(new IllegalArgumentException("El nombre no puede ser nulo"));

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> categoriaService.save(categoria)
        );

        assertEquals("El nombre no puede ser nulo", ex.getMessage());
        verify(categoriaRepository, never()).save(any());
    }
}