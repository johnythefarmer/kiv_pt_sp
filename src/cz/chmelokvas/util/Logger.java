package cz.chmelokvas.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import cz.chmelokvas.brewery.Dock;
import cz.chmelokvas.brewery.Time;

public class Logger {
	private PrintWriter pw;
	private final SortedSet<Event> events = new TreeSet<Event>();
	
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
	
	public void printFinalStatistics(){
		StringBuffer sb = new StringBuffer("\n\n\n\n\n");
		sb.append("-----CELKOVA STATISTIKA-----\n");
		int trucksCount = 0;
		for(Dock d:Controller.c.dock){
			trucksCount += d.getGarage().size();
		}
		sb.append("Pocet vytvorenych aut: " + trucksCount + "\n");
		sb.append("Pocet vytvorenych cisteren: " + 0 + "\n");//TODO dopsat!!
		sb.append("Pocet vytvorenych kamionu: " + 0 + "\n");//TODO dopsat!!
		sb.append("Pocet rozvozenych sudu: " + Controller.c.deliveredBarrels + "\n");
		sb.append("Pocet rozvozenych hl: " + Controller.c.deliveredHL + "\n");
		sb.append("Pocet nestihnutych objednavek: " + Controller.c.deliveredLate + "\n");
		sb.append("Pocet vyprodukovanych hl: " + 0);//TODO dopsat!!
		System.out.println(sb);
		pw.println(sb);
	}
	
	
	public void close(){
		if(pw != null){
			pw.close();
		}
	}
	
	private void setOutputFile(String file){
		try {
			pw = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}
}
