package cz.chmelokvas.brewery;

public class Instruction {
	private final State state;
	private final TransportNode destination;
	private final Time finished;
	private final Order order;
	private Instruction next;
	
	public Instruction(State state, TransportNode destination, Time finished, Order order){
		this.state = state;
		this.destination = destination;
		this.finished = finished;
		this.order = order;
		this.next = null;
	}
	
	public Instruction(State state, TransportNode destination, Time finished){
		this.state = state;
		this.destination = destination;
		this.finished = finished;
		this.order = null;
		this.next = null;
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

	public Order getOrder() {
		return order;
	}
	
	
}
