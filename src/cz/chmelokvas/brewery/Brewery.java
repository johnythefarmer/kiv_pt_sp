package cz.chmelokvas.brewery;


public class Brewery extends Stock {
//	private int productionPerDay;
	
	private String name;
	
	public Brewery(String name, int idCont, float x, float y){
		this.name = name;
		this.x = x;
		this.y = y;
		this.idCont = idCont;
		this.idProv = 0;
		this.provider = null;
	}
	
	public void checkTimeEvents(){
		//TODO co bude s casem delat pivovar
	}
	
	public void setName(String name){
		this.name = name;
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
