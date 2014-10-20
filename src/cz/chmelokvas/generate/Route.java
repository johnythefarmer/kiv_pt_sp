package cz.chmelokvas.generate;

public class Route {
	private Node value;
	private float d;
	
	public Route(float d, Node value){
		this.d = d;
		this.value = value;
	}

	public Node getValue() {
		return value;
	}

	public void setValue(Node value) {
		this.value = value;
	}

	public float getD() {
		return d;
	}

	public void setD(float d) {
		this.d = d;
	}
	
	
}
