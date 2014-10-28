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
//	private Logger logger = Logger.getInstance();
	private final List<List<Instruction>> tomorrow = new LinkedList<List<Instruction>>();
	private int createdCars = 0;
	
	public Dock(int idCont, float x, float y){
		this.provider = this;
		this.idCont = idCont;
		this.idProv = 0;
		customers.add(this);
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Provede vsechny udalosti, co se staly za dany krok v case to jest:
	 * <li>Urci cestu nakladniho automobilu</li>
	 * <li>Vysle dalsi auta na cestu</li>
	 * <li>Posune auta o dany casovy usek</li>
	 */
	public void checkTimeEvents(){
//		System.out.println(orders);
		//Pocitani a planovani cest
//		Time t = new Time(0,16,0);
		if(c.mainTime.getHour() == 8){
			for(List<Instruction> inst:tomorrow){
				Car c = getFirstWaitingTruck();
				c.setInstructions(inst);
			}
			tomorrow.remove(tomorrow);
		}
		
		if(c.mainTime.getHour() > 8 && c.mainTime.getHour() < 16){
//			createInstructions(orders);
			prepareOrders();
			logger.log(c.mainTime, 6, this + " vytvorilo " + createdCars + " novych aut");
			createdCars = 0;
		}
		
		
		
		//Konani pohybu uz zamestnanych aut
		moveCars();
	}
	
	public void prepareOrders(){
		
		while(!orders.isEmpty()){
			SortedSet<Order> selected = new TreeSet<Order>(cmp);
			Order o = orders.last();
			selected.add(o);
			orders.remove(o);
			Pub p = o.getPub();
			
			checkNeighbours(p,selected);
			
			Stack<Integer> s = new Stack<Integer>();
			
			prepareStackForPath(0, p.idProv, s);
			
			while(!s.isEmpty()){
				int i = s.pop();
				Pub tmp = ((Pub)customers.get(i));
				checkPub(tmp,selected);
//				checkNeighbours(tmp,selected);
				
				
			}
			
			beingPrepared.addAll(selected);
			createInstructions(selected);
			
			
		}
	}
	
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
	public void moveCars(){
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
	
	private void finishInstruction(Car car, Instruction i){
		car.setPosition(i.getDestination());
		
		
		switch(i.getState()){
			case LOADING: car.load(i.getAmount());break;
			case UNLOADING: car.unload(i.getAmount());break;
			case LOADING_EMPTY_BARRELS: car.loadEmpty(i.getAmount());break;
			case UNLOADING_EMPTY_BARRELS: car.unloadEmpty(i.getAmount());break;
			default: break;
		}
		
		logger.log(i.getFinished(), 4, car + " " + i.getState().getStrFin() + " " + i.getDestination());
		
		if(i.getOrder() != null){
			deliverOrder(i.getOrder(),car);
		}
	}
	
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
	public int checkCarCapacity(Set<Order> orders){
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
	 * Priradi prvnimu volnemu autu posloupnost instrukci, ktere ma provezt cestou do hospod
	 * @param orders Vsechny objednavky ktere musi cestou vyridit
	 */
	public void createInstructions(Set<Order> orders){
		//Urceni kolik ma auto nalozit sudu a pridani odpovidajicich instrukci
		int sum = checkCarCapacity(orders);
		List<Instruction> instructions = new LinkedList<Instruction>();
		addFirstInstructions(instructions,c.mainTime, sum);
		
		//Oriznuti objednavek ktere nestihame + a vraceni posledni objednavky
		Order order = checkTimeOfDelivery(orders, instructions);
		
		//odebrani poctu sudu od objednavek, ktere vyrizovat nebudeme
		for(Order o:orders){
			sum -= o.getAmount();
		}
		
		//Pokud po odmazani nepotrebnych instrukci nezbylo nic, nema cenu podnikat cestu
		if(instructions.size() == 0){
			return;
		}

		correctTime(instructions,sum);
		
		createInstructionsPathHome(instructions, order.getPub(), sum);
		
		
		Car c = getFirstWaitingTruck();
		c.setInstructions(instructions);
	}
	
	private Order checkTimeOfDelivery(Set<Order> orders, List<Instruction> instructions){
		Order order = null;
		for(Iterator<Order> it = orders.iterator(); it.hasNext();){
			Order o = it.next();
			
			createTravellingInstructions(instructions,o);
			if(instructions.get(instructions.size()-1).getFinished().getHour() >= 16){
				SortedSet<Order> tmpOrd = new TreeSet<Order>(cmp);
				tmpOrd.addAll(orders);
				List<Instruction> tomorrow = deliverTomorrow(tmpOrd);
				if(tomorrow != null){
					this.tomorrow.add(tomorrow);
					removeUnnecessaryInstr(instructions);
					break;
				}
			}
			
			order = o;
			it.remove();
		}
		
		return order;
	}
	
	private void addFirstInstructions(List<Instruction> instructions, Time t, int sum){
		int loadingMinutes = sum*CarType.TRUCK.getReloadingSpeed();
		
		Time tmpTime = new Time(t.value());
		instructions.add(0,new Instruction(State.WAITING, this, tmpTime));
		
		tmpTime = tmpTime.getTimeAfterMinutes(loadingMinutes);
		
		instructions.add(1,new Instruction(State.LOADING, this, sum, tmpTime));
	}
	
	private List<Instruction> deliverTomorrow(Set<Order> orders){
	
		int sum = checkCarCapacity(orders);
		List<Instruction> instructions = new LinkedList<Instruction>();
		addFirstInstructions(instructions, new Time(c.mainTime.getDay() + 1, 8, 0), sum);
				
		Order order = null;
		for(Iterator<Order> it = orders.iterator(); it.hasNext();){
			Order o = it.next();
			createTravellingInstructions(instructions,o);
			if(instructions.get(instructions.size() - 1).getFinished().value() > o.getTime().getTimeAfterMinutes(60*24).value()){
				//nestihame
				return null;
			}
			order = o;
			it.remove();
		}
		createInstructionsPathHome(instructions, order.getPub(), sum);

		return instructions;
	}
	
	
	public List<List<Instruction>> getTomorrow() {
		return tomorrow;
	}

	private void correctTime(List<Instruction> instructions, int sum){
		int correct = instructions.get(1).getFinished().value() - c.mainTime.getTimeAfterMinutes(sum*CarType.TRUCK.getReloadingSpeed()).value();

		if(correct != 0){
			for(int i = 1; i < instructions.size(); i++){
				instructions.get(i).getFinished().subMinutes(correct);
			}
		}
	}
	
	private void removeUnnecessaryInstr(List<Instruction> instructions){
		LinkedList<Instruction> linkedInstructions = (LinkedList<Instruction>)instructions;
		do{
			linkedInstructions.remove(instructions.size() -1);
		}while(linkedInstructions.size() != 0 && linkedInstructions.getLast().getOrder() == null);
	}
	
	/**
	 * Vezme danou polohu auta a vypocte pro nej instrukce tak aby se dostal do mista, ktere danou objednavku objednalo
	 * @param c Auto kteremu budeme davat instrukce
	 * @param o Objednavka, kterou musi auto vyridit
	 */
	public void createTravellingInstructions(List<Instruction> instructions, Order o){

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
		
		t = t.getTimeAfterMinutes(reloadingMinutes);
		instructions.add(new Instruction(State.UNLOADING,o.getPub(), o.getAmount(), t));
		t = t.getTimeAfterMinutes(reloadingMinutes);
		instructions.add(new Instruction(State.LOADING_EMPTY_BARRELS,o.getPub(), t, o.getAmount(), o));
		
	}
	
	/**
	 * Naplni zasobnik id bodu, ktere musi auto navstivit, pokud chce dorazit z bodu {@code source} do bodu {@code destination}
	 * @param source id pocatecniho bodu
	 * @param destination id ciloveho bodu
	 * @param nodes zasobnik kde budeme ukladat uzly
	 */
	public void prepareStackForPath(int source, int destination, Stack<Integer> nodes){
		int i = destination;
		nodes.push(i);
		//poradi je treba prohodit, proto se uziva zasobniku
		while(p[source][i] != 0){
			int tmp = p[source][i];
			nodes.push(tmp);
			i = tmp;
		}
	}
	
	public void createInstructionsPathHome(List<Instruction> instructions, TransportNode n, int sum){
		int source = n.idProv;
		int destination = 0;
		Stack<Integer> nodes = new Stack<Integer>();
		
		prepareStackForPath(source, destination, nodes);
		
		addPathInstructions(source, instructions, nodes);
		
		Time t = ((LinkedList<Instruction>)instructions).getLast().getFinished();
		
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
	public void addPathInstructions(int i, List<Instruction> instructions, Stack<Integer> nodes){
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
	public Car getFirstWaitingTruck(){
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
	
	public String toString(){
		return "Prekladiste " + idCont;
	}
}
