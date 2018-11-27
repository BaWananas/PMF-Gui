package model;

public class Split_Data {
	
	public Split_Data() {
	}
	
	/*
	 * Recuperation donnees Arduino depuis la classe "Collect_Data_Serial"
	 * Forme : Humidite;temperature;temperature de rosee
	 * Type = float
	 */
	
	Collect_Data_Serial collect_data_serial = new Collect_Data_Serial();
	String data = collect_data_serial.getData();
	
	String[] parts = data.split(";");
	String humidite = parts[0];
	String temperature = parts[1];
	String rosee = parts[2];
	
	/*
	 * Conversion String vers float pour traitement des donnees
	 */
	
	float humidite_float = Float.parseFloat(humidite);
	float temperature_float = Float.parseFloat(temperature);
	float rosee_float = Float.parseFloat(rosee);
	
	/*
	 * Accesseurs
	 * 
	 */
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String[] getParts() {
		return parts;
	}
	public void setParts(String[] parts) {
		this.parts = parts;
	}
	public String getHumidite() {
		return humidite;
	}
	public void setHumidite(String humidite) {
		this.humidite = humidite;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getRosee() {
		return rosee;
	}
	public void setRosee(String rosee) {
		this.rosee = rosee;
	}
	public float getHumidite_float() {
		return humidite_float;
	}
	public void setHumidite_float(float humidite_float) {
		this.humidite_float = humidite_float;
	}
	public float getTemperature_float() {
		return temperature_float;
	}
	public void setTemperature_float(float temperature_float) {
		this.temperature_float = temperature_float;
	}
	public float getRosee_float() {
		return rosee_float;
	}
	public void setRosee_float(float rosee_float) {
		this.rosee_float = rosee_float;
	}
	
	
}
