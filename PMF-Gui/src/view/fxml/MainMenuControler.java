package view.fxml;

import java.util.ArrayList;
import java.util.Calendar;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
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
	@FXML
	private LineChart<?, ?> graph_temp;
	@FXML
	private Pane graph_pane;
	@SuppressWarnings("rawtypes")
	private XYChart.Series serie = new XYChart.Series<>();
	private ArrayList<Double> temps = new ArrayList<>();
	
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
	private Text info_tr;
	
	private long startTime;
	
	public void setMainApp(FxManager manager)
	{
		this.manager = manager;
		this.initGraph();
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
	 * Configure the graphic
	 */
	@SuppressWarnings("unchecked")
	private void initGraph()
	{
		this.startTime = Calendar.getInstance().getTimeInMillis();
		this.serie.setName("Température de la canette");
		this.graph_temp.getData().addAll(this.serie);
	}
	
	/*
	 * Updtae the graphic data with the new values
	 */
	public synchronized void updateGraph(Double temp)
	{
		String s = "" + (int)((Calendar.getInstance().getTimeInMillis() - this.startTime)/1000);
		javafx.application.Platform.runLater(new Runnable() {
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void run() {
				if (serie.getData().size() > 30)
				{
					serie.getData().remove(0);
				}
				serie.getData().add(new XYChart.Data(s, temp));
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
		this.graph_pane.setDisable(false);
		AnimationsManager.extendWindow(this.graph_pane);
	}
	/*
	 * On the graph button aren't focused
	 */
	public void onGraphNotFocused()
	{
		this.graph.setImage(new Image(this.getClass().getResourceAsStream("images/graph1.png")));
		this.graph_pane.setDisable(true);
		AnimationsManager.minimizeWindow(this.graph_pane);
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
				this.setTempStatus();
				System.out.println("Setting consigne");
			}
		} catch (Exception e) {
			System.out.println("Consigne isn't set");
		}
	}
	
	/*
	 * Set the regulation animation in terms of the regulation status 
	 */
	public void setTempStatus()
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
	 * @return the graph_pane
	 */
	public Pane getGraph_pane() {
		return graph_pane;
	}

	/**
	 * @param graph_pane the graph_pane to set
	 */
	public void setGraph_pane(Pane graph_pane) {
		this.graph_pane = graph_pane;
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
	 * @return the graph_temp
	 */
	public LineChart<?, ?> getGraph_temp() {
		return graph_temp;
	}

	/**
	 * @param graph_temp the graph_temp to set
	 */
	public void setGraph_temp(LineChart<?, ?> graph_temp) {
		this.graph_temp = graph_temp;
	}

	/**
	 * @param info_humidity the info_humidity to set
	 */
	public void setInfo_humidity(Text info_humidity) {
		this.info_humidity = info_humidity;
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

	/**
	 * @return the info_tr
	 */
	public Text getInfo_tr() {
		return info_tr;
	}

	/**
	 * @param info_tr the info_tr to set
	 */
	public void setInfo_tr(Text info_tr) {
		this.info_tr = info_tr;
	}

	/**
	 * @return the serie
	 */
	@SuppressWarnings("rawtypes")
	public XYChart.Series getSerie() {
		return serie;
	}

	/**
	 * @param serie the serie to set
	 */
	@SuppressWarnings("rawtypes")
	public void setSerie(XYChart.Series serie) {
		this.serie = serie;
	}

	/**
	 * @return the temps
	 */
	public ArrayList<Double> getTemps() {
		return temps;
	}

	/**
	 * @param temps the temps to set
	 */
	public void setTemps(ArrayList<Double> temps) {
		this.temps = temps;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	
	
	
}