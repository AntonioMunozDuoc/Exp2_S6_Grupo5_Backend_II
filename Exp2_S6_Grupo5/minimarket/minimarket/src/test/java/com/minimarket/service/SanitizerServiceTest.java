package com.minimarket.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.minimarket.security.service.SanitizerService;

class SanitizerServiceTest {

    private SanitizerService sanitizerService;

    @BeforeEach
    void setUp() {
        sanitizerService = new SanitizerService();
    }

    // Éxito: texto limpio no se modifica
    @Test
    void debeRetornarTextoLimpioSinModificacion() {

        String resultado = sanitizerService.sanitize("Arroz");

        assertEquals("Arroz", resultado);
    }

    // Éxito: elimina etiquetas HTML
    @Test
    void debeEliminarEtiquetasHTML() {

        String resultado = sanitizerService.sanitize("<b>Leche</b>");

        assertEquals("Leche", resultado);
    }

    // Éxito: elimina script malicioso completo
    @Test
    void debeEliminarScriptMalicioso() {

        String resultado = sanitizerService.sanitize("<script>alert('xss')</script>Azúcar");

        assertEquals("Azúcar", resultado);
    }

    // Éxito: texto vacío retorna vacío
    @Test
    void debeRetornarVacioSiInputEsVacio() {

        String resultado = sanitizerService.sanitize("");

        assertEquals("", resultado);
    }

    // Error: input nulo retorna null
    @Test
    void debeRetornarNullSiInputEsNulo() {

        String resultado = sanitizerService.sanitize(null);

        assertNull(resultado);
    }

    // Éxito: elimina atributos peligrosos como onclick
    @Test
    void debeEliminarAtributosOnclick() {

        String resultado = sanitizerService.sanitize("<p onclick=\"robar()\">Producto</p>");

        assertEquals("Producto", resultado);
    }
}