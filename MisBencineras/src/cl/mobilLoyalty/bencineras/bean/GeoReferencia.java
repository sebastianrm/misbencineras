package cl.mobilLoyalty.bencineras.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class GeoReferencia implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float latitud;
	private float longitud;
	private ServiCentro serviCentro;
	
	
	public ServiCentro getServiCentro() {
		return serviCentro;
	}

	public void setServiCentro(ServiCentro serviCentro) {
		this.serviCentro = serviCentro;
	}

	private ArrayList<String> direccionGoogle;

	
	


	public ArrayList<String> getDireccionGoogle() {
		return direccionGoogle;
	}

	public void setDireccionGoogle(ArrayList<String> direccionGoogle) {
		this.direccionGoogle = direccionGoogle;
	}

	public GeoReferencia(float latitud, float longitud) {
		this.latitud = latitud;
		this.longitud = longitud;
	}



	public GeoReferencia() {
		super();
		// TODO Auto-generated constructor stub
	}

	public float getLatitud() {
		return latitud;
	}

	public void setLatitud(float latitud) {
		this.latitud = latitud;
	}

	public float getLongitud() {
		return longitud;
	}

	public void setLongitud(float longitud) {
		this.longitud = longitud;
	}

}
