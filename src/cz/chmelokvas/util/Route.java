package cz.chmelokvas.util;

public class Route {
	private int v;
	
	private float d;
	
	public Route(float d, int v){
		this.v = v;
		this.d = d;
	}
	
	public int getValue(){
		return v;
	}
	
	public float getDistance(){
		return d;
	}
}
