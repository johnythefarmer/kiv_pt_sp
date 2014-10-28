package cz.chmelokvas.brewery;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

import cz.chmelokvas.util.Controller;
import cz.chmelokvas.util.KeyPriorityQueue;
import cz.chmelokvas.util.Route;


public class Brewery extends Stock {

//	/** Atribut mnozstvi vyrobeneho piva za den *//*
//	private final static int PRODUCTION_PER_DAY = 7000;
	
	/** Atribut mnostvi camionu */
	protected List<Car> garage_for_camions = new LinkedList<Car>();
	
	/** Atribut mnostvi cisteren */
	protected List<Car> garage_for_cisterns = new LinkedList<Car>(); 
	
//	/** Pripravene instrukce pro prekladiste */
//	private List<List<Instruction>> instruction_for_dock = new LinkedList<List<Instruction>>();
	
	/** Celkova produkce piva */
	private int countBeers = 0;
	
	
	/** Atribut nazev pivovaru */
	private final String name;
	
	public Brewery(String name, int idCont, float x, float y){
		this.name = name;
		this.x = x;
		this.y = y;
		this.idCont = idCont;
		this.idProv = 0;
		this.provider = this;
		
	}
	
	public void checkTimeEvents(){
		
		productionBeer();
		
		

	}
	
	/**
	 * Zpracuj vsechny objednavky z tankovek
	 */
	public void prepareOrdersCisterns(){
		while(!orders.isEmpty()){
			SortedSet<Order> selected = new TreeSet<Order>(cmp);
			Order o = orders.last();
			selected.add(o);
			orders.remove(o);
			Pub p = o.getPub();
			
			checkNeighbours(p,selected);
			
//			System.out.println(p + " " + d[0][p.idProv]);
			
			/* Zasobnik uzlu prujezdu */
			Stack<Integer> s = new Stack<Integer>();
			
			prepareStackForPath(0, p.idProv, s);
			
			while(!s.isEmpty()){
				int i = s.pop();
				Pub tmp = ((Pub)customers.get(i));
				checkPub(tmp,selected);
//				checkNeighbours(tmp,selected);
				
				
			}
			
			beingPrepared.addAll(selected);
			System.out.println(selected);
			createInstructionsForCisterns(selected, c.mainTime, 0);
			
			
		}
	}
	
	/**
	 * Vezme sousedy hospody a proveri:
	 * 	je to hospoda, maji stejne prekladiste, je to tank
	 * @param p	vybrana hospoda
	 * @param selected	seznam vybranych objednavek
	 */
	private void checkNeighbours(Pub p, Set<Order> selected){
		for(Route r: p.routes){
			TransportNode neighbourN = c.nodes.get(r.getValue());
			if(neighbourN instanceof Pub && neighbourN.provider.equals(this) && ((Pub) neighbourN).isTank()){
				Pub neighbour = (Pub)neighbourN;
				checkPub(neighbour, selected);
			}
		}
	}
	
	/**
	 * Naplni zasobnik id bodu, ktere musi auto navstivit, pokud chce dorazit z bodu {@code source} do bodu {@code destination}
	 * @param source id pocatecniho bodu(pivovar)
	 * @param destination id ciloveho bodu
	 * @param nodes zasobnik kde budeme ukladat uzly
	 */
	public void prepareStackForPath(int source, int destination, Stack<Integer> nodes){
		// TODO cesta a ulozit do nodes
	}
	
	/**
	 * Overi, zda vybrany soused hospody ma vytvorenou objednavku
	 * @param p	soused(hospoda)
	 * @param selected	seznam vybranych objednavek
	 */
	private void checkPub(Pub p, Set<Order> selected){
		Order today = p.getTodayOrder();
		Order yesterday = p.getYesterdayOrder();
		if(today != null && orders.contains(today)){
			selected.add(today);
			orders.remove(today);
		}
		if(yesterday != null && orders.contains(yesterday)){
			selected.add(yesterday);
			orders.remove(yesterday);
		}
	}
	
