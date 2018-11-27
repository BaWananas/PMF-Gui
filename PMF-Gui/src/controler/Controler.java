package controler;

import model.ModelFacade;
import view.ViewFacade;

public class Controler {
	
	private ModelFacade model;
	private ViewFacade view;
	
	public Controler(ModelFacade model, ViewFacade view)
	{
		this.setModel(model);
		this.setView(view);
		System.out.println("Launching contoler");
	}

	
	//Getters and setters//
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
