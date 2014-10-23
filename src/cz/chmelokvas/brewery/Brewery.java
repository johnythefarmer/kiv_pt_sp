package cz.chmelokvas.brewery;

public class Brewery extends Stock {
//	private int productionPerDay;
	
	private String name;
	
	public Brewery(String name, int id, float x, float y){
		this.name = name;
		this.x = x;
		this.y = y;
		this.idCont = id;
		this.idProv = 0;
		this.provider = this;
	}
	
	public void checkTimeEvents(){
		//TODO co bude s casem delat pivovar
	}
	
	public void setName(String name){
		this.name = name;
	}
	
/*	private void produceBeer()
	{
		
	}*/
}
