/**
 * 
 */
package cl.mobilLoyalty.bencineras;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import cl.mobilLoyalty.bencineras.bean.QuienSoy;
import cl.mobilLoyalty.bencineras.logic.Filemanager;

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

		quienSoy = Filemanager.abrir(this);

		if (quienSoy.getKey() != null && !quienSoy.getKey().equals("")) {
			// si conmtiene la key entonces salta al incio de forma inmediata
			NavigationManager.navegarAActivityPrincipal(this, null, quienSoy);
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
	
	@SuppressLint("ParserError")
	public void logIn(View view) {

		if (quienSoy.getKey() != null && !quienSoy.getKey().equals("")) {
			// si conmtiene la key entonces salta al incio de forma inmediata
			NavigationManager.navegarAActivityPrincipal(this, null, quienSoy);

		} else {
			// si el archivo properties no contiene la key entonces se debe
			// registrar
			NavigationManager.navegarAActivityLogIn(this,quienSoy);

		}
	}
}
