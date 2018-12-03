package model;

import java.io.IOException;

import controler.Controler;

public class ModelFacade {
	
	private Controler controler;
	private Serial serial;
	
	public ModelFacade()
	{
		this.serial = new Serial(this);

		try {
			/*
			 * Trouve le port sur lequel l'Arduino est connecté
			 */
			serial.definePort();
			serial.initialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void stop()
	{
		try {
			this.serial.exitListening();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setConsigne(int consigne)
	{
		this.serial.setConsigne(consigne);
	}
	
	public Double getDewPoint()
	{
		return this.serial.getTemperature_rosee();
	}

	//Getters and setters//
	/**
	 * @return the controler
	 */
	public Controler getControler() {
		return controler;
	}

	/**
	 * @param controler the controler to set
	 */
	public void setControler(Controler controler) {
		this.controler = controler;
	}

	/**
	 * @return the serial
	 */
	public Serial getSerial() {
		return serial;
	}

	/**
	 * @param serial the serial to set
	 */
	public void setSerial(Serial serial) {
		this.serial = serial;
	}

}
