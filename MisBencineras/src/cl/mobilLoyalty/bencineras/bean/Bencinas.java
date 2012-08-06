package cl.mobilLoyalty.bencineras.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Bencinas implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String descripcion;
	private Float precios;
	private Timestamp fechaUlmtimaModificacion;
	private ServiCentro serviCentro;
	private Double distancia;
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Float getPrecios() {
		return precios;
	}
	public void setPrecios(Float precios) {
		this.precios = precios;
	}
	public Timestamp getFechaUlmtimaModificacion() {
		return fechaUlmtimaModificacion;
	}
	public void setFechaUlmtimaModificacion(Timestamp fechaUlmtimaModificacion) {
		this.fechaUlmtimaModificacion = fechaUlmtimaModificacion;
	}
	public ServiCentro getServiCentro() {
		return serviCentro;
	}
	public void setServiCentro(ServiCentro serviCentro) {
		this.serviCentro = serviCentro;
	}
	public Double getDistancia() {
		return distancia;
	}
	public void setDistancia(Double distancia) {
		this.distancia = distancia;
	}
	
	
		


}
