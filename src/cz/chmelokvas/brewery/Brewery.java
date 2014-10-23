package cz.chmelokvas.brewery;

public class Brewery extends Stock {
//	private int productionPerDay;
	
	private String name;
	private TransportNode transportNode;
	
	public Brewery(String name, TransportNode transportNode){
		this.name = name;
		this.transportNode = transportNode;
	}
	
	public void checkTimeEvents(){
		//TODO co bude s casem delat pivovar
	}
	
	public void setName(String name){
		this.name = name;
	}
	public void setTransportNode(TransportNode tn){
		this.transportNode = tn;
	}
	
/*	private void produceBeer()
	{
		
	}*/
}
