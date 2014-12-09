package cz.chmelokvas.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import cz.chmelokvas.brewery.Brewery;
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
	
	/**
	 * Vypise vsechny udalosti, ktere se u neho nashromazdily
	 * Seradi je podle casu
	 */
	public void printEvents(){
		pw.println("---" + Controller.c.mainTime + "---");
		for(Iterator<Event> it = events.iterator(); it.hasNext();){
			Event e = it.next();
			
			switch (e.getPriority()) {
				case 1: Controller.c.gui.getArea1().append(e+"\n"); break;
				case 2: Controller.c.gui.getArea2().append(e+"\n"); break;
				case 3: Controller.c.gui.getArea3().append(e+"\n"); break;
				case 4: Controller.c.gui.getArea4().append(e+"\n"); break;
				case 5: Controller.c.gui.getArea5().append(e+"\n"); break;
				case 6: Controller.c.gui.getArea6().append(e+"\n"); break;
				default: break;
			}

			
			if(e.getPriority() <= Controller.c.minLogPriority){
				System.out.println(e);
			}
			pw.println(e);
			it.remove();
		}
		
		pw.println("\n\n\n");
	}
	
	/**
	 * Vypise konecnou statistiku
	 */
	public void printFinalStatistics(){
		StringBuffer sb = new StringBuffer("\n\n\n\n\n");
		sb.append("-----CELKOVA STATISTIKA-----\n");
		int trucksCount = 0;
		for(Dock d:Controller.c.dock){
			trucksCount += d.getGarage().size();
		}
		
		Brewery b = Controller.c.brewery;
		Controller.c.gui.getAreaStatistika().append("Pocet vytvorenych aut: " + trucksCount + "\n");
		Controller.c.gui.getAreaStatistika().append("Pocet vytvorenych cisteren: " + b.getCisternCount() + "\n");
		Controller.c.gui.getAreaStatistika().append("Pocet vytvorenych kamionu: " + b.getCamionCount() + "\n");
		Controller.c.gui.getAreaStatistika().append("Pocet rozvozenych sudu: " + Controller.c.deliveredBarrels + "\n");
		Controller.c.gui.getAreaStatistika().append("Pocet rozvozenych hl: " + Controller.c.deliveredHL + "\n");
		Controller.c.gui.getAreaStatistika().append("Pocet nestihnutych objednavek: " + Controller.c.deliveredLate + "\n");
		Controller.c.gui.getAreaStatistika().append("Pocet vyprodukovanych hl: " + b.getCountBeers() + "");
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
