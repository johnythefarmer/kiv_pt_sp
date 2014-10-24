package cz.chmelokvas.util;

import java.util.ArrayList;
import java.util.Collection;

import cz.chmelokvas.brewery.Car;
import cz.chmelokvas.brewery.Dock;
import cz.chmelokvas.brewery.Instruction;
import cz.chmelokvas.brewery.Order;
import cz.chmelokvas.brewery.Pub;
import cz.chmelokvas.brewery.State;
import cz.chmelokvas.brewery.Stock;
import cz.chmelokvas.brewery.Time;
import cz.chmelokvas.brewery.TransportNode;


public class Main {

	public static void main(String[] args) {
		ParseData pd = new ParseData("export.txt");
		
		Controller.c = pd.getC();
		Controller.c.simulate();
		
		/*Controller c = new Controller();
		c.nodes[0] = new Dock();
		Stock s = (Stock)c.nodes[0];
		s.setProvider(s);
		s.setCustomers(new TransportNode[Controller.N + 1]);
		s.setD(new float[Controller.N + 1][Controller.N + 1]);
		s.setP(new int[Controller.N + 1][Controller.N + 1]);
		s.setC(c);
		s.getCustomers()[0] = s;
		s.setGarage(new ArrayList<Car>());
		
		for(int i = 0; i < Controller.N; i++){
			Pub p = new Pub(i+1,i+1,false);
			p.setProvider(s);
			p.setC(c);
			c.nodes[i+1] = p;
			s.getCustomers()[i+1] = c.nodes[i+1];
		}
		
//		Car a = Car.getTruck(s);
		Instruction t0 = new Instruction(State.WAITING, c.nodes[0], new Time(0,7,0));
		Instruction t1 = new Instruction(State.TRAVELLING, c.nodes[2], new Time(0,7,30));
		Instruction t2 = new Instruction(State.TRAVELLING,c.nodes[3], new Time(0,9,40));
		Order o = new Order(new Time(0,8,20), (Pub)c.nodes[3], 3);
		s.recieveOrder(o);
		Instruction t3 = new Instruction(State.UNLOADING, c.nodes[3], new Time(0,9,50), o);
		Instruction t4 = new Instruction(State.TRAVELLING, c.nodes[0], new Time(0,10,30));
		t3.setNext(t4);
		t2.setNext(t3);
		t1.setNext(t2);
		t0.setNext(t1);
		a.setCurrentInstruction(t0);
//		s.getGarage().add(a);
		
		c.addRoute(0, 2, 2.3f);
		c.addRoute(2,4, 2f);
		c.addRoute(4, 3, 1f);
		c.addRoute(3, 6, 3.5f);
		c.addRoute(6,5, 3.5f);
		c.addRoute(3, 7, 1f);
		c.addRoute(7,5, 10f);
		c.addRoute(0, 1, 2);
		c.addRoute(1, 2, 2);
		c.addRoute(2, 3, 2);
		c.addRoute(3, 4, 2);
		c.addRoute(4, 5, 2);
		c.addRoute(5, 6, 2);
		c.addRoute(6, 7, 2);
		c.addRoute(7, 8, 2);
		c.addRoute(8, 9, 2);
		c.addRoute(9, 10, 2);
		s.floydWarshal(s.getD(), s.getP(), Controller.N + 1);
		
		long t = System.currentTimeMillis();
		s.floydWarshal(s.getD(), s.getP(), Controller.N + 1);
		System.out.println(System.currentTimeMillis() - t);
		System.out.println(s.getD()[7][5]);
		
		while(c.mainTime.getDay() < 1){
			System.out.println(c.mainTime);
			c.mainTime.addMinutes(30);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(c.mainTime);
	
		Pub p = new Pub(10,120);
		p.setC(c);
		for(int i = 0; i < 100; i++){
			System.out.println(p.makeOrder());
		}
		c.simulate();
//		System.out.println("Pozice "+ a + ": " +a.getPosition());*/
		
	}

}
