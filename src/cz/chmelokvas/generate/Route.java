package cz.chmelokvas.generate;

public class Route {
	
	/** Atribut uzlu */
	private Node value;
	
	/** Atribut vzdalenosti */
	private float d;
	
	/**
	 * Konstruktor Routeru
	 * @param d	vzdalenost
	 * @param value	uzel
	 */
	public Route(float d, Node value){
		this.d = d;
		this.value = value;
	}
	
	/**
	 * Vrati aktualni uzel
	 * @return value	uzel
	 */
	public Node getValue() {
		return value;
	}
	
	/**
	 * Nastavi uzel
	 * @param value	uzel
	 */
	public void setValue(Node value) {
		this.value = value;
	}

	/**
	 * Vrati vzdalenosti
	 * @return d	vzdalenost
	 */
	public float getD() {
		return d;
	}
	/**
	 * Nastavi vzdalenost
	 * @param d	vzdalenost
	 */
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
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		Route other = (Route) obj;
		if (value == null) {
			if (other.value != null){
				return false;
			}
		} else if (!value.equals(other.value)){
			return false;
		}
		return true;
	}
	
	
	
}
