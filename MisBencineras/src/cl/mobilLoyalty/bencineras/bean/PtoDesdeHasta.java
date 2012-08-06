/**
 * 
 */
package cl.mobilLoyalty.bencineras.bean;

import java.io.Serializable;

/**
 * @author Administrador
 * 
 */
public class PtoDesdeHasta implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double latDesde;
	private Double longDesde;
	private Integer metros;
	private Bencinas bencinera;



	public Integer getMetros() {
		return metros;
	}

	public void setMetros(Integer metros) {
		this.metros = metros;
	}

	public Double getLatDesde() {
		return latDesde;
	}

	public void setLatDesde(Double latDesde) {
		this.latDesde = latDesde;
	}

	public Double getLongDesde() {
		return longDesde;
	}

	public void setLongDesde(Double longDesde) {
		this.longDesde = longDesde;
	}

	public Bencinas getBencinera() {
		return bencinera;
	}

	public void setBencinera(Bencinas bencinera) {
		this.bencinera = bencinera;
	}

}
