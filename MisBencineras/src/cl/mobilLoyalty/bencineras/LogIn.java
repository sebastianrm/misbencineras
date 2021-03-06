/**
 * 
 */
package cl.mobilLoyalty.bencineras;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import cl.mobilLoyalty.bencineras.bean.QuienSoy;
import cl.mobilLoyalty.bencineras.logic.Filemanager;
import cl.mobilLoyalty.bencineras.utils.Utiles;

/**
 * @author Sebastian Retamal
 * 
 */
public class LogIn extends Activity {

	private QuienSoy quienSoy;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		quienSoy = NavigationManager.getQuienSoy(this);
		setContentView(R.layout.login);
	}
	
	
	@SuppressLint("ParserError")
	public void registrar(View view) {

		if (quienSoy.getKey() != null && !quienSoy.getKey().equals("")) {
			// si conmtiene la key entonces salta al incio de forma inmediata
			NavigationManager.navegarAActivityPrincipal(this, null, quienSoy);

		} else {
			// si el archivo properties no contiene la key entonces se debe
			// registrar
			NavigationManager.navegarAActivityRegistar(this, quienSoy);

		}
	}
	

	@SuppressLint("ParserError")
	public void enviar(View view) {

		CallWs callWs = new CallWs();
		String[] consult;

		EditText mailtxt = (EditText) findViewById(R.id.editTextMail);
		EditText passtxt = (EditText) findViewById(R.id.editTextPass);

		if (mailtxt.getText().toString().equals("")) {
			Toast.makeText(LogIn.this,
					"El correo electronicos no debe ser vacio",
					Toast.LENGTH_LONG).show();
		} else if (passtxt.getText().toString().equals("")) {
			Toast.makeText(LogIn.this, "la contraseņa no coincide",
					Toast.LENGTH_LONG).show();
		} else {

			consult = new String[2];
			consult[0] = mailtxt.getText().toString();
			consult[1] = passtxt.getText().toString();
			callWs.execute(consult);

		}

	}

	/**
	 * 
	 * @author Administrador
	 * 
	 */
	public class CallWs extends AsyncTask<String, Float, String> {

		ProgressDialog progDailog = null;

		@Override
		protected void onPreExecute() {
			progDailog = new ProgressDialog(LogIn.this);
			progDailog.setMessage("Iniciando Session...");
			progDailog.setIndeterminate(true);
			progDailog.setCancelable(true);
			progDailog.show();
		}
		
		
		protected String doInBackground(String... urls) {

			// http://localhost:8080/fidelizacion/registrar/iniciosession/mail/password/nombreApp/AmbienteApp
			String URL = Utiles.END_POINT_FIDELIZACION+"iniciosession/"
					+ urls[0]
					+ "/"
					+ urls[1]
					+ "/"
					+ quienSoy.getNombre_app()
					+ "/" + quienSoy.getAmbiente();

			HttpClient httpclient = new DefaultHttpClient();

			URL = URL.replaceAll(" ", "%20");

			// Prepare a request object
			HttpGet httpget = new HttpGet(URL);

			// Execute the request
			HttpResponse response;
			String result = null;

			try {
				response = httpclient.execute(httpget);

				// Get hold of the response entity
				HttpEntity entity = response.getEntity();

				if (entity != null) {

					// A Simple JSON Response Read
					InputStream instream = entity.getContent();
					result = convertStreamToString(instream);

					instream.close();
				}

			} catch (ClientProtocolException e) {
				Log.e("ERROR AL INICIAR SESSION ", e.getMessage());
			} catch (IOException e) {
				Log.e("ERROR AL INICIAR SESSION ", e.getMessage());
			}
			return result;
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
					sb.append(line.trim());
				}
			} catch (IOException e) {
				Log.e("ERROR AL CONVERTIR RESPUESTA EN STRING ", e.getMessage());
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					Log.e("ERROR AL INICIAR SESSION ", e.getMessage());
				}
			}
			return sb.toString();
		}

		@Override
		protected void onPostExecute(String resp) {

			progDailog.dismiss();

			if (resp == null ) {

				Toast.makeText(LogIn.this,
						"SISTEMA TEMPORALMENTE FUERA DELINEA",
						Toast.LENGTH_LONG).show();
			} else {
				/**
				 * si no es vacio orderno
				 */
				quienSoy.setKey(resp.trim());

				actualizaLista();
			}
		}

	}

	public void actualizaLista() {

		/**
		 * guardamos el archivo
		 * 
		 */

		if (quienSoy.getKey().length() >= 6) {
			Filemanager.guardar(quienSoy, this);
			NavigationManager.navegarAActivityPrincipal(this, null, quienSoy);
		} else {
			EditText passtxt = (EditText) findViewById(R.id.editTextPass);
			passtxt.setText("");
			
			Toast.makeText(LogIn.this, "MAIL O CONTRASEŅA INCORRECTA",
					Toast.LENGTH_LONG).show();
		}

	}

}
