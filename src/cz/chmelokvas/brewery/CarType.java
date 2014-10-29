package cz.chmelokvas.brewery;

public enum CarType {
	CAMION(100,90f/60f,5,"Kamion"),CISTERN(50,1,2,"Cisterna"),TRUCK(30,70f/60f,5, "Nakladni auto");
	
	private final int capacity;
	private final float speed;
	private final int reloadingSpeed;
	private final String name;
	private CarType(int capacity, float speed, int reloadingSpeed, String name){
		this.capacity = capacity;
		this.speed = speed;
		this.reloadingSpeed = reloadingSpeed;
		this.name = name;
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
	
	public String toString(){
		return name;
	}
}
