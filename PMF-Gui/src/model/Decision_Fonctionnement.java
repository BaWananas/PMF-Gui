package model;

public class Decision_Fonctionnement {
	
	/*
	 * Decision a prendre en fonction des donneees calculees
	 * 
	 */
	Split_Data split_data = new Split_Data();
	ModelFacade modelfacade = new ModelFacade();
	Serial serial = new Serial();
	
	public double difference_temperature_atteindre;
	
	public String path_file_instruction = "instructions.txt";
	public String message_alerte = "Impossible d'aller à cette temperature, risque de condensation";
	public String message_normal = "Changement de temperature en cours";
	public String message_bonne_temperature = "Bonne temperature atteinte";
	public String message_chauffage_urgence = "Rechauffement du frigo pour éviter la condensation";
	
	public Decision_Fonctionnement() {
		
	}
	
	public String action_temperature() {
		difference_temperature_atteindre = modelfacade.getConsigne() - split_data.getTemperature_double();
		if(difference_temperature_atteindre > 0) {
			if(modelfacade.getConsigne() > split_data.getRosee_double()) {
				/*
				 * On peut chauffer
				 * Appel de la methode writeData de la classe Collect_Data_Serial 
				 */
				serial.writeData(100);
				return message_normal;
			}
		}
		if(difference_temperature_atteindre < 0) {
			if(modelfacade.getConsigne() > split_data.getRosee_double()) {
				/*
				 * On peut refroidir
				 */
				
				serial.writeData(100);
				return message_normal;

			}

		}
		if(difference_temperature_atteindre == 0) {
			if(modelfacade.getConsigne() > split_data.getRosee_double()) {

				/*
				 * Pas besoin de chauffer, bonne température
				 */
				serial.writeData(0);
				return message_bonne_temperature;
			}
		}
		

		if(split_data.getTemperature_double() <= split_data.getRosee_double()) {
			/*
			 * On chauffe pour eviter la formation de la condensation
			 */
			
			serial.writeData(100);
			return message_chauffage_urgence;
			
		}
		
		if(modelfacade.getConsigne() <= split_data.getRosee_double()) {
			/*
			 * Risque de condensation
			 * L'utilisateur veut atteindre une température dangereuse
			 * Pas d'ordre envoye
			 */
			
			serial.writeData(0);
			return message_alerte;

		}

		/*
		 * Si la température actuelle est inferieure a la temperature de rosée
		 */
		serial.writeData(100);
		return message_chauffage_urgence;
		}
	

	public Split_Data getSplit_data() {
		return split_data;
	}

	public void setSplit_data(Split_Data split_data) {
		this.split_data = split_data;
	}

	public ModelFacade getModelfacade() {
		return modelfacade;
	}

	public void setModelfacade(ModelFacade modelfacade) {
		this.modelfacade = modelfacade;
	}

	public Serial getCollect_data_serial() {
		return serial;
	}

	public void setCollect_data_serial(Serial serial) {
		this.serial = serial;
	}


	public double getDifference_temperature_atteindre() {
		return difference_temperature_atteindre;
	}

	public void setDifference_temperature_atteindre(double difference_temperature_atteindre) {
		this.difference_temperature_atteindre = difference_temperature_atteindre;
	}

	public String getPath_file_instruction() {
		return path_file_instruction;
	}

	public void setPath_file_instruction(String path_file_instruction) {
		this.path_file_instruction = path_file_instruction;
	}

	public String getMessage_alerte() {
		return message_alerte;
	}

	public void setMessage_alerte(String message_alerte) {
		this.message_alerte = message_alerte;
	}

	public String getMessage_normal() {
		return message_normal;
	}

	public void setMessage_normal(String message_normal) {
		this.message_normal = message_normal;
	}

	public String getMessage_bonne_temperature() {
		return message_bonne_temperature;
	}

	public void setMessage_bonne_temperature(String message_bonne_temperature) {
		this.message_bonne_temperature = message_bonne_temperature;
	}

	public String getMessage_chauffage_urgence() {
		return message_chauffage_urgence;
	}

	public void setMessage_chauffage_urgence(String message_chauffage_urgence) {
		this.message_chauffage_urgence = message_chauffage_urgence;
	}
	
	
}			
