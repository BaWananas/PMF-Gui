package model;

/*
 * Classe principale du modèle
 * Appelle les 2 principales méthodes de la classe "Serial"
 */

public class Main {
	
	/*
	 * Composition
	 */
	static Serial serial = new Serial();

	public static void main(String[] args) throws Exception {
		
		/*
		 * Trouve le port sur lequel l'Arduino est connecté
		 */
		serial.definePort();
		serial.ini_exec();
		
		serial.initialize();
	}

}
