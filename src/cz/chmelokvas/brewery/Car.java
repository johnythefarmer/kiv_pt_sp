package cz.chmelokvas.brewery;

public class Car {
	/**Kapacita kamionu	v !SUDECH! */
	private static final int CAM_CAP = 100;
	
	/**Kapacita cisterny v !HEKTOLITRECH! */
	private static final int CISTERN_CAP = 50;
	
	/**Kapacita nakladaku v !SUDECH! */
	private static final int TRUCK_CAP = 30;
	
	/**Rychlost kamionu v km/h	 */
	private static final float CAM_SPEED = 90;
	
	/**Rychlost cisterny v km/h	 */
	private static final float CISTERN_SPEED = 60;
	
	/**Rychlost nakladaku v km/h	 */
	private static final float TRUCK_SPEED = 70;
	
	/**Pocet minut kolik zabere nalozeni/vylozeni 1 sudu*/
	private static final int RELOADING_SPEED_BARREL = 5;
	
	/**Pocet minut kolik zabere nacerpani/precerpani 1 hl piva*/
	private static final int RELOADING_SPEED_HL = 2;
	
	/** Soucasny stav vozu */
	private State state;
	
	/** Soucasna instrukce */
	private Instruction currentInstruction;
	
	/**Dopravni uzel ve kterem se prave nachazi*/
	private TransportNode position;
	
	/**Kapacita dane instance*/
	private final int capacity;
	
	/**Rychlost dane instance*/
	private final float speed;
	
	/**Rychlost nalozeni/vylozeni/nacerpani/precerpani */
	private final int reloadingSpeed;
	
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
	private Car(TransportNode position, int capacity, float speed, int reloadingSpeed){
		this.state = State.WAITING;
		this.currentInstruction = null;
		this.position = position;
		this.capacity = capacity;
		this.speed = speed;
		this.reloadingSpeed = reloadingSpeed;
		this.empty = capacity;
		this.full = 0;
	}
	
	/**
	 * Tovarni metoda pro vytvareni kamionu
	 * @param position Misto kde se kamion vytvori
	 * @return Instance s vlastnostmi kamionu
	 */
	public static Car getCamion(TransportNode position){
		return new Car(position,CAM_CAP,CAM_SPEED,RELOADING_SPEED_BARREL);
	}
	
	/**
	 * Tovarni metoda pro vytvareni cisterny
	 * @param position Misto kde se cisterna vytvori
	 * @return Instance s vlastnostmi cisterny
	 */
	public static Car getCistern(TransportNode position){
		return new Car(position,CISTERN_CAP,CISTERN_SPEED,RELOADING_SPEED_HL);
	}
	
	/**
	 * Tovarni metoda pro vytvareni nakladaku
	 * @param position Misto kde se nakladak vytvori
	 * @return Instance s vlastnostmi nakladaku
	 */
	public static Car getTruck(TransportNode position){
		return new Car(position,TRUCK_CAP,TRUCK_SPEED,RELOADING_SPEED_BARREL);
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Instruction getCurrentInstruction() {
		return currentInstruction;
	}

	public void setCurrentInstruction(Instruction currentInstruction) {
		this.currentInstruction = currentInstruction;
	}

	public TransportNode getPosition() {
		return position;
	}

	public void setPosition(TransportNode position) {
		this.position = position;
	}

	public int getCapacity() {
		return capacity;
	}

	public float getSpeed() {
		return speed;
	}

	public int getReloadingSpeed() {
		return reloadingSpeed;
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
}
