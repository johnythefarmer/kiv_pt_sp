package cz.chmelokvas.brewery;

public class Order implements Comparable<Order>{

	/** Cas objednani */
	private final Time time;
	
	/** Hospoda ktera podala objednavku*/
	private final Pub pub;
	
	/** Objednane zbozi */
	private final int amount;
	
	public Order(Time t, Pub p, int amount){
		this.time = t;
		this.pub = p;
		this.amount = amount;
	}

	public Time getTime() {
		return time;
	}

	public Pub getPub() {
		return pub;
	}

	public int getAmount() {
		return amount;
	}
	
	public String toString(){
		return "Objednavka(" + time + "): " + amount + " sudu do hospody " + ((TransportNode)pub).idCont;
	}

	@Override
	public int compareTo(Order o) {
		return Integer.compare(time.value(), o.getTime().value());
	}	
}
