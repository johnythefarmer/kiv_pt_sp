package cz.chmelokvas.util;

/**
 * Hrana grafu
 */
public class Route {
	private final int v;
	
	private final float d;
	
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
