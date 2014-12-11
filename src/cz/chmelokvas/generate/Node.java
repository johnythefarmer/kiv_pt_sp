package cz.chmelokvas.generate;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Uzel v grafu
 * @author Lukas Cerny A13B0286P
 *
 */
public class Node {
	
	/**	ID bodu
	 * 		0 : pivovar
	 * 		1 - 8 : prekladiste
	 * 		9 - 200 : hospody z tanku
	 * 		201 - 4000 : hospody ze sudu
	*/
	private final int id;
	
	/** Nazev pivovaru */
	private String name;
	
	/** X souradnice bodu */
	private final float x;
	
	/** Y souradnice bodu */
	private final float y;

	
	/** Instance nejblizsiho prekladiste/pivovaru */
	private Node dock;
	private Color color;
	
	/** List nejblizsich sousedu */
	private final List<Route> neighbours;
	

	/**
	 * vytvori bod na danych souradnicich s danym id
	 * @param x x-ova souradnice
	 * @param y y-ova souradnice
	 * @param id id
	 */
	public Node(float x, float y, int id){
		this.x = x;
		this.y = y;
		this.dock = null;
		this.color = null;
		this.neighbours = new ArrayList<Route>();
		this.id = id;
	}
	
	public String toString(){
		return x+"\t"+y;
	}
	
	/**
	 * Nastavi prekladiste
	 * @param	dock	prekladiste
	 */
	public void setDock(Node dock){
		this.dock = dock;
	}
	
	/**
	 * Vrati aktualni prekladiste
	 * @return	dock	prekladiste
	 */
	public Node getDock(){
		return this.dock;
	}
	
	/**
	 * Vrati X souradnici uzlu
	 * @return	x	X souradnice
	 */
	public float getX(){
		return this.x;
	}
	/**
	 * Vrati Y souradnici uzlu
	 * @return	y	Y souradnice
	 */
	public float getY(){
		return this.y;
	}

	
	public void setColor(Color color){
		this.color = color;
	}
	public Color getColor(){
		return this.color;
	}
	
	/**
	 * Vrati aktualni seznam nejblizsich sousedu
	 * @return	neighbours	seznam sousedu
	 */
	public List<Route> getNeighbours(){
		return neighbours;
	}
	/**
	 * Vrati ID uzlu
	 * @return	id	ID uzlu
	 */
	public int getID(){
		return this.id;
	}
	/**
	 * Nastavi jmeno uzlu
	 * @return	name	jmeno uzlu
	 */
	public String getName(){
		return this.name;
	}
	/**
	 * Nastavi jmeno uzlu
	 * @param name	jmeno uzlu
	 */
	public void setName(String name){
		this.name = name;
	}
}
