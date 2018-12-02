package view;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.util.Duration;

public abstract class AnimationsManager {
	
	public static void minimizeWindow(Node node)
	{
		/*
		 * Create the animations
		 */
		FadeTransition fade = new FadeTransition(Duration.millis(500), node);
		ScaleTransition scale = new ScaleTransition(Duration.millis(500), node);
		fade.setFromValue(1);
		fade.setToValue(0);
		fade.setCycleCount(1);
		scale.setFromX(1);
		scale.setFromY(1);
		scale.setFromZ(1);
		scale.setToX(0);
		scale.setToY(0);
		scale.setToZ(0);
		scale.setCycleCount(1);
		
		/*
		 * Play the animations
		 */
		scale.play();
		fade.play();
	}
	
	public static void minimizeWindow(Node node, javafx.event.EventHandler<ActionEvent> event)
	{
		/*
		 * Create the animations
		 */
		FadeTransition fade = new FadeTransition(Duration.millis(500), node);
		ScaleTransition scale = new ScaleTransition(Duration.millis(500), node);
		fade.setFromValue(1);
		fade.setToValue(0);
		fade.setCycleCount(1);
		scale.setFromX(1);
		scale.setFromY(1);
		scale.setFromZ(1);
		scale.setToX(0);
		scale.setToY(0);
		scale.setToZ(0);
		scale.setCycleCount(1);
		
		/*
		 * Play the animations
		 */
		scale.play();
		fade.play();
		fade.setOnFinished(event);
	}
	
	public static void extendWindow(Node node)
	{
		/*
		 * Create the animations
		 */
		FadeTransition fade = new FadeTransition(Duration.millis(500), node);
		ScaleTransition scale = new ScaleTransition(Duration.millis(500), node);
		fade.setFromValue(0);
		fade.setToValue(1);
		fade.setCycleCount(1);
		scale.setFromX(0);
		scale.setFromY(0);
		scale.setFromZ(0);
		scale.setToX(1);
		scale.setToY(1);
		scale.setToZ(1);
		scale.setCycleCount(1);
		
		/*
		 * Play the animations
		 */
		scale.play();
		fade.play();
	}
	
	public static void extendWindow(Node node, javafx.event.EventHandler<ActionEvent> event)
	{
		/*
		 * Create the animations
		 */
		FadeTransition fade = new FadeTransition(Duration.millis(500), node);
		ScaleTransition scale = new ScaleTransition(Duration.millis(500), node);
		fade.setFromValue(0);
		fade.setToValue(1);
		fade.setCycleCount(1);
		scale.setFromX(0);
		scale.setFromY(0);
		scale.setFromZ(0);
		scale.setToX(1);
		scale.setToY(1);
		scale.setToZ(1);
		scale.setCycleCount(1);
		
		/*
		 * Play the animations
		 */
		scale.play();
		fade.play();
		fade.setOnFinished(event);
	}

}
