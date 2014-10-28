package cz.chmelokvas.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import cz.chmelokvas.brewery.Time;

public class Logger {
	private PrintWriter pw;
	private SortedSet<Event> events = new TreeSet<Event>();
	
	private static final Logger INSTANCE = new Logger();
	
	private Logger(){}
	
	public static Logger getInstance(String file){
		if(INSTANCE.pw == null){
			INSTANCE.setOutputFile(file);
		}
		return INSTANCE;
	}
	
	public static Logger getInstance(){
		return INSTANCE;
	}
	
	public void log(Time t, int priority, String message){
		events.add(new Event(t, priority, message));
	}
	
	public void printEvents(){
		pw.println("---" + Controller.c.mainTime + "---");
		for(Iterator<Event> it = events.iterator(); it.hasNext();){
			Event e = it.next();
			
			if(e.getPriority() <= Controller.c.minLogPriority){
				System.out.println(e);
			}
			pw.println(e);
			it.remove();
		}
		
		pw.println("\n\n\n");
	}
	
	private void setOutputFile(String file){
		try {
			pw = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}
}
