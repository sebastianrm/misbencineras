/**
 * 
 */
package cl.mobilLoyalty.bencineras;

import java.io.Serializable;

import cl.mobilLoyalty.bencineras.bean.PtoDesdeHasta;
import cl.mobilLoyalty.bencineras.logic.AppLogic;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author sebastian
 * 
 */
public class NavigationManager {

	/**
	 * 
	 * @param activity
	 */
	public static void navegarAActivityPrincipal(Activity activity,AppLogic resultadoBusqueda) {
		activity.finish();
		Intent itemintent = new Intent(activity, MisBencinerasActivity.class);
		
		
		Bundle b = new Bundle();

		// adjunto el valor seleccionado anteriormente

		b.putSerializable("selleciones", resultadoBusqueda);
		
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
			PtoDesdeHasta seleccion,AppLogic resultadoBusqueda) {
		activity.finish();
		Intent itemintent = new Intent(activity, MapaBencinas.class);
		Bundle b = new Bundle();

		// adjunto el valor seleccionado anteriormente

		b.putSerializable("sellecion", seleccion);
		b.putSerializable("selleciones", resultadoBusqueda);
		
		itemintent.putExtra("android.intent.extra.INTENT", b);

		activity.startActivity(itemintent);
	}

	public static void navegarActivityLista(Activity activity,
			AppLogic selleciones) {
		activity.finish();
		Intent itemintent = new Intent(activity, ListaActivity.class);
		Bundle b = new Bundle();

		// adjunto el valor seleccionado anteriormente

		b.putSerializable("selleciones", selleciones);

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
}
