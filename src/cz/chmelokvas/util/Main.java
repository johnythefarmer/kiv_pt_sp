package cz.chmelokvas.util;

/**
 * Hlavni trida aplikace
 * @author Lukas Cerny A13B0286P
 *
 */
public class Main {

	/**
	 * Hlavni metoda aplikace
	 * @param args argumenty z prikazove radky
	 */
	public static void main(String[] args) {
		Gui gui = Gui.getInstance();
		gui.setName("----------");		
	}

	/**
	 * Nacte data ze souboru a zinicializuje ridici jednotku
	 * @param nameFile soubor ze ktereho cteme
	 */
	public static void initController(String nameFile){
		ParseData pd = new ParseData(nameFile);
		Controller.c = pd.getC();
		Controller.c.gui.setName(Controller.c.brewery.getName());
		Runnable r = new Runnable() {
			public void run() {
				Controller.c.simulate();
			}
		};
		
		Thread t = new Thread(r);
		t.start();
		
		
	}
}
