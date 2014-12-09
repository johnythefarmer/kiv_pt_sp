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

	public static String fileName;
	
	/** Casovy krok*/
	public static final int STEP = 60;
	
	/** Seznam vsech dopravnich uzlu */
	public List<TransportNode> nodes;
	
	/** Seznam vsech hospod */
	public List<Pub> pub;
	
	/** Seznam vsech prekladist */
	public List<Dock> dock;
	
	/** Pivovar */
	public Brewery brewery;
	
	/** Nastroj pro osetrovani vystupu do souboru a do cli */
	private final Logger logger = Logger.getInstance("output.txt");
	
	final Gui gui = Gui.getInstance();
	
	/**
	 * Minimalni priorita vystupu:<br>
	 * <li>1 = CHYBY
	 * <li>2 = VYRIZENI OBJEDNAVKY
	 * <li>3 = PODANI OBJEDNAVKY
	 * <li>4 = POHYB AUT
	 * <li>5 = ZASOBOVANI DO PREKLADIST
	 * <li>6 = PRODUKCE, VYTVARENI AUT
	 */
	public int minLogPriority = 1;
	
	/**
	 * Pocet dorucenych sudu celkem
	 */
	public int deliveredBarrels;
	
	/**
	 * Pocet dorucenych hl celkem
	 */
	public int deliveredHL;
	
	/**
	 * Pocet objednavek dorucenych po 24 hodinach
	 */
	public int deliveredLate;
	
	/** Vsechny objednavky pro dany den	 */
	public List<Order> todayOrders = new ArrayList<Order>();
	
	/**hlavni cas cele aplikace*/
	public Time mainTime = new Time(0,0,0);
	
	/** cas ukonceni simulace */
	public Time endTime = new Time(7,0,0);
	
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
	}
	

	/**
	 * Metoda ktera bude obstaravat celou simulaci
	 */
	public void simulate(){
		System.out.println("Pocitam vzdalenosti mezi dopravnimi uzly...");
		for(Dock d:dock){
			d.floydWarshal(d.getD(), d.getP(), d.getD().length);
		}
		brewery.calculateShortestPathsDijkstra();
		
		System.out.println("Vzdalenosti vypocitany.");
//		System.out.println("Pro spusteni simulace zmacknete enter.");
//		Main.sc.nextLine();
//		Main.sc.nextLine();
		
		int oldDay = -1;
		while(mainTime.value() < endTime.value()){
			
			
			
			System.out.println("---" + mainTime + "---");
			//generovani objednavek na zacatku dne
			if(oldDay != mainTime.getDay()){
				for(Pub p:pub){
					
					if(p.getYesterdayOrder() != null && mainTime.value() > p.getYesterdayOrder().getTime().getTimeAfterMinutes(60*24).value()){
						System.err.println("Nestihlo se");
						System.err.println(p.getYesterdayOrder() + "" + p.getProvider());
					}
				}
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
	
	/**
	 * Vsem prikaze, at vykonaji casove operace
	 */
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
					brewery.recieveOrder(o);
					it.remove();
				}else{
					o.getPub().getProvider().recieveOrder(o);
					it.remove();
				}
			}
		}
	}
}
