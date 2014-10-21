package cz.chmelokvas.brewery;

import cz.chmelokvas.util.Controller;

public class Dock extends Stock {
	public void checkTimeEvents(){
		//Pocitani a planovani cest
		
		//Konani pohybu uz zamestnanych aut
		for(Car car : garage){
			Instruction i = car.getCurrentInstruction();
			while(i != null && Math.abs(i.getFinished().value() - c.mainTime.value()) < Controller.STEP){
				//vykonani potrebne aktivity pred prechodem na dalsi instrukci
				car.setPosition(i.getDestination());
				System.out.println(car + " finished " +i.getState() +" to  " + i.getDestination());
				
				//prechod na dalsi instrukci
				car.setCurrentInstruction(i.getNext());
				i = car.getCurrentInstruction();
				String text = "";
				if(i != null){
					car.setState(i.getState());
					text = " to " + i.getDestination();
				}else{
					car.setState(State.WAITING);
				}
				
				System.out.println(car + " started " +car.getState() + text);
			}
		}
	}
}
