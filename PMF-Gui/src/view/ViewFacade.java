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
	public boolean updateConsigne(int consigne)
	{
		if (this.getFxmanager().getPrimaryStageControler().getTemp_type_image().getAccessibleText().equalsIgnoreCase("celsius"))
		{
			if (consigne < 0 || consigne > 60)
			{
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.fxmanager.getPrimaryStage());
				alert.initModality(Modality.WINDOW_MODAL);
				alert.setTitle("Error : Cannot set the specified temperature");
				alert.setResizable(false);
				alert.setHeaderText("La température demandée n'entre pas dans l'intervale des valeurs possibles des températures ambiantes pour la formule du point de rosée.");
				alert.setContentText("La formule de Magnu-tetens demande des valeurs de température ambiante comprise dans l'interval : [0;60]");
				alert.show();
				return false;
			}
			else if (consigne <= this.getControler().getTr())
			{
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.fxmanager.getPrimaryStage());
				alert.initModality(Modality.WINDOW_MODAL);
				alert.setTitle("Error : Cannot set the specified temperature");
				alert.setResizable(false);
				alert.setHeaderText("La température demandée dépasse le seuil maximal de condensation autorisé, merci de choisir une valeur strictement supérieur au point de rosée suivant : " + this.getControler().getTr());
				alert.setContentText("La condensation peut engendrer une détérioration des composants de votre frigo. Merci de votre compréhension.");
				alert.show();
				return false;
			}
			else
			{
				this.controler.updateConsigne(consigne);
				return true;
			}
		}
		else
		{
			if (consigne < 32 || consigne > 140)
			{
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.fxmanager.getPrimaryStage());
				alert.initModality(Modality.WINDOW_MODAL);
				alert.setTitle("Error : Cannot set the specified temperature");
				alert.setResizable(false);
				alert.setHeaderText("La température demandée n'entre pas dans l'intervale des valeurs possibles des températures ambiantes pour la formule du poitn de rosée.");
				alert.setContentText("La formule de Magnu-tetens demande des valeurs de température ambiante comprise dans l'interval : [0;60]");
				alert.show();
				return false;
			}
			else if (consigne <= this.getFxmanager().getPrimaryStageControler().cToF(this.getControler().getTr()))
			{
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(this.fxmanager.getPrimaryStage());
				alert.initModality(Modality.WINDOW_MODAL);
				alert.setTitle("Error : Cannot set the specified temperature");
				alert.setResizable(false);
				alert.setHeaderText("La température demandée dépasse le seuil maximal de condensation autorisé, merci de choisir une valeur strictement supérieur au point de rosée suivant : " + this.getFxmanager().getPrimaryStageControler().cToF(this.getControler().getTr()));
				alert.setContentText("La condensation peut engendrer une détérioration des composants de votre frigo. Merci de votre compréhension.");
				alert.show();
				return false;
			}
			else
			{
				this.controler.updateConsigne(this.getFxmanager().getPrimaryStageControler().fToC(consigne));
				return true;
			}
		}
		
	}
	
	/*
	 * Set the actual temperature
	 */
	public void setTemp(Double temp)
	{
		if (this.getFxmanager().getPrimaryStageControler().getTemp_type_image().getAccessibleText().equalsIgnoreCase("celsius"))
		{
			this.getFxmanager().getPrimaryStageControler().getTemp_value().setText("" + temp);
		}
		else
		{
			this.getFxmanager().getPrimaryStageControler().getTemp_value().setText("" + this.getFxmanager().getPrimaryStageControler().cToF(temp));
		}
		
		if (!this.getFxmanager().getPrimaryStageControler().getConsigne().getText().isEmpty()) 
		{
			this.getFxmanager().getPrimaryStageControler().setTempStatus();
		}
		else
		{
			this.getFxmanager().getPrimaryStageControler().getConsigne().setText("" + (this.controler.getTr()+1));;
		}
		
		this.getFxmanager().getPrimaryStageControler().updateGraph(temp);
		
	}
	
	/*
	 * Set the actual humidity level
	 */
	public void setHumidity(int percent)
	{
		this.fxmanager.getPrimaryStageControler().getInfo_humidity().setText("" + percent + "%");
	}
	
	/*
	 * Set the temperature precision
	 */
	public void setTr(Double tr)
	{
		this.fxmanager.getPrimaryStageControler().getInfo_tr().setText("" + tr + " °C");
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
