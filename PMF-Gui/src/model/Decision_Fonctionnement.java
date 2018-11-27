package model;

public class Decision_Fonctionnement {
	
	/*
	 * Decision a prendre en fonction des donneees calculees
	 * 
	 */
	
	Split_Data split_data = new Split_Data();
	
	public Decision_Fonctionnement() {
		
	}
	
	public void action_temperature() {
		if(split_data.getTemperature_float() > split_data.getRosee_float()) {
			/*
			 * On refroidit
			 * Module Peltier actionné
			 */
			
		}
		else {
			/*
			 * On laisse chauffer
			 */
			
		}
			
	}
	
	
	
	
}
