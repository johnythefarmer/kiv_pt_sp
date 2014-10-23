package cz.chmelokvas.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import cz.chmelokvas.brewery.Dock;
import cz.chmelokvas.brewery.Pub;
import cz.chmelokvas.brewery.Brewery;
import cz.chmelokvas.brewery.Stock;
import cz.chmelokvas.brewery.TransportNode;


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
	private Dock[] dock;
	
	/** Atribut pole instance hospod */
	private Pub[] pub;
	
	public ParseData(){
	}
	
	

	private void importData(){
		String line;
		FileReader rd = null;
		BufferedReader br = null;
		int index = 0;
		
		try {
			rd = new FileReader(nameFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		br = new BufferedReader(rd);
		try {
			while(true){
				
				line = br.readLine();
				loadBrewery(line);
				
				line = br.readLine();
				index = Integer.valueOf(line);
				dock = new Dock[index];
				for(int i = 0; i < index; i++){
					line = br.readLine();
					loadDock(line, i);
				}
				
				line = br.readLine();
				System.out.println(line);
				index = Integer.valueOf(line);
				pub = new Pub[index];
				for(int i = 0; i < index; i++){
					line = br.readLine();
					loadPub(line, i);
				}
				
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
	
	private void loadNeighbours(String line){
		
	}
	
	/**
	 * Nacte hospody a ulozi do pole
	 * Format dat:
	 * 	ID	prekladiste	X	Y
	 * @param line nactena radka
	 * @param i	index do pole
	 */
	private void loadPub(String line, int i){
		String parse[];
		float x, y;
		int idC, idP;
		
		parse = line.split("\t");
		/* ID hospody */
		idC = Integer.valueOf(parse[0]);
		/* ID prekladiste */
		idP = Integer.valueOf(parse[1]);
		/* X souradnice */
		x = Float.valueOf(parse[2]);
		/* Y souradnice */
		y = Float.valueOf(parse[3]);
		
		pub[i] = new Pub(idP, idC, x, y);
	}
	
	/**
	 * Nacte prekladiste a ulozi do pole
	 * Format dat:
	 * 	ID	X	Y
	 * @param line nactena radka
	 * @param i	index do pole
	 */
	private void loadDock(String line, int i){
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
		
		dock[i] = new Dock(id, x, y);
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
	}
}
