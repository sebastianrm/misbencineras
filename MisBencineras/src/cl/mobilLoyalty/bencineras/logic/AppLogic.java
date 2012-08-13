package cl.mobilLoyalty.bencineras.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import cl.mobilLoyalty.bencineras.bean.Bencinas;

public class AppLogic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double latitud = null;
	private Double longitud = null;
	private String bencinaSelecionada;
	private Double metros;
	private String servicentro;

	public String getServicentro() {
		return servicentro;
	}

	public void setServicentro(String servicentro) {
		this.servicentro = servicentro;
	}

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

	public ArrayList<String> getServicentros() {

		HashSet<String> hashSet = new HashSet<String>();

		Iterator<Bencinas> iterator = bencineras.iterator();

		while (iterator.hasNext()) {
			Bencinas next = iterator.next();

			hashSet.add(next.getServiCentro().getEmpresa());

		}
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(0, "TODAS");
		arrayList.addAll(hashSet);

		return arrayList;

	}

	public List<Bencinas> getBencinerasFiltro() {
		Iterator<Bencinas> iterator = bencineras.iterator();

		ArrayList<Bencinas> arrayListResp = new ArrayList<Bencinas>();

		while (iterator.hasNext()) {
			Bencinas next = iterator.next();

			if (next.getDistancia() <= metros
					&& next.getDescripcion().equals(bencinaSelecionada)) {

				if (servicentro.equals("TODAS")) {
					arrayListResp.add(next);
				} else if (next.getServiCentro().getEmpresa()
						.equals(servicentro)) {
					arrayListResp.add(next);
				}
			}

		}

		return arrayListResp;
	}

}
