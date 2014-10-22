package cz.chmelokvas.brewery;

import java.util.ArrayList;
import java.util.List;

import cz.chmelokvas.util.Controller;
import cz.chmelokvas.util.Route;

public class TransportNode {
	protected Controller c;
	
	protected Stock provider;
	
	/**ID pod kterym je uzel ulozen v kontroleru*/
	protected int idCont;
	
	/**ID pod kterym je uzel ulozen u dodavatele */
	protected int idProv;
	
	/**Cesty mezi uzly*/
	protected List<Route> routes = new ArrayList<Route>();
	
	/** Atribut konstanty X souradnice */
	protected double x;
	
	/** Atribut konstanty Y souradnice */
	protected double y;
	
	public List<Route> getRoutes(){
		return routes;
	}
	
	public Stock getProvider(){
		return provider;
	}
	
	public void setProvider(Stock s){
		this.provider = s;
	}
	
	public Controller getC() {
		return c;
	}

	public void setC(Controller c) {
		this.c = c;
	}
}
