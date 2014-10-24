package cz.chmelokvas.generate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;

public class Data extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	/** Nazev pivovaru */
	private final String nameBrewery = "Chmelokvas";
	
	/** Celkovy pocet hospod (z tanku + ze sudu) */
	private final int countPub = 4000;
	
	/** Pocet prekladist + pivovar */
	private final int countDock = 9;
	
	/** Pocet hospod z  tanku */
	private final int pubFromTank = 200;
	
	/** X rozmer mapy */
	private final int sizeMapX = 500;
	
	/** Y rozmer mapy */
	private final int sizeMapY = 500;
	
	/** Nazev souboru pro export */
	private final String nameExportFile = "export.txt";
	
	/** ID bodu
	 * 		0 -> pivovar
	 * 		1 - 8 -> prekladiste
	 * 		9 - 200 -> hospody z tanku
	 * 		201 - 4000 -> hospody ze sudu
	*/
	private int idCount = 1;
	
	/** Pole bodu prekladist + pivovar */
	private final Node[] ps;
	
	/** Pole bodu hospod */
	private final Node[] ph;
	
	
	public Data(){
		long t = System.currentTimeMillis();
		
		this.ps = genStocks();
		this.ph = genPubs();
		
		pub2dock();
		neirNeighbourFind(ph);
		neirNeighbourFind(ps);
		export();
		
		System.out.println("Cas generovani: "+(System.currentTimeMillis()-t));

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(sizeMapX, sizeMapY);
		this.setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(sizeMapX*2, sizeMapY*2);
		this.setVisible(true);
	}
	
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.scale(2, 2);
		g2.drawRect(0, 0, sizeMapX, sizeMapY);
		int pivovar=0, prekladiste=0, hospodaZT=0, hospodaZS=0;
		
