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
	
	private final List<List<Instruction>> tomorrow = new LinkedList<List<Instruction>>();
	
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
		
		if(c.mainTime.getHour() > 8 && c.mainTime.getHour() <= 16){
//			createInstructions(orders);
			prepareOrders();
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
			
//			System.out.println(p + " " + d[0][p.idProv]);
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
			createInstructions(selected, 0);
			
			
		}
	}
	
	private void checkPub(Pub p, Set<Order> selected){
		Order today = p.getTodayOrder();
		Order yesterday = p.getYesterdayOrder();
		if(orders.contains(today)){
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
	 * Pokud dane auto za dany krok casu neco udelalo, je tento krok vypsan a je zadana nova instrukce
	 */
	public void moveCars(){
		for(Car car : garage){
			
			Instruction i = car.getCurrentInstruction();
//			System.out.println(car + " " + car.getInstructions() + (i != null && i.getFinished().value() >= c.mainTime.value() && i.getFinished().value() < (c.mainTime.value() + 60)));
//			System.out.println((i.getFinished().value() >= c.mainTime.value()) + " " + (i.getFinished().value() < (c.mainTime.value() + 60)) + " " + i.getFinished());
			while(i != null && i.getFinished().value() >= c.mainTime.value() && i.getFinished().value() < (c.mainTime.value() + 60)){
				//vykonani potrebne aktivity pred prechodem na dalsi instrukci
				car.setPosition(i.getDestination());
				
				
				switch(i.getState()){
					case LOADING: car.load(i.getAmount());break;
					case UNLOADING: car.unload(i.getAmount());break;
					case LOADING_EMPTY_BARRELS: car.loadEmpty(i.getAmount());break;
					case UNLOADING_EMPTY_BARRELS: car.unloadEmpty(i.getAmount());break;
					default: break;
				}
				
				System.out.println(i.getFinished() + " " + car + " " + i.getState().getStrFin() + " " + i.getDestination() + "\t" + car.getInstructions());
				
				if(i.getOrder() != null){
//					System.out.println("----" + i.getOrder());
					deliverOrder(i.getOrder(),car);
				}
				
				//prechod na dalsi instrukci
//				((LinkedList<Instruction>)car.getInstructions()).removeFirst();
				car.getInstructions().remove(0);
				
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
	public void createInstructions(Set<Order> orders, int depth){
//		Car car = getFirstWaitingTruck();
		
		//Urceni kolik ma auto nalozit sudu a pridani odpovidajicich instrukci
		int sum = checkCarCapacity(orders);
//		int loadingMinutes = sum*CarType.TRUCK.getReloadingSpeed();
		
		List<Instruction> instructions = new LinkedList<Instruction>();
		
		addFirstInstructions(instructions,c.mainTime, sum);
		/*
		Time tmpTime = new Time(c.mainTime.value());
		instructions.add(0,new Instruction(State.WAITING, this, tmpTime));
		
		tmpTime = tmpTime.getTimeAfterMinutes(loadingMinutes);
		instructions.add(0,new Instruction(State.LOADING, this, sum, tmpTime));
	*/	
		Order order = checkTimeOfDelivery(orders, instructions, depth);
//		System.out.println(orders.isEmpty());
		
//		System.err.println(orders.size());
//		boolean deliverTomorrow = car.getLastInstruction().getFinished().getHour() >= 16;
		
		
//		System.err.println(orders);
		
		for(Order o:orders){
//			System.err.println("odebiram");
			
			sum -= o.getAmount();
		}
		
		if(!orders.isEmpty()){
			/*if(depth == 3){
				System.err.println("dsfd");
			}*/
			if(depth >= 4){
//				System.err.println(orders);
				tomorrow.add(deliverTomorrow(orders));
			}else {
//				System.err.println(orders);
				createInstructions(orders, depth + 1);
			}
		}
		
		if(instructions.size() == 0){
			return;
		}
//		System.err.println(instructions);
		correctTime(instructions,sum);
//		System.err.println(instructions + "\n\n");
//		System.out.println(instructions.get(instructions.size()-1));
		
		createInstructionsPathHome(instructions, order.getPub(), sum);
		
		
		Car c = getFirstWaitingTruck();
//		System.out.println(instructions);
		c.setInstructions(instructions);
		/*if(deliverTomorrow){
			Time eightTomorrow = new Time(c.mainTime.getDay() + 1, 8, 0);
			int delay = eightTomorrow.value() - car.getCurrentInstruction().getFinished().value();
			for(Instruction i : car.getInstructions()){
				i.getFinished().addMinutes(delay);
			}
		}*/
	}
	
	private Order checkTimeOfDelivery(Set<Order> orders, List<Instruction> instructions, int depth){
		Order order = null;
		for(Iterator<Order> it = orders.iterator(); it.hasNext();){
			Order o = it.next();
			
			createTravellingInstructions(instructions,o);
			if(instructions.get(instructions.size()-1).getFinished().getHour() >= 16){
				if(depth >= 3){
					removeUnnecessaryInstr(instructions);
					break;
				}else{
					it.remove();
					return o;
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
		/*if(tmpTime.getHour() >= 16){
			System.err.println("ani to nezkousej");
		}*/
		instructions.add(1,new Instruction(State.LOADING, this, sum, tmpTime));
	}
	
	private List<Instruction> deliverTomorrow(Set<Order> orders){
	
		int sum = checkCarCapacity(orders);
		List<Instruction> instructions = new LinkedList<Instruction>();
		addFirstInstructions(instructions, new Time(c.mainTime.getDay() + 1, 8, 0), sum);
		
//		Order order = checkTimeOfDelivery(orders, instructions);
		
		Order order = null;
		for(Iterator<Order> it = orders.iterator(); it.hasNext();){
			Order o = it.next();
			createTravellingInstructions(instructions,o);
			/*if(instructions.get(instructions.size()-1).getFinished().getHour() >= 16){
				removeUnnecessaryInstr(instructions);
				break;
			}*/
			order = o;
			it.remove();
		}
		
	/*	if(!orders.isEmpty()){
			tomorrow.add(deliverTomorrow(orders));
		}
		
		for(Order o:orders){
//			System.err.println("odebiram");
			
			sum -= o.getAmount();
		}
		
		correctTime(instructions,sum);*/
		
//		System.err.println("Pred pocitanim cesty domu: " + orders.size());
		createInstructionsPathHome(instructions, order.getPub(), sum);
		
//		System.err.println(c.mainTime + "chci vyridit zitra: " + instructions);
		
		return instructions;
	}
	
	
	public List<List<Instruction>> getTomorrow() {
		return tomorrow;
	}

	private void correctTime(List<Instruction> instructions, int sum){
		int correct = instructions.get(1).getFinished().value() - c.mainTime.getTimeAfterMinutes(sum*CarType.TRUCK.getReloadingSpeed()).value();

//		System.err.println(correct);
		/*if(correct < 0){
//			System.err.println("dsfsdfdsfsdfdfsdfsdfsdfdsfdfdsfsfsdf");
		}*/
//		System.err.println("KOREKTIM!!!!");
		for(int i = 1; i < instructions.size(); i++){
//			System.err.println(instructions.get(i).getFinished());
			instructions.get(i).getFinished().subMinutes(correct);
//			System.err.println(instructions.get(i).getFinished());
		}
	}
	
	private void removeUnnecessaryInstr(List<Instruction> instructions){
//		System.out.println("xdfd");
		LinkedList<Instruction> linkedInstructions = (LinkedList<Instruction>)instructions;
		do{
//			System.err.println(linkedInstructions.remove(instructions.size() -1));
			linkedInstructions.remove(instructions.size() -1);
		}while(linkedInstructions.size() != 0 && linkedInstructions.getLast().getOrder() == null);
//		System.err.println(instructions);
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
		System.out.println("Vytvarim nove auto!");
		return newCar;
	}
	
	public String toString(){
		return "Prekladiste " + idCont;
	}
}
