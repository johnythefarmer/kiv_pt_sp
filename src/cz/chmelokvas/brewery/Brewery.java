package cz.chmelokvas.brewery;

import java.util.ArrayList;
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

/**
 * Trida reprezentujici pivovar
 * @author Lukas Cerny A13B0286P, Jan Dvorak A13B0293P
 *
 */
public class Brewery extends Stock {
	
	/** Mnozstvi kamionu*/
	private final List<Car> garageCamion = new LinkedList<Car>();
	
	/** Mnostvi cisteren */
	private final List<Car> garageCistern = new LinkedList<Car>(); 
	
	/**
	 * Seznam instrukci, ktere autum predame zitra v osm
	 */
	private final List<List<Instruction>> tomorrow = new LinkedList<List<Instruction>>();
	
	/**
	 * Kapacita pivovaru
	 */
	private static final int MAX_BREWERY_CAPACITY = 7000;
	
	/** Celkova produkce piva */
	private int countBeers = 0;
	
	
	/** Atribut nazev pivovaru */
	private final String name;
	
	/**
	 * Vytvori pivovar s danym jmenem na danych souradnicich
	 * @param name Nazev pivovaru
	 * @param idCont Id u kontroleru
	 * @param x x-ova souradnice
	 * @param y y-ova souradnice
	 */
	public Brewery(String name, int idCont, float x, float y){
		this.name = name;
		this.x = x;
		this.y = y;
		this.idCont = idCont;
		this.idProv = 0;
		this.provider = this;
		this.full = 7000;
		
	}
	
	/**
	 * Provede vsechny udalosti, co se staly za dany krok v case to jest:<br>
	 * Urci cestu aut<br>
	 * Vysle dalsi auta na cestu<br>
	 * Posune auta o dany casovy usek<br>
	 * Produkce piva<br>
	 */
	public void checkTimeEvents(){
		
		//Predani instrukci ze vcerejska
		if(c.mainTime.getHour() == 8){
			for(List<Instruction> inst:tomorrow){
				Car c = getFirstWaitingCistern();
				c.setInstructions(inst);
			}
			tomorrow.remove(tomorrow);
		}
	
		
		if(c.mainTime.getMinute() == 0){
			if(c.mainTime.getHour() > 8 && c.mainTime.getHour() <= 16){
				prepareOrdersCisterns();
			}
			produceBeer();
		}
		
		
		moveCamions();
		
		moveCisterns();

	}
	
	/**
	 * Posouva cisterny
	 */
	private void moveCisterns(){
		for(Car car : garageCistern){
			
			Instruction i = car.getCurrentInstruction();
			while(i != null && i.getFinished().value() >= c.mainTime.value() && i.getFinished().value() < (c.mainTime.value() + Controller.STEP)){
				//vykonani potrebne aktivity pred prechodem na dalsi instrukci
				finishInstructionCistern(car, i);
				
				//prechod na dalsi instrukci
				i = startNewInstructionsCistern(car);
			}
		}
	}
	
	/**
	 * Predane auto dokonci danou instrukci
	 * @param car Predane auto
	 * @param i Instrukce, kterou ma auto vykonat
	 */
	private void finishInstructionCistern(Car car, Instruction i){
		car.setPosition(i.getDestination());
		
		
		switch(i.getState()){
			case LOADING: 
				car.load(i.getAmount());
				if(car.getPosition().equals(this)){unload(i.getAmount());}
				break;
			case UNLOADING: 
				if(i.getOrder() != null){
					car.unload(i.getAmount());
					break;
				}
			default: break;
		}
		
		logger.log(i.getFinished(), 4, car + " " + i.getState().getStrFin() + " " + i.getDestination());
		
		//pokud byla u instrukce prilozena objednavka tak ji vyrid
		if(i.getOrder() != null){
			deliverOrder(i.getOrder(),car);
		}
	}
	
	/**
	 * Prechod daneho auta na dalsi instrukci
	 * @param car Auto, kteremu zaridime prechod na dalsi instrukci
	 * @return Instrukci kterou ma auto provest dale
	 */
	private Instruction startNewInstructionsCistern(Car car){
		Time time = car.getCurrentInstruction().getFinished();
		car.getInstructions().remove(0);
		
		Instruction i = car.getCurrentInstruction();
		String position;
		if(i != null){
			car.setState(i.getState());
			position = i.getDestination() + "";
		}else{
			car.setState(State.WAITING);
			position = car.getPosition() + "";
		}
		
		logger.log(time, 4, car + " " +car.getState().getStrStart() + " " + position);
		
		return i;
	}
	
