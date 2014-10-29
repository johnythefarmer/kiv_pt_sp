package cz.chmelokvas.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;

public class Kresli extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Controller c;
	public int[] dijk;
	
	public Kresli(Controller c){
		this.c=c;
		System.out.println(c);
		this.c.brewery.calculateShortestPathsDijkstra();
		System.out.println("test");

		this.setSize(500, 500);
		this.setVisible(true);
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.drawRect(0, 0, 500, 500);
		
		g2.fill(new Ellipse2D.Double(c.brewery.getX(), c.brewery.getY(), 5, 5));
		g2.setColor(Color.red);
		for(int i = 0; i < c.dock.size(); i++){
			g2.fill(new Ellipse2D.Double(c.dock.get(i).getX(), c.dock.get(i).getY(), 5, 5));
		}

		int i = c.dock.get(3).getIdCont();

		while(c.brewery.getP()[0][i] != 0){
			int tmp = c.brewery.getP()[0][i];
			g2.drawString(String.valueOf(c.nodes.get(tmp).getIdCont()),(int) c.nodes.get(tmp).getX(),(int) c.nodes.get(tmp).getY());
			g2.setColor(Color.blue);
			g2.fill(new Ellipse2D.Double(c.nodes.get(tmp).getX(), c.nodes.get(tmp).getX(), 3, 3));
			i = tmp;
			System.out.println(tmp);
		}
	}
}
