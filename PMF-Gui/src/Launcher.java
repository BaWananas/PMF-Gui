import javafx.application.Application;
import javafx.stage.Stage;
import model.ModelFacade;
import view.ViewFacade;

public class Launcher extends Application{
	
	private ModelFacade model;
	private ViewFacade view;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.model = new ModelFacade();
		this.view = new ViewFacade();
	}

	
	
}
