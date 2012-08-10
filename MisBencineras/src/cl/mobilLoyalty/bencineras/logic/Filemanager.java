/**
 * 
 */
package cl.mobilLoyalty.bencineras.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import cl.mobilLoyalty.bencineras.bean.QuienSoy;

/**
 * @author Administrador
 * 
 */
public class Filemanager {

	private static final int READ_BLOCK_SIZE = 100;
	private static final String NOMBRE_ARCHIVO = "conf.txt";

	public static boolean isExternalStorageReadOnly() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
			return true;
		}
		return false;
	}

	public static boolean isExternalStorageAvailable() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
			return true;
		}
		return false;
	}

	public void writeFile(String filename, String textfile) {
		try {
			if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
				File file = new File(Environment.getExternalStorageDirectory(),
						filename);
				OutputStreamWriter outw = new OutputStreamWriter(
						new FileOutputStream(file));
				outw.write(textfile);
				outw.close();
			}
		} catch (Exception e) {
		}
	}

	@SuppressWarnings("static-access")
	public static void guardar(QuienSoy quienSoy, Activity activity) {
		// Clase que permite grabar texto en un archivo
		OutputStreamWriter ows = null;
		try {

			ows = new OutputStreamWriter(activity.openFileOutput(
					NOMBRE_ARCHIVO, activity.MODE_PRIVATE));

			ows.write("nombre_app=" + quienSoy.getNombre_app() + "\nambiente="
					+ quienSoy.getAmbiente() + "android\nkey="
					+ quienSoy.getKey()); // Escribe en el buffer la cadena de
											// texto
			ows.flush(); // Volca lo que hay en el buffer al archivo
			ows.close(); // Cierra el archivo de texto

		} catch (IOException e) {
			Log.e("Ficheros", "ERROR AL ESCRIBIR FICHERO A MEMORIA INTERNA");
		}

		Toast.makeText(activity.getBaseContext(),
				"El archivo se ha almacenado!!!", Toast.LENGTH_SHORT).show();

	}

	public static boolean existeArchivo(Activity activity) {
		try {
			// Se lee el archivo de texto indicado
			FileInputStream fin = activity.openFileInput(NOMBRE_ARCHIVO);
		} catch (FileNotFoundException e) {
			return false;
		}
		return true;
	}

	public static QuienSoy abrir(Activity activity) {
		QuienSoy qs = new QuienSoy();

		if (existeArchivo(activity)) {
			try {
				BufferedReader fin = null;
				try {
					// Se lee el archivo de texto indicado
					fin = new BufferedReader(new InputStreamReader(
							activity.openFileInput(NOMBRE_ARCHIVO)));
				} catch (FileNotFoundException e) {
					Log.e("Ficheros", "NO EXISTE ARCHIVO");
				}

				// Se lee el archivo de texto mientras no se llegue al final de
				// él
				String strRead = "";
				while ((strRead = fin.readLine()) != null) {
					// Se lee por bloques de 100 caracteres
					// ya que se desconoce el tamaño del texto
					// Y se va copiando a una cadena de texto
					// str += strRead;

					String[] split = strRead.trim().split("=");

					if (split[0].equals("key")) {
						if (split.length >1) {
							qs.setKey(split[1].trim());
						}else{
							qs.setKey("");
						}
					} else if (split[0].equals("nombre_app")) {
						qs.setNombre_app(split[1].trim());
					} else if (split[0].equals("ambiente")) {
						qs.setAmbiente(split[1].trim());
					}

				}

				// Se muestra el texto leido en la caje de texto
				// txtTexto.setText(str);

				fin.close();

				Toast.makeText(activity.getBaseContext(),
						"El archivo ha sido cargado", Toast.LENGTH_SHORT)
						.show();
				return qs;
			} catch (IOException e) {
				Log.e("Ficheros", "ERROR AL LEER FICHERO EN MEMORIA INTERNA");
			}
		} else {
			qs.setNombre_app("MisBencineras");
			qs.setAmbiente("android");
			qs.setKey("");
			guardar(qs, activity);
		}
		return qs;

	}

}
