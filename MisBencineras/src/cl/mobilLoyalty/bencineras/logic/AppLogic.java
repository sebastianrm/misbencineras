package cl.mobilLoyalty.bencineras.logic;

import java.io.Serializable;
import java.util.ArrayList;

import cl.mobilLoyalty.bencineras.bean.Bencinas;

public class AppLogic implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double latitud = null;
	private Double longitud = null;
	private String bencinaSelecionada;
	private Double metros;
	
	private ArrayList<Bencinas> bencineras;
	
	
	
	public ArrayList<Bencinas> getBencineras() {
		return bencineras;
	}



	public void setBencineras(ArrayList<Bencinas> bencineras) {
		this.bencineras = bencineras;
	}



	public String getBencinaSelecionada() {
		return bencinaSelecionada;
	}



	public void setBencinaSelecionada(String bencinaSelecionada) {
		this.bencinaSelecionada = bencinaSelecionada;
	}



	public Double getMetros() {
		return metros;
	}



	public void setMetros(Double metros) {
		this.metros = metros;
	}



	public Double getLatitud() {
		return latitud;
	}



	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}



	public Double getLongitud() {
		return longitud;
	}



	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}


	public AppLogic() {
		super();
		// TODO Auto-generated constructor stub
	}


	

}