//		g2.drawLine(0, 500/3, 500, 500/3);
//		g2.drawLine(0, 500-500/3, 500, 500-500/3);
//		g2.drawLine(500/3, 0, 500/3, 500);
//		g2.drawLine(500-500/3, 0, 500-500/3, 500);
		
		for(int i = 0; i < ps.length; i++){
			for(Route r: ps[i].getNeighbours()){
//				System.out.println(n.get);
				g2.setColor(new Color(10,10,10,50));
				g2.drawLine((int)ps[i].getX(), (int)ps[i].getY(), (int)r.getValue().getX(), (int)r.getValue().getY());
			}
			if(ps[i].getID() == 0){
				g2.setColor(Color.blue);
				pivovar++;
			}
			else{
				g2.setColor(Color.red);
				prekladiste++;
			}
	//		g2.setColor(ps[i].getColor());
			g2.fill(new Ellipse2D.Double(ps[i].getX()-2.5, ps[i].getY()-2.5, 5,5));
		}
		
		for(int j = 0; j < ph.length; j++){
			for(Route r: ph[j].getNeighbours()){
//				System.out.println(n.get);
				g2.setColor(new Color(100,100,100,40));
				g2.drawLine((int)ph[j].getX(), (int)ph[j].getY(), (int)r.getValue().getX(), (int)r.getValue().getY());
			}
//			if(ph[j].getDock().equals(ps[0])){
//				hospodaZT++;
//				g2.setColor(Color.blue);
//			}
//			else{
				g2.setColor(Color.green);
				hospodaZS++;
//			}
	//		g2.setColor(ph[j].getDock().getColor());
			
			g2.fill(new Ellipse2D.Double(ph[j].getX()-1.5, ph[j].getY()-1.5, 3,3));
		}
		System.out.println("Pivovar: "+pivovar+"   Prekladiste: "+prekladiste+"   Z tanku: "+hospodaZT+"   Ze sudu: "+hospodaZS);
	}
	
	/**
	 * Generovani prekladiste a pivovaru
	 * @return	p	Seznam prekladist + pivovar
	 */
	private Node[] genStocks(){
		Node [] p = new Node[countDock];
		Random rd = new Random();
		Color[] cl = {Color.yellow, Color.black, Color.red, Color.green, Color.blue,
					Color.orange, Color.magenta, Color.pink, Color.darkGray};
		
		/* Pomocne X a Y souradnice pro generovani  */
		int xTmp = 0, yTmp = 0;
		
		/* X a Y souradnice prekladiste/pivovaru */
		float x, y;
		
		/* Generuj XY 67 - 100 v kazdem sektoru */
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++)
			{	
				/* Generovani souradnic prekladiste/pivovaru */
				x = rd.nextFloat()*33+xTmp+67;
				y = rd.nextFloat()*33+yTmp+67;
				
				if(i != 1 || j != 1)
				{
					/* Prekladiste */
					p[idCount] = new Node(x, y, idCount);
					p[idCount].setColor(cl[idCount]);
					idCount++;
				}else
				{	
					/* Pivovar */
					p[0] = new Node(x, y, 0);
					p[0].setName(nameBrewery);
					p[0].setColor(cl[idCount]);
					
				}
				xTmp += 166;
			}
			yTmp += 166;
			xTmp = 0;
		}
		return p;
	}
	
	/**
	 * Generovani hospod
	 * 
	 * @return	p	Pole hospod
	 */
	private Node[] genPubs(){		
		Node [] p = new Node[countPub];
		Node tmp;
		Random rd = new Random();
		
		/* X a Y souradnice hospody */
		float x, y;
		
		/* Vzdalenost vygenovaneho bodu a jiz ulozeneho bodu */
		float lng;
		
		for(int i = 0; i < p.length; i++){
			x = rd.nextFloat()*sizeMapX;
			y = rd.nextFloat()*sizeMapY;
			tmp = new Node(x, y, idCount);
			
			if(i == 0)
			{ 
				p[0] = tmp;
				idCount++;
				continue;
			}
			
			for(int j = 0; j < i; j++){
				
				lng = lengthEdge(tmp, p[j]);
				
				if(lng >= 2.0 || ps[j%countDock].getX() != x && ps[j%countDock].getY() != getY()){
						p[i] = tmp;
						idCount++;
						break;
				}
			}
		}
		return p;
	}
	
	private boolean territoryCheck(Node objA, Node objB){
		boolean p = objA.getX()+100 > objB.getX() && objA.getX()-100 < objB.getX() &&
				objA.getY()+100 > objB.getY() && objA.getY()-100 < objB.getY();
		return p;
	}
	
	/**
	 * Hledani nejblizsich ceste pro danny vstup
	 * 
	 * @param	argv	Ukazatel na vstupni data (prekladiste,hospody)
	 */
	private void neirNeighbourFind(Node[] argv){
		TreeSet<Route> tree = null;
		float leng;
		
		for(Node objA : argv){
			tree = new TreeSet<Route>(new Compar());
			
			for(Node objB : ph){
				if(objA == objB){continue;}
				
				if(territoryCheck(objA, objB)){
					leng = lengthEdge(objA, objB);
					tree.add(new Route(leng,objB));
				}
			}
			neirNeighbourAdd(tree, objA, argv.length);
		}
	}
	
	private void neirNeighbourAdd(Set<Route> tree, Node objA, int leng){
		int count = 0;
		for(Route x : tree){
			count = objA.getNeighbours().size();
			if(count <= 50){
				if(leng == countPub && count < 15 && x.getValue().getNeighbours().size() < 15){
						addNeighbourNode(objA, x); 
				}
				if(leng == countDock){
					addNeighbourNode(objA,x);
				}
			}else{ break; }
			
		}
	}
	
	
	/**
	 * Pridani hospody do seznamu sousedu pro oba uzly
	 * @param x	1. uzel
	 * @param y	2. uzel s hodnotou vzdalenosti
	 */
	private void addNeighbourNode(Node x, Route y){
		if(!x.getNeighbours().contains(y)){
			x.getNeighbours().add(y);
			y.getValue().getNeighbours().add(new Route(y.getD(), x));
		}
	}
	
	class Compar implements Comparator<Route>{
		 
	    @Override
	    public int compare(Route e1, Route e2) {
	        if(e1.getD() > e2.getD()){
	            return 1;
	        } else {
	            return -1;
	        }
	    }
	}
	
	/**
	 * Vypocet vzdalenosti 2 uzlu
	 * @param a	1. uzel
	 * @param b	2. uzel
	 * @return	p	vzdalenost 2 uzlu
	 */
	private float lengthEdge(Node a, Node b)
	{
		/* Vzorec sqrt( (a1-b1)^2 + (a2-b2)^2 ) */
		return (float) Math.sqrt(Math.pow(a.getX()-b.getX(), 2.0) +
				Math.pow(a.getY()-b.getY(), 2.0));
	}
	
	/** 
	 * Prirad k hospode ze sudu nejblizsi prekladiste
	 * 
	*/
	private void pub2dock()
	{
		float lng, lngMin;
		int ind = 0;
		
		for(int i = 0; i < ph.length; i++)
		{
			lngMin = Float.MAX_VALUE;
			
			/* Porovnavej pouze prekladiste (index 0 je pivovar) */
			for(int j = 1; j < ps.length; j++)
			{
				lng = lengthEdge(ph[i], ps[j]);
				
				if(lng < lngMin)
				{
					lngMin = lng;
					ind = j;
				}
			}
			ph[i].setDock(ps[ind]);
		}
	}
	
	/**
	 * Export dat do textoveho souboru
	 */
	private void export(){
		
		FileWriter file = null;
		
		try {
			file = new FileWriter(nameExportFile);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedWriter bf = new BufferedWriter(file);
		
		exportDock(bf);
					
		exportPub(bf);
		
		exportNeighbours(bf);
		
		try {
			bf.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Export hospod
	 * @param bf buffer pro zapis
	 */
	private void exportPub(BufferedWriter bf){
		String line;
		try {
			/* Pocet hospod z tanku */
			line = pubFromTank+"\n";
			bf.write(line);
			
			for(Node x : ph){
				/* Pocet hospod ze sudu */
				if(x.getID() == (pubFromTank+countDock)){
					line = (countPub-pubFromTank)+"\n";
					bf.write(line);
				}
				line = x.getID()+"\t"+x.getDock().getID()+"\t"+x+"\n";
				bf.write(line);
			}
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Export prekladist + pivovar
	 * @param bf buffer pro zapis
	 */
	private void exportDock(BufferedWriter bf){
		String line;
		try {
			/* ID_pivovar	Nazev_pivovar	X	Y */
			line = ps[0].getID()+"\t"+ps[0].getName()+"\t"+ps[0]+"\n";
			bf.write(line);
			
			/* Pocet prekladist */
			line = (countDock-1)+"\n";
			bf.write(line);
			
			/* ID_prekladiste	X	Y */
			for(int i = 1; i < ps.length; i++){
				line = ps[i].getID()+"\t"+ps[i]+"\n";
				bf.write(line);
			}
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Export sousedu
	 * @param bf buffer pro zapis
	 */
	private void exportNeighbours(BufferedWriter bf){
		String line;
		try {
			/* ID_zdroj	ID_prekladiste:ID_soused1,vzdalenost;ID_soused2,vzdalenost;ID_soused3,vzdalenost;... */	
			for(Node x : ps){
				line = x.getID()+":";
				for(Route y : x.getNeighbours()){
					line += y.getValue().getID()+","+y.getD()+";";
				}
				line += "\n";
				bf.write(line);
			}
			for(Node x : ph){
				line = x.getID()+":";
				for(Route y : x.getNeighbours()){
					line += y.getValue().getID()+","+y.getD()+";";
				}
				line += "\n";
				bf.write(line);
			}
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	
	public static void main(String [] arg)
	{
		Data d = new Data();
		System.out.println("Hospoda s poslednim ID: "+d.ph[d.ph.length-1].getID());
		
	}
}
