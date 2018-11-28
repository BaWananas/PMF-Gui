package view;

import java.awt.AWTException;

import controler.Controler;
import javafx.stage.Stage;

public class ViewFacade {
	
	private Controler controler;
	private FxManager fxmanager;
	private NotificationManager notifManager;
	
	public ViewFacade(Stage primaryStage)
	{
		System.out.println("Launching view");
		this.setFxmanager(new FxManager(this, primaryStage));
		try {
			this.notifManager = new NotificationManager("images/temp_hight.png");
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Update the consigne value
	 */
	public void updateConsigne(Double consigne)
	{
		this.controler.updateConsigne(consigne);
	}
	
	/*
	 * Set the actual temperature
	 */
	public void setTemp(Double temp)
	{
		this.getFxmanager().getPrimaryStageControler().getTemp_value().setText("" + temp);
	}
	
	/*
	 * Set the actual humidity level
	 */
	public void setHumidity(int percent)
	{
		this.fxmanager.getPrimaryStageControler().getInfo_humidity().setText("" + percent + "%");
	}
	
	/*
	 * Set the status of the door
	 */
	public void isDoorOpen(boolean bool)
	{
		this.fxmanager.getPrimaryStageControler().getInfo_door_status().setText("" + bool);
	}
	
	/*
	 * Set the temperature precision
	 */
	public void setPrecision(int precision)
	{
		this.fxmanager.getPrimaryStageControler().getInfo_precision().setText("" + precision + "%");
	}

	
	//Getters and setters//	
	/**
	 * @return the fxmanager
	 */
	public FxManager getFxmanager() {
		return fxmanager;
	}

	/**
	 * @param fxmanager the fxmanager to set
	 */
	public void setFxmanager(FxManager fxmanager) {
		this.fxmanager = fxmanager;
	}

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
	 * @return the notifManager
	 */
	public NotificationManager getNotifManager() {
		return notifManager;
	}

	/**
	 * @param notifManager the notifManager to set
	 */
	public void setNotifManager(NotificationManager notifManager) {
		this.notifManager = notifManager;
	}

}
