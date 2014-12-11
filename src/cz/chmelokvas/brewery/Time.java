package cz.chmelokvas.brewery;


/**
 * Trida reprezentujici cas
 * @author Jan Dvorak A13B0293P
 *
 */
public class Time {

	/** Den */
	private int day;
	
	/** Hodina */
	private int  hour;
	
	/** Minuta */
	private int minute;
	
	public Time(int day, int hour, int minute){
		this.day = day;
		this.hour = hour;
		this.minute = minute;
	}
	
	/**
	 * Vytvori cas, ktery je vzdaleny n zadanych minut od pocatecniho casu
	 * @param minutes minuty
	 */
	public Time(int minutes){
		addMinutes(minutes);
	}
	
	/**
	 * Vrati cas, ktery se lisi od tohoto o zadane minuty
	 * @param minutes minuty
	 * @return rozdilny cas
	 */
	public Time getTimeAfterMinutes(int minutes){
		return new Time(value() + minutes);
	}
	
	public void subMinutes(int minute){
		this.minute -= minute;
		while(this.minute < 0){
			subHours(1);
			this.minute += 60;
		}
	}
	
	public void subHours(int hour){
		this.hour -= hour;
		
		while(this.hour < 0){
			subDays(1);
			this.hour += 24;
		}
	}
	
	public void subDays(int day){
		this.day -= day;
		if(this.day < 0){
			throw new IllegalArgumentException();
		}
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		Time other = (Time) obj;
		if (value() != other.value()){
			return false;
		}
		return true;
	}
	
	
}
