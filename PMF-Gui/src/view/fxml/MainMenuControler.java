package view.fxml;

import javafx.event.Event;
import javafx.scene.image.ImageView;
import view.FxManager;

public class MainMenuControler {
	
	private FxManager manager;
	
	public void setMainApp(FxManager manager)
	{
		this.manager = manager;
	}
	
	public void exit()
	{
		this.manager.getPrimaryStage().close();
	}
	
	public void minimize()
	{
		this.manager.getPrimaryStage().setIconified(true);
	}
	
	public void onButtonFocused(Event e)
	{
		ImageView button = (ImageView)e.getSource();
		button.setFitWidth(button.getFitWidth() + 3);
		button.setFitHeight(button.getFitHeight() + 3);
		button.setX(button.getX() - 1.5);
		button.setY(button.getY() - 1.5);
		button.setOpacity(0.7);
	}
	
	public void onButtonNotFocused(Event e)
	{
		ImageView button = (ImageView)e.getSource();
		button.setFitWidth(button.getFitWidth() - 3);
		button.setFitHeight(button.getFitHeight() - 3);
		button.setX(button.getX() + 1.5);
		button.setY(button.getY() + 1.5);
		button.setOpacity(1);
	}
	
}