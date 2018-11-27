package view;

import javafx.stage.Stage;

public class ViewFacade {
	
	private FxManager fxmanager;
	
	public ViewFacade(Stage primaryStage)
	{
		System.out.println("Launching view");
		this.setFxmanager(new FxManager(this, primaryStage));
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

}
