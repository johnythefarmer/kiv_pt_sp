package cz.chmelokvas.util;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import cz.chmelokvas.brewery.Order;
import cz.chmelokvas.brewery.Pub;
import cz.chmelokvas.brewery.Time;
import cz.chmelokvas.brewery.TransportNode;

public class Controller {
	/** Casovy krok*/
	private static final int STEP = 60;
	
	/**Nko v konecnem programu zmizi. je to jen pomocna promena pro generovani zkusebnich dat */
	public static final int N = 10;
	
	/** Vsechny dopravni uzly simulace */
	public TransportNode[] nodes = new TransportNode[N+1];
	
	/** Vsechny objednavky pro dany den	 */
	public Set<Order> todayOrders = new TreeSet<Order>();
	
	/**hlavni cas cele aplikace*/
	public Time mainTime = new Time(0,0,0);
	
	/** cas ukonceni simulace */
	public Time endTime = new Time(2,0,0);
	
	/**
	 * Prida silnici mezi dvema dopravnimi uzly
	 * @param a id prvniho uzlu u Controlleru
	 * @param b id druheho uzlu u Controlleru
	 * @param d delka silnice
	 */
	public void addRoute(int a, int b, float d){
		nodes[a].getRoutes().add(new Route(d, b));
		nodes[b].getRoutes().add(new Route(d, a));
	}
	
	/**
	 * Metoda ktera bude obstaravat celou simulaci
	 */
	public void simulate(){
		int oldDay = -1;
		while(mainTime.value() < endTime.value()){
			
			
			
			System.out.println(mainTime);
			//generovani objednavek na zacatku dne
			if(oldDay != mainTime.getDay()){
				generateOrders();
			}
		
			if(mainTime.getHour() >= 8 && mainTime.getHour() < 16){
				//Rozeslani objednavek v dany cas
				sendOrders();
			}
			
			//tady se zavola neco jako vsechno se hne o kus nebo neco podobnyho
			oldDay = mainTime.getDay();
			mainTime.addMinutes(STEP);
			/*try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
//		System.out.println(mainTime);
	}
	
	/**
	 * Pro vsechny hospody simulace vypocte objednavku pro dany den
	 */
	private void generateOrders(){
		for(TransportNode node : nodes){
			if(node instanceof Pub){
				todayOrders.add(((Pub)node).makeOrder());
			}
		}
	}
	
	/**
	 * Pokud nastal cas pro odeslani objednavky, odesle ji
	 */
	private void sendOrders(){
		for(Iterator<Order> it = todayOrders.iterator(); it.hasNext();){
			Order o = it.next();
			if(Math.abs(o.getTime().value() - mainTime.value()) < STEP){
				System.out.println(o + " byla predana prekladisti " + o.getPub().getProvider());
				o.getPub().getProvider().recieveOrder(o);
				it.remove();
			}
		}
	}
}
