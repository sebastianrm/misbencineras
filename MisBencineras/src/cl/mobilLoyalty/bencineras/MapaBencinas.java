package cl.mobilLoyalty.bencineras;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import cl.mobilLoyalty.bencineras.bean.PtoDesdeHasta;
import cl.mobilLoyalty.bencineras.bean.QuienSoy;
import cl.mobilLoyalty.bencineras.logic.AppLogic;
import cl.mobilLoyalty.bencineras.logic.HelloItemizedOverlay;
import cl.mobilLoyalty.bencineras.utils.Utiles;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapaBencinas extends MapActivity {
	private QuienSoy quienSoy;
	private PtoDesdeHasta sellecion;
	private List<Overlay> mapOverlays;
	private HelloItemizedOverlay itemizedoverlay;
	private MapView mapView;
	private MapController myMapController;
	private AppLogic resultadoBusqueda;

	public PtoDesdeHasta getSellecion() {
		return sellecion;
	}

	public void setSellecion(PtoDesdeHasta sellecion) {
		this.sellecion = sellecion;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);

		Serializable sellecionSeriali = NavigationManager.getSellecion(this);
		Serializable sellec = NavigationManager.getSelleciones(this);

		quienSoy = NavigationManager.getQuienSoy(this);

		if (sellecionSeriali instanceof PtoDesdeHasta) {
			sellecion = (PtoDesdeHasta) sellecionSeriali;
		}

		if (sellec instanceof AppLogic) {
			resultadoBusqueda = (AppLogic) sellec;
		}

		sellecion
				.setMetros(Math.round(Math.round(resultadoBusqueda.getMetros())));

		/**
		 * pintamos leyenda de la bencinera
		 */

		TextView empresa = (TextView) findViewById(R.id.textViewEmpresa);
		empresa.setText(sellecion.getBencinera().getServiCentro().getEmpresa());

		TextView precio = (TextView) findViewById(R.id.textViewPrecio);
		precio.setText(String.valueOf(Math.round(sellecion.getBencinera()
				.getPrecios())));

		TextView metros = (TextView) findViewById(R.id.textViewMetros);
		metros.setText(String.valueOf(Math.round(Math.round(sellecion
				.getBencinera().getDistancia()))));

		TextView ultanaje = (TextView) findViewById(R.id.textViewCombus);
		ultanaje.setText(sellecion.getBencinera().getDescripcion());

		TextView direccion = (TextView) findViewById(R.id.textViewDir);

		direccion.setText(sellecion.getBencinera().getServiCentro()
				.getDireccion());

		/**
		 * Mapa
		 */
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		myMapController = mapView.getController();

		pintaPintadorMapas();
		RegistraConsultaWs registraConsultaWs = new RegistraConsultaWs();
		registraConsultaWs.execute("");

	}

	private void pintaPintadorMapas() {

		mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.red_dot);
		itemizedoverlay = new HelloItemizedOverlay(drawable, this);
		
		itemizedoverlay.setDrawableBencinera(this.getResources().getDrawable(R.drawable.yellow_dot));

		// ubicacion actual
		GeoPoint point1 = new GeoPoint((int) (sellecion.getLatDesde() * 1e6),
				(int) (sellecion.getLongDesde() * 1e6));

		myMapController.setCenter(point1);
		/**
		 * zoom
		 */

		if (sellecion.getMetros() <= 1500) {
			myMapController.setZoom(16);
		} else if (sellecion.getMetros() <= 3000) {
			myMapController.setZoom(15);
		} else if (sellecion.getMetros() <= 4500) {
			myMapController.setZoom(14);
		} else if (sellecion.getMetros() <= 5500) {
			myMapController.setZoom(13);
		} else if (sellecion.getMetros() <= 8000) {
			myMapController.setZoom(12);
		}
		OverlayItem overlayitem1 = new OverlayItem(point1, "",
				"Ubicacion actual");

		itemizedoverlay.addOverlay(overlayitem1);

		itemizedoverlay.addAllOverlay(sellecion.getBencinera());

		mapOverlays.add(itemizedoverlay);

		mapView.postInvalidate();

	}

	public void volver(View view) {

		NavigationManager.navegarActivityLista(this, resultadoBusqueda,
				quienSoy);

	}

	/**
	 * Tread que registra consulta
	 * 
	 * @author Sebastian Retamal
	 * 
	 */
	public class RegistraConsultaWs extends AsyncTask<String, Float, Boolean> {

		protected Boolean doInBackground(String... urls) {

			// pc seba wireless
			// String URL =
			// /trx/{latitud}/{longitud}/{ultanaje}/{empresa}/{latempresa}/{longempresa}/{precio}/{distancia}{key}
			// secretaria
			String URL = Utiles.END_POINT_BENCINAS_CERRCANAS
					+ "trx/"
					+ resultadoBusqueda.getLatitud()
					+ "/"
					+ resultadoBusqueda.getLongitud()
					+ "/"
					+ resultadoBusqueda.getBencinaSelecionada()
					+ "/"
					+ sellecion.getBencinera().getServiCentro().getEmpresa()
					+ "/"
					+ sellecion.getBencinera().getServiCentro().getGeoRef()
							.getLatitud()
					+ "/"
					+ sellecion.getBencinera().getServiCentro().getGeoRef()
							.getLongitud() + "/"
					+ sellecion.getBencinera().getPrecios() + "/"
					+ sellecion.getBencinera().getDistancia() + "/"
					+ quienSoy.getKey();

			HttpClient httpclient = new DefaultHttpClient();

			URL = URL.replaceAll(" ", "%20");

			// Prepare a request object
			HttpGet httpget = new HttpGet(URL);

			try {
				httpclient.execute(httpget);

				return true;

			} catch (ClientProtocolException e) {
				Log.e("Error al registrar", e.getMessage());
				return false;
			} catch (IOException e) {
				Log.e("Error al registrar", e.getMessage());
				return false;
			}
		}

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
