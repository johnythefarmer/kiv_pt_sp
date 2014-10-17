package cz.chmelokvas.generate;

import java.awt.Color;

public class Node {
	private float x;
	private float y;
	private Node dock;
	private Color color;
	
	public Node(float x, float y){
		this.x = x;
		this.y = y;
		this.dock = null;
		this.color = null;
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
	
	
}
