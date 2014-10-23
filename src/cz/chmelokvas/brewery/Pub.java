package cz.chmelokvas.brewery;

import java.util.Random;

public class Pub extends TransportNode {	
	private static Random r = new Random();
	private TransportNode node;
	
	public Pub(int idProv, int idCont){
		this.idProv = idProv;
		this.idCont = idCont;
	}
	public Pub(TransportNode node){
		this.node = node;
	}
	
	public Order makeOrder(){
		return new Order(generateTime(), this, generateAmount());
	}
	
	private Time generateTime(){
		double d;
		do{
			d = r.nextGaussian()*3 + 10;
		}while(d < 8 || d > 16);
		
		int h = (int)Math.floor(d);
		int m = (int)((d - (double)h)*60.);
		
		return new Time(c.mainTime.getDay(), h, m);
	}
	
	private int generateAmount(){
		int hod = r.nextInt(100), amount = 0;
		if(hod <= 25){
			amount = 1;
		}else if(hod <= 50){
			amount = 2;
		}else if(hod <= 70){
			amount = 3;
		}else if(hod <= 85){
			amount = 4;
		}else if(hod <= 95){
			amount = 5;
		}else {
			amount = 6;
		}
		
		return amount;
	}
	
	public String toString(){
		return "Hospoda " +  idCont;
	}
}
