package uniandes.dpoo.aerolinea.modelo.cliente;


import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public abstract class Cliente {
	private List<Tiquete> tiquetesSinUsar;
	private List<Tiquete> tiquetesUsados;
	
	public Cliente() {
		tiquetesSinUsar = new ArrayList<>();
		tiquetesUsados = new ArrayList<>();
	}
	
	public void agregarTiquete(Tiquete tiquete) {
		tiquetesSinUsar.add(tiquete);
	}
	
	public int calcularValorTotalTiquetes() {
		int total = 0;
		
		for (Tiquete tiquete : tiquetesSinUsar) {
			total += tiquete.getTarifa();
		}
		
		for (Tiquete tiquete : tiquetesUsados) {
			total += tiquete.getTarifa();
		}
		
		return total;
	}
	
	public abstract String getTipoCliente();
	public abstract String getIdentificador();
	
	public void usarTiquetes(Vuelo vuelo) {
		Iterator<Tiquete> it = tiquetesSinUsar.iterator();
		
		while (it.hasNext()) {
			Tiquete tiquete = it.next();
			
			if (vuelo.equals(tiquete.getVuelo())) {
				tiquete.marcarComoUsado();
				it.remove();
				tiquetesUsados.add(tiquete);
			}
		}
	}
	
}