	/**
	 * Zpracuj vsechny objednavky z tankovek
	 */
	private void prepareOrdersCisterns(){
		for(Order o:orders){
			if(c.mainTime.getDay() == 5 && o.getPub().idCont == 40){
				System.out.print("");
			}
		}
		while(!orders.isEmpty()){
			List<Order>[] selected = initSectorLists();
			
			int n = selected.length;
			for(int i = 0; i < n; i++){
				List<Order> tmp = selected[i];
				
				if(!tmp.isEmpty()){
//					Dock d = (Dock)tmp.get(0).getPub().getProvider();
					SortedSet<Order> tmpSet = new TreeSet<Order>(cmp);
						//rozdeleni objednavek po trech
						while(tmp.size() > 3){
							List<Order> tmpList = tmp.subList(0, 2);
							tmpSet.addAll(tmpList);
							tmp.removeAll(tmpList);
							decidePreparingMethod(tmpSet);
							tmpSet = new TreeSet<Order>(cmp);
							
						}
						tmpSet.addAll(tmp);
						decidePreparingMethod(tmpSet);
				}
			}
		}
	}
	
	/**
	 * Rozradi objednavky do seznamu podle sektoru
	 * @return pole seznamu
	 */
	private List<Order>[] initSectorLists(){
		int n = Controller.c.dock.size();
		
		//resime pro kazdy sektor zvlast
		@SuppressWarnings("unchecked")
		List<Order>[] selected = new List[n];
		for(int i = 0; i < n; i++){
			selected[i] = new ArrayList<Order>();
		}
		for(Iterator<Order> it = orders.iterator(); it.hasNext();){
			Order o = it.next();
			selected[o.getPub().getProvider().idCont - 1].add(o);
			beingPrepared.add(o);
			it.remove();
		}
		
		return selected;
	}
	
	/**
	 * Podle casu se rozhodne jakou metodu odeslani provedeme
	 * @param orders mnozina objednavek
	 */
	private void decidePreparingMethod(SortedSet<Order> orders){
		if(c.mainTime.getHour() == 16){
			prepareOrderForSectorLate(orders);
		}else {
			prepareOrderForSector(orders);
		}
	}
	
	/**
	 * Projde mnozinu objednavek, a zkusi pro kazdy prvek vypocitat cestu.<br>
	 * Pokud zjisti, ze se to neda stihnout, zkusi to naplanovat na zitra.<br>
	 * Pokud bychom to ani zitra nestihli, doruci to jeste dnes ale opozdene
	 * @param d Prekladiste v jehoz sektoru se budeme pohybovat
	 * @param instructions Instrukce, ktere budeme modifikovat pro cesty k vyrizeni danych objednavek
	 * @param selected Mnozina vyrizovanych objednavek
	 */
	private void checkTimeOfDeliveryLate(Dock d, List<Instruction> instructions, Set<Order> selected){
		for(Iterator<Order> it = selected.iterator(); it.hasNext();){
			Order o = it.next();
			
			createTravellingInstructions(d,instructions,o);
			if(instructions.get(instructions.size()-1).getFinished().value() >= o.getTime().getTimeAfterMinutes(60*24).value()){
					removeUnnecessaryInstr(instructions);
					break;
			}
			
			//Pokud bude cesta realizovana, odstranime tuto objednavku z mnoziny
			it.remove();
		}
	}
	
