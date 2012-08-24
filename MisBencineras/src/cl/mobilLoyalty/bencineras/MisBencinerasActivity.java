package cl.mobilLoyalty.bencineras;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import cl.mobilLoyalty.bencineras.bean.Bencinas;
import cl.mobilLoyalty.bencineras.bean.GeoReferencia;
import cl.mobilLoyalty.bencineras.bean.QuienSoy;
import cl.mobilLoyalty.bencineras.bean.Region;
import cl.mobilLoyalty.bencineras.bean.ServiCentro;
import cl.mobilLoyalty.bencineras.logic.AppLogic;
import cl.mobilLoyalty.bencineras.logic.CustomComparator;
import cl.mobilLoyalty.bencineras.utils.Utiles;

public class MisBencinerasActivity extends Activity {

	private QuienSoy quienSoy;
	private AppLogic resultadoBusqueda;
	Intent locatorService = null;
	Button searchBtn = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		quienSoy = NavigationManager.getQuienSoy(this);
		Serializable selleciones2 = NavigationManager.getSelleciones(this);

		if (selleciones2 != null && selleciones2 instanceof AppLogic) {

			resultadoBusqueda = (AppLogic) selleciones2;

		} else if (selleciones2 == null) {
			resultadoBusqueda = new AppLogic();
			resultadoBusqueda.setLatitud(0.0);
			resultadoBusqueda.setLongitud(0.0);
		}

		if (quienSoy != null && !quienSoy.getKey().equals("")) {
			setContentView(R.layout.main);
			searchBtn = (Button) findViewById(R.id.button1);

			if (resultadoBusqueda.getBencineras() == null
					|| resultadoBusqueda.getBencineras().isEmpty()) {
				// startService();

				if (!startService()) {
					CreateAlert("Error!",
							"El servicio de Ubicacion no puede ser iniciado");
				}
			} else {

				pintarSpinner();

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

		Spinner spMetros = (Spinner) findViewById(R.id.spinnerMetros);

		Double metros = Double.valueOf((String) spMetros.getSelectedItem());

		Spinner spCombustible = (Spinner) findViewById(R.id.spinnerCombustible);

		String combustible = (String) spCombustible.getSelectedItem();

		Spinner spSc = (Spinner) findViewById(R.id.spinnerServicentros);

		String servicento = (String) spSc.getSelectedItem();

		resultadoBusqueda.setMetros(metros);
		resultadoBusqueda.setBencinaSelecionada(combustible);
		resultadoBusqueda.setServicentro(servicento);

		RegistraConsultaWs registraConsultaWs = new RegistraConsultaWs();

		registraConsultaWs.execute("");

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
			// /trx/{latitud}/{longitud}/{ultanaje}/{empresa}/{key}
			// secretaria
			String URL = Utiles.END_POINT_BENCINAS_CERRCANAS + "trx/"
					+ resultadoBusqueda.getLatitud() + "/"
					+ resultadoBusqueda.getLongitud() + "/"
					+ resultadoBusqueda.getBencinaSelecionada() + "/"
					+ resultadoBusqueda.getServicentro() + "/"
					+ quienSoy.getKey();

			HttpClient httpclient = new DefaultHttpClient();

			URL = URL.replaceAll(" ", "%20");

			// Prepare a request object
			HttpGet httpget = new HttpGet(URL);

			try {
				httpclient.execute(httpget);

				return true;

			} catch (ClientProtocolException e) {
				Log.e("ClientProtocolException", e.getMessage());
				return false;
			} catch (IOException e) {
				Log.e("IOException", e.getMessage());
				return false;
			}
		}

	}

	/**
	 * 
	 * @author Administrador
	 * 
	 */
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

