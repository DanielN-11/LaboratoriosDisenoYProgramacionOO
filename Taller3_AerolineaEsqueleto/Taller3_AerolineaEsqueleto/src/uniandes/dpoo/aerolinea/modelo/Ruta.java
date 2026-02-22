package uniandes.dpoo.aerolinea.modelo;

/**
 * Esta clase tiene la información de una ruta entre dos aeropuertos que cubre una aerolínea.
 */
public class Ruta
{
	private String codigoRuta;
	private Aeropuerto destino;
	private String horaLlegada;
	private String horaSalida;
	private Aeropuerto origen;
	
	public Ruta(Aeropuerto origen, Aeropuerto destino, String horaSalida, String horaLlegada, String codigoRuta) {
		this.origen = origen;
		this.destino = destino;
		this.horaSalida = horaSalida;
		this.horaLlegada = horaLlegada;
		this.codigoRuta = codigoRuta;
	}
	

	public String getCodigoRuta() {
		return codigoRuta;
	}  
	
    public Aeropuerto getDestino() {
		return destino;
	}
    
    public int getDuracion() {
    	int salidaHoras = getHoras(horaSalida);
    	int salidaMin = getMinutos(horaSalida);
    	
    	int llegadaHoras = getHoras(horaLlegada);
        int llegadaMin = getMinutos(horaLlegada);
        
        int salidaTotalMin = salidaHoras * 60 + salidaMin;
        int llegadaTotalMin = llegadaHoras * 60 + llegadaMin;
        
        int duracion = llegadaTotalMin - salidaTotalMin;
        
        if (duracion < 0) {
			duracion += 24 * 60;
		}
        return duracion;
    }

	public String getHoraLlegada() {
		return horaLlegada;
	}
	
	/**
     * Dada una cadena con una hora y minutos, retorna las horas.
     * 
     * Por ejemplo, para la cadena '715' retorna 7.
     * @param horaCompleta Una cadena con una hora, donde los minutos siempre ocupan los dos últimos caracteres
     * @return Una cantidad de horas entre 0 y 23
     */
    public static int getHoras( String horaCompleta )
    {
        int horas = Integer.parseInt( horaCompleta ) / 100;
        return horas;
    }


	public String getHoraSalida() {
		return horaSalida;
	}
	
	// TODO completar
		/**
	     * Dada una cadena con una hora y minutos, retorna los minutos.
	     * 
	     * Por ejemplo, para la cadena '715' retorna 15.
	     * @param horaCompleta Una cadena con una hora, donde los minutos siempre ocupan los dos últimos caracteres
	     * @return Una cantidad de minutos entre 0 y 59
	     */
	    public static int getMinutos( String horaCompleta )
	    {
	        int minutos = Integer.parseInt( horaCompleta ) % 100;
	        return minutos;
	    }


	public Aeropuerto getOrigen() {
		return origen;
	}
}
