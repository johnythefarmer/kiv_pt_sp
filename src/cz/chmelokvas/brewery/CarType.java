package cz.chmelokvas.brewery;

public enum CarType {
	CAMION(100,90,5),CISTERN(50,60,2),TRUCK(30,70,5);
	
	private final int capacity;
	private final float speed;
	private final int reloadingSpeed;
	private CarType(int capacity, float speed, int reloadingSpeed){
		this.capacity = capacity;
		this.speed = speed;
		this.reloadingSpeed = reloadingSpeed;
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
}
