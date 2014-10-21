package cz.chmelokvas.brewery;

public class Instruction {
	private final State state;
	private final TransportNode destination;
	private final Time finished;
	private Instruction next;
	
	public Instruction(State state, TransportNode destination, Time finished){
		this.state = state;
		this.destination = destination;
		this.finished = finished;
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
}
