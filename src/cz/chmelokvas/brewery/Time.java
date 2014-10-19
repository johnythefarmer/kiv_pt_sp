package cz.chmelokvas.brewery;


public class Time {

	/** Atribut konstanty den */
	private int day;
	
	/** Atribut konstanty hodina */
	private int  hour;
	
	/** Atribut konstanty minuta */
	private int minute;
	
	public Time(int day, int hour, int minute){
		this.day = day;
		this.hour = hour;
		this.minute = minute;
	}
	
	public void addMinutes(int minute){
		this.minute += minute;
		
		if(this.minute >= 60){
			addHours(this.minute/60);
			this.minute %= 60;
		}
	}
	
	public void addHours(int hour){
		this.hour += hour;
		
		if(this.hour >= 24){
			addDays(this.hour/24);
			this.hour %= 24;
		}
	}
	
	public void addDays(int day){
		this.day += day;
	}
	
	public String toString(){
		return String.format("Den %d: %02d:%02d", day, hour, minute);
	}
	
	public int value(){
		return (day*24 + hour)*60 + minute;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	
}
