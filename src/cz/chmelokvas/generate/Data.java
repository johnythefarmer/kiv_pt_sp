package cz.chmelokvas.generate;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import javax.swing.JFrame;

public class Data extends JFrame {
	
	private static final long serialVersionUID = 1L;
	Point[] ps;
	Point[] ph;
	
	public Data(){
		this.ps = generateS();
		this.ph = generateH();

		this.setSize(500, 500);
		this.setVisible(true);
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawRect(0, 0, 500, 500);
		
		for(int i = 0; i < ps.length; i++){
			g2.fill(new Ellipse2D.Double(ps[i].x, ps[i].y, 5,5));
		}
		
		for(int j = 0; j < ph.length; j++){
			g2.draw(new Ellipse2D.Double(ph[j].x, ph[j].y, 1,1));
		}
	}
	
	private Point[] generateH(){
		int hospod = 4000;
		
		Point [] p = new Point[hospod];
		Random rd = new Random();
		int x, y;
		float vzdalenost;
		
		for(int i = 0; i < p.length; i++){
			x = rd.nextInt(500);
			y = rd.nextInt(500);
			
			if(i == 0){ p[i] = new Point(x, y); continue; }
			
			for(int j = 0; j < i; j++){
				
				vzdalenost = (float) Math.sqrt((x-p[j].x)*(x-p[j].x)+(y-p[j].y)*(y-p[j].y));
				
				if(vzdalenost >= 2.0){
					p[i] = new Point(x, y);
					break;
				}
			}
		}
		return p;
	}
	
	 
	private Point[] generateS(){
		Point [] p = new Point[9];
		Random rd = new Random();
		int xTmp = 0;
		int yTmp = 0;
		
		double x, y;
		int k = 0;
		
		/* Generuj XY 67 - 100 v kazdem sektoru */
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				x = rd.nextInt(33)+xTmp+67;
				y = rd.nextInt(33)+yTmp+67;
				p[k++] = new Point((int)x, (int)y);
				xTmp += 166;
			}
			yTmp += 166;
			xTmp = 0;
			
		}
		return p;
	}
	
	public static void main(String [] arg){
		Data d = new Data();
		
	}
}