	/**
	 * Pripravi cesty pro auta, ktera vyrazi zitra rano v osm.<br>
	 * Jedna se o objednavky mezi treti a ctvrtou hodinou.
	 * @param selected objednavky ktere vyrizujeme
	 */
	private void prepareOrderForSectorLate(SortedSet<Order> selected){
		Order first = selected.first();
		
		//pocet hl ktere budeme cerpat
		int sum = checkCarCapacity(selected, CarType.CISTERN);
		
		Time t = new Time(c.mainTime.getDay() + 1, 8,0);
		
		//vytvoreni pocatecnich instrukci
		List<Instruction> instructions = addFirstInstructions(t, sum, CarType.CISTERN);
		
		Stack<Integer> nodes = new Stack<Integer>();
		
		
		
		//pocitani cesty do nejblizsi hospody z mnoziny
		Pub p = first.getPub();
		Dock d = (Dock) p.getProvider();
		
		prepareStackForPath(p.getIdCont(), nodes);
		
		addPathInstructions(nodes, instructions, CarType.CISTERN);
		
		
		//Pocitani cest do dalsi hospod a kontrola jestli to stihneme
		checkTimeOfDeliveryLate(d, instructions, selected);
		
		//Odebrani mnozstvi hl ktere dovezeme zitra
		for(Order o :selected){
			sum -= o.getAmount();
		}
		orders.addAll(selected);
		//pokud nam jiz nic nezbylo nema cenu cestu dal resit
		if(instructions.isEmpty()){
			return;
		}
		
		TransportNode node = instructions.get(instructions.size() - 1).getDestination();
		
		createPathToHome(node,instructions, sum, CarType.CISTERN);
	
		Car c = getFirstWaitingCistern();
		c.setInstructions(instructions);
	}
	
	/**
	 * Pripravi cesty pro cisterny nad danou mnozinou objednavek ze stejneho sektoru
	 * @param selected mnozina objednavek
	 */
	private void prepareOrderForSector(SortedSet<Order> selected){
		Order first = selected.first();
		
		//pocet hl ktere budeme cerpat
		int sum = checkCarCapacity(selected, CarType.CISTERN);
		
		//vytvoreni pocatecnich instrukci
		List<Instruction> instructions = addFirstInstructions(c.mainTime, sum, CarType.CISTERN);
		
		Stack<Integer> nodes = new Stack<Integer>();
		
		
		
		//pocitani cesty do nejblizsi hospody z mnoziny
		Pub p = first.getPub();
		Dock d = (Dock) p.getProvider();
		
		prepareStackForPath(p.getIdCont(), nodes);
		
		addPathInstructions(nodes, instructions, CarType.CISTERN);
		
		
		//Pocitani cest do dalsi hospod a kontrola jestli to stihneme
		checkTimeOfDelivery(d, instructions, selected);
		
		//Odebrani mnozstvi hl ktere dovezeme zitra
		for(Order o :selected){
			sum -= o.getAmount();
		}
		
		//pokud nam jiz nic nezbylo nema cenu cestu dal resit
		if(instructions.isEmpty()){
			return;
		}
		
		//jelikoz bylo zmeneno mnozstvi hl je treba urcit novou dobu cest
		correctTimeCistern(instructions, sum, c.mainTime);
		
		TransportNode node = instructions.get(instructions.size() - 1).getDestination();
		
		createPathToHome(node,instructions, sum, CarType.CISTERN);
	
		Car c = getFirstWaitingCistern();
		c.setInstructions(instructions);
	}
	
	/**
	 * Projde mnozinu objednavek, a zkusi pro kazdy prvek vypocitat cestu.<br>
	 * Pokud zjisti, ze se to neda stihnout, zkusi to naplanovat na zitra.<br>
	 * Pokud bychom to ani zitra nestihli, doruci to jeste dnes ale opozdene
	 * @param d Prekladiste v jehoz sektoru se budeme pohybovat
	 * @param selected Mnozina vyrizovanych objednavek
	 * @param instructions Instrukce, ktere budeme modifikovat pro cesty k vyrizeni danych objednavek
	 */
	private void checkTimeOfDelivery(Dock d, List<Instruction> instructions, Set<Order> selected){
		for(Iterator<Order> it = selected.iterator(); it.hasNext();){
			Order o = it.next();
			
			createTravellingInstructions(d,instructions,o);
			if(instructions.get(instructions.size()-1).getFinished().getHour() >= 16){
				SortedSet<Order> tmpOrd = new TreeSet<Order>(cmp);
				tmpOrd.addAll(selected);
//				
//				//Zkus odeslat zitra
				List<Instruction> tomorrow = deliverTomorrow(tmpOrd, d);
//				
//				//Pokud bychom to nestihli, udelej to dnes ale dorazis pozdeji
				if(tomorrow != null){
					this.tomorrow.add(tomorrow);
					removeUnnecessaryInstr(instructions);
					break;
				}
			}
			
			//Pokud bude cesta realizovana, odstranime tuto objednavku z mnoziny
			it.remove();
		}
	}
	
