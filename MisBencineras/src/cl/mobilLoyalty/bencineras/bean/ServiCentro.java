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

public class ServiCentro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String empresa;
	private String direccion;
	private GeoReferencia geoRef;

	private Set<Bencinas> Bencinas;

	private Region region;

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Set<Bencinas> getBencinas() {
		return Bencinas;
	}

	public void setBencinas(Set<Bencinas> bencinas) {
		Bencinas = bencinas;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public GeoReferencia getGeoRef() {
		return geoRef;
	}

	public void setGeoRef(GeoReferencia geoRef) {
		this.geoRef = geoRef;
	}

}
