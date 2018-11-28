package controler;

import model.ModelFacade;
import view.ViewFacade;

public class Controler {
	
	private ModelFacade model;
	private ViewFacade view;
	
	public Controler(ModelFacade model, ViewFacade view)
	{
		this.setModel(model);
//		this.model.setControler(this);
		this.setView(view);
		this.view.setControler(this);
		System.out.println("Launching contoler");
	}

	
	//Public methods//
	public void updateConsigne(Double consigne)
	{
		//TODO
		System.out.println("Updating consigne");
	}
	
	public void setTemp(Double temp)
	{
		this.view.setTemp(temp);
	}
	
	public void setHumidity(int percent)
	{
		this.view.setHumidity(percent);
	}
	
	public void setPrecision(int precision)
	{
		this.getView().setPrecision(precision);
	}
	
	public void setDoorStatus(boolean isOpen)
	{
		this.getView().isDoorOpen(isOpen);
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
