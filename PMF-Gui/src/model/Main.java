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
		 * Trouve le port sur lequel l'Arduino est connect�
		 */
		serial.definePort();
		
		/*
		 * Lance l'�coute du port s�rie
		 */
		serial.initialize();

	}

}
