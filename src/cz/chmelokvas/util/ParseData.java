package cz.chmelokvas.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	/** Atribut jmena vstupniho souboru */
	// TODO jmeno souboru pres konstruktor
	private final String nameFile = "export.txt";
	
	/** Atribut instance pivovaru */
	private Brewery brewery;
	
	/** Atribut pole instanci prekladiste */
	private List<Dock> dock = new ArrayList<>();
	
	/** Atribut pole instance hospod */
	private List<Pub> pub = new ArrayList<>();
	
	/** Atribut poctu tankovych hospod */
	private int tankCount;
	
	/** Atribut poctu sudovych hospod */
	private int kegCount;
	
	/** Atribut poctu prekladist */
	private int dockCount;
		

	private void importData(){
		String line;
		FileReader rd = null;
		BufferedReader br = null;
		
		try {
			rd = new FileReader(nameFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		br = new BufferedReader(rd);
		try {
			while(true){
				
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
				kegCount = Integer.valueOf(line);
				for(int i = 0; i < kegCount; i++){
					line = br.readLine();
					loadPub(line, false);
				}
				
				/* Nacti sousedy */
				while((line = br.readLine()) != null){
					loadNeighbours(line);
				}
				break;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		
		if(idCont == 0){
			// pivovar
			for(int i = 0; i < parse.length; i++){
				tmpParse = parse[i].split(",");
				v = Integer.valueOf(tmpParse[0]);
				d = Float.valueOf(tmpParse[1]);
				brewery.getRoutes().add(new Route(d, v));
			}
		}
		if(idCont > 0 && idCont <= dockCount){
			// prekladiste
			for(int i = 0; i < parse.length; i++){
				tmpParse = parse[i].split(",");
				v = Integer.valueOf(tmpParse[0]);
				d = Float.valueOf(tmpParse[1]);
				dock.get(idCont-1).getRoutes().add(new Route(d, v));
			}	
		}
		if(idCont > dockCount){
			// hospody
			for(int i = 0; i < parse.length; i++){
				tmpParse = parse[i].split(",");
				v = Integer.valueOf(tmpParse[0]);
				d = Float.valueOf(tmpParse[1]);
				pub.get(idCont-dockCount-1).getRoutes().add(new Route(d, v));
			}	
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
		
		pub.add(new Pub(tmpDock, idP, idC, x, y, isTank));
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
		
		dock.add(new Dock(id, x, y));
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
	
	public static void main(String [] arg){
		ParseData pd = new ParseData();
		pd.importData();
		
		int i = 3999;
		System.out.println(pd.pub.get(i).getRoutes().get(0).getValue());
		System.out.println(pd.pub.get(i).getRoutes().get(pd.pub.get(i).getRoutes().size()-1).getValue());
		System.out.println("\n");
		i = 8;
		System.out.println(pd.dock.get(i-1).getRoutes().get(0).getValue());
		System.out.println(pd.dock.get(i-1).getRoutes().get(pd.dock.get(i-1).getRoutes().size()-1).getValue());
	}
}