	/**
	 * Zkontroluje, zda se dane objednavky vejdou do auta a pokud ne, vrati je zpet pro zpracovani dalsich objednavek
	 * @param car Auto, do ktereho budeme chti nakladat
	 * @param orders Objednavky, ktere ma dane auto stihnout
	 * @return Celkovy pocet, ktery mame do auta nalozit
	 */
	public int checkCarCapacity(Set<Order> orders){
		int sum = 0;
		for(Iterator<Order> it = orders.iterator(); it.hasNext();){
			Order o = it.next();
			
			if((sum + o.getAmount()) < (CarType.CISTERN.getCapacity()*3)/4){
				sum += o.getAmount();
			}else{
				beingPrepared.remove(o);
				this.orders.add(o);
				it.remove();
			}
		}
		
		return sum;
	}
	
	private void createInstructionsForCisterns(SortedSet<Order> selected, Time time, int i){
		
		LinkedList<Instruction> instructions = new LinkedList<>();
	
		/* Vyber nejblizsi objednavku */
		//TODO overit comparator, zda radi podle vzdalenosti od pivovaru
		Order order = selected.first();
		
		int sum = checkCarCapacity(selected);
		
		/* Predej aktualni cas */
		Time tmpTime = time;
		
		/* Cas nakladu plneho camionu */
		int loadingMinutes = sum * CarType.CISTERN.getReloadingSpeed();
		
		/* 1. instrukce - cekej v case vytvoreni v pivovaru */
		instructions.add(new Instruction(State.WAITING, this, tmpTime));
		
		/* Posun cas o cas nakladu cisterny */
		tmpTime = tmpTime.getTimeAfterMinutes(loadingMinutes);
		
		/* 2. instrukce - naloz cisternu */
		instructions.add(new Instruction(State.LOADING, this, sum, tmpTime));
		
	}
	
	private void createPathForCamions(){
		
	}
	
	
	
	
	/*
	 *  vemeš všechny objednávky z danýho sektoru v danou hodinu a seřadíš je podle vzdálenosti od pivovaru  
	 *  pak pojedeš do nejbližší. z tý najdeš cestu do tý druhý nejbližší (pomocí d v danym sektoru) 
	 *  a z tý do další (zase pomocí d v danym sektoru) až dojedeš do poslední... 
	 *  no a odtud pojedeš rovnou domů (pomocí d z pivovaru - tentokrát nepřevráceně)
	 */
	
	
	
	
	
	
	
	/** Realizace objednavek
	 * 
	 * @param dock	prekladiste, ktere objednava
	 * @param mn	kolik plnych aut ma poslat
	 */
	public void prepareOrdersCamions(Dock dock, int mn){
		LinkedList<Instruction> instructions;
		for(int i = 0; i  < mn; i++){
			instructions = createInstructionsForCamion(dock, c.mainTime);
			Car camion = getFirstWaitingCamion();
			camion.getInstructions().addAll(instructions);
			garage_for_camions.add(camion);
		}
	}	
	
	public void moveCamions(){
		for(Car car : garage_for_camions){
			Instruction i = car.getCurrentInstruction();
			while(i != null && Math.abs(i.getFinished().value() - c.mainTime.value()) < Controller.STEP){
				//vykonani potrebne aktivity pred prechodem na dalsi instrukci
				car.setPosition(i.getDestination());
				
				
				switch(i.getState()){
					case LOADING: 
						car.load(i.getAmount());
						unload(car.getCapacity()/2);
						break;
					case UNLOADING: 
						car.unload(i.getAmount());
						((Dock) car.getPosition()).load(car.getCapacity());
						break;
					case LOADING_EMPTY_BARRELS: 
						car.loadEmpty(i.getAmount());
						((Dock) car.getPosition()).load(car.getCapacity());
						break;
					case UNLOADING_EMPTY_BARRELS: 
						car.unloadEmpty(i.getAmount());
						break;
					default: 
						break;
				}
				
				System.out.println(i.getFinished() + " " + car + " " + i.getState().getStrFin() + " " + i.getDestination());
				
			
					
				//prechod na dalsi instrukci
				((LinkedList<Instruction>)car.getInstructions()).removeFirst();
				i = car.getCurrentInstruction();
				String position;
				if(i != null){
					car.setState(i.getState());
					position = i.getDestination() + "";
				}else{
					car.setState(State.WAITING);
					position = car.getPosition() + "";
				}
				
				System.out.println(car + " " +car.getState().getStrStart() + " " + position);
			}
		}
	}
	
