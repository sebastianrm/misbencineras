/**
 * 
 */
package cl.mobilLoyalty.bencineras.bean;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author Administrador
 * 
 */
public class QuienSoy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3051356750872921407L;
	private String nombre_app;
	private String ambiente;
	private String key;
	private static final String nombreArchivo = "conf.txt";

	public QuienSoy(String nombre_app, String ambiente, String key,
			String nombreArchivo) {
		super();
		this.nombre_app = nombre_app;
		this.ambiente = ambiente;
		this.key = key;
	}

	public QuienSoy() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuienSoy(Properties props) {
		// props.put("nombre_app", "MisBencineras");
		// props.put("ambiente", "android");
		// props.put("key", "");
		this.nombre_app = props.getProperty("nombre_app");
		this.ambiente = props.getProperty("ambiente");
		this.key = props.getProperty("key");

	}

	public String getNombre_app() {
		return nombre_app;
	}

	public void setNombre_app(String nombre_app) {
		this.nombre_app = nombre_app;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	@Override
	public String toString() {
		return "QuienSoy [nombre_app=" + nombre_app + ", ambiente=" + ambiente
				+ ", key=" + key + ", nombreArchivo=" + nombreArchivo + "]";
	}

}
