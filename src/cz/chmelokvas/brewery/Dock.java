package cz.chmelokvas.brewery;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

import cz.chmelokvas.util.Route;

public class Dock extends Stock {
	
	/**
	 * Seznam instrukci, ktere autum predame zitra v osm
	 */
	private final List<List<Instruction>> tomorrow = new LinkedList<List<Instruction>>();
	
	/**
	 * Pocet vytvorenych aut v dane hodine
	 */
	private int createdCars = 0;

	private static final int MAX_DOCK_CAPACITY = 2000;

	

	/**
	 * Vytvori nove prekladiste na danych souradnicich a prida mu dane id pro kontroler
	 * vlozi sebe samo na prvni index v poli svych zakazniku
	 * @param idCont id u kontroleru
	 * @param x x-ova souradnice
	 * @param y y-ova souradnice
	 */
	public Dock(int idCont, float x, float y){
		this.provider = this;
		this.idCont = idCont;
		this.idProv = 0;
		customers.add(this);
		this.x = x;
		this.y = y;
		
		// pro testovani je pri vytvoreni prekladiste plne plnych sudu
		this.full = MAX_DOCK_CAPACITY;
		this.empty = 0;
	}
	
	/**
	 * Provede vsechny udalosti, co se staly za dany krok v case to jest:
	 * <li>Urci cestu nakladniho automobilu
	 * <li>Vysle dalsi auta na cestu
	 * <li>Posune auta o dany casovy usek
	 */
	public void checkTimeEvents(){
		//Predani instrukci ze vcerejska
		if(c.mainTime.getHour() == 8){
			for(List<Instruction> inst:tomorrow){
				Car c = getFirstWaitingTruck();
				c.setInstructions(inst);
			}
			tomorrow.remove(tomorrow);
		}
		
		//vyrizovani objednavek
		if(c.mainTime.getHour() > 8 && c.mainTime.getHour() < 16){
			prepareOrders();
			logger.log(c.mainTime, 6, this + " vytvorilo " + createdCars + " novych aut");
			createdCars = 0;
		}

		//Konani pohybu uz zamestnanych aut
		moveCars();
		
		if(checkDockCapacityForCamion()){
			//TODO makeOrder() pro pivovar
		}
	}
	
	/**
	 * Pro vsechny jiz prijmute objednavky urci cesty, kterymi budou reseny
	 */
	private void prepareOrders(){
		while(!orders.isEmpty()){
			//vybrane objednavky musi byt serazeny podle vzdalenosti od prekladiste
			SortedSet<Order> selected = new TreeSet<Order>(cmp);
			Order o = orders.last();
			selected.add(o);
			orders.remove(o);
			Pub p = o.getPub();
			
			//pokud sou u nejvzdalenejsi hospody sousedi, kteri jiz objednali zahrn je take do cesty
			checkNeighbours(p,selected);
			
			Stack<Integer> s = new Stack<Integer>();
			
			prepareStackForPath(0, p.idProv, s);
			
			//vezme hospody po ceste a pokud si take objednaly tak je take prida
			while(!s.isEmpty()){
				int i = s.pop();
				Pub tmp = ((Pub)customers.get(i));
				checkPub(tmp,selected);		
			}
			
			//vybrane objednavky oznaci jako za pripravovane a zacne pripravovat instrukce pro cestu auta
			beingPrepared.addAll(selected);
			createInstructions(selected);
			
			
		}
	}
	
	/**
	 * Zjisti zda dana hospoda jiz objednala a jestli ano vlozi ji do dane mnoziny
	 * @param p Zkoumana hospoda
	 * @param selected Mnozina kam ji pridavame
	 */
	private void checkPub(Pub p, Set<Order> selected){
		Order today = p.getTodayOrder();
		Order yesterday = p.getYesterdayOrder();
		//zkontroluje zda jiz neobjednala dnes
		if(today != null && orders.contains(today)){
			selected.add(today);
			orders.remove(today);
		}
		//zkontroluje zda neobjednala vcera
		if(yesterday != null && orders.contains(yesterday)){
			selected.add(yesterday);
			orders.remove(yesterday);
		}
	}
	
