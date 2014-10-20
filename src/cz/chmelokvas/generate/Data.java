package cz.chmelokvas.generate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Random;
import java.util.TreeSet;

import javax.swing.JFrame;

public class Data extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	/* Nazev pivovaru */
	private final String nameBrewery = "Chmelokvas";
	
	/* Celkovy pocet hospod (z tanku + ze sudu) */
	private final int countPub = 4000;
	
	/* Pocet prekladist + pivovar */
	private final int countDock = 9;
	
	/* Pocet hospod z tanku */
	private final int pubFromTank = 200;
	
	/* X rozmer mapy */
	private final int sizeMapX = 500;
	/* Y rozmer mapy */
	private final int sizeMapY = 500;
	
	/* Nazev souboru pro export */
	private final String nameExportFile = "export.txt";
	
	/* ID bodu
	 * 		0 -> pivovar
	 * 		1 - 8 -> prekladiste
	 * 		9 - 200 -> hospody z tanku
	 * 		201 - 4000 -> hospody ze sudu
	*/
	private int idCount = 1;
	
	/* Pole bodu prekladist + pivovar */
	private Node[] ps;
	
	/* Pole bodu hospod */
	private Node[] ph;
	
	
	public Data(){
		long t = System.currentTimeMillis();
		this.ps = generateS();
		this.ph = generateH();
		this.pub2dock();
		this.ph = neirNeighboursToPoint(ph, ph, 15);
		this.ps = neirNeighboursToPoint(ps, ph, 50);
		this.export();
		System.out.println("Cas generovani: "+(System.currentTimeMillis()-t));

		this.setSize(sizeMapX, sizeMapY);
		this.setVisible(true);
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawRect(0, 0, sizeMapX, sizeMapY);
		int pivovar=0, prekladiste=0, hospodaZT=0, hospodaZS=0;
		
//		g2.drawLine(0, 500/3, 500, 500/3);
//		g2.drawLine(0, 500-500/3, 500, 500-500/3);
//		g2.drawLine(500/3, 0, 500/3, 500);
//		g2.drawLine(500-500/3, 0, 500-500/3, 500);
		
		for(int i = 0; i < ps.length; i++){
			if(ps[i].getID() == 0){
				g2.setColor(Color.black);
				pivovar++;
			}
			else{
				g2.setColor(Color.red);
				prekladiste++;
			}
	//		g2.setColor(ps[i].getColor());
			g2.fill(new Ellipse2D.Double(ps[i].getX(), ps[i].getY(), 4,4));
		}
		
		for(int j = 0; j < ph.length; j++){
			if(ph[j].getDock().equals(ps[0])){
				hospodaZT++;
				g2.setColor(Color.black);
			}
			else{
				g2.setColor(Color.green);
				hospodaZS++;
			}
	//		g2.setColor(ph[j].getDock().getColor());
			g2.fill(new Ellipse2D.Double(ph[j].getX(), ph[j].getY(), 2,2));
		}
		System.out.println("Pivovar: "+pivovar+"   Prekladiste: "+prekladiste+"   Z tanku: "+hospodaZT+"   Ze sudu: "+hospodaZS);
	}
	
	private Node[] generateH(){		
		Node [] p = new Node[countPub];
		Node tmp;
		Random rd = new Random();
		
		/* X a Y souradnice hospody */
		float x, y;
		
		/* Vzdalenost vygenovaneho bodu a jiz ulozeneho bodu */
		float lng;
		
		for(int i = 0; i < p.length; i++){
			x = rd.nextInt(sizeMapX);
			y = rd.nextInt(sizeMapY);
			tmp = new Node(x, y, idCount);
			
			/* 1. vygenerovana hospoda je z tanku */
			if(i == 0)
			{ 
				p[i] = tmp;
				p[i].setDock(ps[0]);			// hospoda z tanku objednava z pivovaru
				ps[0].inclNeighbours();
				ps[0].getNeighbours().add(tmp);	// pivovar drzi seznam hospod z tanku
				idCount++;
				continue;
			}
			
			for(int j = 0; j < i; j++){
				
				lng = lengthEdge(tmp, p[j]);
				
				if(lng >= 2.0 || ps[j%countDock].getX() != x && ps[j%countDock].getY() != getY())
				{
					if( i < pubFromTank)
					{
						/* Hospody z tanku */
						p[i] = tmp;
						p[i].setDock(ps[0]);
						ps[0].getNeighbours().add(tmp);
						idCount++;
						break;
					}else
					{
						/* Hospody ze sudu */
						p[i] = tmp;
						idCount++;
						break;
					}
				}
			}
		}
		return p;
	}
	
	/* Pro co (a) hledam nejblizsi hospody (b) */
	private Node[] neirNeighboursToPoint(Node[] a, Node[] b, int cnt){
		 TreeSet<Node> tree;
		 float leng;
		 int k;
//		 int countPub = 0;
		 
		 for(int i = 0; i < a.length; i++)
		 {
			 if(a[i].getID() == 0) continue;
			 a[i].inclNeighbours();
			 
			 tree = new TreeSet<Node>(new Compar());
			 
			 for(int j = 0; j < b.length; j++){
				 if(i == j) continue;
				 
				 /* Nejblizsi hospody z okruhu 50 km */
				 if(a[i].getX()+50 > b[j].getX() && a[i].getX()-50 < b[j].getX() &&
					a[i].getY()+50 > b[j].getY() && a[i].getY()-50 < b[j].getY()){
					 
				 	leng = lengthEdge(a[i], b[j]);
					tree.add(new Node(b[j], leng));
				 }
			 }
			 if(tree.size() >= cnt)
			 {	
				 k = 0;
				 for(Node x : tree)
				 {
					 /* ulozi pouze predem definovany pocet sousedu */
					 if(k++ == cnt) break;
					 /* Ulozi instanci nejblizsiho souseda k danne hospode se vzdalenosti */
					 a[i].getNeighbours().add(x);
			//		 System.out.println(x.getID());
				 }
				 
// Kontrola sousedu
//				 System.out.println("--------------------------------------------");
//				 System.out.println("Hospoda: "+i +"     "+a[i]);
//				 for(int l = 0; l < a[i].getNeighbours().size(); l++){
//					 System.out.println("Sousedi: "+l+"   "+a[i].getNeighbours().get(l).getX()+"   "+a[i].getNeighbours().get(l).getY());
//				 }
//				 countPub++;
			 }else for(Node x : tree) a[i].getNeighbours().add(x);
		 }
//		 System.out.println("------------------------------------------------------   "+countPub);
		 return a;
	}
	
	class Compar implements Comparator<Node>{
		 
	    @Override
	    public int compare(Node e1, Node e2) {
	        if(e1.getD() > e2.getD()){
	            return 1;
	        } else {
	            return -1;
	        }
	    }
	}
	
	private Node[] generateS(){
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
				x = rd.nextInt(33)+xTmp+67;
				y = rd.nextInt(33)+yTmp+67;
				
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
	
	private float lengthEdge(Node a, Node b)
	{
		/* Vzorec sqrt( (a1-b1)^2 + (a2-b2)^2 ) */
		float p = (float) Math.sqrt(Math.pow(a.getX()-b.getX(), 2.0) +
				Math.pow(a.getY()-b.getY(), 2.0));
		return p;
	}
	
	/* Prirad k hospode ze sudu nejblizsi prekladiste */
	private void pub2dock()
	{
		float lng, lngMin;
		int ind = 0;
		
		/* 0 az pubFromTank maji prekladiste pivovar */
		for(int i = pubFromTank; i < ph.length; i++)
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
	
	private void export(){
		
		String line = null;
		FileWriter file = null;
		try {file = new FileWriter(nameExportFile);} 
		catch (IOException e) {e.printStackTrace();}
		BufferedWriter bf = new BufferedWriter(file);
		
		try {
			/* ID_pivovar	Nazev_pivovar	X	Y */
			line = ps[0].getID()+"\t"+ps[0].getName()+"\t"+ps[0]+"\n";
			bf.write(line);
			
			/* Pocet prekladist */
			line = (countDock-1)+"\n";
			bf.write(line);
			
			/* ID_prekladiste	X	Y */
			for(Node x : ps){
				line = x.getID()+"\t"+x+"\n";
				bf.write(line);
			}
			
			/* Pocet hospod z tanku */
			line = pubFromTank+"\n";
			bf.write(line);
			
			for(Node x : ph){
				/* Pocet hospod ze sudu */
				if(x.getID() == (pubFromTank+countDock)){
					line = (countPub-pubFromTank)+"\n";
					bf.write(line);
				}
				line = x.getID()+"\t"+x+"\n";
				bf.write(line);
			}
						
			/* ID_zdroj:ID_soused1,vzdalenost;ID_soused2,vzdalenost;ID_soused3,vzdalenost;... */	
			for(Node x : ps){
				line = x.getID()+":";
				for(Node y : x.getNeighbours()){
					line += y.getDock().getID()+","+y.getD()+";";
				}
				line += "\n";
				bf.write(line);
			}
			for(Node x : ph){
				line = x.getID()+":";
				for(Node y : x.getNeighbours()){
					line += y.getDock().getID()+","+y.getD()+";";
				}
				line += "\n";
				bf.write(line);
			}
		} 
		catch (IOException e1) {e1.printStackTrace();}
		
		try {bf.close();} 
		catch (IOException e) {e.printStackTrace();}
	}
	
	private void lengXY(Node p)
	{
		float lengXmin = Float.MAX_VALUE, lengXmax = Float.MIN_VALUE, lengYmin = Float.MAX_VALUE, lengYmax = Float.MIN_VALUE;
		for(int i = 0; i < this.ph.length; i++)
		{
			if(this.ph[i].getDock().equals(p))
			{
				if(ph[i].getX() < lengXmin) lengXmin = ph[i].getX();
				if(ph[i].getX() > lengXmax) lengXmax = ph[i].getX();
				if(ph[i].getY() < lengYmin) lengYmin = ph[i].getY();
				if(ph[i].getY() > lengYmax) lengYmax = ph[i].getY();
			}	
		}
//		System.out.println(Math.abs(lengXmax - lengXmin) + " x " + Math.abs(lengYmax - lengYmin));
	}
	
	private void checkPub(){
		int ck[] = new int[countDock];
		for(int i = 0; i < this.ph.length; i++)
		{			
			if(this.ph[i].getDock().equals(this.ps[0])) ck[0]++;
			if(this.ph[i].getDock().equals(this.ps[1])) ck[1]++;
			if(this.ph[i].getDock().equals(this.ps[2])) ck[2]++;
			if(this.ph[i].getDock().equals(this.ps[3])) ck[3]++;
			if(this.ph[i].getDock().equals(this.ps[4])) ck[4]++;
			if(this.ph[i].getDock().equals(this.ps[5])) ck[5]++;
			if(this.ph[i].getDock().equals(this.ps[6])) ck[6]++;
			if(this.ph[i].getDock().equals(this.ps[7])) ck[7]++;
			if(this.ph[i].getDock().equals(this.ps[8])) ck[8]++;
		}
		int count = 0;
		for(int j = 0; j < ck.length; j++)
		{
			System.out.println("Sklad_"+j+": "+ck[j]);
			count += ck[j];
		}
		System.out.println("Celkem: "+count);
	}
	
	public static void main(String [] arg)
	{
		Data d = new Data();
		System.out.println("Hospoda s poslednim ID: "+d.ph[d.ph.length-1].getID());
		
//		for(int i = 0; i < d.ps.length; i++)
//		{
//			d.lengXY(d.ps[i]);
//		}
//		d.checkPub();
		
	}
}
