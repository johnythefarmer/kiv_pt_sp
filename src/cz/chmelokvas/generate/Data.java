package cz.chmelokvas.generate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.TreeSet;

import javax.swing.JFrame;

public class Data extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private final static int countPub = 4000;
	Node[] ps;
	Node[] ph;
	
	
	public Data(){
//		long t = System.currentTimeMillis();
		this.ps = generateS();
		this.ph = generateH();
		this.pub2dock();
		this.neirPub();
//		System.out.println(System.currentTimeMillis()-t);

//		this.setSize(500, 500);
//		this.setVisible(true);
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
				
				lng = lengthEdge(tmp, p[j]);
				
				if(lng >= 2.0 || ps[j%9].getX() != x && ps[j%9].getY() != getY()){
					p[i] = new Node(x, y);
					break;
				}
			}
		}
		return p;
	}
	
	private void neirPub(){
		 int neighbours = 15;
		 TreeSet<Node> tree;
		 Node[] p;
		 float leng = 0;
		 int k;
//		 int countPub = 0;
		 
		 for(int i = 0; i < ph.length; i++){
			 tree = new TreeSet<Node>(new Compar());
			 for(int j = 0; j < ph.length; j++){
				 if(i == j) continue;
				 
				 if(ph[i].getX()+50 > ph[j].getX() && ph[i].getX()-50 < ph[j].getX() &&
					ph[i].getY()+50 > ph[j].getY() && ph[i].getY()-50 < ph[j].getY()){
					 
				 	leng = lengthEdge(ph[i], ph[j]);
					tree.add(new Node(ph[j], leng));	
				 }
			 }
			 if(tree.size() >= neighbours){
				 p = new Node[neighbours];
				 k = 0;
				 for(Node x : tree){
					 if(k >= neighbours) break;
					 p[k] = x.getDock();
					 k++;
				 }
				 ph[i].setNeighbours(p);
				/* System.out.println("--------------------------------------------");
				 System.out.println("Hospoda: "+i +"     "+ph[i]);
				 for(int l = 0; l < ph[i].getNeighbours().length; l++){
				 	System.out.println("Sousedi: "+l+"   "+ph[i].getNeighbours()[l].getX()+"   "+ph[i].getNeighbours()[l].getY());
				 }
				 countPub++;*/
			 }
		 }
//		 System.out.println("----------   "+countPub);
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
	
	private void lengXY(Node p){
//		int xy = 0;
		float lengXmin = Float.MAX_VALUE, lengXmax = Float.MIN_VALUE, lengYmin = Float.MAX_VALUE, lengYmax = Float.MIN_VALUE;
		for(int i = 0; i < this.ph.length; i++){
			if(this.ph[i].getDock().equals(p)){
				if(ph[i].getX() < lengXmin){
					lengXmin = ph[i].getX();
				}
				if(ph[i].getX() > lengXmax){
					lengXmax = ph[i].getX();
				}
				if(ph[i].getY() < lengYmin){
					lengYmin = ph[i].getY();
				}
				if(ph[i].getY() > lengYmax){
					lengYmax = ph[i].getY();
				}
			}	
		}
//		System.out.println(Math.abs(lengXmax - lengXmin) + " x " + Math.abs(lengYmax - lengYmin));
		
//		return xy;
	}
	
	private void checkPub(){
		int ck[] = new int[9];
		for(int i = 0; i < this.ph.length; i++){
						
			if(this.ph[i].getDock().equals(this.ps[0])){
				ck[0]++;
			}
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
		for(int i = 0; i < d.ps.length; i++){
			d.lengXY(d.ps[i]);
		}
//		d.pub2dock();
//		d.checkPub();
		
	}
}
