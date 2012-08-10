/**
 * 
 */
package cl.mobilLoyalty.bencineras;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cl.mobilLoyalty.bencineras.bean.Bencinas;
import cl.mobilLoyalty.bencineras.bean.GeoReferencia;
import cl.mobilLoyalty.bencineras.bean.PtoDesdeHasta;
import cl.mobilLoyalty.bencineras.bean.QuienSoy;
import cl.mobilLoyalty.bencineras.bean.Region;
import cl.mobilLoyalty.bencineras.bean.ServiCentro;
import cl.mobilLoyalty.bencineras.logic.AppLogic;
import cl.mobilLoyalty.bencineras.logic.CustomComparator;

/**
 * @author Administrador
 * 
 */
public class ListaActivity extends Activity {
	private QuienSoy quienSoy;
	private AppLogic resultadoBusqueda;
	private ListView lstOpciones;
	private PtoDesdeHasta seleccion;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista);
		lstOpciones = (ListView) findViewById(R.id.listView1);

		// asigno el valor por referencia

		Serializable selleciones2 = NavigationManager.getSelleciones(this);

		// asigno el key del usuario
		quienSoy = NavigationManager.getQuienSoy(this);

		if (selleciones2 instanceof AppLogic) {
			resultadoBusqueda = (AppLogic) selleciones2;
		}

		// si tra bencineras o el resultado de la busqueda entonces no debe
		// realizar la busqueda en el ws

		// realizo la llamada al web service

		CallWs callWs;
		String[] consult;

		if (resultadoBusqueda.getBencineras() == null
				|| resultadoBusqueda.getBencineras().isEmpty()) {

			callWs = new CallWs();
			callWs.context = this;
			// callWs.lista = R.layout.listitem_2_item;
			// callWs.lista = R.layout.listitem_2_item;
			consult = new String[4];
			consult[0] = resultadoBusqueda.getLatitud().toString();
			consult[1] = resultadoBusqueda.getLongitud().toString();
			consult[2] = resultadoBusqueda.getBencinaSelecionada();
			consult[3] = resultadoBusqueda.getMetros().toString();
			callWs.execute(consult);

		} else {
			actualizaLista();
		}

		lstOpciones.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Bencinas bencinas = resultadoBusqueda.getBencineras().get(
						position);

				seleccion = new PtoDesdeHasta();

				seleccion.setBencinera(bencinas);
				seleccion.setLatDesde(resultadoBusqueda.getLatitud());
				seleccion.setLongDesde(resultadoBusqueda.getLongitud());

				eleccion("Mostrar en mapa");
				// Toast.makeText(
				// getApplicationContext(),
				// bencinas.getServiCentro().getEmpresa() + " Precio: $"
				// + bencinas.getPrecios(), Toast.LENGTH_LONG)
				// .show();
			}
		});

	}

	public void volver(View view) {
		resultadoBusqueda.setBencineras(null);
		NavigationManager.navegarAActivityPrincipal(this, resultadoBusqueda,
				quienSoy);

	}

	public void verMapa() {

		NavigationManager.navegarActivityMapa(this, seleccion,
				resultadoBusqueda,quienSoy);

	}

	private void actualizaLista() {
		// actualizo los titulos

		AdaptadorPrediccion adaptador = new AdaptadorPrediccion(this,
				R.layout.listitem_2_item, resultadoBusqueda.getBencineras());

		lstOpciones.setAdapter(adaptador);
	}

	public void eleccion(String cadena) {
		// se prepara la alerta creando nueva instancia
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		// agrego el titulo
		alertbox.setTitle(cadena);
		// seleccionamos la cadena a mostrar
		alertbox.setMessage(seleccion.getBencinera().getServiCentro()
				.getEmpresa()
				+ " Precio: $"
				+ Math.round(seleccion.getBencinera().getPrecios()));
		// elegimos un positivo SI y creamos un Listener
		alertbox.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			// Funcion llamada cuando se pulsa el boton Si
			public void onClick(DialogInterface arg0, int arg1) {
				verMapa();
			}
		});

		// elegimos un positivo NO y creamos un Listener
		alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
			// Funcion llamada cuando se pulsa el boton No
			public void onClick(DialogInterface arg0, int arg1) {
				// mensaje("Pulsado el botón NO");

			}
		});
		// mostramos el alertbox
		alertbox.show();
	}

	/**
	 * Tread que recupera resultados desde la respuesta
	 * 
	 * @author Sebastian Retamal
	 * 
	 */
	public class CallWs extends AsyncTask<String, Float, ArrayList<Bencinas>> {

		ProgressDialog progDailog = null;
		Activity context;

		@Override
		protected void onPreExecute() {
			progDailog = new ProgressDialog(ListaActivity.this);
			progDailog.setMessage("Buscando Bencineras...");
			progDailog.setIndeterminate(true);
			progDailog.setCancelable(true);
			progDailog.show();
		}

		protected ArrayList<Bencinas> doInBackground(String... urls) {

			// pc seba wireless
			// String URL =
			// "http://10.130.30.39:8080/MisBencinerasServer/cercana/cercana/"
			// + urls[0] + "/" + urls[1] + "/" + urls[2] + "/" + urls[3];
			// secretaria
			String URL = "http://10.130.30.39:8080/MisBencinerasServer/cercana/cercana/"
					+ urls[0]
					+ "/"
					+ urls[1]
					+ "/"
					+ urls[2]
					+ "/"
					+ urls[3]
					+ "/" + quienSoy.getKey();

			HttpClient httpclient = new DefaultHttpClient();

			URL = URL.replaceAll(" ", "%20");

			// Prepare a request object
			HttpGet httpget = new HttpGet(URL);

			// Execute the request
			HttpResponse response;
			String result = null;
			ArrayList<Bencinas> arrayList = null;

			try {
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				serviCentro.setDireccion(jsonObjectSC.getString("direccion"));
				serviCentro.setEmpresa(jsonObjectSC.getString("empresa"));

				GeoReferencia geoReferencia = new GeoReferencia();
				JSONObject jsonObjectGR = jsonObjectSC.getJSONObject("geoRef");

				geoReferencia.setLatitud(Float.valueOf(jsonObjectGR
						.getString("latitud")));
				geoReferencia.setLongitud(Float.valueOf(jsonObjectGR
						.getString("longitud")));

				Region region = new Region();
				JSONObject jsonObjectRegion = jsonObjectSC
						.getJSONObject("region");
				region.setNombre(jsonObjectRegion.getString("nombre"));

				bencina.setDistancia(Double.valueOf(row.getString("distancia")));

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
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
		}

		@Override
		protected void onPostExecute(ArrayList<Bencinas> resp) {

			progDailog.dismiss();

			if (resp == null || resp.isEmpty()) {
				resultadoBusqueda.setBencineras(resp);
				Toast.makeText(
						ListaActivity.this,
						"no se han encontrado Bencineras cercanas, intente a una mayor distancia.!",
						Toast.LENGTH_LONG).show();
			} else {
				/**
				 * si no es bacio orderno
				 */
				Collections.sort(resp, new CustomComparator());
				resultadoBusqueda.setBencineras(resp);
				actualizaLista();
			}
		}

	}

	/**
	 * 
	 * @author Administrador
	 * 
	 */
	public class AdaptadorPrediccion extends ArrayAdapter {

		Activity context;

		@SuppressWarnings("unchecked")
		public AdaptadorPrediccion(Activity context2, int simpleListItem1,
				List<Bencinas> bencineras) {
			super(context2, simpleListItem1, bencineras);
			this.context = context2;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = context.getLayoutInflater();

			View item1 = inflater.inflate(R.layout.listitem_2_item, null);

			TextView empresa = (TextView) item1
					.findViewById(R.id.textViewEmpresa);
			empresa.setText(resultadoBusqueda.getBencineras().get(position)
					.getServiCentro().getEmpresa());

			TextView precio = (TextView) item1
					.findViewById(R.id.textViewPrecio);
			precio.setText(String.valueOf(Math.round(resultadoBusqueda
					.getBencineras().get(position).getPrecios())));

			TextView metros = (TextView) item1
					.findViewById(R.id.textViewMetros);
			metros.setText(String.valueOf(Math.round(Math
					.round(resultadoBusqueda.getBencineras().get(position)
							.getDistancia()))));

			TextView ultanaje = (TextView) item1
					.findViewById(R.id.textViewCombus);
			ultanaje.setText(resultadoBusqueda.getBencineras().get(position)
					.getDescripcion());

			TextView direccion = (TextView) item1
					.findViewById(R.id.textViewDir);

			direccion.setText(resultadoBusqueda.getBencineras().get(position)
					.getServiCentro().getDireccion());

			return (item1);
		}

	}
}
