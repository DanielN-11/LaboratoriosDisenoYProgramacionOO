package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ComboTest
{
    private Combo combo;

    @BeforeEach
    void setUp() throws Exception
    {
        ProductoMenu hamburguesa = new ProductoMenu("corral", 14000);
        ProductoMenu papas = new ProductoMenu("papas medianas", 5500);
        ProductoMenu gaseosa = new ProductoMenu("gaseosa", 5000);

        ArrayList<ProductoMenu> items = new ArrayList<>();
        items.add(hamburguesa);
        items.add(papas);
        items.add(gaseosa);

        combo = new Combo("combo corral", 0.10, items);
    }

    @Test
    void testGetNombre()
    {
        assertEquals("combo corral", combo.getNombre());
    }

    @Test
    void testGetPrecio()
    {
        assertEquals(22050, combo.getPrecio());
    }

    @Test
    void testGenerarTextoFactura()
    {
        String esperado = "Combo combo corral\n" +
                          " Descuento: 0.1\n" +
                          "            22050\n";

        assertEquals(esperado, combo.generarTextoFactura());
    }
}