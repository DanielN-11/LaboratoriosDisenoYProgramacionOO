package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoAjustadoTest
{
    private ProductoAjustado productoAjustado;
    private ProductoMenu productoBase;
    private Ingrediente queso;
    private Ingrediente tomate;
    
    @BeforeEach
    void setUp() throws Exception
    {
        productoBase = new ProductoMenu("corral", 14000);
        queso = new Ingrediente("queso mozzarella", 2500);
        tomate = new Ingrediente("tomate", 1000);

        productoAjustado = new ProductoAjustado(productoBase);
    }

    @Test
    void testGetNombre()
    {
        assertEquals("corral", productoAjustado.getNombre());
    }

    @Test
    void testGetPrecioSinAjustes()
    {
        assertEquals(14000, productoAjustado.getPrecio());
    }

    @Test
    void testGetPrecioConAgregados()
    {
        productoAjustado.agregarIngrediente(queso);
        productoAjustado.agregarIngrediente(tomate);

        assertEquals(17500, productoAjustado.getPrecio());
    }

    @Test
    void testGetPrecioConEliminados()
    {
        productoAjustado.eliminarIngrediente(tomate);

        assertEquals(14000, productoAjustado.getPrecio());
    }

    @Test
    void testGenerarTextoFactura()
    {
        productoAjustado.agregarIngrediente(queso);
        productoAjustado.eliminarIngrediente(tomate);

        String esperado = "corral\n" +
                          "    +queso mozzarella                2500\n" +
                          "    -tomate\n" +
                          "            16500\n";

        assertEquals(esperado, productoAjustado.generarTextoFactura());
    }
}