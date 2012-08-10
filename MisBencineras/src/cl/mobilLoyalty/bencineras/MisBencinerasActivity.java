package cl.mobilLoyalty.bencineras;

import java.io.Serializable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import cl.mobilLoyalty.bencineras.bean.QuienSoy;
import cl.mobilLoyalty.bencineras.logic.AppLogic;

public class MisBencinerasActivity extends Activity {

	private QuienSoy quienSoy;
	private AppLogic selleciones;
	Intent locatorService = null;
	Button searchBtn = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		quienSoy = NavigationManager.getQuienSoy(this);

		if (quienSoy != null && !quienSoy.getKey().equals("")) {
			selleciones = new AppLogic();
			selleciones.setLatitud(0.0);
			selleciones.setLongitud(0.0);
			setContentView(R.layout.main);
			searchBtn = (Button) findViewById(R.id.button1);
			Serializable selleciones2 = NavigationManager.getSelleciones(this);
			quienSoy = NavigationManager.getQuienSoy(this);
			if (selleciones2 instanceof AppLogic) {
				selleciones = (AppLogic) selleciones2;
			}
			if (selleciones.getBencineras() == null
					|| selleciones.getBencineras().isEmpty()) {
				startService();

				if (!startService()) {
					 CreateAlert("Error!", "El servicio de Ubicacion no puede ser iniciado");
				}
			}
		} else {
			NavigationManager.navegarAActivityInicio(this);
		}
	}

	// public boolean stopService() {
	// if (this.locatorService != null) {
	// this.locatorService = null;
	// }
	// return true;
	// }

	public boolean startService() {
		try {

			FetchCordinates fetchCordinates = new FetchCordinates();
			fetchCordinates.execute();
			return true;
		} catch (Exception error) {
			return false;
		}

	}

	  public AlertDialog CreateAlert(String title, String message) {
	        AlertDialog alert = new AlertDialog.Builder(this).create();
	 
	        alert.setTitle(title);
	 
	        alert.setMessage(message);
	 
	        return alert;
	 
	    }
	
	
	
	@SuppressLint("ParserError")
	public void consultar(View view) {

		Spinner sp = (Spinner) findViewById(R.id.spinner1);

		Double metros = Double.valueOf((String) sp.getSelectedItem());
		selleciones.setMetros(metros);

		final RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);

		int idSeleccionado = rg.getCheckedRadioButtonId();

		// final RadioButton radioSexButton;

		// find the radiobutton by returned id
		CharSequence text = (CharSequence) ((RadioButton) findViewById(idSeleccionado))
				.getText();

		selleciones.setBencinaSelecionada(text.toString());

		NavigationManager.navegarActivityLista(this, selleciones, quienSoy);

	}

	public class FetchCordinates extends AsyncTask<String, Integer, String> {
		ProgressDialog progDailog = null;

		public LocationManager mLocationManager;
		public VeggsterLocationListener mVeggsterLocationListener;

		@Override
		protected void onPreExecute() {
			mVeggsterLocationListener = new VeggsterLocationListener();
			mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			mLocationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0,
					mVeggsterLocationListener);

			progDailog = new ProgressDialog(MisBencinerasActivity.this);
			progDailog.setMessage("Cargando su ubicacion...");
			progDailog.setIndeterminate(true);
			progDailog.setCancelable(true);
			progDailog.show();

		}

		protected void onPostExecute(String result) {
			progDailog.dismiss();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			while (selleciones.getLatitud() == 0.0
					&& selleciones.getLongitud() == 0.0) {

			}
			return null;
		}

		public class VeggsterLocationListener implements LocationListener {

			@Override
			public void onLocationChanged(Location location) {

				// int lat = (int) location.getLatitude(); // * 1E6);
				// int log = (int) location.getLongitude(); // * 1E6);
				// int acc = (int) (location.getAccuracy());

				String info = location.getProvider();
				try {

					// LocatorService.myLatitude=location.getLatitude();

					// LocatorService.myLongitude=location.getLongitude();

					// lati = location.getLatitude();
					// longi = location.getLongitude();

					selleciones.setLatitud(location.getLatitude());
					selleciones.setLongitud(location.getLongitude());

				} catch (Exception e) {
					progDailog.dismiss();
					Toast.makeText(getApplicationContext(),
							"No es posible obtener su ubicacion",
							Toast.LENGTH_LONG).show();
				}

			}

			@Override
			public void onProviderDisabled(String provider) {
				Log.i("OnProviderDisabled", "OnProviderDisabled");
			}

			@Override
			public void onProviderEnabled(String provider) {
				Log.i("onProviderEnabled", "onProviderEnabled");
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				Log.i("onStatusChanged", "onStatusChanged");

			}

		}

	}

}