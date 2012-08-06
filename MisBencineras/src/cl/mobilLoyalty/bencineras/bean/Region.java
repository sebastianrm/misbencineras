/**
 * 
 */
package cl.mobilLoyalty.bencineras.bean;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Sebastian Retamal
 * 
 */
public class Region implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nombre;
	
	private Set<ServiCentro> serviCentros;
	
	

	public Set<ServiCentro> getServiCentros() {
		return serviCentros;
	}

	public void setServiCentros(Set<ServiCentro> serviCentros) {
		this.serviCentros = serviCentros;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
