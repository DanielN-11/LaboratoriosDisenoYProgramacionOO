package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class PedidoTest
{
    private Pedido pedido;

    @BeforeEach
    void setUp() throws Exception
    {
        pedido = new Pedido("Daniel", "Cra 1 # 2-3");
    }

    @Test
    void testGetIdPedido()
    {
        assertTrue(pedido.getIdPedido() >= 0);
    }

    @Test
    void testGetNombreCliente()
    {
        assertEquals("Daniel", pedido.getNombreCliente());
    }

    @Test
    void testAgregarProductoYPrecioTotal()
    {
        pedido.agregarProducto(new ProductoMenu("corral", 14000));
        pedido.agregarProducto(new ProductoMenu("papas medianas", 5500));

        assertEquals(23205, pedido.getPrecioTotalPedido());
    }

    @Test
    void testGenerarTextoFactura()
    {
        pedido.agregarProducto(new ProductoMenu("corral", 14000));
        pedido.agregarProducto(new ProductoMenu("papas medianas", 5500));

        String esperado = "Cliente: Daniel\n" +
                          "Dirección: Cra 1 # 2-3\n" +
                          "----------------\n" +
                          "corral\n" +
                          "            14000\n" +
                          "papas medianas\n" +
                          "            5500\n" +
                          "----------------\n" +
                          "Precio Neto:  19500\n" +
                          "IVA:          3705\n" +
                          "Precio Total: 23205\n";

        assertEquals(esperado, pedido.generarTextoFactura());
    }

    @Test
    void testGuardarFactura() throws Exception
    {
        pedido.agregarProducto(new ProductoMenu("corral", 14000));

        File archivo = new File("factura_test.txt");
        pedido.guardarFactura(archivo);

        assertTrue(archivo.exists());

        String contenido = Files.readString(archivo.toPath());
        assertEquals(pedido.generarTextoFactura(), contenido);

        archivo.delete();
    }
}