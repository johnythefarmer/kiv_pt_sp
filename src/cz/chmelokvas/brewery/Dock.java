package cz.chmelokvas.brewery;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.stream.Collectors;

import cz.chmelokvas.util.Controller;

public class Dock extends Stock {
	
	
	public Dock(int idCont, float x, float y){
		this.provider = this;
		this.idCont = idCont;
		this.idProv = 0;
		this.x = x;
		this.y = y;
	}
	
	public void checkTimeEvents(){
		//Pocitani a planovani cest
		Time t = new Time(0,13,0);
		if(c.mainTime.value() == t.value()|| c.mainTime.value() == t.value()+60){
//			Set<Order> or = ;
			createInstructions((Set<Order>)(orders.stream().filter(o->{return !o.isBeingDelivered();}).collect(Collectors.toSet())));
		}
		
		//Konani pohybu uz zamestnanych aut
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
				car.getInstructions().removeFirst();
//				car.setCurrentInstruction(i.getNext());
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
	
	public void createInstructions(Set<Order> orders){
		Car car = getFirstWaitingTruck();
		int sum = 0;
		for(Iterator<Order> it = orders.iterator(); it.hasNext();){
			Order o = it.next();
			
			if((sum + o.getAmount()) < (car.getCapacity()*3)/5){
				sum += o.getAmount();
			}else{
				//TODO tady se vlozi objednavka zpatky do nevyrizenych objednavek
				it.remove();
			}
		}
		System.out.println(sum);
		
		int loadingMinutes = sum*car.getReloadingSpeed();
		Time tmpTime = new Time(c.mainTime.value());
		
		car.addInstruction(new Instruction(State.WAITING, this, tmpTime));
		tmpTime = tmpTime.getTimeAfterMinutes(loadingMinutes);
		car.addInstruction(new Instruction(State.LOADING,this,tmpTime));
		
		for(Order o: orders){
			createTravellingInstructions(car,o);
		}
		
//		car.setCurrentInstruction(i);
/*		Instruction in = car.getCurrentInstruction();
		while(in!=null){
			System.out.println(in.getState() + " " + in.getDestination());
			in = in.getNext();
		}*/
	}
	
	public void createTravellingInstructions(Car c, Order o){
/*		if(in.getNext() != null){
			System.out.println("si kokot");
		}*/
		
		Instruction in = c.getInstructions().getLast();
		TransportNode source = in.getDestination();
		TransportNode destination = o.getPub();
		Time t = in.getFinished();
		Stack<Integer> nodes = new Stack<Integer>();
		
		int i = source.idProv;
		int j = destination.idProv;
		nodes.push(j);
		while(p[i][j] != 0){
			int tmp = p[i][j];
			nodes.push(tmp);
			j = tmp;
		}
		
		int x = i, y = 0;
		while(!nodes.empty()){
			y = nodes.pop();

			float distance = d[x][y];
			
			t = t.getTimeAfterMinutes((int)(distance/c.getSpeed()));
			
			c.addInstruction(new Instruction(State.TRAVELLING,customers[y],t));
			
			x = y;
		}
		
		t = t.getTimeAfterMinutes(o.getAmount()*c.getReloadingSpeed());
		c.addInstruction(new Instruction(State.UNLOADING,o.getPub(),t,o));
	}
	
	public Car getFirstWaitingTruck(){
		for(Car c: garage){
			if(c.getState() == State.WAITING){
				return c;
			}
		}
		Car newCar = Car.getTruck(this, garage.size());
		garage.add(newCar);
		System.out.println("Vytvarim novej autak");
		return newCar;
	}
}
