package cz.chmelokvas.brewery;

public enum State {
	REFUELING("cerpa v", "docerpal v"), TRAVELLING("cestuje do", "dorazil do"), LOADING("naklada v", "nalozil v"), UNLOADING("vyklada v","vylozil v"),LOADING_EMPTY_BARRELS("naklada prazdne sudy v", "nalozil prazdne sudy v"), UNLOADING_EMPTY_BARRELS("vyklada prazdne sudy v","vylozil prazdne sudy v"), WAITING("ceka v", "prestal cekat v");
	
	private final String strStart,strFin;
	
	private State(String strStart, String strFin){
		this.strStart = strStart;
		this.strFin = strFin;
	}

	public String getStrStart() {
		return strStart;
	}

	public String getStrFin() {
		return strFin;
	}
	
	
}
