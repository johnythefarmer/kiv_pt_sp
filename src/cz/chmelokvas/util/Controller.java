package cz.chmelokvas.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.chmelokvas.brewery.Brewery;
import cz.chmelokvas.brewery.Dock;
import cz.chmelokvas.brewery.Order;
import cz.chmelokvas.brewery.Pub;
import cz.chmelokvas.brewery.Stock;
import cz.chmelokvas.brewery.Time;
import cz.chmelokvas.brewery.TransportNode;

public class Controller {
	public static Controller c;

	
	/** Casovy krok*/
	public static final int STEP = 60;
	
	/**Nko v konecnem programu zmizi. je to jen pomocna promena pro generovani zkusebnich dat */
//	public static final int N = 10;
	
	/** Vsechny dopravni uzly simulace */
//	public TransportNode[] nodes = new TransportNode[N+1];
	
	public List<TransportNode> nodes;
	
	public List<Pub> pub;
	
	public List<Dock> dock;
	
	public Brewery brewery;
	
	private final Logger logger = Logger.getInstance("output.txt");
	
	//TODO bude se nacitat z prikazovy radky
	/**
	 * Minimalni priorita vystupu:<br>
	 * <li>1 = CHYBY
	 * <li>2 = VYRIZENI OBJEDNAVKY
	 * <li>3 = PODANI OBJEDNAVKY
	 * <li>4 = POHYB AUT
	 * <li>5 = ZASOBOVANI DO PREKLADIST
	 * <li>6 = PRODUKCE, VYTVARENI AUT
	 */
	public int minLogPriority = 6;
	
	public int deliveredBarrels;
	public int deliveredHL;
	public int deliveredLate;
	
	/** Vsechny objednavky pro dany den	 */
	public List<Order> todayOrders = new ArrayList<Order>();
	
	/**hlavni cas cele aplikace*/
	public Time mainTime = new Time(0,0,0);
	
	/** cas ukonceni simulace */
	public Time endTime = new Time(1,0,0);
	
	public Controller(List<Pub> pub, List<Dock> dock, Brewery brewery){
		this.pub = pub;
		this.dock = dock;
		this.brewery = brewery;
		
		nodes = new ArrayList<TransportNode>();
		nodes.add(brewery);
		nodes.addAll(dock);
		nodes.addAll(pub);
		
		for(TransportNode n:nodes){
			n.setC(this);
		}
	}
	
	/**
	 * Prida silnici mezi dvema dopravnimi uzly
	 * @param a id prvniho uzlu u Controlleru
	 * @param b id druheho uzlu u Controlleru
	 * @param d delka silnice
	 */
	public void addRoute(int a, int b, float d){
		TransportNode nA = nodes.get(a);
		TransportNode nB = nodes.get(b);
		nA.getRoutes().add(new Route(d, b));
		nB.getRoutes().add(new Route(d, a));
		
		if(nA.getProvider().equals(nB.getProvider())){
			Stock s = nA.getProvider();
			int tmpA = nA.getIdProv();
			int tmpB = nB.getIdProv();
			s.getD()[tmpA][tmpB] = d;
			s.getD()[tmpB][tmpA] = d;
		}
		
		//TODO pridat vkladani hran pro pivovar
	}
	
	/**
	 * Metoda ktera bude obstaravat celou simulaci
	 */
	public void simulate(){
		for(Dock d:dock){
			d.floydWarshal(d.getD(), d.getP(), d.getD().length);
		}
		
		int oldDay = -1;
		while(mainTime.value() < endTime.value()){
			
			
			
			System.out.println("---" + mainTime + "---");
			//generovani objednavek na zacatku dne
			if(oldDay != mainTime.getDay()){
				logger.log(mainTime, 6, "Novy den, cas na generovani objednavek.");
				generateOrders();
			}
		
			checkTime();
			
			if(mainTime.getHour() >= 8 && mainTime.getHour() < 16){
				//Rozeslani objednavek v dany cas
				sendOrders();
			}
			
			logger.printEvents();
			
			oldDay = mainTime.getDay();
			mainTime.addMinutes(STEP);
			
			System.out.println("\n\n");
		}
		
		logger.printFinalStatistics();
		logger.close();
	}
	
	/**
	 * Pro vsechny hospody simulace vypocte objednavku pro dany den
	 */
	private void generateOrders(){
		
		for(Pub p : pub){
			todayOrders.add(p.makeOrder());
		}
	}
	
	private void checkTime(){
		for(Dock d: dock){
			d.checkTimeEvents();
		}
		brewery.checkTimeEvents();
	}
	
	/**
	 * Pokud nastal cas pro odeslani objednavky, odesle ji
	 */
	private void sendOrders(){
		for(Iterator<Order> it = todayOrders.iterator(); it.hasNext();){
			Order o = it.next();
			if(Math.abs(o.getTime().value() - mainTime.value()) < STEP){
				if(o.getPub().isTank()){
//					brewery.recieveOrder(o);
					it.remove();
				}else{
					o.getPub().getProvider().recieveOrder(o);
					it.remove();
				}
			}
		}
	}
}
