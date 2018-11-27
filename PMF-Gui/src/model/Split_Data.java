package model;

public class Split_Data {
	
	/*Recuperation donnees Arduino
	
	Forme : Consigne;Temperature_reelle;hygrometrie
	
	*/
	
	public Collect_Data collect_data = new Collect_Data();
	
	public String test = "8;15;25";
	
	public void Split_Method(String test) {
		String[] arrOfStr = test.split(";",3);
		
		for (String data : arrOfStr) {
			System.out.println(data);
		}
	}
}