	/**
	 * Pro vsechny sousedy hospody p ze stejneho sektoru zkontrolujeme jestli take neobjednali
	 * a kdyz ano tak je prida k vybranym
	 * @param p Hospoda jejiz sousedy zkoumame
	 * @param selected Vybrane hospody pro cestu
	 */
	private void checkNeighbours(Pub p, Set<Order> selected){
		for(Route r: p.routes){
			TransportNode neighbourN = c.nodes.get(r.getValue());
			if(neighbourN instanceof Pub && neighbourN.provider.equals(this)){
				Pub neighbour = (Pub)neighbourN;
				checkPub(neighbour, selected);
			}
		}
	}
	
	/**
	 * Pokud dane auto za dany krok casu neco udelalo, jsou vypsany vykonane instrukce a je zadana nova instrukce
	 */
	private void moveCars(){
		for(Car car : garage){
			
			Instruction i = car.getCurrentInstruction();
			while(i != null && i.getFinished().value() >= c.mainTime.value() && i.getFinished().value() < (c.mainTime.value() + 60)){
				//vykonani potrebne aktivity pred prechodem na dalsi instrukci
				finishInstruction(car, i);
				
				//prechod na dalsi instrukci
				i = startNewInstructions(car);
			}
		}
	}
	
	private boolean isCarInDock(Car car){
		return car.getStock().equals(car.getPosition());
	}
	
