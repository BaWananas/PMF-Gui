package view;

import java.awt.AWTException;

import controler.Controler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
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
			this.notifManager = new NotificationManager("fxml/images/temp_hight.png");
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Update the consigne value
	 */
	public void updateConsigne(Double consigne)
	{
		if (consigne <= this.getControler().getTr())
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(this.fxmanager.getPrimaryStage());
			alert.initModality(Modality.WINDOW_MODAL);
			alert.setTitle("Error : Cannot set the specified temperature");
			alert.setResizable(false);
			alert.setHeaderText("La temp�rature demand�e d�passe le seuil maximal de condensation autoris�, merci de choisir une valeur strictement sup�rieur au point de ros�e suivant : " + this.getControler().getTr());
			alert.setContentText("La condensation peut engendrer une d�t�rioration des composants de votre frigo. Merci de votre compr�hension.");
			alert.show();
		}
		else
		{
			this.controler.updateConsigne(consigne);
		}
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
