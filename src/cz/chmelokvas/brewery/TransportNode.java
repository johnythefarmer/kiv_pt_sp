package cz.chmelokvas.brewery;

import java.util.List;

import cz.chmelokvas.util.Route;

public class TransportNode {
	protected Stock provider;
	
	/**ID pod kterym je uzel ulozen v kontroleru*/
	protected int idCont;
	
	/**ID pod kterym je uzel ulozen u dodavatele */
	protected int idProv;
	
	/**Cesty mezi uzly*/
	protected List<Route> routes;
	
	/** Atribut konstanty X souradnice */
	protected double x;
	
	/** Atribut konstanty Y souradnice */
	protected double y;	
}
