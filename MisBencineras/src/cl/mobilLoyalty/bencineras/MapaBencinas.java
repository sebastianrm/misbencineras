package cl.mobilLoyalty.bencineras;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cl.mobilLoyalty.bencineras.bean.PtoDesdeHasta;
import cl.mobilLoyalty.bencineras.bean.QuienSoy;
import cl.mobilLoyalty.bencineras.logic.AppLogic;
import cl.mobilLoyalty.bencineras.logic.HelloItemizedOverlay;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapaBencinas extends MapActivity {
	private QuienSoy props;
	private PtoDesdeHasta sellecion;
	private List<Overlay> mapOverlays;
	private HelloItemizedOverlay itemizedoverlay;
	private MapView mapView;
	private MapController myMapController;
	private AppLogic resultadoBusqueda;
	protected static HashMap<Integer, Integer> distanciasZoom = new HashMap<Integer, Integer>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5503416114647714917L;

		{
			put(1000, 16);
			put(1500, 15);
			put(2000, 14);
			put(2500, 13);
			put(3000, 12);
		}
	};

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

		props = NavigationManager.getProperties(this);
		
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

	}

	private void pintaPintadorMapas() {

		mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.red_dot);
		itemizedoverlay = new HelloItemizedOverlay(drawable, this);

		// ubicacion actual
		GeoPoint point1 = new GeoPoint((int) (sellecion.getLatDesde() * 1e6),
				(int) (sellecion.getLongDesde() * 1e6));

		myMapController.setCenter(point1);
		/**
		 * zoom
		 */
		
		myMapController.setZoom(distanciasZoom.get(sellecion.getMetros()));

		OverlayItem overlayitem1 = new OverlayItem(point1, "",
				"Ubicacion actual");

		itemizedoverlay.addOverlay(overlayitem1);

		itemizedoverlay.addAllOverlay(sellecion.getBencinera());

		mapOverlays.add(itemizedoverlay);

		mapView.postInvalidate();

	}

	public void volver(View view) {

		NavigationManager.navegarActivityLista(this, resultadoBusqueda,props);

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