	/**
	 * Zkusi naplanovat doruceni danych objednavek pristi den
	 * @param orders Mnozina vyrizovanych objednavek
	 * @param d Prekladiste v jehoz sektoru se pohybujeme
	 * @return Seznam  Instrukci pro cestu zitra, nebo null, pokud bychom to zitra nestihli
	 */
	private List<Instruction> deliverTomorrow(SortedSet<Order> orders, Dock d){
		int sum = checkCarCapacity(orders, CarType.CISTERN);
		List<Instruction> instructions = addFirstInstructions(new Time(c.mainTime.getDay() + 1, 8, 0), sum,CarType.CISTERN);
				
		Stack<Integer> nodes = new Stack<Integer>();
		
		
		
		Pub p = orders.first().getPub();

		
		prepareStackForPath(p.getIdCont(), nodes);
		
		addPathInstructions(nodes, instructions, CarType.CISTERN);
		
		for(Iterator<Order> it = orders.iterator(); it.hasNext();){
			Order o = it.next();
			createTravellingInstructions(d,instructions,o);
			if(instructions.get(instructions.size() - 1).getFinished().value() > o.getTime().getTimeAfterMinutes(60*24).value()){
				//nestihame
				return null;
			}
			it.remove();
		}
		
		TransportNode node = instructions.get(instructions.size() - 1).getDestination();
		
		createPathToHome(node,instructions, sum, CarType.CISTERN);
		

		return instructions;
	}
	
	/**
	 * Pokud jsme modifikovali mnozstvi objednavek, ktere chceme dorucit,
	 * musime posunout instrukce v case, jelikoz se nam timpadem zmenila doba nakladani
	 * @param instructions Seznam instrukci ktery chceme modifikovat
	 * @param sum Nove mnozstvi sudu, ktere je treba nalozit
	 * @param t cas podle ktereho upravujeme
	 */
	private void correctTimeCistern(List<Instruction> instructions, int sum, Time t){
		int correct = instructions.get(1).getFinished().value() - t.getTimeAfterMinutes(sum*CarType.CISTERN.getReloadingSpeed()).value();

		if(correct != 0){
			for(int i = 1; i < instructions.size(); i++){
				instructions.get(i).getFinished().subMinutes(correct);
			}
		}
	}
	
	/**
	 * Z daneho seznamu odstrani nepotrebne instrukce,
	 * ktere slouzily k obslouzeni posledni objednavky, ktera jiz vyrizovana nebude
	 * @param instructions seznam instrukci pro auto
	 */
	private void removeUnnecessaryInstr(List<Instruction> instructions){
		LinkedList<Instruction> linkedInstructions = (LinkedList<Instruction>)instructions;
		do{
			linkedInstructions.remove(instructions.size() -1);
		}while(linkedInstructions.size() != 0 && linkedInstructions.getLast().getOrder() == null);
	}
	
	/**
	 * Urci instrukce pro cestu v danem sektoru
	 * @param d Prekladiste v jehoz sektoru se pohybujeme
	 * @param instructions seznam isntrukci pro auto
	 * @param o Objednavka, kterou na konci dane cesty chceme vyridit
	 */
	private void createTravellingInstructions(Dock d, List<Instruction> instructions, Order o){
		Stack<Integer> nodes = new Stack<Integer>();
		Instruction in = instructions.get(instructions.size() - 1);
		
		int oId = o.getPub().idProv, inId = in.getDestination().idProv;
		
		d.prepareStackForPath(inId,oId, nodes);
		d.addPathInstructions(inId, instructions, nodes, CarType.CISTERN);
		
		int reloadingMinutes = o.getAmount()*CarType.CISTERN.getReloadingSpeed();
		
		Time t = instructions.get(instructions.size() -1).getFinished();
		
		/* Instrukce vyrizovani objednavky.
		 * U posledni instrukce (nakladani prazdnych sudu) je dana i objednavka.
		 * Auto tak vi, ze po vykonani teto instrukce je objednavka vyrizena
		 */
		t = t.getTimeAfterMinutes(reloadingMinutes);
		instructions.add(new Instruction(State.UNLOADING,o.getPub(), t, o.getAmount(), o));
	}
	
