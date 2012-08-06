/**
 * 
 */
package cl.mobilLoyalty.bencineras.logic;

import java.util.Comparator;

import cl.mobilLoyalty.bencineras.bean.Bencinas;

/**
 * @author Administrador
 *
 */
public class CustomComparator implements Comparator<Bencinas> {

	@Override
	public int compare(Bencinas lhs, Bencinas rhs) {
		  return lhs.getPrecios().compareTo(rhs.getPrecios());
	}

}
