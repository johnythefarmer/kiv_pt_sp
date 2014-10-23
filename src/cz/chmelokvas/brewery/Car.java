package cz.chmelokvas.brewery;

import java.util.LinkedList;

public class Car {
	/** Typ auta */
	private final CarType type;
	
	/** Soucasny stav vozu */
	private State state;
	
	/** Soucasna instrukce */
	private final LinkedList<Instruction> instructions;
	
	/**Dopravni uzel ve kterem se prave nachazi*/
	private TransportNode position;
	
	/**
	 * Pocet prazdnych sudu.<br>
	 * V pripade cisterny je zbyvajici misto v nadrzi
	 */
	private int empty;
	
	/**
	 * Pocet plnych sudu.<br>
	 * V pripade cisterny je to pocet hektolitru
	 */
	private int full;
	
	private int id;
	
	/**
	 * Privatni konstruktor. Vytvori auto na danem miste s danymi specifikacemi.<br>
	 * Konstruktor je privatni, jelikoz chceme zamezit vkladani nesmyslnych hodnot
	 * do atributu jako je kapacita nebo rychlost. Instance se ziskavaji pres metody
	 * {@code getCamion(TransportNode pozice)}, {@code getCistern(TransportNode pozice)} a {@code getTruck(TransportNode pozice)}
	 * @param position Misto kde se instance vytvori. Pozice daneho auta
	 * @param capacity Kapacita ulozneho prostoru auta
	 * @param speed Rychlost jakou auto cestuje silnici
	 * @param reloadingSpeed Pocet minut kolik zabere naklad/vyklad sudu nebo nacerpani/precerpani hl piva
	 */
	private Car(TransportNode position, CarType type, int id){
		this.state = State.WAITING;
		this.instructions = new LinkedList<Instruction>();
		this.position = position;
		this.type = type;
		this.empty = type.getCapacity();
		this.full = 0;
		this.id = id;
	}
	
	/**
	 * Tovarni metoda pro vytvareni kamionu
	 * @param position Misto kde se kamion vytvori
	 * @return Instance s vlastnostmi kamionu
	 */
	public static Car getCamion(TransportNode position, int id){
		return new Car(position,CarType.CAMION, id);
	}
	
	/**
	 * Tovarni metoda pro vytvareni cisterny
	 * @param position Misto kde se cisterna vytvori
	 * @return Instance s vlastnostmi cisterny
	 */
	public static Car getCistern(TransportNode position, int id){
		return new Car(position,CarType.CISTERN, id);
	}
	
	/**
	 * Tovarni metoda pro vytvareni nakladaku
	 * @param position Misto kde se nakladak vytvori
	 * @return Instance s vlastnostmi nakladaku
	 */
	public static Car getTruck(TransportNode position, int id){
		return new Car(position,CarType.TRUCK,id);
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Instruction getCurrentInstruction() {
		if(instructions.isEmpty()){
			return null;
		}
		return instructions.getFirst();
	}

	public void addInstruction(Instruction instruction) {
		instructions.addLast(instruction);
	}

	public TransportNode getPosition() {
		return position;
	}

	public void setPosition(TransportNode position) {
		this.position = position;
	}

	public int getCapacity() {
		return type.getCapacity();
	}

	public float getSpeed() {
		return type.getSpeed();
	}

	public int getReloadingSpeed() {
		return type.getReloadingSpeed();
	}
	
	public int getEmpty() {
		return empty;
	}

	public int getFull() {
		return full;
	}

	/**
	 * Nalozi n sudu (resp. hl) piva
	 * @param n mnozstvi piva
	 */
	public void load(int n){
		this.full += n;
		this.empty -= n;
	}
	
	/**
	 * Vylozi n sudu (resp. hl) piva
	 * @param n mnozstvi piva
	 */
	public void unload(int n){
		this.full -= n;
		this.empty += n;
	}
	
	public String toString(){
		return type + " " + id;
	}

	public LinkedList<Instruction> getInstructions() {
		return instructions;
	}
	
	
}
