package cz.chmelokvas.brewery;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Brewery extends Stock {

	/** Atribut mnozstvi vyrobeneho piva za den */
	private final static int PRODUCTION_PER_DAY = 7000;
	
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
	
/*<<<<<<< HEAD
	public void prepareOrders(){
		// TODO zpracovani objednavek
=======*/
	public void createInstructionsForCamion(Dock dock){
		List<Instruction> instructions = new ArrayList<>();
		
		int loadingMinutes = CarType.CAMION.getCapacity() * CarType.CAMION.getReloadingSpeed();
		
		Time tmpTime = new Time(c.mainTime.value());
		instructions.add(new Instruction(State.WAITING, this, tmpTime));
			
		tmpTime = tmpTime.getTimeAfterMinutes(loadingMinutes);
		instructions.add(new Instruction(State.LOADING, this, CarType.CAMION.getCapacity(), tmpTime));
		
		TransportNode source = this;
		TransportNode destination = dock;
		
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
	}
	
	private void productionBeer(){
		if(c.mainTime.getHour() == 13){
			full += 307;
		}
		else{
			full += 291;
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
