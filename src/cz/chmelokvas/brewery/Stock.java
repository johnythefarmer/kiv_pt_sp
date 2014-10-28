package cz.chmelokvas.brewery;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import cz.chmelokvas.util.Logger;

public abstract class Stock extends TransportNode {
	
	/** Atribut konstanty stavu skladu */
	protected int state;
	protected Logger logger = Logger.getInstance();
	
	protected Comparator<Order> cmp = new Comparator<Order>() {

		@Override
		public int compare(Order o1, Order o2) {
			int cmp = Float.compare(d[0][o1.getPub().idProv], d[0][o2.getPub().idProv]);
			if(cmp == 0){
				return Integer.compare(o1.getTime().value(), o2.getTime().value());
			}
			return cmp;
		}
		
	};
	
	/** Seznam prijatych objednavek */
	protected SortedSet<Order> orders = new TreeSet<Order>(cmp);
	
	protected SortedSet<Order> beingPrepared = new TreeSet<Order>(cmp);
	
	/** Atribut konstanty auto */
	protected List<Car> garage;
	
	public SortedSet<Order> getBeingPrepared() {
		return beingPrepared;
	}

	/**Pole zakazniku daneho skladu. Na nultem indexu lezi nase instance*/
	protected List<TransportNode> customers;
	
	/** pole vzdalenosti do ostatnich vrcholu*/
	protected float[][] d;
	
	/**pole predchudcu*/
	protected int[][] p;
	
	
	public Stock(){
		this.provider = this;
		this.customers = new ArrayList<TransportNode>();
		this.garage = new ArrayList<Car>();
	}
	



	public int getState() {
		return state;
	}



	public void setState(int state) {
		this.state = state;
	}


	public void recieveOrder(Order o){
//		System.out.println("Prijata objednavka od hospody " + o.getPub());
		logger.log(o.getTime(), 3, this + ": prijata objednavka od hospody " + o.getPub());
		orders.add(o);
	}
	
	public void deliverOrder(Order o, Car c){
		Time finished = c.getCurrentInstruction().getFinished();
		if(finished.value() > o.getTime().getTimeAfterMinutes(60*24).value()){
			logger.log(c.getCurrentInstruction().getFinished(), 1, c + " nestihlo dorucit vcas objednavku " + o);
//			throw new IllegalArgumentException("Nestihli jsme to!!");
		}
		
		if(beingPrepared.contains(o)){
//			System.out.println("Vyrizena objednavka " + o);
			logger.log(finished, 2, this + ": vyrizena " + o);
			beingPrepared.remove(o);
			Pub p = o.getPub();
			if(o.equals(p.getTodayOrder())){
				p.setTodayOrder(null);
			}else if(o.equals(p.getYesterdayOrder())){
				p.setYesterdayOrder(null);
			}
			/*if(beingPrepared.contains(o)){
				System.err.println("Neodebralo se");
			}*/
		}

	}


	public List<Car> getGarage() {
		return garage;
	}



	public void setGarage(List<Car> garage) {
		this.garage = garage;
	}



	public List<TransportNode> getCustomers() {
		return customers;
	}



	public void setCustomers(List<TransportNode> customers) {
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
