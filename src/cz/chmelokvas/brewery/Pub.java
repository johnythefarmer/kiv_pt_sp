package cz.chmelokvas.brewery;

import java.util.Random;

import cz.chmelokvas.util.Controller;

public class Pub extends TransportNode {	
	private static Random r = new Random();
	
	/** Dnesni objednavka */
	private Order todayOrder;
	
	/** Vcerejsi objednavka */
	private Order yesterdayOrder;
	
	/** Znaci, zda je hospoda tankova */
	private boolean isTank;
	
	public Pub(int idProv, int idCont, boolean isTank){
		this.idProv = idProv;
		this.idCont = idCont;
		this.isTank = isTank;
	}
	
	public Pub(Stock provider, int idProv, int idCont, float x, float y, boolean isTank){
		this.idProv = idProv;
		this.idCont = idCont;
		this.x = x;
		this.y = y;
		this.isTank = isTank;
		this.provider = provider;
	}
	
	/**
	 * Vytvori objednavku
	 * @return	objednavka
	 */
	public Order makeOrder(){
		if(todayOrder != null){
			yesterdayOrder = todayOrder;
		}
		todayOrder = new Order(generateTime(), this, generateAmount());
		return todayOrder;
	}
	
	public Order getTodayOrder(){
		return todayOrder;
	}
	
	public Order getYesterdayOrder(){
		return yesterdayOrder;
	}
	
	public void setTodayOrder(Order todayOrder){
		this.todayOrder = todayOrder;
	}
	
	public void setYesterdayOrder(Order yesterdayOrder){
		this.yesterdayOrder = yesterdayOrder;
	}
	
	/**
	 * Vytvori cas pomoci gaussova rozlozeni mezi 8h - 16h.
	 * S nejvetsi pravdepodobnosti kolem 10h.
	 * @return	cas objednavky
	 */
	private Time generateTime(){
		double d;
		do{
			d = r.nextGaussian()*3 + 10;
		}while(d < 8 || d > 16);
		
		int h = (int)Math.floor(d);
		int m = (int)((d - (double)h)*60.);
		
		return new Time(Controller.c.mainTime.getDay(), h, m);
	}
	
	/**
	 * Vygeneruje mnozstvi objednanych sudu/hl
	 * @return	mnozsti sudu/hl
	 */
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
	
	
	
	public boolean isTank() {
		return isTank;
	}

	public void setTank(boolean isTank) {
		this.isTank = isTank;
	}

	public String toString(){
		return "Hospoda " +  idCont;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idCont;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		TransportNode other = (TransportNode) obj;
		if (idCont != other.idCont){
			return false;
		}
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)){
			return false;
		}
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)){
			return false;
		}
		return true;
	}
}