	/**
	 * Predane auto dokonci danou instrukci
	 * @param car Predane auto
	 * @param i Instrukce, kterou ma auto vykonat
	 */
	private void finishInstruction(Car car, Instruction i){
		car.setPosition(i.getDestination());
		
		
		switch(i.getState()){
			case LOADING: car.load(i.getAmount());break;
			case UNLOADING: car.unload(i.getAmount());break;
			case LOADING_EMPTY_BARRELS: car.loadEmpty(i.getAmount());break;
			case UNLOADING_EMPTY_BARRELS:
				car.unloadEmpty(i.getAmount());
				if(isCarInDock(car)){
					checkDockCapacity(i.getAmount());
				};break;
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
	private Instruction startNewInstructions(Car car){
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
	 * Zkontroluje, zda se dane objednavky vejdou do auta a pokud ne, vrati je zpet pro zpracovani dalsich objednavek
	 * @param car Auto, do ktereho budeme chti nakladat
	 * @param orders Objednavky, ktere ma dane auto stihnout
	 * @return Celkovy pocet, ktery mame do auta nalozit
	 */
	private int checkCarCapacity(Set<Order> orders){
		int sum = 0;
		for(Iterator<Order> it = orders.iterator(); it.hasNext();){
			Order o = it.next();
			
			if((sum + o.getAmount()) < (CarType.TRUCK.getCapacity()*3)/4){
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
	 * Kontrola stavu sudu v prekladisti
	 * @param count kladne cislo pridava prazdne sudy, zaporne odebira plne sudy
	 * @return	true - pri objednavce mohou byt sudy nalozeny, pri vraceni vraceny
	 * 			false - pri objednavce neni dostatek sudu v prekladisti, prekladiste je plne sudu
	 */
	public boolean checkDockCapacity(int count){
		int tmpFull = full;
		int tmpEmpty = empty;

		if(count < 0 && (tmpFull += count) >= 0){
			full += count;
			return true;
		}
		if(count > 0 && (tmpEmpty += count) <= MAX_DOCK_CAPACITY){
			empty += count;
			return true;
		}
		return false;
	}
	
	/**
	 * Kontrola zda se cely naklad kamionu vejde do prekladiste
	 * @return	true	kamion muze vyrazit doplnit prekladiste
	 */
	public boolean checkDockCapacityForCamion(){
		return (CarType.CAMION.getCapacity() + full) <= MAX_DOCK_CAPACITY;
	}
	
	/**
	 * Priradi prvnimu volnemu autu posloupnost instrukci, ktere ma provezt cestou do hospod
	 * @param orders Vsechny objednavky ktere musi cestou vyridit
	 */
	private void createInstructions(Set<Order> orders){
		//Urceni kolik ma auto nalozit sudu a pridani odpovidajicich instrukci
		int sum = checkCarCapacity(orders);
		 List<Instruction> instructions = addFirstInstructions(c.mainTime, sum);
		
		//Oriznuti objednavek ktere nestihame
		checkTimeOfDelivery(orders, instructions);
		
		//odebrani poctu sudu od objednavek, ktere vyrizovat nebudeme 
		//(vyrizovane byly z mnoziny odebrany)
		for(Order o:orders){
			sum -= o.getAmount();
		}
		
		//Pokud po odmazani nepotrebnych instrukci nezbylo nic, nema cenu podnikat cestu
		if(instructions.size() == 0){
			return;
		}

		correctTime(instructions,sum);
		
		createInstructionsPathHome(instructions, sum);
		
		
		Car c = getFirstWaitingTruck();
		c.setInstructions(instructions);
	}
	
	/**
	 * Projde mnozinu objednavek, a zkusi pro kazdy prvek vypocitat cestu.<br>
	 * Pokud zjisti, ze se to neda stihnout, zkusi to naplanovat na zitra.<br>
	 * Pokud bychom to ani zitra nestihli, doruci to jeste dnes ale opozdene
	 * @param orders Mnozina vyrizovanych objednavek
	 * @param instructions Instrukce, ktere budeme modifikovat pro cesty k vyrizeni danych objednavek
	 * @return Posledni objednavka, ktera bude dnes vyrizena
	 */
	private Order checkTimeOfDelivery(Set<Order> orders, List<Instruction> instructions){
		Order order = null;
		for(Iterator<Order> it = orders.iterator(); it.hasNext();){
			Order o = it.next();
			
			createTravellingInstructions(instructions,o);
			if(instructions.get(instructions.size()-1).getFinished().getHour() >= 16){
				SortedSet<Order> tmpOrd = new TreeSet<Order>(cmp);
				tmpOrd.addAll(orders);
				
				//Zkus odeslat zitra
				List<Instruction> tomorrow = deliverTomorrow(tmpOrd);
				
				//Pokud bychom to nestihli, udelej to dnes ale dorazis pozdeji
				if(tomorrow != null){
					this.tomorrow.add(tomorrow);
					removeUnnecessaryInstr(instructions);
					break;
				}
			}
			
			order = o;
			//Pokud bude cesta realizovana, odstranime tuto objednavku z mnoziny
			it.remove();
		}
		
		return order;
	}
	
	

	/**
	 * Vytvori pocatecni instrukce zacinajici v case t.<br>
	 * Prvni instrukce bude, ze bude cekat do daneho casu<br>
	 * Dale bude nakladat sum*(rychlost nakladani) minut
	 * @param t Cas kdy ma byt ukoncena prvni instrukce
	 * @param sum Mnozstvi, ktere ma auto pomoci druhe instrukce nalozit
	 * @return Spojovy seznam instrukci
	 */
	private List<Instruction> addFirstInstructions(Time t, int sum){
		List<Instruction> instructions = new LinkedList<Instruction>();
		int loadingMinutes = sum*CarType.TRUCK.getReloadingSpeed();
		
		//vytvareni uplne prvni instrukce
		Time tmpTime = new Time(t.value());
		instructions.add(0,new Instruction(State.WAITING, this, tmpTime));
		
		//vytvareni instrukce nakladani
		tmpTime = tmpTime.getTimeAfterMinutes(loadingMinutes);
		instructions.add(1,new Instruction(State.LOADING, this, sum, tmpTime));
		
		return instructions;
	}
	
	/**
	 * Zkusi naplanovat doruceni danych objednavek pristi den
	 * @param orders Mnozina vyrizovanych objednavek
	 * @return Seznam  Instrukci pro cestu zitra, nebo null, pokud bychom to zitra nestihli
	 */
	private List<Instruction> deliverTomorrow(Set<Order> orders){
		int sum = checkCarCapacity(orders);
		List<Instruction> instructions = addFirstInstructions(new Time(c.mainTime.getDay() + 1, 8, 0), sum);
				
		for(Iterator<Order> it = orders.iterator(); it.hasNext();){
			Order o = it.next();
			createTravellingInstructions(instructions,o);
			if(instructions.get(instructions.size() - 1).getFinished().value() > o.getTime().getTimeAfterMinutes(60*24).value()){
				//nestihame
				return null;
			}
			it.remove();
		}
		createInstructionsPathHome(instructions, sum);

		return instructions;
	}
	
	/**
	 * Vrati seznam vsech seznamu instrukci na zitrek
	 * @return Seznam vsech seznamu instrukci na zitrek
	 */
	public List<List<Instruction>> getTomorrow() {
		return tomorrow;
/*=======

		createInstructionsPathHome(car, order.getPub(), sum);
>>>>>>> Kontrola stavu zasob v prekladisti*/
	}
	
	
	/**
	 * Pokud jsme modifikovali mnozstvi objednavek, ktere chceme dorucit,
	 * musime posunout instrukce v case, jelikoz se nam timpadem zmenila doba nakladani
	 * @param instructions Seznam instrukci ktery chceme modifikovat
	 * @param sum Nove mnozstvi sudu, ktere je treba nalozit
	 */
	private void correctTime(List<Instruction> instructions, int sum){
		int correct = instructions.get(1).getFinished().value() - c.mainTime.getTimeAfterMinutes(sum*CarType.TRUCK.getReloadingSpeed()).value();

		if(correct != 0){
			for(int i = 1; i < instructions.size(); i++){
				instructions.get(i).getFinished().subMinutes(correct);
			}
		}
	}
	
	/**
	 * Z daneho seznamu odstrani nepotrebne instrukce,
	 * ktere slouzily k obslouzeni posledni objednavky, ktera jiz vyrizovana nebude
	 * @param instructions
	 */
	private void removeUnnecessaryInstr(List<Instruction> instructions){
		LinkedList<Instruction> linkedInstructions = (LinkedList<Instruction>)instructions;
		do{
			linkedInstructions.remove(instructions.size() -1);
		}while(linkedInstructions.size() != 0 && linkedInstructions.getLast().getOrder() == null);
	}
	
	/**
	 * Z mista, kde by se auto nachazelo po vykonani posledni instrukce v {@code instructions}
	 * zjistime cestu do mista, kde mame vyridit objednavku o a pro kazdy prujezd uzlem
	 * musime vypocitat instrukci kdy jim projede
	 * @param instructions Seznam instrukci pro auto
	 * @param o Dalsi objednavka ktera ma byt vyrizena
	 */
	private void createTravellingInstructions(List<Instruction> instructions, Order o){

		Instruction in = ((LinkedList<Instruction>)instructions).getLast();
		
		TransportNode source = in.getDestination();
		TransportNode destination = o.getPub();
		
		
		Stack<Integer> nodes = new Stack<Integer>();
		
		int i = source.idProv;
		int j = destination.idProv;
		
		prepareStackForPath(i, j, nodes);
		
		addPathInstructions(i, instructions, nodes);
		
		int reloadingMinutes = o.getAmount()*CarType.TRUCK.getReloadingSpeed();
		
		Time t = instructions.get(instructions.size() -1).getFinished();
		
		/* Instrukce vyrizovani objednavky.
		 * U posledni instrukce (nakladani prazdnych sudu) je dana i objednavka.
		 * Auto tak vi, ze po vykonani teto instrukce je objednavka vyrizena
		 */
		t = t.getTimeAfterMinutes(reloadingMinutes);
		instructions.add(new Instruction(State.UNLOADING,o.getPub(), o.getAmount(), t));
		t = t.getTimeAfterMinutes(reloadingMinutes);
		instructions.add(new Instruction(State.LOADING_EMPTY_BARRELS,o.getPub(), t, o.getAmount(), o));
		
	}
	
	/**
	 * Naplni zasobnik id bodu, ktere musi auto navstivit,
	 * pokud chce dorazit z bodu {@code source} do bodu {@code destination}
	 * @param source id pocatecniho bodu
	 * @param destination id ciloveho bodu
	 * @param nodes zasobnik kde budeme ukladat uzly
	 */
	private void prepareStackForPath(int source, int destination, Stack<Integer> nodes){
		int i = destination;
		nodes.push(i);
		//poradi je treba prohodit, proto se uziva zasobniku
		while(p[source][i] != 0){
			int tmp = p[source][i];
			nodes.push(tmp);
			i = tmp;
		}
	}
	
	
	/**
	 * Vytvori instrukce pro cestu 
	 * a posleze i instrukce pro vylozeni daneho mnozstvi prazdnych sudu
	 * @param instructions Dany seznam instrukci ktery modifikujeme 
	 * @param sum Mnozstvi prazdnych sudu, ktere je treba vylozit
	 */
	private void createInstructionsPathHome(List<Instruction> instructions, int sum){
		Instruction last = ((LinkedList<Instruction>)instructions).getLast();
		
		int source = last.getDestination().idProv;
		int destination = 0;
		
		Time t = last.getFinished();
		
		Stack<Integer> nodes = new Stack<Integer>();
		
		prepareStackForPath(source, destination, nodes);
		
		addPathInstructions(source, instructions, nodes);
				
		t = t.getTimeAfterMinutes(sum*CarType.TRUCK.getReloadingSpeed());
		
		instructions.add(new Instruction(State.UNLOADING_EMPTY_BARRELS, this, sum, t));

	}
	
	/**
	 * Vytvori instrukce, ktere budou urcovat danemu autu cestu pres uzly, ktere jsou ulozeny v zasobniku
	 * @param i uzel ve kterem se auto nachazi slo by ho zjistit pomoci
	 *  {@code ((LinkedList<Instruction>)c.getInstructions()).getLast().getDestination().idProv},
	 *   coz ale uznejme je hodne slozite a stejne jsme jiz hodnotu jednou ziskali
	 * @param c Auto, kteremu dane instrukce predame
	 * @param nodes Zasobnik, ktery obsahuje uzly, pres ktere auto pojede
	 */
	private void addPathInstructions(int i, List<Instruction> instructions, Stack<Integer> nodes){
		Time t = ((LinkedList<Instruction>)instructions).getLast().getFinished();
		int x = i, y = 0;
		while(!nodes.empty()){
			y = nodes.pop();

			float distance = d[x][y];
			
			t = t.getTimeAfterMinutes((int)(distance/CarType.TRUCK.getSpeed()));
			
			instructions.add(new Instruction(State.TRAVELLING,customers.get(y),t));
			
			x = y;
		}
	}
	
	/**
	 * Vrati prvni cekajici nakladak
	 * @return prvni cekajici nakladak
	 */
	private Car getFirstWaitingTruck(){
		for(Car c: garage){
			if(c.getCurrentInstruction() == null && c.getPosition().equals(this)){
				return c;
			}
		}
		Car newCar = Car.getTruck(this, garage.size());
		garage.add(newCar);
//		System.out.println("Vytvarim nove auto!");
		createdCars++;
		return newCar;
	}
	
	@Override
	public String toString(){
		return "Prekladiste " + idCont;
	}
}
