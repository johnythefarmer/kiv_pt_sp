package cz.chmelokvas.brewery;

import java.util.LinkedList;
import java.util.List;

import cz.chmelokvas.util.Loggable;

/**
 * Auto
 * @author Jan Dvorak A13B0293P
 *
 */
public class Car implements Loggable{
	/**
	 * Sklad pod ktery dane auto patri
	 */
	private final Stock stock;
	
	/** Typ auta */
	private final CarType type;
	
	/** Soucasny stav vozu */
	private State state;
	
	/** Soucasna instrukce */
	private List<Instruction> instructions;
	
	/**Dopravni uzel ve kterem se prave nachazi*/
	private TransportNode position;
	
	private final StringBuilder sb = new StringBuilder("");
	
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
	
	/** Identifikacni cislo auta u daneho skladu */
	private final int id;
	
	/**
	 * Privatni konstruktor. Vytvori auto na danem miste s danymi specifikacemi.<br>
	 * Konstruktor je privatni, jelikoz chceme zamezit vkladani nesmyslnych hodnot
	 * do atributu jako je kapacita nebo rychlost. Instance se ziskavaji pres metody
	 * {@code getCamion(TransportNode pozice)}, {@code getCistern(TransportNode pozice)} a {@code getTruck(TransportNode pozice)}
	 * @param position Misto kde se instance vytvori. Pozice daneho auta
	 * @param type Typ auta
	 * @param id ID u daneho skladu
	 */
	private Car(TransportNode position, CarType type, int id){
		this.state = State.WAITING;
		this.position = position;
		this.type = type;
		this.empty = 0;
		this.full = 0;
		this.id = id;
		this.stock = (Stock)position;
	}
	
	/**
	 * Tovarni metoda pro vytvareni kamionu
	 * @param position Misto kde se kamion vytvori
	 * @param id ID u daneho skladu
	 * @return Instance s vlastnostmi kamionu
	 */
	public static Car getCamion(TransportNode position, int id){
		return new Car(position,CarType.CAMION, id);
	}
	
	/**
	 * Tovarni metoda pro vytvareni cisterny
	 * @param position Misto kde se cisterna vytvori
	 * @param id ID u daneho skladu
	 * @return Instance s vlastnostmi cisterny
	 */
	public static Car getCistern(TransportNode position, int id){
		return new Car(position,CarType.CISTERN, id);
	}
	
	/**
	 * Tovarni metoda pro vytvareni nakladaku
	 * @param position Misto kde se nakladak vytvori
	 * @param id ID u daneho skladu
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

	/**
	 * Vrati prave provadenou instrukci
	 * @return	provadena instrukce
	 */
	public Instruction getCurrentInstruction() {
		if(instructions == null || instructions.isEmpty()){
			return null;
		}
		return ((LinkedList<Instruction>)instructions).getFirst();
	}
	
	/**
	 * Vrati posledni instrukci, ktera mu byla prirazena
	 * @return	posledni prirazena instrukce
	 */
	public Instruction getLastInstruction(){
		if(instructions.isEmpty()){
			return null;
		}
		return ((LinkedList<Instruction>)instructions).getLast();
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
	
	
	public CarType getType() {
		return type;
	}

	public void setEmpty(int empty){
		this.empty = empty;
	}
	
	public int getEmpty() {
		return empty;
	}

	public void setFull(int full){
		this.full = full;
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
	}
	
	/**
	 * Vylozi n sudu (resp. hl) piva
	 * @param n mnozstvi piva
	 */
	public void unload(int n){
//		if(n <= this.full){
			this.full -= n;
//		}
	}
	
	/**
	 * Nalozi n prazdnych sudu (resp. hl) piva
	 * @param n mnozstvi piva
	 */
	public void loadEmpty(int n){
		this.empty += n;
	}

	/**
	 * Vylozi n prazdnych sudu (resp. hl) piva
	 * @param n mnozstvi piva
	 */
	public void unloadEmpty(int n){
		this.empty -= n;
	}
	
	public String toString(){
		return type + " " + stock.idCont +"_" + id + "(empty:" + empty + ", full:" + full + ")";
	}

	/**
	 * Vrati seznam instrukci
	 * @return seznam instrukci
	 */
	public List<Instruction> getInstructions() {
		return instructions;
	}

	public Stock getStock() {
		return stock;
	}

	/**
	 * Nastavi autu instrukce pro cestu
	 * @param instructions instrukce pro cestu
	 */
	public void setInstructions(List<Instruction> instructions){
		this.instructions = instructions;
		if(!instructions.isEmpty()){
			sb.append("\t").append(instructions).append("\n");
		}
		
	}
	
	/**
	 * Vrati id auta
	 * @return id auta
	 */
	public final int getId(){
		return id;
	}
	
	@Override
	public String tempInfo() {
		StringBuilder sb = new StringBuilder(type + " " + stock.idCont +"_" + id + "\n");
		sb.append("Stav : " + getState() + "\n");
		sb.append("Pozice: " + getPosition() + "\n");
		if(!instructions.isEmpty()){
			sb.append("Smer: " + instructions.get(0).getDestination() + "\n");
		}else{
			sb.append("Smer: Zadny\n");
		}
		
		sb.append("Prazdnych sudu: " + getEmpty() + "\n");
		sb.append("Plnych sudu: " + getFull() + "\n");
		sb.append("instrukce: " + instructions + "\n");
		return sb.toString();
	}

	@Override
	public String finalInfo() {
		StringBuilder sb = new StringBuilder(type + " " + stock.idCont +"_" + id + "\n");
		sb.append(this.sb);
		return "-------------------\n" + sb.toString() + "-------------------\n";
	}
}
