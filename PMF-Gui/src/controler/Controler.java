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
		this.view.getNotifManager().displayMessage("PMF just started ! ");
		System.out.println("Launching contoler");
	}

	
	//Public methods//
	/*
	 * Stop the program
	 */
	public void stop()
	{
		this.getView().getNotifManager().displayMessage("Goodbye master !");
		this.getView().getNotifManager().stop();
		this.view.getFxmanager().getPrimaryStage().close();
	}
	
	/*
	 * Update the consigne value
	 */
	public void updateConsigne(Double consigne)
	{
		//TODO
		System.out.println("Updating consigne");
	}
	
	/*
	 * Set the actual temperature
	 */
	public void setTemp(Double temp)
	{
		this.view.setTemp(temp);
	}
	
	/*
	 * Set the actual humidity level
	 */
	public void setHumidity(int percent)
	{
		this.view.setHumidity(percent);
	}
	
	/*
	 * Set the temperature precision
	 */
	public void setPrecision(int precision)
	{
		this.getView().setPrecision(precision);
	}
	
	/*
	 * Set the status of the fridge door
	 */
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
