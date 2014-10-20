package cz.chmelokvas.generate;

import java.awt.Color;
import java.util.ArrayList;

public class Node {
	
	/**	ID bodu
	 * 		0 -> pivovar
	 * 		1 - 8 -> prekladiste
	 * 		9 - 200 -> hospody z tanku
	 * 		201 - 4000 -> hospody ze sudu
	*/
	private int id;
	
	/** Nazev pivovaru */
	private String name;
	
	/** X souradnice bodu */
	private float x;
	
	/** Y souradnice bodu */
	private float y;
	
	/** Vzdalenost 2 bodu */
	private float d;
	
	/** Instance nejblizsiho prekladiste/pivovaru */
	private Node dock;
	private Color color;
	
	/** List nejblizsich sousedu */
	private ArrayList<Node> neighbours;
	
	/** Konstruktor souradnice X a Y, ID */
	public Node(float x, float y, int id){
		this.x = x;
		this.y = y;
		this.dock = null;
		this.color = null;
		this.neighbours = null;
		this.id = id;
	}
	
	/** Konstruktor pro TreeSet
	 * Uklada uzel hospody a vzdalenost
	 */
	public Node(Node dock, float d){
		this.dock = dock;
		this.d = d;
	}
	
	public String toString(){
		return x+"\t"+y;
	}
	
	public void setDock(Node dock){
		this.dock = dock;
	}
	public Node getDock(){
		return this.dock;
	}
	public float getX(){
		return this.x;
	}
	public float getY(){
		return this.y;
	}
	public void setColor(Color color){
		this.color = color;
	}
	public Color getColor(){
		return this.color;
	}
	public float getD(){
		return this.d;
	}
	public ArrayList<Node> getNeighbours(){
		return neighbours;
	}
	public void inclNeighbours(){
		this.neighbours = new ArrayList<>();
	}
	public int getID(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
}
