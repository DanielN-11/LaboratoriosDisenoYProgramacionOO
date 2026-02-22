package uniandes.dpoo.aerolinea.persistencia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import uniandes.dpoo.aerolinea.exceptions.AeropuertoDuplicadoException;
import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.modelo.Aerolinea;
import uniandes.dpoo.aerolinea.modelo.Aeropuerto;
import uniandes.dpoo.aerolinea.modelo.Avion;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;

public class PersistenciaAerolineaJson implements IPersistenciaAerolinea
{
    // Secciones
    private static final String AEROPUERTOS = "aeropuertos";
    private static final String RUTAS = "rutas";
    private static final String AVIONES = "aviones";
    private static final String VUELOS = "vuelos";

    /// Aeropuerto
    private static final String NOMBRE = "nombre";
    private static final String CODIGO = "codigo";
    private static final String NOMBRE_CIUDAD = "nombreCiudad";
    private static final String LATITUD = "latitud";
    private static final String LONGITUD = "longitud";

    // Ruta
    private static final String CODIGO_RUTA = "codigoRuta";
    private static final String ORIGEN = "origen";
    private static final String DESTINO = "destino";
    private static final String HORA_SALIDA = "horaSalida";
    private static final String HORA_LLEGADA = "horaLlegada";

    // Avión
    private static final String NOMBRE_AVION = "nombreAvion";
    private static final String CAPACIDAD = "capacidad";

    // Vuelo
    private static final String FECHA = "fecha";

    @Override
    public void cargarAerolinea(String archivo, Aerolinea aerolinea)
            throws IOException, InformacionInconsistenteException
    {
        String jsonCompleto = new String(Files.readAllBytes(new File(archivo).toPath()));
        JSONObject raiz = new JSONObject(jsonCompleto);

        Map<String, Aeropuerto> aeropuertosPorCodigo = new HashMap<>();

        if (!raiz.has(AEROPUERTOS))
        {
            throw new InformacionInconsistenteException(
                "El archivo JSON no tiene la sección '" + AEROPUERTOS + "'. " +
                "Con tu clase Aeropuerto se necesitan nombre/código/ciudad/latitud/longitud para reconstruirlos."
            );
        }

        JSONArray jAeropuertos = raiz.getJSONArray(AEROPUERTOS);
        for (int i = 0; i < jAeropuertos.length(); i++)
        {
            JSONObject ja = jAeropuertos.getJSONObject(i);

            String nombre = ja.getString(NOMBRE);
            String codigo = ja.getString(CODIGO);
            String ciudad = ja.getString(NOMBRE_CIUDAD);
            double lat = ja.getDouble(LATITUD);
            double lon = ja.getDouble(LONGITUD);

            try
            {
                Aeropuerto aeropuerto = new Aeropuerto(nombre, codigo, ciudad, lat, lon);
                aeropuertosPorCodigo.put(codigo, aeropuerto);
            }
            catch (AeropuertoDuplicadoException e)
            {
                throw new InformacionInconsistenteException("Aeropuerto duplicado: " + codigo);
            }
        }

        // Cargar rutas
        JSONArray jRutas = raiz.getJSONArray(RUTAS);
        for (int i = 0; i < jRutas.length(); i++)
        {
            JSONObject jr = jRutas.getJSONObject(i);

            String codigoRuta = jr.getString(CODIGO_RUTA);
            String codOrigen = jr.getString(ORIGEN);
            String codDestino = jr.getString(DESTINO);
            String horaSalida = jr.getString(HORA_SALIDA);
            String horaLlegada = jr.getString(HORA_LLEGADA);

            Aeropuerto origen = aeropuertosPorCodigo.get(codOrigen);
            Aeropuerto destino = aeropuertosPorCodigo.get(codDestino);

            if (origen == null)
                throw new InformacionInconsistenteException("No existe aeropuerto origen con código: " + codOrigen);
            if (destino == null)
                throw new InformacionInconsistenteException("No existe aeropuerto destino con código: " + codDestino);

            Ruta ruta = new Ruta(origen, destino, horaSalida, horaLlegada, codigoRuta);
            aerolinea.agregarRuta(ruta);
        }

        // Cargar aviones
        JSONArray jAviones = raiz.getJSONArray(AVIONES);
        for (int i = 0; i < jAviones.length(); i++)
        {
            JSONObject ja = jAviones.getJSONObject(i);

            String nombre = ja.getString(NOMBRE_AVION);
            int capacidad = ja.getInt(CAPACIDAD);

            Avion avion = new Avion(nombre, capacidad);
            aerolinea.agregarAvion(avion);
        }

        // Cargar vuelos (usando el método público de Aerolinea)
        JSONArray jVuelos = raiz.getJSONArray(VUELOS);
        for (int i = 0; i < jVuelos.length(); i++)
        {
            JSONObject jv = jVuelos.getJSONObject(i);

            String fecha = jv.getString(FECHA);
            String codigoRuta = jv.getString(CODIGO_RUTA);
            String nombreAvion = jv.getString(NOMBRE_AVION);

            try
            {
                aerolinea.programarVuelo(fecha, codigoRuta, nombreAvion);
            }
            catch (Exception e)
            {
                throw new InformacionInconsistenteException(
                    "Error cargando vuelo (ruta=" + codigoRuta + ", fecha=" + fecha + ", avion=" + nombreAvion + "): " + e.getMessage()
                );
            }
        }
    }

