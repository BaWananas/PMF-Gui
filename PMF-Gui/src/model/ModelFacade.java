package model;

public class ModelFacade {
	
	/*
	 * Classe principale du modèle
	 */
	
	Serial serial = new Serial();
	Split_Data split_data = new Split_Data();
	
	public String type_systeme = "Fermé";

	
	/*
	 * Données modifiable depuis l'interface
	 */
	public double consigne = 15;
	public double precision = 1; //Valeur en %
	public double Fahrenheit = 273.15;

	public double temperature_mesuree_fahrenheit;
	public double hysteresis;
	public double temperature_haute;
	public double temperature_bas;
	public double incertitude;
	
	
	public ModelFacade() {

	}
	
	public void determiner_valeur_environnement() {
		hysteresis = (consigne*precision)/100;
		temperature_mesuree_fahrenheit = Fahrenheit + split_data.getTemperature_double();
		temperature_haute = hysteresis + consigne;
		temperature_bas = hysteresis-consigne;
		incertitude = temperature_haute-temperature_bas;
	}


	public double getConsigne() {
		return consigne;
	}

	public void setConsigne(double consigne) {
		this.consigne = consigne;
	}

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getHysteresis() {
		return hysteresis;
	}

	public void setHysteresis(double hysteresis) {
		this.hysteresis = hysteresis;
	}

	public double getTemperature_haute() {
		return temperature_haute;
	}

	public void setTemperature_haute(double temperature_haute) {
		this.temperature_haute = temperature_haute;
	}

	public double getTemperature_bas() {
		return temperature_bas;
	}

	public void setTemperature_bas(double temperature_bas) {
		this.temperature_bas = temperature_bas;
	}

	public double getIncertitude() {
		return incertitude;
	}

	public void setIncertitude(double incertitude) {
		this.incertitude = incertitude;
	}

	public Serial getSerial() {
		return serial;
	}

	public void setSerial(Serial serial) {
		this.serial = serial;
	}

	public Split_Data getSplit_data() {
		return split_data;
	}

	public void setSplit_data(Split_Data split_data) {
		this.split_data = split_data;
	}

	public String getType_systeme() {
		return type_systeme;
	}

	public void setType_systeme(String type_systeme) {
		this.type_systeme = type_systeme;
	}

	public double getFahrenheit() {
		return Fahrenheit;
	}

	public void setFahrenheit(double fahrenheit) {
		Fahrenheit = fahrenheit;
	}

	public double getTemperature_mesuree_fahrenheit() {
		return temperature_mesuree_fahrenheit;
	}

	public void setTemperature_mesuree_fahrenheit(double temperature_mesuree_fahrenheit) {
		this.temperature_mesuree_fahrenheit = temperature_mesuree_fahrenheit;
	}

	
}
