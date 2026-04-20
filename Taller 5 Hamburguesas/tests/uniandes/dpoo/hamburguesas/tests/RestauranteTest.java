package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class RestauranteTest
{
    private Restaurante restaurante;
    private File archivoIngredientes;
    private File archivoMenu;
    private File archivoCombos;

    @BeforeEach
    void setUp() throws Exception
    {
        restaurante = new Restaurante();

        archivoIngredientes = new File("ingredientes_test.txt");
        archivoMenu = new File("menu_test.txt");
        archivoCombos = new File("combos_test.txt");

        try (FileWriter writer = new FileWriter(archivoIngredientes)) {
            writer.write("lechuga;1000\n");
            writer.write("tomate;1000\n");
        }

        try (FileWriter writer = new FileWriter(archivoMenu)) {
            writer.write("corral;14000\n");
            writer.write("papas medianas;5500\n");
            writer.write("gaseosa;5000\n");
        }

        try (FileWriter writer = new FileWriter(archivoCombos)) {
            writer.write("combo corral;10%;corral;papas medianas;gaseosa\n");
        }
    }

    @Test
    void testCargarInformacion() throws Exception
    {
        restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);

        assertEquals(2, restaurante.getIngredientes().size());
        assertEquals(3, restaurante.getMenuBase().size());
        assertEquals(1, restaurante.getMenuCombos().size());
    }

    @Test
    void testIniciarPedido() throws Exception
    {
        restaurante.iniciarPedido("Daniel", "Cra 1");

        assertNotNull(restaurante.getPedidoEnCurso());
    }

    @Test
    void testIniciarPedidoDuplicado()
    {
        assertThrows(YaHayUnPedidoEnCursoException.class, () -> {
            restaurante.iniciarPedido("Daniel", "Cra 1");
            restaurante.iniciarPedido("Juan", "Cra 2");
        });
    }

    @Test
    void testCerrarPedido() throws Exception
    {
        restaurante.iniciarPedido("Daniel", "Cra 1");
        restaurante.cerrarYGuardarPedido();

        assertNull(restaurante.getPedidoEnCurso());
        assertEquals(1, restaurante.getPedidos().size());
    }

    @Test
    void testCerrarSinPedido()
    {
        assertThrows(NoHayPedidoEnCursoException.class, () -> {
            restaurante.cerrarYGuardarPedido();
        });
    }

    @Test
    void testIngredienteRepetido() throws Exception
    {
        try (FileWriter writer = new FileWriter(archivoIngredientes)) {
            writer.write("lechuga;1000\n");
            writer.write("lechuga;2000\n");
        }

        assertThrows(IngredienteRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
        });
    }

    @Test
    void testProductoRepetido() throws Exception
    {
        try (FileWriter writer = new FileWriter(archivoMenu)) {
            writer.write("corral;14000\n");
            writer.write("corral;15000\n");
        }

        assertThrows(ProductoRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
        });
    }

    @Test
    void testProductoFaltante() throws Exception
    {
        try (FileWriter writer = new FileWriter(archivoCombos)) {
            writer.write("combo raro;10%;corral;producto inexistente\n");
        }

        assertThrows(ProductoFaltanteException.class, () -> {
            restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
        });
    }
}