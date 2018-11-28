package view;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import view.fxml.MainMenuControler;

public class FxManager {
	
	private ViewFacade view;
	private Stage primaryStage;
	
	//Container of the primaryStage
	private AnchorPane primaryContainer;
	//Controler of the primaryStage
	private MainMenuControler primaryStageControler;
	
	private double xPrimaryStageOffset;
	private double yPrimaryStageOffset;
	
	public FxManager(ViewFacade view, Stage primaryStage)
	{
		this.view = view;
		this.setPrimaryStage(primaryStage);
		try {
			this.initDefaultStage();
		} catch (IOException e) {
			System.out.println("Error : An error has occured during initialising stage : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void initDefaultStage() throws IOException
	{
		//Init the stage
		this.primaryStage.setTitle("Pimp My Frigogidaire");
		this.primaryStage.setResizable(false);
		this.primaryStage.initStyle(StageStyle.TRANSPARENT);
		this.primaryStage.setAlwaysOnTop(true);
		this.primaryStage.setOpacity(1);
		primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("images/background.png")));
		
		//Init the container
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("fxml/mainMenu.fxml"));
		this.primaryContainer = (AnchorPane)loader.load();
		this.primaryContainer.setBackground(Background.EMPTY);
		
		//Init the scene
		Scene scene = new Scene(this.primaryContainer);
		scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
		this.primaryStage.setScene(scene);
		
		//Load the controler
		MainMenuControler controler = loader.getController();
		this.primaryStageControler = controler;
		controler.setMainApp(this);
		
		//Load the stage
		this.setMovableFrame(primaryStage);
		this.primaryStage.show();
	}
	
	private void setMovableFrame(Stage stage)
	{
		stage.getScene().setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xPrimaryStageOffset = event.getSceneX();
				yPrimaryStageOffset = event.getSceneY();
			}
		});
		stage.getScene().setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				stage.setX(event.getScreenX() - xPrimaryStageOffset);
				stage.setY(event.getScreenY() - yPrimaryStageOffset);
			}
		});
	}
	
//	private synchronized void incrementOpacity(double opacity)
//	{
//		this.primaryStage.setOpacity(this.primaryStage.getOpacity()+opacity);
//	}

	
	//Getters and setters//	
	/**
	 * @return the view
	 */
	public ViewFacade getView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(ViewFacade view) {
		this.view = view;
	}

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

	/**
	 * @return the primaryContainer
	 */
	public AnchorPane getPrimaryContainer() {
		return primaryContainer;
	}

	/**
	 * @param primaryContainer the primaryContainer to set
	 */
	public void setPrimaryContainer(AnchorPane primaryContainer) {
		this.primaryContainer = primaryContainer;
	}

	/**
	 * @return the primaryStageControler
	 */
	public MainMenuControler getPrimaryStageControler() {
		return primaryStageControler;
	}

	/**
	 * @param primaryStageControler the primaryStageControler to set
	 */
	public void setPrimaryStageControler(MainMenuControler primaryStageControler) {
		this.primaryStageControler = primaryStageControler;
	}

}
