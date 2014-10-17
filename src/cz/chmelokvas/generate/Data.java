package cz.chmelokvas.generate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import javax.swing.JFrame;

public class Data extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private final static int countPub = 4000;
	Node[] ps;
	Node[] ph;
	
	
	public Data(){
	//	long t = System.currentTimeMillis();
		this.ps = generateS();
		this.ph = generateH();
		this.pub2dock();
	//	System.out.println(System.currentTimeMillis()-t);

		this.setSize(500, 500);
		this.setVisible(true);
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawRect(0, 0, 500, 500);
		
//		g2.drawLine(0, 500/3, 500, 500/3);
//		g2.drawLine(0, 500-500/3, 500, 500-500/3);
//		g2.drawLine(500/3, 0, 500/3, 500);
//		g2.drawLine(500-500/3, 0, 500-500/3, 500);
		
		for(int i = 0; i < ps.length; i++){
			g2.setColor(ps[i].getColor());
			g2.fill(new Ellipse2D.Double(ps[i].getX(), ps[i].getY(), 4,4));
		}
		
		for(int j = 0; j < ph.length; j++){
			g2.setColor(ph[j].getDock().getColor());
			g2.fill(new Ellipse2D.Double(ph[j].getX(), ph[j].getY(), 2,2));
		}
	}
	
	private Node[] generateH(){		
		Node [] p = new Node[countPub];
		Node tmp;
		Random rd = new Random();
		float x, y;
		float lng;
		
		for(int i = 0; i < p.length; i++){
			x = rd.nextInt(500);
			y = rd.nextInt(500);
			tmp = new Node(x, y);
			
			if(i == 0){ p[i] = new Node(x, y); continue; }
			
			for(int j = 0; j < i; j++){
				
		//		lng = (float) Math.sqrt((x-p[j].getX())*(x-p[j].getX())+(y-p[j].getY())*(y-p[j].getY()));
				lng = lengthEdge(tmp, p[j]);
				
				if(lng >= 2.0 || ps[j%9].getX() != x && ps[j%9].getY() != getY()){
					p[i] = new Node(x, y);
					break;
				}
			}
		}
		return p;
	}
	
	private Node[] generateS(){
		Node [] p = new Node[8];
		Random rd = new Random();
		Color[] cl = {Color.yellow, Color.black, Color.red, Color.green, Color.blue,
					Color.orange, Color.magenta, Color.pink, Color.darkGray};
		int xTmp = 0;
		int yTmp = 0;
		
		float x, y;
		int k = 0;
		
		/* Generuj XY 67 - 100 v kazdem sektoru */
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(i != 1 || j != 1){
				x = rd.nextInt(33)+xTmp+67;
				y = rd.nextInt(33)+yTmp+67;
				p[k] = new Node(x, y);
				p[k].setColor(cl[k]);
				k++;}
				xTmp += 166;
			}
			yTmp += 166;
			xTmp = 0;
			
		}
		return p;
	}
	
	private float lengthEdge(Node a, Node b){
		float p = (float) Math.sqrt(Math.pow(a.getX()-b.getX(), 2.0) +
				Math.pow(a.getY()-b.getY(), 2.0));
		return p;
	}
	
	/* Prirad k hospode nejblizsi prekladiste */
	private void pub2dock(){
		float lng, lngMin;
		int ind = 0;
		
		for(int i = 0; i < ph.length; i++){
	//		System.out.println(ph[i]);
			lngMin = Float.MAX_VALUE;
			for(int j = 0; j < ps.length; j++){		
				
				lng = lengthEdge(ph[i], ps[j]);
				
				if(lng < lngMin){
					lngMin = lng;
					ind = j;
				}
			}
			ph[i].setDock(ps[ind]);
		}
	}
	
	private void checkPub(){
		int ck[] = new int[9];
		for(int i = 0; i < this.ph.length; i++){
						
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
		for(int j = 0; j < ck.length; j++){
			System.out.println("Sklad_"+j+": "+ck[j]);
			count += ck[j];
		}
		System.out.println("Celkem: "+count);
	}
	
	public static void main(String [] arg){
		Data d = new Data();
//		d.pub2dock();
//		d.checkPub();
		
	}
}
