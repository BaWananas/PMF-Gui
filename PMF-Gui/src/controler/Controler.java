package controler;

import model.ModelFacade;
import view.ViewFacade;

public class Controler {
	
	private ModelFacade model = null;
	private ViewFacade view = null;
	
	public Controler(ModelFacade model, ViewFacade view)
	{
		this.setView(view);
		this.view.setControler(this);
		this.setModel(model);
		this.model.setControler(this);
		this.view.getNotifManager().displayMessage("PMF just started ! ");
		System.out.println("Launching contoler");
	}

	
	//Public methods//
	/*
	 * Stop the program
	 */
	public void stop()
	{
		this.model.stop();
		this.getView().getNotifManager().displayMessage("Goodbye master !");
		this.getView().getNotifManager().stop();
		this.view.getFxmanager().getPrimaryStage().close();
	}
	
	/*
	 * Update the consigne value
	 */
	public void updateConsigne(int consigne)
	{
		if (this.model != null)
		{
			this.model.setConsigne(consigne);
			System.out.println("Updating consigne");
		}
	}
	
	/*
	 * Set the actual temperature
	 */
	public void setTemp(Double temp)
	{
		if (this.view != null)
		{
			this.view.setTemp(temp);
		}
	}
	
	/*
	 * Set the actual humidity level
	 */
	public void setHumidity(int percent)
	{
		if (this.view != null)
		{
			this.view.setHumidity(percent);
		}
	}
	
	/*
	 * Set the temperature precision
	 */
	public void setTr(Double tr)
	{
		if (this.view != null)
		{
			this.getView().setTr(tr);
		}
	}
	
	/*
	 * Get the dewpoint
	 */
	public Double getTr()
	{
		if (model != null)
		{
			return this.model.getDewPoint();
		}
		return 0d;
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
