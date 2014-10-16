package cz.chmelokvas.generate;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

import javax.swing.JFrame;

public class Data extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Point[] ps;
	
	public Data(){
		this.setSize(500, 500);
		this.setVisible(true);
		this.ps = generateS();
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawRect(0, 0, 500, 500);
		
		for(int i = 0; i < 9; i++){
		//6	System.out.println("x: "+ps[i].x +"   y: "+ps[i].y);
			g2.drawRect(ps[i].x, ps[i].y, 10, 10);
		}
	}
	
	private Point[] generateH(){
		Point [] p = new Point[4000];
		
		
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
