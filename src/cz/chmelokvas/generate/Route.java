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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Route other = (Route) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
	
}
