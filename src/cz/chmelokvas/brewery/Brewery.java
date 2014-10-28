package cz.chmelokvas.brewery;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.swing.plaf.SliderUI;
import javax.xml.soap.Node;

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
		
		int j = destination.idProv;
		
		// cesta z I(pivovar) do J
		int idN, idB = j;
		while((idN = getP()[0][idB]) != 0){
			
//			float distance = dock.getP()[idB][idN];
			float distance = lengthEdge(c.nodes.get(idB), c.nodes.get(idN));
			
			tmpTime = tmpTime.getTimeAfterMinutes((int)(distance/CarType.CAMION.getSpeed()));
			
			instructions.add(new Instruction(State.TRAVELLING,c.nodes.get(idN),tmpTime));
			idB = idN;
		}
		
				
		tmpTime = tmpTime.getTimeAfterMinutes(loadingMinutes);
		instructions.add(new Instruction(State.UNLOADING, dock, CarType.CAMION.getCapacity(), tmpTime));
		
		int sum = dock.getEmpty();
		if(sum >= CarType.CAMION.getCapacity()){
			sum = CarType.CAMION.getCapacity();
			tmpTime = tmpTime.getTimeAfterMinutes(sum*CarType.CAMION.getReloadingSpeed());
			
		}else{
			tmpTime = tmpTime.getTimeAfterMinutes(dock.getEmpty()*CarType.CAMION.getReloadingSpeed());
		}
		
		instructions.add(new Instruction(State.LOADING_EMPTY_BARRELS,dock, sum, tmpTime));
		
		
		
		instructions.add(new Instruction(State.UNLOADING_EMPTY_BARRELS, this, sum, tmpTime));
		
				
		
	}
	
	private float lengthEdge(TransportNode a, TransportNode b)
	{
		/* Vzorec sqrt( (a1-b1)^2 + (a2-b2)^2 ) */
		return (float) Math.sqrt(Math.pow(a.getX()-b.getX(), 2.0) +
				Math.pow(a.getY()-b.getY(), 2.0));
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
}
