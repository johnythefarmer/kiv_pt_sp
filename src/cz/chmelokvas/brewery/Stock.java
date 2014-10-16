package cz.chmelokvas.brewery;

import java.util.List;

import cz.chmelokvas.util.Controller;
import cz.chmelokvas.util.KeyPriorityQueue;
import cz.chmelokvas.util.Route;

public class Stock extends TransportNode {
	Controller c;
	
	
	/** Atribut konstanty stavu skladu */
	protected int state;
	
	/** Atribut konstanty objednavka */
	protected Order order;
	
	/** Atribut konstanty auto */
	protected List<Car> garage;
	
	/**Pole zakazniku daneho skladu. Na nultem indexu lezi nase instance*/
	protected TransportNode[] customers;
	
	/** pole vzdalenosti do ostatnich vrcholu*/
	protected float[] d;
	
	/**pole predchudcu*/
	protected int[] p;
	
	public void calculateShortestPaths(){
		for(int i = 0; i < d.length; i++){
			d[i] = Float.MAX_VALUE;
		}
		
		KeyPriorityQueue<Integer> queue = new KeyPriorityQueue<Integer>();
		
		d[0] = 0;
		queue.add(d[0],idCont);
		
		while(!queue.isEmpty()){
			int u = queue.poll();
			
			for(Route v : c.nodes[u].routes){
				int vId = v.getValue();
				
				if(!c.nodes[vId].provider.equals(this) && !c.nodes[vId].equals(this))continue;
				
				float dist = d[c.nodes[u].idProv] + v.getDistance();
				
				if(dist < d[c.nodes[vId].idProv]){
					d[c.nodes[vId].idProv] = dist;
					p[c.nodes[vId].idProv] = u;
					queue.add(d[c.nodes[vId].idProv], vId);
				}
			}
		}
	}
	
}
