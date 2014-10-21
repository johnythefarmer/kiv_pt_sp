package cz.chmelokvas.brewery;

import java.util.ArrayList;
import java.util.List;

import cz.chmelokvas.util.KeyPriorityQueue;
import cz.chmelokvas.util.Route;

public abstract class Stock extends TransportNode {
	
	/** Atribut konstanty stavu skladu */
	protected int state;
	
	/** Seznam prijatych objednavek */
	protected List<Order> orders = new ArrayList<Order>();
	
	/** Atribut konstanty auto */
	protected List<Car> garage;
	
	/**Pole zakazniku daneho skladu. Na nultem indexu lezi nase instance*/
	protected TransportNode[] customers;
	
	/** pole vzdalenosti do ostatnich vrcholu*/
	protected float[] d;
	
	/**pole predchudcu*/
	protected int[] p;
	
	
	
	



	public int getState() {
		return state;
	}



	public void setState(int state) {
		this.state = state;
	}


	public void recieveOrder(Order o){
		System.out.println("Prijata objednavka od hospody " + o.getPub());
		orders.add(o);
	}


	public List<Car> getGarage() {
		return garage;
	}



	public void setGarage(List<Car> garage) {
		this.garage = garage;
	}



	public TransportNode[] getCustomers() {
		return customers;
	}



	public void setCustomers(TransportNode[] customers) {
		this.customers = customers;
	}



	public float[] getD() {
		return d;
	}



	public void setD(float[] d) {
		this.d = d;
	}



	public int[] getP() {
		return p;
	}



	public void setP(int[] p) {
		this.p = p;
	}



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
				if(c.nodes[vId].provider != null && !c.nodes[vId].provider.equals(this) && !c.nodes[vId].equals(this)){
					continue;
				}
				
				float dist = d[c.nodes[u].idProv] + v.getDistance();
				
				if(dist < d[c.nodes[vId].idProv]){
					d[c.nodes[vId].idProv] = dist;
					p[c.nodes[vId].idProv] = u;
					queue.add(d[c.nodes[vId].idProv], vId);
				}
			}
		}
	}
	
	public abstract void checkTimeEvents();
	
}
