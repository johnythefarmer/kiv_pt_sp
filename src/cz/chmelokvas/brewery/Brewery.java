package cz.chmelokvas.brewery;

import java.util.List;


public class Brewery extends Stock {

	/** Atribut mnozstvi vyrobeneho piva za den */
	private final static int PRODUCTION_PER_DAY = 7000;
	
	/** Atribut mnostvi cisteren */
	protected List<Car> garage_for_tanks;
	
	/** Atribut nazev pivovaru */
	private String name;
	
	public Brewery(String name, int idCont, float x, float y){
		this.name = name;
		this.x = x;
		this.y = y;
		this.idCont = idCont;
		this.idProv = 0;
		this.provider = this;
	}
	
	public void checkTimeEvents(){
		
		productionBeer();
		
		/* TODO 
		 * kamiony budou jezdit do prekladist klesne-li pocet sudu na velikost kamionu
		 * kamion -> full - 2*100 hl
		 * cisterna -> full - hl
		 */
		
	}
	
	private void productionBeer(){
		if(c.mainTime.getHour() == 23) full += 307;
		else full += 291;
	}
	
	public String getName(){
		return name;
	}
	
	public void unload(int n){
		this.full -= n;
	}
	
	/*public void calculateShortestPathsDijkstra(){
		for(int i = 0; i < d.length; i++){
			d[i] = Float.MAX_VALUE;
		}
		
		KeyPriorityQueue<Integer> queue = new KeyPriorityQueue<Integer>();
		
		d[0] = 0;
		queue.add(d[0],idCont);
		
		while(!queue.isEmpty()){
			int u = queue.poll();
			for(Route v : c.nodes[u].routes){
				int vId = v.getValue();
				if(c.nodes[vId].provider != null && !c.nodes[vId].provider.equals(this) && !c.nodes[vId].equals(this)){
					continue;
				}
				
				float dist = d[c.nodes[u].idProv] + v.getDistance();
				
				if(dist < d[c.nodes[vId].idProv]){
					d[c.nodes[vId].idProv] = dist;
					p[c.nodes[vId].idProv] = u;
					queue.add(d[c.nodes[vId].idProv], vId);
				}
			}
		}
	}*/
	
/*	private void produceBeer()
	{
		
	}*/
}
