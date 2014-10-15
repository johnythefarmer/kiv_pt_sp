package cz.chmelokvas.brewery;

import java.util.List;

public class Stock extends TransportNode {
	
	/** Atribut konstanty stavu skladu */
	protected int state;
	
	/** Atribut konstanty objednavka */
	protected Order order;
	
	/** Atribut konstanty auto */
	protected List<Car> garage;
	
}
