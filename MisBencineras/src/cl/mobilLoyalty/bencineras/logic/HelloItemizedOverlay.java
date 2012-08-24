package cl.mobilLoyalty.bencineras.logic;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import cl.mobilLoyalty.bencineras.bean.Bencinas;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class HelloItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	Context mContext;

	public HelloItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	public HelloItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	public void addAllOverlay(ArrayList<Bencinas> bencinas, Drawable imagen) {

		Iterator<Bencinas> iterator = bencinas.iterator();

		while (iterator.hasNext()) {

			Bencinas next = iterator.next();

			GeoPoint point = new GeoPoint((int) (next.getServiCentro()
					.getGeoRef().getLatitud() * 1e6), (int) (next
					.getServiCentro().getGeoRef().getLongitud() * 1e6));

			OverlayItem overlayitem = new OverlayItem(point, next
					.getServiCentro().getEmpresa(), next.getDescripcion()
					+ " Precio: " + next.getPrecios());

			if (imagen != null) {
				overlayitem.setMarker(imagen);
				imagen.setBounds(0, 0, imagen.getIntrinsicWidth(),
						imagen.getIntrinsicHeight()); // setBounds
				boundCenterBottom(imagen); // correct shadow problem
			}

			mOverlays.add(overlayitem);
		}
		populate();
	}

	public void addAllOverlay(Bencinas bencina, Drawable imagen) {

		GeoPoint point = new GeoPoint((int) (bencina.getServiCentro()
				.getGeoRef().getLatitud() * 1e6), (int) (bencina
				.getServiCentro().getGeoRef().getLongitud() * 1e6));
		OverlayItem overlayitem = new OverlayItem(point, bencina
				.getServiCentro().getEmpresa(), bencina.getDescripcion()
				+ " Precio: " + bencina.getPrecios());
		
		if (imagen != null) {
			overlayitem.setMarker(imagen);
			imagen.setBounds(0, 0, imagen.getIntrinsicWidth(),
					imagen.getIntrinsicHeight()); // setBounds
			boundCenterBottom(imagen); // correct shadow problem
		}
		
		
		mOverlays.add(overlayitem);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}

}
