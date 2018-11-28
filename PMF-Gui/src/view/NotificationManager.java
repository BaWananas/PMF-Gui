package view;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

public class NotificationManager {
	
	private SystemTray tray;
	private TrayIcon icon;
	
	public NotificationManager(String imagePath) throws AWTException
	{
		this.tray = SystemTray.getSystemTray();
		this.icon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(this.getClass().getResource(imagePath)), "Pimp My Frigogidaire");
		this.icon.setImageAutoSize(true);
		this.icon.setToolTip("Pimp My Frigogidaire is started");
        this.tray.add(this.icon);
	}
	
	/*
	 * Display a notification
	 */
	public void displayMessage(String text)
	{
		this.icon.displayMessage("Pimp My Frigogidaire", text, MessageType.INFO);
	}
	
	/*
	 * Display a notification
	 */
	public void displayMessage(String title, String text)
	{
		this.icon.displayMessage(title, text, MessageType.NONE);
	}
	
	/*
	 * Display a notification
	 */
	public void displayMessage(String text, MessageType type)
	{
		this.icon.displayMessage("Pimp My Frigogidaire", text, type);
	}
	
	/*
	 * Display a notification
	 */
	public void displayMessage(String title, String text, MessageType type)
	{
		this.icon.displayMessage(title, text, type);
	}
	
	/*
	 * Remove the tray icon
	 */
	public void stop()
	{
		this.tray.remove(this.icon);
	}

	/**
	 * @return the tray
	 */
	public SystemTray getTray() {
		return tray;
	}

	/**
	 * @param tray the tray to set
	 */
	public void setTray(SystemTray tray) {
		this.tray = tray;
	}

	/**
	 * @return the icon
	 */
	public TrayIcon getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(TrayIcon icon) {
		this.icon = icon;
	}

}
