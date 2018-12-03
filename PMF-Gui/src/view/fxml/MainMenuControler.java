package view.fxml;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import view.AnimationsManager;
import view.FxManager;

public class MainMenuControler {
	
	private FxManager manager;
	
	/*
	 * Button for the graphs
	 */
	@FXML
	private ImageView graph;
	
	/*
	 * Display for the Temperature
	 */
	@FXML
	private ImageView temp_type_image;
	@FXML
	private Text temp_value;
	@FXML
	private ImageView temp_image;
	
	/*
	 * Display for the consigne
	 */
	@FXML
	private TextField consigne;
	
	/*
	 * Add or remove degree to the consigne
	 */
	@FXML
	private ImageView consigne_add;
	@FXML
	private ImageView consigne_remove;
	
	/*
	 * Info pane
	 */
	@FXML
	private Pane info;
	@FXML
	private Text info_humidity;
	@FXML
	private Text info_precision;
	@FXML
	private Text info_door_status;
	
	
	public void setMainApp(FxManager manager)
	{
		this.manager = manager;
	}
	
	/*
	 * On close request
	 * Exit the program
	 */
	public void exit()
	{
		AnimationsManager.minimizeWindow(this.manager.getPrimaryContainer(), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				getManager().getView().getControler().stop();
			}
		});
	}
	
	/*
	 * On minilize request
	 * Minimize the window
	 */
	public void minimize()
	{
		manager.getPrimaryStage().setIconified(true);
	}
	
	/*
	 * On button is focused by user
	 */
	public void onButtonFocused(Event e)
	{
		ImageView button = (ImageView)e.getSource();
		button.setFitWidth(button.getFitWidth() + 3);
		button.setFitHeight(button.getFitHeight() + 3);
		button.setX(button.getX() - 1.5);
		button.setY(button.getY() - 1.5);
		button.setOpacity(0.7);
	}
	
	/*
	 * On button lose the focus
	 */
	public void onButtonNotFocused(Event e)
	{
		ImageView button = (ImageView)e.getSource();
		button.setFitWidth(button.getFitWidth() - 3);
		button.setFitHeight(button.getFitHeight() - 3);
		button.setX(button.getX() + 1.5);
		button.setY(button.getY() + 1.5);
		button.setOpacity(1);
	}
	
	/*
	 * On the graph button are focused
	 */
	public void onGraphFocused()
	{
		this.graph.setImage(new Image(this.getClass().getResourceAsStream("images/graph2.png")));
	}
	/*
	 * On the graph button aren't focused
	 */
	public void onGraphNotFocused()
	{
		this.graph.setImage(new Image(this.getClass().getResourceAsStream("images/graph1.png")));
	}
	
	public void changeTempType()
	{
		if (this.temp_type_image.getAccessibleText() != null)
		{
			if (this.temp_type_image.getAccessibleText().contains("celsius"))
			{
				this.temp_type_image.setAccessibleText("fahrenheit");
				this.temp_type_image.setImage(new Image(this.getClass().getResourceAsStream("images/fahrenheit.png")));
				this.temp_value.setText("" + this.cToF(Double.parseDouble(this.temp_value.getText())));
				
				if (this.consigne.getText() != null)
				{
					try {
						this.consigne.setText("" + this.cToF(Double.parseDouble(this.consigne.getText())));
					} catch (Exception e) {
						System.out.println("Consigne isn't set");
					}
				}
			}
			else if (this.temp_type_image.getAccessibleText().contains("fahrenheit"))
			{
				this.temp_type_image.setAccessibleText("celsius");
				this.temp_type_image.setImage(new Image(this.getClass().getResourceAsStream("images/celsius.png")));
				this.temp_value.setText("" + this.fToC(Double.parseDouble(this.temp_value.getText())));
				
				if (this.consigne.getText() != null)
				{
					try {
						this.consigne.setText("" + this.fToC(Double.parseDouble(this.consigne.getText())));
					} catch (Exception e) {
						System.out.println("Consigne isn't set");
					}
				}
			}
		}
	}
	
	/*
	 * Increment consigne value
	 */
	public void incrementConsigne()
	{
		if (this.consigne.getText() != null)
		{
			try {
				this.consigne.setText("" + (Double.parseDouble(this.consigne.getText()) + 1d));
			} catch (Exception e) {
				this.consigne.setText(this.temp_value.getText());
			}
		}
	}	
	
	/*
	 * Decrement consigne value
	 */
	public void decrementConsigne()
	{
		if (this.consigne.getText() != null)
		{
			try {
				this.consigne.setText("" + (Double.parseDouble(this.consigne.getText()) - 1d));
			} catch (Exception e) {
				this.consigne.setText(this.temp_value.getText());
			}
		}
	}
	
	/*
	 * Send the consigne to the Controler -> Model -> Arduino
	 */
	public void updateConsigne()
	{
		try {
			if (this.manager.getView().updateConsigne(this.arrondir_valeur(Double.parseDouble(this.consigne.getText()))))
			{
				if (Double.parseDouble(this.consigne.getText()) < Double.parseDouble(temp_value.getText()))
				{
					this.temp_image.setImage(new Image(this.getClass().getResourceAsStream("images/temp_low.png")));
				}
				else if (Double.parseDouble(this.consigne.getText()) > Double.parseDouble(temp_value.getText()))
				{
					this.temp_image.setImage(new Image(this.getClass().getResourceAsStream("images/temp_hight.png")));
				}
				else if (Double.parseDouble(this.consigne.getText()) == Double.parseDouble(temp_value.getText()))
				{
					this.temp_image.setImage(new Image(this.getClass().getResourceAsStream("images/temp.png")));
				}
				System.out.println("Setting consigne");
			}
		} catch (Exception e) {
			System.out.println("Consigne isn't set");
		}
	}
	
	/*
	 * Show infos
	 */
	public void showInfos()
	{
		this.info.setDisable(false);
		AnimationsManager.extendWindow(this.info);
	}
	
	/*
	 * Hide infos
	 */
	public void hideInfos()
	{
		this.info.setDisable(true);
		AnimationsManager.minimizeWindow(this.info);
	}
	
	//Private methods//
	public Double cToF(Double celsius)
	{
		Double d = (celsius * (Double)(9d/5d)) + 32d;
		d = d * 10d;
		d = (double)(Math.round(d))/10d;
		return d;
	}
	
	public Double fToC(Double fahrenheit)
	{
		Double d = (fahrenheit - 32d) * (Double)(5d/9d);
		d = d * 10d;
		d = (double)(Math.round(d))/10d;
		return d;
	}
	
	public int cToF(int celsius)
	{
		Double d = (celsius * (Double)(9d/5d)) + 32d;
		d = d * 10d;
		d = (double)(Math.round(d))/10d;
		return d.intValue();
	}
	
	public int fToC(int fahrenheit)
	{
		Double d = (fahrenheit - 32d) * (Double)(5d/9d);
		d = d * 10d;
		d = (double)(Math.round(d))/10d;
		return d.intValue();
	}
	
    /*
     * Cette classe permet d'arrondir la consigne entrée
     */
    public int arrondir_valeur(double consigne) {
        return (int)(double)Math.round(consigne * 1) / 1;
    }
	

	//Getters and setters//
	/**
	 * @return the manager
	 */
	public FxManager getManager() {
		return manager;
	}

	/**
	 * @param manager the manager to set
	 */
	public void setManager(FxManager manager) {
		this.manager = manager;
	}

	/**
	 * @return the graph
	 */
	public ImageView getGraph() {
		return graph;
	}

	/**
	 * @param graph the graph to set
	 */
	public void setGraph(ImageView graph) {
		this.graph = graph;
	}

	/**
	 * @return the temp_type_image
	 */
	public ImageView getTemp_type_image() {
		return temp_type_image;
	}

	/**
	 * @return the temp_image
	 */
	public ImageView getTemp_image() {
		return temp_image;
	}

	/**
	 * @param temp_image the temp_image to set
	 */
	public void setTemp_image(ImageView temp_image) {
		this.temp_image = temp_image;
	}

	/**
	 * @return the info
	 */
	public Pane getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(Pane info) {
		this.info = info;
	}

	/**
	 * @return the info_humidity
	 */
	public Text getInfo_humidity() {
		return info_humidity;
	}

	/**
	 * @param info_humidity the info_humidity to set
	 */
	public void setInfo_humidity(Text info_humidity) {
		this.info_humidity = info_humidity;
	}

	/**
	 * @return the info_precision
	 */
	public Text getInfo_precision() {
		return info_precision;
	}

	/**
	 * @param info_precision the info_precision to set
	 */
	public void setInfo_precision(Text info_precision) {
		this.info_precision = info_precision;
	}

	/**
	 * @return the info_door_status
	 */
	public Text getInfo_door_status() {
		return info_door_status;
	}

	/**
	 * @param info_door_status the info_door_status to set
	 */
	public void setInfo_door_status(Text info_door_status) {
		this.info_door_status = info_door_status;
	}

	/**
	 * @param temp_type_image the temp_type_image to set
	 */
	public void setTemp_type_image(ImageView temp_type_image) {
		this.temp_type_image = temp_type_image;
	}

	/**
	 * @return the temp_value
	 */
	public Text getTemp_value() {
		return temp_value;
	}

	/**
	 * @param temp_value the temp_value to set
	 */
	public void setTemp_value(Text temp_value) {
		this.temp_value = temp_value;
	}

	/**
	 * @return the consigne
	 */
	public TextField getConsigne() {
		return consigne;
	}

	/**
	 * @param consigne the consigne to set
	 */
	public void setConsigne(TextField consigne) {
		this.consigne = consigne;
	}

	/**
	 * @return the consigne_add
	 */
	public ImageView getConsigne_add() {
		return consigne_add;
	}

	/**
	 * @param consigne_add the consigne_add to set
	 */
	public void setConsigne_add(ImageView consigne_add) {
		this.consigne_add = consigne_add;
	}

	/**
	 * @return the consigne_remove
	 */
	public ImageView getConsigne_remove() {
		return consigne_remove;
	}

	/**
	 * @param consigne_remove the consigne_remove to set
	 */
	public void setConsigne_remove(ImageView consigne_remove) {
		this.consigne_remove = consigne_remove;
	}
	
	
	
	
}