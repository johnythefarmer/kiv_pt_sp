package cz.chmelokvas.util;

import java.util.Scanner;

public class Main {
	public static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		ParseData pd = new ParseData("export.txt");
		Controller.c = pd.getC();
		
		int minLog = 0;
		if(args.length == 1){
			
			minLog = Integer.parseInt(args[0]);
		}else{
			System.out.println("Zvolte prioritu: ");
			System.out.println("1 = CHYBY");
			System.out.println("2 = VYRIZENI OBJEDNAVKY");
			System.out.println("3 = PODANI OBJEDNAVKY");
			System.out.println("4 = POHYB AUT");
			System.out.println("5 = ZASOBOVANI DO PREKLADIST");
			System.out.println("6 = PRODUKCE, VYTVARENI AUT");
			
			minLog = sc.nextInt();
			
		}
		
		
//		Kresli kd = new Kresli(Controller.c);\
		Controller.c.minLogPriority = minLog;
		Controller.c.simulate();
		sc.close();
	}

}
