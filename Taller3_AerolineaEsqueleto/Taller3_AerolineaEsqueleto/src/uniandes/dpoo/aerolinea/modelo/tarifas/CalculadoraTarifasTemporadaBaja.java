package uniandes.dpoo.aerolinea.modelo.tarifas;

import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteCorporativo;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteNatural;

public class CalculadoraTarifasTemporadaBaja extends CalculadoraTarifas {
	protected int COSTO_POR_KM_CORPORATIVO = 900;
	protected int COSTO_POR_KM_NATURAL = 600;
	
	protected double DESCUENTO_GRANDES = 0.2;
	protected double DESCUENTO_MEDIANAS = 0.1;
	protected double DESCUENTO_PEQ = 0.02;
	
	public CalculadoraTarifasTemporadaBaja() {
		super();
	}
	
	@Override
	public int calcularCostoBase(Vuelo vuelo, Cliente cliente) {
		Ruta ruta = vuelo.getRuta();
		int distancia = calcularDistanciaVuelo(ruta);
		
		int costoPorKm;
		
		if (cliente instanceof ClienteNatural) {
			costoPorKm = COSTO_POR_KM_NATURAL;
		} else if (cliente instanceof ClienteCorporativo) {
			costoPorKm = COSTO_POR_KM_CORPORATIVO;
		} else {
			costoPorKm = COSTO_POR_KM_NATURAL;
		}
		
		return distancia * costoPorKm;
	}
	
	@Override
	public double calcularPorcentajeDescuento(Cliente cliente) {
		if (!(cliente instanceof ClienteCorporativo)) {
			return 0.0;
		}
		
		ClienteCorporativo corp = (ClienteCorporativo) cliente;
		int tam = corp.getTamanoEmpresa();
		
		if (tam == ClienteCorporativo.GRANDE) {
			return DESCUENTO_GRANDES;
		} else if (tam == ClienteCorporativo.MEDIANA) {
			return DESCUENTO_MEDIANAS;
		} else {
			return DESCUENTO_PEQ;
		}
	}
}

