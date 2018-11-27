import controler.Controler;
import javafx.application.Application;
import javafx.stage.Stage;
import model.ModelFacade;
import view.ViewFacade;

public class Launcher extends Application{
	
	private Controler controler;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.controler = new Controler(new ModelFacade(), new ViewFacade(primaryStage));
	}

	
	
}
