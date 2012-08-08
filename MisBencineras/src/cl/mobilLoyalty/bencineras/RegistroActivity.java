package cl.mobilLoyalty.bencineras;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import cl.mobilLoyalty.bencineras.bean.QuienSoy;

public class RegistroActivity extends Activity {
	private QuienSoy quienSoy;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		quienSoy = NavigationManager.getProperties(this);
		setContentView(R.layout.registro);
	}
	
	
	@SuppressLint("ParserError")
	public void enviar(View view) {
		
		CallWs callWs = new CallWs();
		String[] consult;
		
		EditText mailtxt = (EditText)findViewById(R.id.editTextMail);
		EditText passtxt = (EditText)findViewById(R.id.editTextPass);
		CheckBox check = (CheckBox)findViewById(R.id.checkBoxConcenti);
		
		String checked = "";
		
		if (check.isChecked()) {
			checked = "true";
		}else{
			checked = "false";
		}
		consult = new String[3];
		consult[0] = mailtxt.getText().toString();
		consult[1] = passtxt.getText().toString();;
		consult[2] = checked;
		callWs.execute(consult);
		
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
			progDailog = new ProgressDialog(RegistroActivity.this);
			progDailog.setMessage("Registrando...");
			progDailog.setIndeterminate(true);
			progDailog.setCancelable(true);
			progDailog.show();
		}

		protected String doInBackground(String... urls) {

			// /registrar/{mail}/{password}/{confirmaEnvioMails}/{nombreApp}/{AmbienteApp}
			// secretaria
			String URL = "http://10.130.30.34:8080/fidelizacion/registrar/registrar/"
					+ urls[0] + "/" + urls[1] + "/" + urls[2] + "/" + quienSoy.getNombre_app()+"/" + quienSoy.getAmbiente();

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
					// Log.i("prediccion", result);

					instream.close();
				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		protected void onPostExecute(String resp) {

			progDailog.dismiss();
			
			if (resp == null || resp.isEmpty()) {
				
				 Toast.makeText(RegistroActivity.this,
				 "no llego nada ",
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

		Properties props = new Properties();

			// si no existe lo creamos
		try {

			OutputStreamWriter fout = new OutputStreamWriter(
					openFileOutput(quienSoy.getNombreArchivo(), Context.MODE_PRIVATE));
			fout.write("nombre_app="+quienSoy.getNombre_app()+"\nambiente="+quienSoy.getAmbiente()+"android\nkey="+quienSoy.getKey());
			fout.close();

		} catch (Exception ex) {
			Log.e("Ficheros", "ERROR AL ESCRIBIR FICHERO A MEMORIA INTERNA");
		}

		
		
		NavigationManager.navegarAActivityPrincipal(this, null,quienSoy);
		
	}
	
	
}
