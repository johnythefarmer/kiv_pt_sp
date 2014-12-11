package cz.chmelokvas.brewery;

/**
 * Enum stavu auta
 * @author Jan Dvorak A13B0293P
 *
 */
public enum State {
	REFUELING("cerpa v", "docerpal v"), TRAVELLING("cestuje do", "dorazil do"), LOADING("naklada v", "nalozil v"), UNLOADING("vyklada v","vylozil v"),LOADING_EMPTY_BARRELS("naklada prazdne sudy v", "nalozil prazdne sudy v"), UNLOADING_EMPTY_BARRELS("vyklada prazdne sudy v","vylozil prazdne sudy v"), WAITING("ceka v", "prestal cekat v");
	
	private final String strStart,strFin;
	
	private State(String strStart, String strFin){
		this.strStart = strStart;
		this.strFin = strFin;
	}

	/**
	 * Vrati retezec reprezentujici stav, kdyz ho zaciname
	 * @return nazev stavu
	 */
	public String getStrStart() {
		return strStart;
	}
	
	/**
	 * Vrati retezec reprezentujici stav, kdyz ho koncime
	 * @return	nazev stavu
	 */
	public String getStrFin() {
		return strFin;
	}
	
	
}
