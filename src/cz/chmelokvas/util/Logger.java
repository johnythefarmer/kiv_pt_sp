package cz.chmelokvas.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import cz.chmelokvas.brewery.Brewery;
import cz.chmelokvas.brewery.Dock;
import cz.chmelokvas.brewery.Pub;
import cz.chmelokvas.brewery.Time;

/**
 * Nastroj pro praci s vystupem do souboru a prikazove radky
 * @author Jan Dvorak A13B0293P
 *
 */
public class Logger {
	/**
	 * Nastroj pro vypis do souboru
	 */
	private PrintWriter pw;
	
	/**
	 * Mnozina udalosti, ktere cekaji na vypsani
	 */
	private final SortedSet<Event> events = new TreeSet<Event>();
	
	/**
	 * Jedinacek
	 */
	private static final Logger INSTANCE = new Logger();
	
	/**
	 * Objekt o kterem vypisujeme informace
	 */
	private Loggable l;
	
	/**
	 * Privatni konstruktor
	 */
	private Logger(){}
	
	/**
	 * Vrati logger nad danym souborem (tedy pokud jsme mu jeste zadny nepriradili)
	 * @param file soubor kam zapisujeme
	 * @return instance jedinacka
	 */
	public static Logger getInstance(String file){
		if(INSTANCE.pw == null){
			INSTANCE.setOutputFile(file);
		}
		return INSTANCE;
	}
	
	/**
	 * Vrati instanci bez zadneho meneni vystupniho souboru
	 * @return instance jedinacka
	 */
	public static Logger getInstance(){
		return INSTANCE;
	}
	
	/**
	 * Zaznamena si udalost k vypisu
	 * @param t cas udalosti
	 * @param priority priorita
	 * @param message zprava udalosti
	 */
	public void log(Time t, int priority, String message){
		events.add(new Event(t, priority, message));
	}
	
	/**
	 * Vypise vsechny udalosti, ktere se u neho nashromazdily
	 * Seradi je podle casu
	 */
	public void printEvents(){
		pw.println("---" + Controller.c.mainTime + "---");
		StringBuilder[] sb = new StringBuilder[6];
		for(int i = 0; i < 6; i++){
			sb[i] = new StringBuilder("");
		}
		for(Iterator<Event> it = events.iterator(); it.hasNext();){
			Event e = it.next();
			sb[e.getPriority()-1].append(e+"\n");
			pw.println(e);
			it.remove();
		}
		
		Controller.c.gui.getArea1().append(sb[0].toString());
		Controller.c.gui.getArea2().append(sb[1].toString());
		Controller.c.gui.getArea3().append(sb[2].toString());
		Controller.c.gui.getArea4().append(sb[3].toString());
		Controller.c.gui.getArea5().setText(l.tempInfo());
		Controller.c.gui.getArea6().append(sb[5].toString());
		pw.println("\n\n\n");
	}
	
	
	public void setLoggableObject(Loggable l){
		this.l = l;
	}
	
	/**
	 * Vypise statistiku
	 */
	public void printStatistics(){
		StringBuffer sb = new StringBuffer("-----"+ Controller.c.mainTime +"-----\n");
		int trucksCount = 0;
		for(Dock d:Controller.c.dock){
			trucksCount += d.getGarage().size();
		}
		
		Brewery b = Controller.c.brewery;
		sb.append("Pocet vytvorenych aut: " + trucksCount + "\n");
		sb.append("Pocet vytvorenych cisteren: " + b.getCisternCount() + "\n");
		sb.append("Pocet vytvorenych kamionu: " + b.getCamionCount() + "\n");
		sb.append("Pocet rozvozenych sudu: " + Controller.c.deliveredBarrels + "\n");
		sb.append("Pocet rozvozenych hl: " + Controller.c.deliveredHL + "\n");
		sb.append("Pocet nestihnutych objednavek: " + Controller.c.deliveredLate + "\n");
		sb.append("Pocet vyprodukovanych hl: " + b.getCountBeers() + "");
		Controller.c.gui.getAreaStatistika().setText(sb.toString());
	}
	
	/**
	 * Vypise konecne statistiky
	 */
	public void printFinalStatistics(){
		StringBuffer sb = new StringBuffer("-----"+ Controller.c.mainTime +"-----\n");
		int trucksCount = 0;
		for(Dock d:Controller.c.dock){
			trucksCount += d.getGarage().size();
		}
		
		Brewery b = Controller.c.brewery;
		sb.append("Pocet vytvorenych aut: " + trucksCount + "\n");
		sb.append("Pocet vytvorenych cisteren: " + b.getCisternCount() + "\n");
		sb.append("Pocet vytvorenych kamionu: " + b.getCamionCount() + "\n");
		sb.append("Pocet rozvozenych sudu: " + Controller.c.deliveredBarrels + "\n");
		sb.append("Pocet rozvozenych hl: " + Controller.c.deliveredHL + "\n");
		sb.append("Pocet nestihnutych objednavek: " + Controller.c.deliveredLate + "\n");
		sb.append("Pocet vyprodukovanych hl: " + b.getCountBeers() + "");
		Controller.c.gui.getAreaStatistika().setText(sb.toString());
		System.out.println(sb);
		pw.println(sb);
		
		printPubStatistics("pubStat.txt");
		printCarStatistics("carStat.txt");
	}
	
	/**
	 * Vypise statistiku o autech
	 * @param fileName soubor kam piseme
	 */
	private void printCarStatistics(String fileName){
		try{
			PrintWriter pw = new PrintWriter(fileName);
			
			pw.println(Controller.c.brewery.finalInfo());
			
			for(Dock d : Controller.c.dock){
				pw.println(d.finalInfo());
			}
			
			pw.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Vypise statistiky o hospodach
	 * @param fileName nazev souboru kam zapisujeme
	 */
	private void printPubStatistics(String fileName){
		try {
			PrintWriter pw = new PrintWriter(fileName);
			
			for(Pub p : Controller.c.pub){
				pw.println(p.finalInfo());
			}
			
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		if(pw != null){
			pw.close();
		}
	}
	
	/**
	 * Nastavi novy soubor pro zapis
	 * @param file soubor
	 */
	private void setOutputFile(String file){
		try {
			pw = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}
}
