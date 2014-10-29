package cz.chmelokvas.util;

import cz.chmelokvas.brewery.Time;

/**
 * Pomocna trida pro tridu {@code Logger}
 * Jedna se o samostatnou udalost, ktera je potreba vypsat
 *
 */
public class Event implements Comparable<Event>{
	private final Time time;
	private final int priority;
	private final String message;
	
	public static final int ERRORS = 1;
	public static final int ORDER_DELIVERY = 2;
	public static final int ORDER_RECEIVED = 3;
	public static final int CAR_MOVEMENT = 4;
	public static final int DOCK_SUPLY = 5;
	public static final int PROUDUCTION = 6;
	
	public Event(Time time, int priority, String message) {
		this.time = time;
		this.priority = priority;
		this.message = message;
	}
	
	@Override
	public int compareTo(Event o) {
		int tDiff = Integer.compare(time.value(), o.getTime().value());
		if(tDiff == 0){
			return message.compareTo(o.getMessage());
		} else{
			return tDiff;
		}
	}
	
	public Time getTime() {
		return time;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String toString(){
		return time + ": " + message;
	}
}
