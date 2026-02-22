package uniandes.dpoo.aerolinea.consola;

import java.io.IOException;

import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.exceptions.VueloSobrevendidoException;
import uniandes.dpoo.aerolinea.modelo.Aerolinea;
import uniandes.dpoo.aerolinea.persistencia.CentralPersistencia;
import uniandes.dpoo.aerolinea.persistencia.TipoInvalidoException;

public class ConsolaArerolinea extends ConsolaBasica
{
    private Aerolinea unaAerolinea;

    /**
     * Es un método que corre la aplicación y permite hacer pruebas con un menú.
     */
    public void correrAplicacion( )
    {
        unaAerolinea = new Aerolinea( );

        boolean continuar = true;
        while( continuar )
        {
            String[] opciones = new String[] {
                    "Cargar aerolínea (JSON)",
                    "Cargar tiquetes (JSON)",
                    "Programar vuelo",
                    "Vender tiquetes",
                    "Registrar vuelo realizado",
                    "Consultar saldo pendiente de un cliente",
                    "Salvar aerolínea (JSON)",
                    "Salvar tiquetes (JSON)",
                    "Ver resumen",
                    "Salir"
            };

            int seleccion = mostrarMenu( "Menú Aerolínea", opciones );

            try
            {
                switch( seleccion )
                {
                    case 1:
                        ejecutarCargarAerolinea( );
                        break;
                    case 2:
                        ejecutarCargarTiquetes( );
                        break;
                    case 3:
                        ejecutarProgramarVuelo( );
                        break;
                    case 4:
                        ejecutarVenderTiquetes( );
                        break;
                    case 5:
                        ejecutarRegistrarVueloRealizado( );
                        break;
                    case 6:
                        ejecutarConsultarSaldoPendiente( );
                        break;
                    case 7:
                        ejecutarSalvarAerolinea( );
                        break;
                    case 8:
                        ejecutarSalvarTiquetes( );
                        break;
                    case 9:
                        imprimirResumen( );
                        break;
                    case 10:
                        continuar = false;
                        System.out.println( "Saliendo..." );
                        break;
                }
            }
            catch( TipoInvalidoException e )
            {
                System.out.println( "Tipo inválido: " + e.getMessage( ) );
                e.printStackTrace( );
            }
            catch( IOException e )
            {
                System.out.println( "Problema de I/O: " + e.getMessage( ) );
                e.printStackTrace( );
            }
            catch( InformacionInconsistenteException e )
            {
                System.out.println( "Información inconsistente: " + e.getMessage( ) );
                e.printStackTrace( );
            }
            catch( VueloSobrevendidoException e )
            {
                System.out.println( "Vuelo sobrevendido: " + e.getMessage( ) );
                e.printStackTrace( );
            }
            catch( Exception e )
            {
                System.out.println( "Error: " + e.getMessage( ) );
                e.printStackTrace( );
            }
        }
    }

    private void ejecutarCargarAerolinea( ) throws TipoInvalidoException, IOException, InformacionInconsistenteException
    {
        String archivo = pedirCadenaAlUsuario( "Digite la ruta del archivo de aerolínea (ej: ./datos/aerolinea.json)" );
        unaAerolinea.cargarAerolinea( archivo, CentralPersistencia.JSON );
        System.out.println( "Cargó aerolínea OK" );
        imprimirResumen( );
    }

    private void ejecutarCargarTiquetes( ) throws TipoInvalidoException, IOException, InformacionInconsistenteException
    {
        String archivo = pedirCadenaAlUsuario( "Digite la ruta del archivo de tiquetes (ej: ./datos/tiquetes.json)" );
        unaAerolinea.cargarTiquetes( archivo, CentralPersistencia.JSON );
        System.out.println( "Cargó tiquetes OK" );
        imprimirResumen( );
    }

    private void ejecutarProgramarVuelo( ) throws Exception
    {
        String fecha = pedirCadenaAlUsuario( "Fecha (YYYY-MM-DD)" );
        String codigoRuta = pedirCadenaAlUsuario( "Código de ruta" );
        String nombreAvion = pedirCadenaAlUsuario( "Nombre del avión" );

        unaAerolinea.programarVuelo( fecha, codigoRuta, nombreAvion );
        System.out.println( "Vuelo programado OK" );
        imprimirResumen( );
    }

    private void ejecutarVenderTiquetes( ) throws VueloSobrevendidoException, Exception
    {
        String idCliente = pedirCadenaAlUsuario( "Identificador del cliente (ojo: debe coincidir con getIdentificador())" );
        String fecha = pedirCadenaAlUsuario( "Fecha del vuelo (YYYY-MM-DD)" );
        String codigoRuta = pedirCadenaAlUsuario( "Código de ruta" );
        int cantidad = pedirEnteroAlUsuario( "Cantidad de tiquetes" );

        int total = unaAerolinea.venderTiquetes( idCliente, fecha, codigoRuta, cantidad );
        System.out.println( "Venta OK. Total pagado: " + total );
        imprimirResumen( );
    }

    private void ejecutarRegistrarVueloRealizado( )
    {
        String fecha = pedirCadenaAlUsuario( "Fecha del vuelo (YYYY-MM-DD)" );
        String codigoRuta = pedirCadenaAlUsuario( "Código de ruta" );

        unaAerolinea.registrarVueloRealizado( fecha, codigoRuta );
        System.out.println( "Vuelo registrado como realizado (tiquetes marcados como usados si existían)" );
    }

    private void ejecutarConsultarSaldoPendiente( )
    {
        String idCliente = pedirCadenaAlUsuario( "Identificador del cliente" );
        String saldo = unaAerolinea.consultarSaldoPendienteCliente( idCliente );
        System.out.println( "Saldo pendiente: " + saldo );
    }

    private void ejecutarSalvarAerolinea( ) throws TipoInvalidoException, IOException
    {
        String archivo = pedirCadenaAlUsuario( "Ruta destino (ej: ./datos/aerolinea_out.json)" );
        unaAerolinea.salvarAerolinea( archivo, CentralPersistencia.JSON );
        System.out.println( "Aerolínea salvada en: " + archivo );
    }

    private void ejecutarSalvarTiquetes( ) throws TipoInvalidoException, IOException
    {
        String archivo = pedirCadenaAlUsuario( "Ruta destino (ej: ./datos/tiquetes_out.json)" );
        unaAerolinea.salvarTiquetes( archivo, CentralPersistencia.JSON );
        System.out.println( "Tiquetes salvados en: " + archivo );
    }

    private void imprimirResumen( )
    {
        System.out.println( "---------------------" );
        System.out.println( "Resumen" );
        System.out.println( "Aviones: " + unaAerolinea.getAviones( ).size( ) );
        System.out.println( "Rutas: " + unaAerolinea.getRutas( ).size( ) );
        System.out.println( "Vuelos: " + unaAerolinea.getVuelos( ).size( ) );
        System.out.println( "Clientes: " + unaAerolinea.getClientes( ).size( ) );
        System.out.println( "Tiquetes: " + unaAerolinea.getTiquetes( ).size( ) );
        System.out.println( "---------------------" );
    }

    public static void main( String[] args )
    {
        ConsolaArerolinea ca = new ConsolaArerolinea( );
        ca.correrAplicacion( );
    }
}
