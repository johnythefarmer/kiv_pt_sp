package cz.chmelokvas.util;

import cz.chmelokvas.brewery.Dock;
import cz.chmelokvas.brewery.Pub;
import cz.chmelokvas.brewery.Stock;
import cz.chmelokvas.brewery.TransportNode;


public class Main {

	public static void main(String[] args) {
		Controller c = new Controller();
		c.nodes[0] = new Dock();
		Stock s = (Stock)c.nodes[0];
		s.setCustomers(new TransportNode[Controller.N + 1]);
		s.setD(new float[Controller.N + 1]);
		s.setP(new int[Controller.N + 1]);
		s.setC(c);
		s.getCustomers()[0] = s;
		
		for(int i = 0; i < Controller.N; i++){
			Pub p = new Pub(i+1,i+1);
			p.setProvider(s);
			p.setC(c);
			c.nodes[i+1] = p;
			s.getCustomers()[i+1] = c.nodes[i+1];
		}
		
		/*c.addRoute(0, 2, 2.3f);
		c.addRoute(2,4, 2f);
		c.addRoute(4, 3, 1f);
		c.addRoute(3, 6, 3.5f);
		c.addRoute(6,5, 3.5f);
		c.addRoute(3, 7, 1f);
		c.addRoute(7,5, 10f);
		
		s.calculateShortestPaths();
		System.out.println(s.getD()[5]);
		int i = 5;
		while(s.getP()[i] != 0){
			i = s.getP()[i];
			System.out.println(s.getCustomers()[i]);
		}
*/		
		/*while(c.mainTime.getDay() < 1){
			System.out.println(c.mainTime);
			c.mainTime.addMinutes(30);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(c.mainTime);*/
	
		/*Pub p = new Pub(10,120);
		p.setC(c);
		for(int i = 0; i < 100; i++){
			System.out.println(p.makeOrder());
		}*/
		c.simulate();
	}

}
