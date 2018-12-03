import controler.Controler;
import javafx.application.Application;
import javafx.stage.Stage;
import model.ModelFacade;
import view.ViewFacade;

public class Launcher extends Application{
	
	private Controler controler;
	private ModelFacade model;
	private ViewFacade view;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.model = new ModelFacade();
		this.view = new ViewFacade(primaryStage);
		this.setControler(new Controler(this.model, this.view));	
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
	 * @return the model
	 */
	public ModelFacade getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(ModelFacade model) {
		this.model = model;
	}

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

	
	
}
