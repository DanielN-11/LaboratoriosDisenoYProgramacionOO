package uniandes.dpoo.aerolinea.modelo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uniandes.dpoo.aerolinea.exceptions.VueloSobrevendidoException;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifas;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public class Vuelo {
	
	private Avion avion;
	private String fecha;
	private Ruta ruta;
	private Map<String, Tiquete> tiquetes;
	
	
	public Vuelo(Ruta ruta, String fecha, Avion avion) {
		this.ruta = ruta;
		this.fecha = fecha;
		this.avion = avion;
		this.tiquetes = new HashMap<String, Tiquete>();
	}
	
	public Ruta getRuta() {
		return ruta;
	}
	
	public String getFecha() {
		return fecha;
	}
	
	public Avion getAvion() {
		return avion;
	}
	
	public Collection<Tiquete> getTiquetes(){
		return tiquetes.values();
	}
	
	public int venderTiquetes(Cliente cliente, CalculadoraTarifas calculadora, int cantidad) throws VueloSobrevendidoException  { 
		int vendidos = tiquetes.size();
		int capacidad = avion.getCapacidad();
		
		if (vendidos + cantidad > capacidad){
            throw new VueloSobrevendidoException(this);
        }
		
		int tarifaUnit = calculadora.calcularTarifa(this, cliente);
		
		int total = 0;
		for (int i = 0; i < cantidad; i++) {
			int consecutivo = vendidos + i + 1;   // 1-based
		    String codigo = generarCodigoTiquete(consecutivo);
			Tiquete t = new Tiquete(codigo, this, cliente, tarifaUnit);
			tiquetes.put(codigo, t);
			cliente.agregarTiquete(t);
			total += tarifaUnit;
		}
		return total;
	}
	
	private String generarCodigoTiquete(int consecutivo) {
	    return ruta.getCodigoRuta() + "-" + fecha + "-" + consecutivo;
	}
	
	@Override
    public boolean equals(Object obj) {
		if (! (obj instanceof Vuelo)) {
			return false;
		}
		Vuelo otro = (Vuelo) obj;
		
		return this.ruta.equals(otro.ruta)&&this.fecha.equals(otro.fecha);
    }
	
	public void registrarTiqueteCargado(Tiquete t) {
	    tiquetes.put(t.getCodigo(), t);
	}
}
