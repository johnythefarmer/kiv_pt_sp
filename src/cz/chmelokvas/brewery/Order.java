package cz.chmelokvas.brewery;

public class Order implements Comparable<Order>{

	/** Cas objednani */
	private final Time time;
	
	/** Hospoda ktera podala objednavku*/
	private final Pub pub;
	
	/** Objednane zbozi */
	private final int amount;
	
	private boolean isBeingDelivered = false;
	
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

	public boolean isBeingDelivered() {
		return isBeingDelivered;
	}

	public void setBeingDelivered(boolean isBeingDelivered) {
		this.isBeingDelivered = isBeingDelivered;
	}

	@Override
	public int compareTo(Order o) {
		return Integer.compare(time.value(), o.getTime().value());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + ((pub == null) ? 0 : pub.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (amount != other.amount)
			return false;
		if (pub == null) {
			if (other.pub != null)
				return false;
		} else if (!pub.equals(other.pub))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}	
	
	
}
