package cz.chmelokvas.brewery;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import cz.chmelokvas.util.Controller;


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

	
	/** Atribut mnostvi cisteren a camionu */
	protected List<Car> garage_for_cars;
	
	/** Atribut nazev pivovaru */
	private final String name;
	
	public Brewery(String name, int idCont, float x, float y){
		this.name = name;
		this.x = x;
		this.y = y;
		this.idCont = idCont;
		this.idProv = 0;
		this.provider = this;
		this.garage_for_cars = new LinkedList<Car>();
	}
	
	public void checkTimeEvents(){
		
		productionBeer();

		
		if(c.mainTime.getHour() > 8 && c.mainTime.getHour() < 16){
			 prepareOrders();
		}
		
		/* TODO 
		 * kamion -> full - 2*100 hl
		 * cisterna -> full - hl
		 */
		
	}

	public void createInstructionsForCamion(Dock dock){
		List<Instruction> instructions = new ArrayList<>();

	}
	
	/** Realizace objednavek
	 * 
	 * @param dock	prekladiste, ktere objednava
	 * @param mn	kolik plnych aut ma poslat
	 */
	public void prepareOrders(Dock dock, int mn){
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
						((Dock) car.getPosition()).unload(car.getCapacity());
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
>>>>>>> Camions instructions, move
		
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
		createTravellingInstructions(dock, instructions);
				
		return instructions;
	}
	
	/**
	 * Cesta pro camions do prekladiste
	 * @param dock	cil cesty
	 * @param instructions	sada instrukci
	 */
	public void createTravellingInstructions(Dock dock, LinkedList<Instruction> instructions){
		
		/* Cil cesty */
		TransportNode destination = dock;
		
<<<<<<< HEAD
		Stack<Integer> nodes = new Stack<Integer>();
		
		int i = source.idProv;
		int j = destination.idProv;
		
		// cesta z I do J
		
		int x = 0, y = 0;
		while(!nodes.empty()){
			y = nodes.pop();

			float distance = d[x][y];
			
			tmpTime = tmpTime.getTimeAfterMinutes((int)(distance/CarType.CAMION.getSpeed()));
			
			instructions.add(new Instruction(State.TRAVELLING,customers.get(y),tmpTime));
			
			x = y;
		}
=======
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
		createInstructionsPathHome(dock, instructions, sum);
		
	}
	
	/**
	 * Cesta pro camions do pivovaru
	 * @param dock	zdroj cesty
	 * @param instructions	sada instrukci
	 * @param sum	mnozstvi nalozenych prazdnych sudu
	 */
	public void createInstructionsPathHome(Dock dock, LinkedList<Instruction> instructions, int sum){

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
		
		// TODO otoc cestu a nahraj do nodes
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
>>>>>>> Camions instructions, move
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
	 * kamion -> full - 2*100 hl
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
	public Car getFirstWaitingCistern(CarType type){
		for(Car c: garage_for_cars){
			if(c.getCurrentInstruction() == null && c.getPosition().equals(this) && c.getType() == type){
				return c;
			}
		}
		Car newCar = null;
		if(type == CarType.CISTERN){
			Car.getCistern(this, garage_for_cars.size());
		}else if(type == CarType.CAMION){
			Car.getCamion(this, garage_for_cars.size());
		}
		
		garage_for_cars.add(newCar);
		System.out.println("Vytvarim nove auto!");
		return newCar;
	}
	
	/*
	
	public void calculateShortestPathsDijkstra(){
//		this.setD(new float[0][routes.size()]);
		this.d = new float[1][c.nodes.size()];
		this.p = new int[1][c.nodes.size()];
		for(int i = 0; i < d[0].length; i++){
			d[0][i] = Float.MAX_VALUE;
		}
		KeyPriorityQueue<Integer> queue = new KeyPriorityQueue<Integer>();
		
		d[0] = 0;
		queue.add(d[0],idCont);
		
		while(!queue.isEmpty()){
			int u = queue.poll();
			for(Route v : c.nodes[u].routes){
				int vId = v.getValue();
				
				float dist = d[0][c.nodes.get(u).idCont] + v.getDistance();
				
				if(dist < d[0][c.nodes.get(vId).idCont]){
					d[0][c.nodes.get(vId).idCont] = dist;
					p[0][c.nodes.get(vId).idCont] = u;
					queue.add(d[0][c.nodes.get(vId).idCont], vId);
				}
			}
		}
	}*/
	
/*	private void produceBeer()
	{
		
	}*/
}
