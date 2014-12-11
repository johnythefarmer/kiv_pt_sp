package cz.chmelokvas.util;

/**
 * Rozhrani umoznujici sledovat v gui nejaky objekt
 * @author Lukas Cerny A13B0286P
 *
 */
public interface Loggable {
	
	/**
	 * Slouzi pro vypis dat v prubeznem case do textArey v GUI
	 * @return informace o instanci
	 */
	public String tempInfo();
	
	/**
	 * Slouzi pro vypis dat na konci
	 * @return informace o instanci za celou dobu
	 */
	public String finalInfo();
}
