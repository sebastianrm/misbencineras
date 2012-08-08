/**
 * 
 */
package cl.mobilLoyalty.bencineras;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import cl.mobilLoyalty.bencineras.bean.QuienSoy;

/**
 * @author Sebastian Retamal
 * 
 */
public class InicioActivity extends Activity {

	private QuienSoy quienSoy;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inicio);

		quienSoy = new QuienSoy();

		if (new File(quienSoy.getNombreArchivo()).exists()) {
			// leemos el archivo
			try {
				BufferedReader fin = new BufferedReader(new InputStreamReader(
						openFileInput(quienSoy.getNombreArchivo())));

				quienSoy = convertStreamToClass(fin);
				

				fin.close();

			} catch (FileNotFoundException e) {
				Log.e(InicioActivity.class.toString(),
						"NO SE ENCUENTRA ARCHIVO", e.getCause());
			} catch (IOException e) {

				Log.e(InicioActivity.class.toString(),
						"ERROR AL ESCRIBIR FICHERO A MEMORIA INTERNA",
						e.getCause());
			}
		} else {
			// si no existe lo creamos
			try {

				OutputStreamWriter fout = new OutputStreamWriter(
						openFileOutput(quienSoy.getNombreArchivo(), Context.MODE_PRIVATE));
				
				quienSoy.setNombre_app("MisBencineras");
				quienSoy.setAmbiente("android");
				quienSoy.setKey("");
				
				fout.write("nombre_app="+quienSoy.getNombre_app()+"\nambiente="+quienSoy.getAmbiente()+"android\nkey="+quienSoy.getKey());
				fout.close();

			} catch (Exception ex) {
				Log.e("Ficheros", "ERROR AL ESCRIBIR FICHERO A MEMORIA INTERNA");
			}

		}

		if (quienSoy.getKey() != null && !quienSoy.getKey().equals("")) {
			// si conmtiene la key entonces salta al incio de forma inmediata
			Button bt = (Button) findViewById(R.id.buttonRegistar);
			bt.setText("Iniciar");
		}

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

	private QuienSoy convertStreamToClass(BufferedReader reader) {

		QuienSoy qs = new QuienSoy();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				String[] split = line.trim().split("=");

				if (split[0].equals("key")) {
					qs.setKey(split[1].trim());
				} else if (split[0].equals("nombre_app")) {
					qs.setNombre_app(split[1].trim());
				} else if (split[0].equals("ambiente")) {
					qs.setAmbiente(split[1].trim());
				}

			}
		} catch (IOException e) {
			Log.e(InicioActivity.class.toString(), "IOException", e.getCause());
		}
		return qs;
	}
}
