package model;

public class Main {
	static Serial serial = new Serial();

	public static void main(String[] args) {
		
		serial.definePort();
		serial.initialize();

	}

}
