/**
 * 
 */
package cl.mobilLoyalty.bencineras;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import cl.mobilLoyalty.bencineras.bean.PtoDesdeHasta;
import cl.mobilLoyalty.bencineras.bean.QuienSoy;
import cl.mobilLoyalty.bencineras.logic.AppLogic;

/**
 * @author sebastian
 * 
 */
public class NavigationManager {

	
	public static void navegarAActivityRegistar(Activity activity, QuienSoy props) {
		activity.finish();
		Intent itemintent = new Intent(activity, RegistroActivity.class);
		
		
		Bundle b = new Bundle();

		// adjunto el valor seleccionado anteriormente

		b.putSerializable("QuienSoy", props);
		
		itemintent.putExtra("android.intent.extra.INTENT", b);
		
		activity.startActivity(itemintent);
		
	}
	public static void navegarAActivityLogIn(Activity activity) {
		activity.finish();
		Intent itemintent = new Intent(activity, LogIn.class);
		
		Bundle b = new Bundle();

		// adjunto el valor seleccionado anteriormente
		
		itemintent.putExtra("android.intent.extra.INTENT", b);
		
		activity.startActivity(itemintent);
	}
	public static void navegarAActivityInicio(Activity activity) {
		activity.finish();
		Intent itemintent = new Intent(activity, InicioActivity.class);
		
		
		Bundle b = new Bundle();

		// adjunto el valor seleccionado anteriormente

		
		itemintent.putExtra("android.intent.extra.INTENT", b);
		
		activity.startActivity(itemintent);
	}
	/**
	 * 
	 * @param activity
	 */
	public static void navegarAActivityPrincipal(Activity activity,AppLogic resultadoBusqueda,QuienSoy props) {
		activity.finish();
		Intent itemintent = new Intent(activity, MisBencinerasActivity.class);
		
		
		Bundle b = new Bundle();

		// adjunto el valor seleccionado anteriormente

		b.putSerializable("selleciones", resultadoBusqueda);
		b.putSerializable("QuienSoy", props);
		
		itemintent.putExtra("android.intent.extra.INTENT", b);
		
		activity.startActivity(itemintent);
	}

	/**
	 * 
	 * @param activity
	 * @param id1
	 * @param id2
	 */
	public static void navegarActivityMapa(Activity activity,
			PtoDesdeHasta seleccion,AppLogic resultadoBusqueda,QuienSoy quienSoy) {
		activity.finish();
		Intent itemintent = new Intent(activity, MapaBencinas.class);
		Bundle b = new Bundle();

		// adjunto el valor seleccionado anteriormente

		b.putSerializable("sellecion", seleccion);
		b.putSerializable("selleciones", resultadoBusqueda);
		b.putSerializable("QuienSoy", quienSoy);
		itemintent.putExtra("android.intent.extra.INTENT", b);

		activity.startActivity(itemintent);
	}

	public static void navegarActivityLista(Activity activity,
			AppLogic selleciones, QuienSoy props) {
		activity.finish();
		Intent itemintent = new Intent(activity, ListaActivity.class);
		Bundle b = new Bundle();

		// adjunto el valor seleccionado anteriormente

		b.putSerializable("selleciones", selleciones);
		b.putSerializable("QuienSoy", props);
		
		itemintent.putExtra("android.intent.extra.INTENT", b);

		activity.startActivity(itemintent);
	}
	
	
	public static Serializable getSelleciones(Activity activity) {
		Serializable serializable = null;
		Intent startingIntent = activity.getIntent();
		if (startingIntent != null) {
			Bundle b = startingIntent
					.getBundleExtra("android.intent.extra.INTENT");
			if (b != null) {
				serializable = b.getSerializable("selleciones");

			}
		}
		return serializable;
	}
	
	public static Serializable getSellecion(Activity activity) {
		Serializable serializable = null;
		Intent startingIntent = activity.getIntent();
		if (startingIntent != null) {
			Bundle b = startingIntent
					.getBundleExtra("android.intent.extra.INTENT");
			if (b != null) {
				serializable = b.getSerializable("sellecion");

			}
		}
		return serializable;
	}

	public static QuienSoy getQuienSoy(Activity activity) {
		
		QuienSoy prop = null;
		Intent startingIntent = activity.getIntent();
		if (startingIntent != null) {
			Bundle b = startingIntent
					.getBundleExtra("android.intent.extra.INTENT");
			if (b != null) {
				prop = (QuienSoy)b.getSerializable("QuienSoy");

			}
		}
		return prop;
	}

}