		/**
		 * 
		 */
		protected void onPostExecute(String result) {
			progDailog.dismiss();

			/**
			 * YA CON LAS COORDENADAS BUSCAMOS TODAS LAS BENCINERAS CERCANAS
			 */

			CallWs callWs = new CallWs();

			callWs.execute("");

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			while (resultadoBusqueda.getLatitud() == 0.0
					&& resultadoBusqueda.getLongitud() == 0.0) {

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

					resultadoBusqueda.setLatitud(location.getLatitude());
					resultadoBusqueda.setLongitud(location.getLongitude());

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

		/**
		 * Tread que recupera resultados desde la respuesta REALIZA CONSULTA POR
		 * TODAS LAS BENCINERAS A 7000 METROS
		 * 
		 * @author Sebastian Retamal
		 * 
		 */
		public class CallWs extends
				AsyncTask<String, Float, ArrayList<Bencinas>> {

			
			ProgressDialog progDailog = null;
			private boolean error = false;

			@Override
			protected void onPreExecute() {
				progDailog = new ProgressDialog(MisBencinerasActivity.this);
				progDailog.setMessage("Buscando Bencineras...");
				progDailog.setIndeterminate(true);
				progDailog.setCancelable(true);
				progDailog.show();
			}

			protected ArrayList<Bencinas> doInBackground(String... urls) {

				// pc seba wireless
				// String URL =
				// /cercana/{latitud}/{longitud}/{key}
				// + urls[0] + "/" + urls[1] + "/" + urls[2] + "/" + urls[3];
				// secretaria
				String URL = Utiles.END_POINT_BENCINAS_CERRCANAS + "cercana/"
						+ resultadoBusqueda.getLatitud() + "/"
						+ resultadoBusqueda.getLongitud() + "/"
						+ quienSoy.getKey();

				// Execute the request
				HttpResponse response;
				String result = null;
				ArrayList<Bencinas> arrayList = null;

				try {

					HttpClient httpclient = new DefaultHttpClient();

					URL = URL.replaceAll(" ", "%20");

					// Prepare a request object
					HttpGet httpget = new HttpGet(URL);

					response = httpclient.execute(httpget);

					// Get hold of the response entity
					HttpEntity entity = response.getEntity();

					if (entity != null) {

						// A Simple JSON Response Read
						InputStream instream = entity.getContent();
						result = convertStreamToString(instream);
						// Log.i("prediccion", result);

						// A Simple JSONObject Creation
						JSONObject json = new JSONObject(result);

						arrayList = parseJSONBencineras(json);

						instream.close();
					}

				} catch (ClientProtocolException e) {
					Log.e("ClientProtocolException", e.getMessage());
					error = true;
				} catch (IOException e) {
					Log.e("IOException", e.getMessage());
					error = true;
				} catch (JSONException e) {
					Log.e("JSONException", e.getMessage());
					error = true;
				} catch (RuntimeException e) {
					Log.e("RuntimeException", e.getMessage());
					error = true;
				}
				return arrayList;
			}

			/**
			 * 
			 * @param json
			 * @return
			 * @throws JSONException
			 */
			private ArrayList<Bencinas> parseJSONBencineras(JSONObject json)
					throws JSONException {

				ArrayList<Bencinas> arrayList = new ArrayList<Bencinas>();

				JSONArray jsonServArray = json.getJSONArray("servicios");

				for (int i = 0; i < jsonServArray.length(); i++) {
					Bencinas bencina = new Bencinas();

					JSONObject row = jsonServArray.getJSONObject(i);
					bencina.setDescripcion(row.getString("descripcion"));
					bencina.setPrecios(Float.valueOf(row.getString("precios")));

					bencina.setFechaUlmtimaModificacion(new Timestamp(row
							.getLong("fechaUlmtimaModificacion")));

					ServiCentro serviCentro = new ServiCentro();
					JSONObject jsonObjectSC = row.getJSONObject("serviCentro");
					serviCentro.setDireccion(jsonObjectSC
							.getString("direccion"));
					serviCentro.setEmpresa(jsonObjectSC.getString("empresa"));

					bencina.setDistancia(Double.valueOf(jsonObjectSC
							.getString("distancia")));

					GeoReferencia geoReferencia = new GeoReferencia();
					JSONObject jsonObjectGR = jsonObjectSC
							.getJSONObject("geoRef");

					geoReferencia.setLatitud(Float.valueOf(jsonObjectGR
							.getString("latitud")));
					geoReferencia.setLongitud(Float.valueOf(jsonObjectGR
							.getString("longitud")));

					Region region = new Region();
					JSONObject jsonObjectRegion = jsonObjectSC
							.getJSONObject("region");
					region.setNombre(jsonObjectRegion.getString("nombre"));

					serviCentro.setRegion(region);
					serviCentro.setGeoRef(geoReferencia);

					bencina.setServiCentro(serviCentro);

					arrayList.add(bencina);

				}

				return arrayList;

			}

			/**
			 * 
			 * @param is
			 * @return
			 */
			private String convertStreamToString(InputStream is) {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
				} catch (IOException e) {
					Log.e("IOException", e.getMessage());
					error = true;
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						Log.e("IOException", e.getMessage());
						error = true;
					}
				}
				return sb.toString();
			}

			@Override
			protected void onPostExecute(ArrayList<Bencinas> resp) {

				progDailog.dismiss();

				if (error == false) {
					if (resp == null || resp.isEmpty()) {
						resultadoBusqueda.setBencineras(resp);
						Toast.makeText(MisBencinerasActivity.this,
								Utiles.NO_SE_HAN_ENCONTRADO_BENCINERAS_CERCANAS,
								Toast.LENGTH_LONG).show();

					} else {
						/**
						 * si no es bacio orderno
						 */
						Collections.sort(resp, new CustomComparator());
						resultadoBusqueda.setBencineras(resp);
					}
				} else {
					resultadoBusqueda.setBencineras(resp);
					Toast.makeText(MisBencinerasActivity.this,
							Utiles.SISTEMA_TEMPORALMENTE_FUERA_DE_LINEA,
							Toast.LENGTH_LONG).show();
				}
				pintarSpinner();
			}

		}

	}

	public void pintarSpinner() {

		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				resultadoBusqueda.getServicentros());

		final Spinner cmbOpciones = (Spinner) findViewById(R.id.spinnerServicentros);

		adaptador
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		cmbOpciones.setAdapter(adaptador);

	}

}