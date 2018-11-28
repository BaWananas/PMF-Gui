package view;

import controler.Controler;
import javafx.stage.Stage;

public class ViewFacade {
	
	private Controler controler;
	private FxManager fxmanager;
	
	public ViewFacade(Stage primaryStage)
	{
		System.out.println("Launching view");
		this.setFxmanager(new FxManager(this, primaryStage));
	}
	
	public void updateConsigne(Double consigne)
	{
		this.controler.updateConsigne(consigne);
	}
	
	public void setTemp(Double temp)
	{
		this.getFxmanager().getPrimaryStageControler().getTemp_value().setText("" + temp);
	}
	
	public void setHumidity(int percent)
	{
		this.fxmanager.getPrimaryStageControler().getInfo_humidity().setText("" + percent + "%");
	}
	
	public void isDoorOpen(boolean bool)
	{
		this.fxmanager.getPrimaryStageControler().getInfo_door_status().setText("" + bool);
	}
	
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

}
