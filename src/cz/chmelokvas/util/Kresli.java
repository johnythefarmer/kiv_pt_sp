package cz.chmelokvas.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Kresli extends JFrame {
	
	public Controller c;
	public int[] dijk;
	
	public Kresli(Controller c){
		this.c=c;
		this.dijk = c.brewery.dijkstra(0);
		this.c.brewery.calculateShortestPathsDijkstra();

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
//		for(int i : dijk){
//		if(i < 0) continue;
//			g2.fill(new Ellipse2D.Double(c.nodes.get(i).getX(), c.nodes.get(i).getX(), 3, 3));
//		}

		for(int i = 0; i < c.brewery.getP().length; i++){
			int id = c.brewery.getP()[i][0];
			g2.fill(new Ellipse2D.Double(c.nodes.get(id).getX(), c.nodes.get(id).getX(), 3, 3));
		}
	}
}
