package cz.chmelokvas.brewery;

import java.util.ArrayList;
import java.util.List;

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
	protected float[][] d;
	
	/**pole predchudcu*/
	protected int[][] p;
	
	
	
	



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
	
	public void deliverOrder(Order o){
		if(orders.contains(o)){
			System.out.println("Vyrizena objednavka do hospody " + o.getPub());
			orders.remove(o);
		}
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



	public float[][] getD() {
		return d;
	}



	public void setD(float[][] d) {
		for(int i = 0; i < d.length; i++){
			for(int j = 0; j < d.length; j++){
				if(i!=j && d[i][j] == 0){
					d[i][j] = Float.MAX_VALUE;
				}
			}
		}
		
		this.d = d;
	}



	public int[][] getP() {
		return p;
	}



	public void setP(int[][] p) {
		this.p = p;
	}



	public void floydWarshal(float[][] distance, int[][] pred, int n){
		for(int k = 0; k < n; k++){
			for(int i = 0; i < n; i++){
				float aji = distance[i][k];
				
				//nepocitej kdyz neexistuje cesta
				if(aji == Float.MAX_VALUE){
					continue;
				}
				
				for(int j = 0; j < n; j++){
					float c = aji + distance[k][j];
				
					if(c < distance[i][j]){
						distance[i][j] = c;
						pred[i][j] = k;
					}
				}
			}
		}
	}
	
	public abstract void checkTimeEvents();
	
}