	/**
	 * Sada instrukci pro camion
	 * @param dock	zdroj objednavky
	 * @param time	aktualni cas objednavky
	 * @return	instructions	instrukce pro camion
	 */
	public LinkedList<Instruction> createInstructionsForCamion(Dock dock, Time time){
		LinkedList<Instruction> instructions = new LinkedList<>();
		
		/* Predej aktualni cas */
		Time tmpTime = time;
		
		/* Cas nakladu plneho camionu */
		int loadingMinutes = CarType.CAMION.getCapacity() * CarType.CAMION.getReloadingSpeed();
		
		/* 1. instrukce - cekej v case vytvoreni v pivovaru */
		instructions.add(new Instruction(State.WAITING, this, tmpTime));
		
		/* Posun cas o cas nakladu camionu */
		tmpTime = tmpTime.getTimeAfterMinutes(loadingMinutes);
		
		/* 2. instrukce - naloz plny camion */
		instructions.add(new Instruction(State.LOADING, this, CarType.CAMION.getCapacity(), tmpTime));
		
		/* Vytvor cestu camionu k prekladisti */
		createPathForCamions(dock, instructions);
				
		return instructions;
	}
	
	/**
	 * Cesta pro camions do prekladiste
	 * @param dock	cil cesty
	 * @param instructions	sada instrukci
	 */
	public void createPathForCamions(Dock dock, LinkedList<Instruction> instructions){
		
		/* Cil cesty */
		TransportNode destination = dock;
		
		/* Cas konce nakladu */
		Time tmpTime = instructions.getLast().getFinished();
		
		/* Mnozina uzlu prujezdu */
		Stack<Integer> nodes = new Stack<Integer>();
		
		/* Pridej do nodes uzly prujezdu */
		addPathInstructions(destination.idCont, nodes, instructions);
		
				
		/* Cas vylozeni plneho camionu */
		int loadingMinutes = CarType.CAMION.getCapacity() * CarType.CAMION.getReloadingSpeed();
		
		/* Posun cas o vylozeni */
		tmpTime = tmpTime.getTimeAfterMinutes(loadingMinutes);
		
		/* 4. instrukce - vyloz plny camion */
		instructions.add(new Instruction(State.UNLOADING, dock, CarType.CAMION.getCapacity(), tmpTime));
		
		/* Mnozstvi prazdnych sudu v prekladisti */
		int sum = (dock.getEmpty() >= CarType.CAMION.getCapacity() ? CarType.CAMION.getCapacity() : dock.getEmpty());
		
		/* Posun cas o nalozeni prazdnych sudu */
		tmpTime = tmpTime.getTimeAfterMinutes(sum*CarType.CAMION.getReloadingSpeed());
		
		/* 5. instrukce - naloz prazdne sudy */		
		instructions.add(new Instruction(State.LOADING_EMPTY_BARRELS,dock, sum, tmpTime));
		
		/* Vytvor cestu camionu zpatky do pivovaru */
		createPathToHomeForCamions(dock, instructions, sum);
		
	}
	
	/**
	 * Cesta pro camions do pivovaru
	 * @param dock	zdroj cesty
	 * @param instructions	sada instrukci
	 * @param sum	mnozstvi nalozenych prazdnych sudu
	 */
	public void createPathToHomeForCamions(Dock dock, LinkedList<Instruction> instructions, int sum){

		/* Cil cesty */
		int destination = dock.getIdProv();
				
		/* Mnozina uzlu prujezdu */
		Stack<Integer> nodes = new Stack<Integer>();
		
		/* Pridej do nodes uzly prujezdu */
		addPathInstructions(destination, nodes, instructions);
		
		/* Cas po prijezdu do pivovaru */
		Time tmpTime = instructions.getLast().getFinished();
		
		/* Cas vylozeni prazdnych sudu */
		tmpTime = tmpTime.getTimeAfterMinutes(sum*CarType.CAMION.getReloadingSpeed());
		
		/* 7. instrukce - vylozeni prazdnych sudu v pivovaru */
		instructions.add(new Instruction(State.UNLOADING_EMPTY_BARRELS, this, sum, tmpTime));
	}	
	
