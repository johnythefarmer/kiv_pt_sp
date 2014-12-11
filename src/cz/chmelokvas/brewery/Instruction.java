package cz.chmelokvas.brewery;

/**
 * Prepravka pro instrukce pro auta
 * @author Jan Dvorak A13B0293P
 *
 */
public class Instruction {
	private final State state;
	private final TransportNode destination;
	private Time finished;
	private final Order order;
	private Instruction next;
	private final int amount;
	
	public Instruction(State state, TransportNode destination, Time finished, int amount, Order order){
		this.state = state;
		this.destination = destination;
		this.finished = finished;
		this.order = order;
		this.amount = amount;
		this.next = null;
	}
	
	public Instruction(State state, TransportNode destination,int amount, Time finished){
		this.state = state;
		this.destination = destination;
		this.finished = finished;
		this.order = null;
		this.next = null;
		this.amount = amount;
	}
	
	public Instruction(State state, TransportNode destination, Time finished){
		this.state = state;
		this.destination = destination;
		this.finished = finished;
		this.order = null;
		this.next = null;
		this.amount = 0;
	}

	public Instruction getNext() {
		return next;
	}

	public void setNext(Instruction next) {
		this.next = next;
	}

	public State getState() {
		return state;
	}

	public TransportNode getDestination() {
		return destination;
	}

	public Time getFinished() {
		return finished;
	}

	public void setFinished(Time finished){
		this.finished = finished;
	}
	
	public Order getOrder() {
		return order;
	}

	public int getAmount() {
		return amount;
	}
	
	public String toString(){
		return finished + " " + destination + " " + state;
	}
}
