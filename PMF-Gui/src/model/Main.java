package model;

/*
 * Classe principale du mod�le
 * Appelle les 2 principales m�thodes de la classe "Serial"
 */

public class Main {
	
	/*
	 * Composition
	 */
	static Serial serial = new Serial();

	public static void main(String[] args) throws Exception {
		
		/*
		 * Trouve le port sur lequel l'Arduino est connect�
		 */
		serial.definePort();
		serial.ini_exec();
		
		serial.initialize();
	}

}
