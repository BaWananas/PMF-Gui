package model;

/*
 * Classe qui convetit les données recues par l'Arduino en double
 */
public class Split_Data {
	
	public Split_Data() {
	}
	
	/*
	 * Recuperation donnees Arduino depuis la classe "Collect_Data_Serial"
	 * Forme : Humidite;temperature;temperature de rosee
	 */
	
	Serial serial = new Serial();
	
	

	public double humidite_double;
	public double temperature_double;
	public double rosee_double;
	
	/*
	 * Conversion String vers double pour traitement des donnees
	 */
	public void determiner_valeur_double() {
		humidite_double = Double.parseDouble(serial.getHumidite());
		temperature_double = Double.parseDouble(serial.getTemperature_mesuree());
		rosee_double = Double.parseDouble(serial.getTemperature_rosee());
	}
	
	public Serial getSerial() {
		return serial;
	}
	public void setSerial(Serial serial) {
		this.serial = serial;
	}
	public double getHumidite_double() {
		return humidite_double;
	}
	public void setHumidite_double(double humidite_double) {
		this.humidite_double = humidite_double;
	}
	public double getTemperature_double() {
		return temperature_double;
	}
	public void setTemperature_double(double temperature_double) {
		this.temperature_double = temperature_double;
	}
	public double getRosee_double() {
		return rosee_double;
	}
	public void setRosee_double(double rosee_double) {
		this.rosee_double = rosee_double;
	}
	
	



	
}