	/**
	 * Vytvori pocatecni instrukce zacinajici v case t.<br>
	 * Prvni instrukce bude, ze bude cekat do daneho casu<br>
	 * Dale bude nakladat sum*(rychlost nakladani) minut
	 * @param t Cas kdy ma byt ukoncena prvni instrukce
	 * @param sum Mnozstvi, ktere ma auto pomoci druhe instrukce nalozit
	 * @param ct Typ auta
	 * @return Spojovy seznam instrukci
	 */
	private List<Instruction> addFirstInstructions(Time t, int sum, CarType ct){
		List<Instruction> instructions = new LinkedList<Instruction>();
		int loadingMinutes = sum*ct.getReloadingSpeed();
		
		//vytvareni uplne prvni instrukce
		Time tmpTime = new Time(t.value());
		instructions.add(0,new Instruction(State.WAITING, this, tmpTime));
		
		//vytvareni instrukce nakladani
		tmpTime = tmpTime.getTimeAfterMinutes(loadingMinutes);
		instructions.add(1,new Instruction(State.LOADING, this, sum, tmpTime));
		
		return instructions;
	}
	
	/**
	 * Naplni zasobnik id bodu, ktere musi auto navstivit, pokud chce dorazit z bodu {@code source} do bodu {@code destination}
	 * @param destination id ciloveho bodu
	 * @param nodes zasobnik kde budeme ukladat uzly
	 */
	private void prepareStackForPath(int destination, Stack<Integer> nodes){
		int i = destination;
		nodes.push(i);
		//poradi je treba prohodit, proto se uziva zasobniku
		while(p[0][i] != 0){
			int tmp = p[0][i];
			nodes.push(tmp);
			i = tmp;
		}
	}
	
	/**
	 * Zkontroluje, zda se dane objednavky vejdou do auta a pokud ne, vrati je zpet pro zpracovani dalsich objednavek
	 * @param orders Objednavky, ktere ma dane auto stihnout
	 * @param ct typ auta
	 * @return Celkovy pocet, ktery mame do auta nalozit
	 */
	private int checkCarCapacity(Set<Order> orders, CarType ct){
		int sum = 0;
		float k = (ct == CarType.CAMION)? 1 : 0.75f;
		for(Iterator<Order> it = orders.iterator(); it.hasNext();){
			Order o = it.next();
			
			if((sum + o.getAmount()) < ct.getCapacity()*k){
				sum += o.getAmount();
			}else{
				beingPrepared.remove(o);
				this.orders.add(o);
				it.remove();
			}
		}
		
		return sum;
	}
	
	/**
	 * Vrati pocet kamionu
	 * @return pocet kamionu
	 */
	public int getCamionCount(){
		return garageCamion.size();
	}
	
	/**
	 * Vrati pocet cisteren
	 * @return pocet cisteren
	 */
	public int getCisternCount(){
		return garageCistern.size();
	}
	
	/** Realizace objednavek
	 * @param dock	prekladiste, ktere objednava
	 * @param mn	kolik plnych aut ma poslat
	 */
	public void prepareOrdersCamions(Dock dock, int mn){
		if(full <= mn*100){
			return;
		}
		List<Instruction> instructions;
		for(int i = 0; i  < mn; i++){
			instructions = createInstructionsForCamion(dock, c.mainTime);
			Car camion = getFirstWaitingCamion();
			camion.setEmpty(0);
			camion.setFull(0);
			camion.setInstructions(instructions);
		}
	}	
	
	/**
	 * Vrati seznam kamionu jako garaz
	 * @return garaz kamiounu
	 */
	public List<Car> getCamionGarage(){
		return garageCamion;
	}
	
	
	/**
	 * Vrati seznam kamionu jako garaz
	 * @return garaz kamiounu
	 */
	public List<Car> getCisternGarage(){
		return garageCistern;
	}
	
	/**
	 * Presun kamionu
	 */
	private void moveCamions(){
		for(Car car : garageCamion){

			Instruction i = car.getCurrentInstruction();
				while(i != null && i.getFinished().value() >= c.mainTime.value() && i.getFinished().value() < (c.mainTime.value() + Controller.STEP)){
					finishInstruction(car, i);
					logger.log(i.getFinished(), 4, car + " " + i.getState().getStrFin() + " " + i.getDestination());
					
					Time time = i.getFinished();
						
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
					
					logger.log(time, 4, car + " " +car.getState().getStrStart() + " " + position);
				}
			
		}
	}
	
