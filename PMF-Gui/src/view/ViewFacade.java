package view;

import javafx.stage.Stage;

public class ViewFacade {
	
	private Stage primaryStage;
	
	public ViewFacade(Stage primaryStage)
	{
		this.setPrimaryStage(primaryStage);	
	}

	
	//Getters and setters//
	/**
	 * @return the primaryStage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * @param primaryStage the primaryStage to set
	 */
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	

}
