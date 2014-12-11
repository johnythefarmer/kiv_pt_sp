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

/**
 * Ridici jednotka aplikace
 * @author Jan Dvorak A13B0293P
 *
 */
public class Controller {
	public static Controller c;
	
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
	
	public final Gui gui = Gui.getInstance();
	
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
		logger.setLoggableObject(brewery);
		
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
		
		gui.getAreaStatistika().setText("Pocitam vzdalenosti mezi dopravnimi uzly...");

		for(Dock d:dock){
			d.floydWarshal(d.getD(), d.getP(), d.getD().length);
		}
		brewery.calculateShortestPathsDijkstra();
		
		gui.getAreaStatistika().setText("Vzdalenosti vypocitany.");
		
		int oldDay = -1;
		while(mainTime.value() < endTime.value()){
			logger.printStatistics();
			
			if(!waitForButton()){
				break;
			}
			
			long t = System.currentTimeMillis();
			
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
			
			long time = System.currentTimeMillis() - t;
			
			waitForEndOfStep(time);
			
			if(gui.end){
				break;
			}
			
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
	 * Ceka do konce casoveho kroku.<br>
	 * @param time cas ktery jiz z tohoto kroku uplynul
	 */
	private void waitForEndOfStep(long time){
		try {
			if(time < gui.jSlider1.getValue()){
				//Vyvazeni delky behu daneho kroku
				Thread.sleep(gui.jSlider1.getValue() - time);
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Ceka ve while cyklu, doku neni zmacknuto tlacitko, nebo nenastal konec.
	 * @return priznak, zda se ma simulace ukoncit
	 */
	private boolean waitForButton(){
		while(gui.stopAndPlay){
			if(gui.end){
				return false;
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return true;
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