	/**
	 * Predany kamion dokonci danou instrukci
	 * @param car Predane auto
	 * @param i Instrukce, kterou ma auto vykonat
	 */
	private void finishInstruction(Car car, Instruction i){
		car.setPosition(i.getDestination());
		
		
		switch(i.getState()){
			case LOADING: 
				car.load(i.getAmount());
				unload(car.getCapacity()/2);
				break;
			case UNLOADING:
				Dock d = ((Dock) car.getPosition());
//					d.addImaginaryBarrels(i.getAmount());
				car.unload(i.getAmount());
				d.load(car.getCapacity());
				break;
			case LOADING_EMPTY_BARRELS:
				car.loadEmpty(i.getAmount());
				((Dock) car.getPosition()).unloadEmpty(car.getCapacity());
				break;
			case UNLOADING_EMPTY_BARRELS: 
				car.unloadEmpty(i.getAmount());
				break;
			default: 
				break;
		}
	}

	
	/**
	 * Sada instrukci pro camion
	 * @param dock	zdroj objednavky
	 * @param time	aktualni cas objednavky
	 * @return	instructions	instrukce pro camion
	 */
	private List<Instruction> createInstructionsForCamion(Dock dock, Time time){
		List<Instruction> instructions = addFirstInstructions(time, CarType.CAMION.getCapacity()/2,CarType.CAMION);

		createPathForCamions(dock, instructions);
				
		return instructions;
	}
	
	/**
	 * Cesta pro camions do prekladiste
	 * @param dock	cil cesty
	 * @param instructions	sada instrukci
	 */
	private void createPathForCamions(Dock dock, List<Instruction> instructions){
		
		/* Cil cesty */
		TransportNode destination = dock;
		
		/* Cas konce nakladu */
		Time tmpTime = instructions.get(instructions.size() - 1).getFinished();
		
		/* Mnozina uzlu prujezdu */
		Stack<Integer> nodes = new Stack<Integer>();
		
		//priprava zasobniku
		prepareStackForPath(destination.idCont,nodes);
		
		/* Pridej do nodes uzly prujezdu */
		addPathInstructions(nodes, instructions, CarType.CAMION);
		
				
		/* Cas vylozeni plneho camionu */
		int loadingMinutes = CarType.CAMION.getCapacity()/2 * CarType.CAMION.getReloadingSpeed();
		
		/* Posun cas o vylozeni */
		tmpTime = tmpTime.getTimeAfterMinutes(loadingMinutes);
		
		/* 4. instrukce - vyloz plny camion */
		instructions.add(new Instruction(State.UNLOADING, dock, CarType.CAMION.getCapacity()/2, tmpTime));
		
		/* Mnozstvi prazdnych sudu v prekladisti */
		int sum = CarType.CAMION.getCapacity();
		
		/* Posun cas o nalozeni prazdnych sudu */
		tmpTime = tmpTime.getTimeAfterMinutes(sum*CarType.CAMION.getReloadingSpeed());
		
		/* 5. instrukce - naloz prazdne sudy */		
		instructions.add(new Instruction(State.LOADING_EMPTY_BARRELS,dock, sum, tmpTime));
		
		/* Vytvor cestu camionu zpatky do pivovaru */
		createPathToHome(dock, instructions, sum, CarType.CAMION);
		
	}
	
	/**
	 * Cesta pro camions do pivovaru
	 * @param node	zdroj cesty
	 * @param instructions	sada instrukci
	 * @param sum	mnozstvi nalozenych prazdnych sudu
	 * @param ct typ auta
	 */
	public void createPathToHome(TransportNode node, List<Instruction> instructions, int sum, CarType ct){

		/* Id prekladiste*/
		int destination = node.getIdCont();
		
		/* Cas po prijezdu do pivovaru */
		Time tmpTime = instructions.get(instructions.size() - 1).getFinished();
		
		int i = destination;
		
		while(p[0][i] != 0){
			int tmp = p[0][i];
			float distance = d[0][i] - d[0][tmp];
			
			tmpTime = tmpTime.getTimeAfterMinutes((int)(distance/ct.getSpeed()));
			
			instructions.add(new Instruction(State.TRAVELLING,Controller.c.nodes.get(tmp),tmpTime));
			
			i = tmp;
		}
		
		float distance = d[0][i];
		tmpTime = tmpTime.getTimeAfterMinutes((int)(distance/ct.getSpeed()));
		
		instructions.add(new Instruction(State.TRAVELLING,Controller.c.nodes.get(0),tmpTime));
		
		if(ct != CarType.CISTERN){
			/* Cas vylozeni prazdnych sudu */
			tmpTime = tmpTime.getTimeAfterMinutes(sum*ct.getReloadingSpeed());
			
			/* 7. instrukce - vylozeni prazdnych sudu v pivovaru */
			instructions.add(new Instruction(State.UNLOADING_EMPTY_BARRELS, this, sum, tmpTime));
		}
		
		
	}	
	
