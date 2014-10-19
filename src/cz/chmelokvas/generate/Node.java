package cz.chmelokvas.generate;

import java.awt.Color;
import java.util.ArrayList;

public class Node {
	private int id;
	private float x;
	private float y;
	private float d;
	private Node dock;
	private Color color;
	private ArrayList<Node> neighbours;
	
	/* Konstruktor souradnice X a Y */
	public Node(float x, float y, int id){
		this.x = x;
		this.y = y;
		this.dock = null;
		this.color = null;
		this.neighbours = null;
		this.id = id;
	}
	
	/* Konstruktor pro TreeSet
	 * Uklada uzel hospody a vzdalenost
	 */
	public Node(Node dock, float d){
		this.dock = dock;
		this.d = d;
	}
	
	/* Konstruktor pro nove vygenerovane souradnice */
	public Node(float x, float y){
		this(x, y, 0);
	}
	
	public String toString(){
		return x+" "+y;
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
	
}
