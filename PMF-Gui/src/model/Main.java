package model;

/*
 * Classe principale
 */

public class Main {
	
	/*
	 * Composition
	 */
	static Serial serial = new Serial();

	public static void main(String[] args) {
		
		/*
		 * Trouve le port sur lequel l'Arduino est connecté
		 */
		serial.definePort();
		
		/*
		 * Lance l'écoute du port série
		 */
		serial.initialize();

	}

}