	/**
	 * Vytvori mnozinu uzlu prujezdu do prekladiste
	 * @param nodes			mnozina uzlu
	 * @param instructions	sada instrukci
	 * @param ct typ auta
	 */
	public void addPathInstructions(Stack<Integer> nodes, List<Instruction> instructions, CarType ct){
		
		/* Cas konce nakladu */
		Time t = instructions.get(instructions.size() - 1).getFinished();
		
		int y = 0;
		float oldDistance = 0;
		while(!nodes.empty()){
			y = nodes.pop();

			float distance = d[0][y] - oldDistance;
			
			t = t.getTimeAfterMinutes((int)(distance/ct.getSpeed()));
			
			instructions.add(new Instruction(State.TRAVELLING,Controller.c.nodes.get(y),t));
			oldDistance = d[0][y];
		}
	}
	
	/**
	 * Prudukce piva
	 */
	private void produceBeer(){
		if(c.mainTime.getHour() == 13){
			if((this.full + 307) < MAX_BREWERY_CAPACITY){
				full += 307;
				logger.log(c.mainTime, 6, this + ": vyprodukovano 307 hl");
				countBeers += 307;
			}
		}
		else{
			if((this.full + 291) < MAX_BREWERY_CAPACITY){
				full += 291; 
				logger.log(c.mainTime, 6, this + ": vyprodukovano 291 hl");
				countBeers += 291;
			}
		}
	}
	
	/**
	 * Vrati nazev pivovaru
	 * @return	nazev pivovaru
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * kamion : full - 100/2 hl
	 * cisterna : full - x hl
	 * @param n mnozstvi
	 */
	public void unload(int n){
		this.full -= n;
	}
	
	
	/**
	 * Vrati celkovou produkci piva
	 * @return	produkce piva
	 */
	public int getCountBeers() {
		return countBeers;
	}

	/**
	 * Vrati prvni cekajici cisternu
	 * @return prvni cekajici cisterna
	 */
	private Car getFirstWaitingCistern(){
		for(Car c: garageCistern){
			if(c.getCurrentInstruction() == null && c.getPosition().equals(this)){
				return c;
			}
		}
		Car newCar = Car.getCistern(this, garageCistern.size());
		
		garageCistern.add(newCar);
		return newCar;
	}
	
	/**
	 * Vrati prvni cekajici camion
	 * @return prvni cekajici camion
	 */
	private Car getFirstWaitingCamion(){
		for(Car c: garageCamion){
			if(c.getCurrentInstruction() == null && c.getPosition().equals(this)){
				return c;
			}
		}
		Car newCar = Car.getCamion(this, garageCamion.size());
		
		garageCamion.add(newCar);
		return newCar;
	}
			
	/**
	 * Pomoci dijkstrova algoritmu vypocte nejkratsi cesty v grafu
	 */
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
	
	@Override
	public String toString(){
		return "Pivovar " + name;
	}

	@Override
	public String tempInfo() {
		StringBuilder sb = new StringBuilder("Pivovar ").append(name).append("\n");
		sb.append("Pocet kamionu: ").append(garageCamion.size()).append("\n");
		sb.append("Pocet cisteren: ").append(garageCistern.size()).append("\n");
		sb.append("Stav: ").append(full).append(" hl\n");
		sb.append("Pocet resenych objednavek: ").append(beingPrepared.size()).append("\n");
		return sb.toString();
	}

	@Override
	public String finalInfo() {
		StringBuilder sb = new StringBuilder(toString()).append("\n");
		
		sb.append("Cisterny: \n");
		for(Car c : garageCistern){
			sb.append(c.finalInfo()).append("\n");
		}
		
		sb.append("Kamiony: \n");
		for(Car c: garageCamion){
			sb.append(c.finalInfo()).append("\n");
		}
		
		return "-------------------\n" + sb.toString() + "-------------------\n";
	}
}