	/**
	 * Vytvori mnozinu uzlu prujezdu do prekladiste
	 * @param i				ID cile
	 * @param nodes			mnozina uzlu
	 * @param instructions	sada instrukci
	 */
	public void addPathInstructions(int i, Stack<Integer> nodes, LinkedList<Instruction> instructions){
		
		/* Cas konce nakladu */
		Time tmpTime = instructions.getLast().getFinished();
		
		/* idN = id aktualni, idB = id predchozi */
		int idN, idB = i;
		
		// TODO otoc cestu
		while((idN = getP()[0][idB]) != 0){
			
//			float distance = dock.getP()[idB][idN];
			float distance = lengthEdge(c.nodes.get(idB), c.nodes.get(idN));
			
			/* Cas prujezdu mezi uzly */
			tmpTime = tmpTime.getTimeAfterMinutes((int)(distance/CarType.CAMION.getSpeed()));
			
			/* 3. || 6. instrukce - cestuj z uzlu idB do idN */
			instructions.add(new Instruction(State.TRAVELLING,c.nodes.get(idN),tmpTime));
			idB = idN;
		}
	}
	
	/**
	 * Vzdalenost 2 uzlu
	 * @param a	uzel 1
	 * @param b	uzel 2
	 * @return	vzdalenost
	 */
	private float lengthEdge(TransportNode a, TransportNode b)
	{
		/* Vzorec sqrt( (a1-b1)^2 + (a2-b2)^2 ) */
		return (float) Math.sqrt(Math.pow(a.getX()-b.getX(), 2.0) +
				Math.pow(a.getY()-b.getY(), 2.0));
	}
	
	private void productionBeer(){
		if(c.mainTime.getHour() == 13){
			full += 307;
			countBeers += 307;
		}
		else{
			full += 291;
			countBeers += 291;
		}
	}
	
	public String getName(){
		return name;
	}
	
	/**
	 * kamion -> full - 100/2 hl
	 * cisterna -> full - x hl
	 * @param n
	 */
	public void unload(int n){
		this.full -= n;
	}
	
	
	
	public int getCountBeers() {
		return countBeers;
	}

	/**
	 * Vrati prvni cekajici cisternu
	 * @return prvni cekajici cisterna
	 */
	public Car getFirstWaitingCistern(){
		for(Car c: garage_for_cisterns){
			if(c.getCurrentInstruction() == null && c.getPosition().equals(this)){
				return c;
			}
		}
		Car newCar = Car.getCistern(this, garage_for_cisterns.size());
		
		garage_for_cisterns.add(newCar);
		System.out.println("Vytvarim novou cisternu!");
		return newCar;
	}
	
	/**
	 * Vrati prvni cekajici camion
	 * @return prvni cekajici camion
	 */
	public Car getFirstWaitingCamion(){
		for(Car c: garage_for_camions){
			if(c.getCurrentInstruction() == null && c.getPosition().equals(this)){
				return c;
			}
		}
		Car newCar = Car.getCamion(this, garage_for_camions.size());
		
		garage_for_camions.add(newCar);
		System.out.println("Vytvarim novy kamion!");
		return newCar;
	}
			
	
	public void calculateShortestPathsDijkstra(){
		this.d = new float[1][c.nodes.size()];
		this.p = new int[1][c.nodes.size()];
		
		for(int i = 0; i < d.length; i++){
			d[i][0] = Float.MAX_VALUE;
		}
		for(int i = 0; i < d[0].length; i++){
			d[0][i] = Float.MAX_VALUE;
		}
		KeyPriorityQueue<Integer> queue = new KeyPriorityQueue<Integer>();
		
		d[0][0] = 0;
		queue.add(d[0][0],idCont);
		
		while(!queue.isEmpty()){
			int u = queue.poll();
			for(Route v : c.nodes.get(u).routes){
				int vId = v.getValue();
				
				float dist = d[0][c.nodes.get(u).idCont] + v.getDistance();
				
				if(dist < d[0][c.nodes.get(vId).idCont]){
					d[0][c.nodes.get(vId).idCont] = dist;
					p[0][c.nodes.get(vId).idCont] = u;
					queue.add(d[0][c.nodes.get(vId).idCont], vId);
				}
			}
		}
	}
}
