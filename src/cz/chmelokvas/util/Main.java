package cz.chmelokvas.util;

public class Main {

	public static void main(String[] args) {
		ParseData pd = new ParseData("export.txt");

		Kresli kd = new Kresli(Controller.c);
		
		Controller.c = pd.getC();
		Controller.c.simulate();
	}

}
