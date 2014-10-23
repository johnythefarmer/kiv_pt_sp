package cz.chmelokvas.brewery;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

import cz.chmelokvas.util.Controller;

public class Dock extends Stock {
	
	
	/**
	 * Provede vsechny udalosti, co se staly za dany krok v case to jest:
	 * <li>Urci cestu nakladniho automobilu</li>
	 * <li>Vysle dalsi auta na cestu</li>
	 * <li>Posune auta o dany casovy usek</li>
	 */
	public void checkTimeEvents(){
		//Pocitani a planovani cest
//		Time t = new Time(0,13,0);
//		if(c.mainTime.value() == t.value()|| c.mainTime.value() == t.value()+60){
			createInstructions(orders);
//		}
		
		//Konani pohybu uz zamestnanych aut
		moveCars();
	}
	
	/**
	 * Pokud dane auto za dany krok casu neco udelalo, je tento krok vypsan a je zadana nova instrukce
	 */
	public void moveCars(){
		for(Car car : garage){
			Instruction i = car.getCurrentInstruction();
			while(i != null && Math.abs(i.getFinished().value() - c.mainTime.value()) < Controller.STEP){
				//vykonani potrebne aktivity pred prechodem na dalsi instrukci
				car.setPosition(i.getDestination());
				System.out.println(i.getFinished() + " " + car + " " + i.getState().getStrFin() + " " + i.getDestination());
				if(i.getOrder() != null){
					deliverOrder(i.getOrder());
				}
				
				//prechod na dalsi instrukci
				((LinkedList<Instruction>)car.getInstructions()).removeFirst();
				i = car.getCurrentInstruction();
				String text = "";
				if(i != null){
					car.setState(i.getState());
					text = " " + i.getDestination();
				}else{
					car.setState(State.WAITING);
				}
				
				System.out.println(car + " " +car.getState().getStrStart() + text);
			}
		}
	}
	
	/**
	 * Zkontroluje, zda se dane objednavky vejdou do auta a pokud ne, vrati je zpet pro zpracovani dalsich objednavek
	 * @param car Auto, do ktereho budeme chti nakladat
	 * @param orders Objednavky, ktere ma dane auto stihnout
	 * @return Celkovy pocet, ktery mame do auta nalozit
	 */
	public int checkCarCapacity(Car car, Set<Order> orders){
		int sum = 0;
		for(Iterator<Order> it = orders.iterator(); it.hasNext();){
			Order o = it.next();
			
			if((sum + o.getAmount()) < car.getCapacity()){
				sum += o.getAmount();
			}else{
				//TODO tady se vlozi objednavka zpatky do nevyrizenych objednavek
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
		Car car = getFirstWaitingTruck();
		
		//Urceni kolik ma auto nalozit sudu a pridani odpovidajicich instrukci
		int sum = checkCarCapacity(car, orders);
		int loadingMinutes = sum*car.getReloadingSpeed();
		
		Time tmpTime = new Time(c.mainTime.value());
		car.addInstruction(new Instruction(State.WAITING, this, tmpTime));
		
		tmpTime = tmpTime.getTimeAfterMinutes(loadingMinutes);
		car.addInstruction(new Instruction(State.LOADING,this,tmpTime));
		
		
		//prirazeni cestovacich instrukci pro dane auto
		for(Order o: orders){
			createTravellingInstructions(car,o);
		}
		
		//TODO cesta zpet
	}
	
	/**
	 * Vezme danou polohu auta a vypocte pro nej instrukce tak aby se dostal do mista, ktere danou objednavku objednalo
	 * @param c Auto kteremu budeme davat instrukce
	 * @param o Objednavka, kterou musi auto vyridit
	 */
	public void createTravellingInstructions(Car c, Order o){

		Instruction in = ((LinkedList<Instruction>)c.getInstructions()).getLast();
		
		TransportNode source = in.getDestination();
		TransportNode destination = o.getPub();
		
		Time t = in.getFinished();
		
		Stack<Integer> nodes = new Stack<Integer>();
		
		int i = source.idProv;
		int j = destination.idProv;
		
		prepareStackForPath(i, j, nodes);
		
		addPathInstructions(i, c, t, nodes);
		
		t = t.getTimeAfterMinutes(o.getAmount()*c.getReloadingSpeed());
		c.addInstruction(new Instruction(State.UNLOADING,o.getPub(),t,o));
	}
	
	/**
	 * Naplni zasobnik id bodu, ktere musi auto navstivit, pokud chce dorazit z bodu {@code source} do bodu {@code destination}
	 * @param source id pocatecniho bodu
	 * @param destination id ciloveho bodu
	 * @param nodes zasobnik kde budeme ukladat uzly
	 */
	public void prepareStackForPath(int source, int destination, Stack<Integer> nodes){
		nodes.push(destination);
		while(p[source][destination] != 0){
			int tmp = p[source][destination];
			nodes.push(tmp);
			destination = tmp;
		}
	}
	
	/**
	 * Vytvori instrukce, ktere budou urcovat danemu autu cestu pres uzly, ktere jsou ulozeny v zasobniku
	 * @param i uzel ve kterem se auto nachazi slo by ho zjistit pomoci
	 *  {@code ((LinkedList<Instruction>)c.getInstructions()).getLast().getDestination().idProv},
	 *   coz ale uznejme je hodne slozite a stejne jsme jiz hodnotu jednou ziskali
	 * @param c Auto, kteremu dane instrukce predame
	 * @param t Cas dokonceni posledni drive predane instrukce. Slo by ziskat podobne jako i ale lehci je predat
	 * @param nodes Zasobnik, ktery obsahuje uzly, pres ktere auto pojede
	 */
	public void addPathInstructions(int i, Car c, Time t, Stack<Integer> nodes){
		int x = i, y = 0;
		while(!nodes.empty()){
			y = nodes.pop();

			float distance = d[x][y];
			
			t = t.getTimeAfterMinutes((int)(distance/c.getSpeed()));
			
			c.addInstruction(new Instruction(State.TRAVELLING,customers[y],t));
			
			x = y;
		}
	}
	
	/**
	 * Vrati prvni cekajici nakladak
	 * @return prvni cekajici nakladak
	 */
	public Car getFirstWaitingTruck(){
		for(Car c: garage){
			if(c.getState() == State.WAITING && c.getPosition().equals(this)){
				return c;
			}
		}
		Car newCar = Car.getTruck(this, garage.size());
		garage.add(newCar);
		System.out.println("Vytvarim nove auto!");
		return newCar;
	}
}
