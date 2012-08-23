package cl.mobilLoyalty.bencineras.utils;

public class Utiles {
	
	private static String IP_SERVER="bencineras.ws.mobilloyalty.cl/mobilloyalty";
//	private static String PORT_SERVER="8080";
	public static String END_POINT_FIDELIZACION="http://"+IP_SERVER+"/fidelizacion/acceso/";
	public static String END_POINT_BENCINAS_CERRCANAS="http://"+IP_SERVER+"/MisBencinerasServer/cercana/";
//	public static String END_POINT_FIDELIZACION="http://"+IP_SERVER+":"+PORT_SERVER+"/fidelizacion/acceso/";
//	public static String END_POINT_BENCINAS_CERRCANAS="http://"+IP_SERVER+":"+PORT_SERVER+"/MisBencinerasServer/cercana/";
	
	public static final String SISTEMA_TEMPORALMENTE_FUERA_DE_LINEA = "Sistema Temporalmente Fuera de Linea!";
	public static final String NO_SE_HAN_ENCONTRADO_BENCINERAS_CERCANAS = "no se han encontrado Bencineras cercanas!";

}
