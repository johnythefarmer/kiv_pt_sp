package cz.chmelokvas.brewery;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import cz.chmelokvas.util.Controller;
import cz.chmelokvas.util.Logger;

public abstract class Stock extends TransportNode {
	
	/** Atribut konstanty stavu skladu */
	protected int state;
	
	/** Utilita pro obstarani vystupu do souboru a prikazove radky */
	protected Logger logger = Logger.getInstance();
	
	/**
	 * Komparator podle vzdalenosti k nam
	 */
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
	
	/** Seznam zpracovavanych objednavek */
	protected SortedSet<Order> beingPrepared = new TreeSet<Order>(cmp);
	
	/** Atribut konstanty auto */
	protected List<Car> garage;
	
	/** Vrati seznam zpracovavanych objednavek */
	public SortedSet<Order> getBeingPrepared() {
		return beingPrepared;
	}

	/**Pole zakazniku daneho skladu. Na nultem indexu lezi nase instance*/
	protected List<TransportNode> customers;
	
	/** pole vzdalenosti do ostatnich vrcholu*/
	protected float[][] d;
	
	/**pole predchudcu*/
	protected int[][] p;
	
	/**
	 * Pocet prazdnych sudu v prekladisti.<br>
	 */
	protected int empty;
	
	/**
	 * Pocet plnych sudu v prekladisti.<br>
	 * V pripade pivovaru je to pocet vyrobeneho piva (v hektolitrech)
	 */
	protected int full;
	
	
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

	/**
	 * Prijme objednavku
	 * @param o	objednavka
	 */
	public void recieveOrder(Order o){
		logger.log(o.getTime(), 3, this + ": prijata objednavka od hospody " + o.getPub());
		orders.add(o);
	}
	
	/**
	 * Doruci objednavku
	 * @param o	objednavka
	 * @param c	auto, ktere objednavku dorucilo
	 */
	public void deliverOrder(Order o, Car c){
		Time finished = c.getCurrentInstruction().getFinished();
		if(finished.value() > o.getTime().getTimeAfterMinutes(60*24).value()){
			Controller.c.deliveredLate++;
			logger.log(c.getCurrentInstruction().getFinished(), 1, c + " nestihlo dorucit vcas objednavku " + o);
		}
		
		if(beingPrepared.contains(o)){
			logger.log(finished, 2, this + ": vyrizena " + o);
			beingPrepared.remove(o);
			Pub p = o.getPub();
			if(o.equals(p.getTodayOrder())){
				p.setTodayOrder(null);
			}else if(o.equals(p.getYesterdayOrder())){
				p.setYesterdayOrder(null);
			}
			
			if(p.isTank()){
				this.c.deliveredHL += o.getAmount();
			}else{
				this.c.deliveredBarrels += o.getAmount();
			}
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


	/**
	 * Vrati distancni matici
	 * @return	distancni matice
	 */
	public float[][] getD() {
		return d;
	}

	/**
	 * Nastavi distancni matici
	 * @param d	distancni matice
	 */
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


	/**
	 * Vrati matici predchudcu
	 * @return	matice predchudcu
	 */
	public int[][] getP() {
		return p;
	}


	/**
	 * Nastavi matici predchudcu
	 * @param p	matice predchudcu
	 */
	public void setP(int[][] p) {
		this.p = p;
	}
	
	/**
	 * Provede mnozinu operaci, ktere se za dany krok udelaly
	 */
	public abstract void checkTimeEvents();
	
}
