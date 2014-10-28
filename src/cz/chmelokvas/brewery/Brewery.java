package cz.chmelokvas.brewery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.swing.plaf.SliderUI;

import cz.chmelokvas.util.KeyPriorityQueue;
import cz.chmelokvas.util.Route;


public class Brewery extends Stock {

//	/** Atribut mnozstvi vyrobeneho piva za den *//*
//	private final static int PRODUCTION_PER_DAY = 7000;
	
	/** Atribut mnostvi camionu */
	protected List<Car> garage_for_camions = new LinkedList<Car>();
	
	/** Atribut mnostvi cisteren */
	protected List<Car> garage_for_cisterns = new LinkedList<Car>(); 
	
	/** Pripravene instrukce pro prekladiste */
	private List<List<Instruction>> instruction_for_dock = new LinkedList<List<Instruction>>();
	
	
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
		
//		if(c.mainTime.getHour() > 8 && c.mainTime.getHour() < 16){
//			 prepareOrders();
//		}

	}
	
	public void prepareOrders(Dock dock){
		// TODO zpracovani objednavek
		
	}
	
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
	 * kamion -> full - 100/2 hl
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
	
	public int[] dijkstra(int id_from){
		Set<Integer> set = new HashSet<Integer>();

		int size = d.length;
		boolean[] closed = new boolean[size];
		float [] distances = new float[size];
		
		set.add(id_from);
		
		for (int i = 0; i < size; i++) {
			if (i != id_from){
				distances[i] = Integer.MAX_VALUE;
			}else{
				distances[i] = 0;
			}
		}
		
		int[] predecessors = new int[size];
		predecessors[id_from] = -1;
		
		while(!set.isEmpty()){
			float minDistance = Float.MAX_VALUE;
			int node = -1;
			
			for(Integer i : set){
				if(distances[i] < minDistance){
					minDistance = distances[i];
					node = i;
				}
			}
			
			set.remove(node);
			closed[node] = true;
			
			for(int i = 0; i < size; i++){
				if(d[node][i] != Float.MAX_VALUE && !closed[i] &&
						distances[node] + d[node][i] < distances[i]){
					distances[i] = distances[node] + d[node][i];
					predecessors[i] = node;
					set.add(i);
				}
			}
			
		}
		return predecessors;
	}
		
	
	public void calculateShortestPathsDijkstra(){
//		this.setD(new float[routes.size()][routes.size()]);
//		for(int i = 0; i < d.length; i++){
//			d[i][0] = Float.MAX_VALUE;
//		}
		KeyPriorityQueue<Integer> queue = new KeyPriorityQueue<Integer>();
		
		d[0][0] = 0;
		queue.add(d[0][0],idCont);
		
		while(!queue.isEmpty()){
			int u = queue.poll();
			for(Route v : c.nodes.get(u).routes){
				int vId = v.getValue();

				
				float dist = d[c.nodes.get(u).idProv][0] + v.getDistance();
				
				if(dist < d[c.nodes.get(vId).idProv][0]){
					d[c.nodes.get(vId).idProv][0] = dist;
					p[c.nodes.get(vId).idProv][0] = u;
					queue.add(d[c.nodes.get(vId).idProv][0], vId);
				}
			}
		}
	}
}
