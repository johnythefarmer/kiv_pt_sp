package cz.chmelokvas.generate;

import java.awt.Color;

public class Node {
	private float x;
	private float y;
	private float d;
	private Node dock;
	private Color color;
	private Node[] neighbours;
	
	/* Konstruktor souradnice X a Y */
	public Node(float x, float y){
		this.x = x;
		this.y = y;
		this.dock = null;
		this.color = null;
		this.neighbours = null;
	}
	
	/* Konstruktor pro TreeSet
	 * Uklada uzel hospody a vzdalenost
	 */
	public Node(Node dock, float d){
		this.dock = dock;
		this.d = d;
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
	public void setNeighbours(Node[] neighbours){
		this.neighbours = neighbours;
	}
	public Node[] getNeighbours(){
		return this.neighbours;
	}
	
}