    @Override
    public void salvarAerolinea(String archivo, Aerolinea aerolinea) throws IOException
    {
        JSONObject raiz = new JSONObject();

        Map<String, Aeropuerto> aeropuertosUnicos = new LinkedHashMap<>();
        for (Ruta ruta : aerolinea.getRutas())
        {
            Aeropuerto o = ruta.getOrigen();
            Aeropuerto d = ruta.getDestino();
            aeropuertosUnicos.put(o.getCodigo(), o);
            aeropuertosUnicos.put(d.getCodigo(), d);
        }

        JSONArray jAeropuertos = new JSONArray();
        for (Aeropuerto a : aeropuertosUnicos.values())
        {
            JSONObject ja = new JSONObject();
            ja.put(NOMBRE, a.getNombre());
            ja.put(CODIGO, a.getCodigo());
            ja.put(NOMBRE_CIUDAD, a.getNombreCiudad());
            ja.put(LATITUD, a.getLatitud());
            ja.put(LONGITUD, a.getLongitud());
            jAeropuertos.put(ja);
        }
        raiz.put(AEROPUERTOS, jAeropuertos);

        // Salvar rutas
        JSONArray jRutas = new JSONArray();
        for (Ruta ruta : aerolinea.getRutas())
        {
            JSONObject jr = new JSONObject();
            jr.put(CODIGO_RUTA, ruta.getCodigoRuta());
            jr.put(ORIGEN, ruta.getOrigen().getCodigo());
            jr.put(DESTINO, ruta.getDestino().getCodigo());
            jr.put(HORA_SALIDA, ruta.getHoraSalida());
            jr.put(HORA_LLEGADA, ruta.getHoraLlegada());
            jRutas.put(jr);
        }
        raiz.put(RUTAS, jRutas);

        // Salvar aviones
        JSONArray jAviones = new JSONArray();
        for (Avion avion : aerolinea.getAviones())
        {
            JSONObject ja = new JSONObject();
            ja.put(NOMBRE_AVION, avion.getNombre());
            ja.put(CAPACIDAD, avion.getCapacidad());
            jAviones.put(ja);
        }
        raiz.put(AVIONES, jAviones);

        // Salvar vuelos
        JSONArray jVuelos = new JSONArray();
        for (Vuelo vuelo : aerolinea.getVuelos())
        {
            JSONObject jv = new JSONObject();
            jv.put(FECHA, vuelo.getFecha());
            jv.put(CODIGO_RUTA, vuelo.getRuta().getCodigoRuta());
            jv.put(NOMBRE_AVION, vuelo.getAvion().getNombre());
            jVuelos.put(jv);
        }
        raiz.put(VUELOS, jVuelos);

        // Escribir archivo
        try (PrintWriter pw = new PrintWriter(archivo))
        {
            pw.print(raiz.toString(2));
        }
    }
}
