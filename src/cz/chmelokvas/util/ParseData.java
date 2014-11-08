package cz.chmelokvas.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.chmelokvas.brewery.Brewery;
import cz.chmelokvas.brewery.Dock;
import cz.chmelokvas.brewery.Pub;
import cz.chmelokvas.brewery.Stock;


public class ParseData {
	
	/*
	 * idCont = id u kontroleru
	 * idProv = id u nadrizeneho, prekladiste id = 0
	 * 
	 * hospoda = idCont(id souboru), idProv = prekladiste
	 * 		instrukce providera = prekladiste
	 * 
	 * prekladiste = idCont(id souboru), idProv = 0, instance = this
	 * pivovar = idCont(id souboru), idProv = 0, instance = null
	 * 
	 */
	
	private Controller c;
	
	/** Atribut jmena vstupniho souboru */
	private final String nameFile;
	
	/** Atribut instance pivovaru */
	private Brewery brewery;
	
	/** Atribut pole instanci prekladiste */
	private final List<Dock> dock = new ArrayList<Dock>();
	
	/** Atribut pole instance hospod */
	private final List<Pub> pub = new ArrayList<Pub>();
	
	/** Atribut poctu tankovych hospod */
	private int tankCount;
	
	/** Atribut poctu sudovych hospod */
	private int barrelCount;
	
	/** Atribut poctu prekladist */
	private int dockCount;
		
	
	public ParseData(String nameFile){
		this.nameFile = nameFile;
		importData();
	}

	private void importData(){
		String line;
		FileReader rd = null;
		BufferedReader br = null;
		
		try {
			rd = new FileReader(nameFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		br = new BufferedReader(rd);
		try {				
			/* Nacti pivovar */
			line = br.readLine();
			loadBrewery(line);
			
			/* Nacti prekladiste */
			line = br.readLine();
			dockCount = Integer.valueOf(line);
			for(int i = 0; i < dockCount; i++){
				line = br.readLine();
				loadDock(line);
			}
			
			/* Nacti hospody z tanku */
			line = br.readLine();
			tankCount = Integer.valueOf(line);
			for(int i = 0; i < tankCount; i++){
				line = br.readLine();
				loadPub(line, true);
			}			
			
			/* Nacti hospody ze sudu */
			line = br.readLine();
			barrelCount = Integer.valueOf(line);
			for(int i = 0; i < barrelCount; i++){
				line = br.readLine();
				loadPub(line, false);
			}
			
			c = new Controller(pub, dock, brewery);
			
			for(Dock d:dock){
				int n = d.getCustomers().size();
				d.setD(new float[n][n]);
				d.setP(new int[n][n]);
			}
			
			/* Nacti sousedy */
			while((line = br.readLine()) != null){
				loadNeighbours(line);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Format dat:
	 * 	ID:ID,d;ID,d;...
	 * @param line
	 */
	private void loadNeighbours(String line){
		String parse[], tmpParse[];
		int idCont, v;
		Float d;
				
		parse = line.split(":");
		idCont = Integer.valueOf(parse[0]);
		
		parse = parse[1].split(";");
		
		for(int i = 0; i < parse.length; i++){
			tmpParse = parse[i].split(",");
			v = Integer.valueOf(tmpParse[0]);
			d = Float.valueOf(tmpParse[1]);
			c.addRoute(idCont, v, d);
		}
	}
	
	/**
	 * Nacte hospody a ulozi do pole
	 * Format dat:
	 * 	ID	prekladiste	X	Y
	 * @param line nactena radka
	 * @param isTank je ci neni tank
	 */
	private void loadPub(String line, boolean isTank){
		String parse[];
		float x, y;
		int idC, idP;
		Stock tmpDock;
		
		parse = line.split("\t");
		/* ID hospody */
		idC = Integer.valueOf(parse[0]);
		/* ID prekladiste */
		idP = Integer.valueOf(parse[1]);
		tmpDock = dock.get(idP-1);
		/* X souradnice */
		x = Float.valueOf(parse[2]);
		/* Y souradnice */
		y = Float.valueOf(parse[3]);
		
		Pub p = new Pub(tmpDock, tmpDock.getCustomers().size(), idC, x, y, isTank);
		
		pub.add(p);
		tmpDock.getCustomers().add(p);
	}
	
	/**
	 * Nacte prekladiste a ulozi do pole
	 * Format dat:
	 * 	ID	X	Y
	 * @param line nactena radka
	 * @param i	index do pole
	 */
	private void loadDock(String line){
		String parse[];
		float x, y;
		int id;
		
		parse = line.split("\t");
		/* ID prekladiste */
		id = Integer.valueOf(parse[0]);
		/* X souradnice */
		x = Float.valueOf(parse[1]);
		/* Y souradnice */
		y = Float.valueOf(parse[2]);
		
		Dock d = new Dock(id, x, y);
		dock.add(d);
	}
	
	/**
	 * /**
	 * Nacte prekladiste a ulozi do pole
	 * Format dat:
	 * 	ID	Nazev	X	Y
	 * @param line nactena radka
	 */
	private void loadBrewery(String line){
		String parse[];
		float x, y;
		int id;
		String name;
		
		parse = line.split("\t");
		/* ID */
		id = Integer.valueOf(parse[0]);
		/* nazev */
		name = parse[1];
		/* X souradnice */
		x = Float.valueOf(parse[2]);
		/* Y souradnice */
		y = Float.valueOf(parse[3]);
		
		brewery = new Brewery(name, id, x, y);
	}
	
	
	
	public Brewery getBrewery() {
		return brewery;
	}

	public void setBrewery(Brewery brewery) {
		this.brewery = brewery;
	}

	public int getTankCount() {
		return tankCount;
	}

	public void setTankCount(int tankCount) {
		this.tankCount = tankCount;
	}

	public int getBarrelCount() {
		return barrelCount;
	}

	public void setBarrelCount(int barrelCount) {
		this.barrelCount = barrelCount;
	}

	public int getDockCount() {
		return dockCount;
	}

	public void setDockCount(int dockCount) {
		this.dockCount = dockCount;
	}

	public List<Dock> getDock() {
		return dock;
	}

	public List<Pub> getPub() {
		return pub;
	}
	
	public Controller getC(){
		return c; 
	}
}
