package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.TimeUnit;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/*
 * Classe qui initialise la conenxion en trouvant le bon port usb sur lequel est connecté la carte Arduino
 * Puis récupère les informations envoyées par l'Arduino
 */
public class Serial implements SerialPortEventListener {
	

	//Difference entre la consigne et la temperature actuelle
	public double difference_temperature_atteindre;
	
	//Messages à renvoyer pour avertir l'utilisateur
	public String path_file_instruction = "instructions.txt";
	public String message_alerte = "Impossible d'aller à cette temperature, risque de condensation.";
	public String message_normal = "Changement de temperature en cours, veuillez patienter.";
	public String message_bonne_temperature = "Bonne temperature atteinte.";
	public String message_chauffage_urgence = "Rechauffement du frigo pour éviter la condensation.";
	
	public String separateur = "\n-----------------------\n";

	/*
	 * Variables issues de l'Arduino
	 */
	public String humidite;
	public String Temperature_mesuree;
	public String Temperature_rosee;
	
	public double humidite_double;
	public double temperature_double;
	public double rosee_double;
	public double rosee_double_elevee;
	
	
	//Type de système, donnée affichée graphiquement
	public String type_systeme = "Fermé";

	
	/*
	 * Données modifiable depuis l'interface
	 */
	public double consigne = 15;
	public double precision = 1; //Valeur en %
	public double Fahrenheit = 273.15;

	//Données calculées à chaque reception de données de l'Arduino
	public double temperature_mesuree_fahrenheit;
	public double hysteresis;
	public double temperature_haute;
	public double temperature_bas;
	
	/*
	 * Consigne en String
	 * Temperature de rosee élevée de 1 °C (en cas de température du frigo insuffisante)
	 */
	public String consigne_string = Double.toString(consigne);
	public String temp_urgence = "50";
	
	
	/*
	 * Variables nécéssaires à la reception/emission de données
	 */
	public BufferedReader input;
	@SuppressWarnings("unused")
	public OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;
	private String position = "20";
	private boolean finish = false;
	
	/*
	 * Agrégation et compositions pour envoi et reception de données
	 */
	SerialPort serialPort;
    CommPortIdentifier portId = null;

	
  //Fichier header
  	public static String Path_File_H = "consigne.h";
  	public static String Full_Path_File_H = "C:\\Users\\Thommes\\Documents\\Arduino\\consigne\\" + Path_File_H;

  	//Fichier C qui utilise le header ci-dessus
  	public static String Path_File_C = "consigne.c";
  	public static String Full_Path_File_C = "C:\\Users\\Thommes\\Documents\\Arduino\\consigne\\" + Path_File_C;

  	//Fichier .ino qui utilise le header ci-dessus
  	public static String Path_File_Ino = "consigne.ino";
  	public static String Full_Path_File_Ino = "C:\\Users\\Thommes\\Documents\\Arduino\\consigne\\" + Path_File_Ino;

  	
  	/*
  	 * Variables à utiliser dans les commandes arduino-cli
  	 * help : https://github.com/arduino/arduino-cli
  	 * Release -> v0.3.1 for windows
  	 */
  	
  	/*
  	 * Objet qui execute les commandes de la meme facon que l'invité de commande
  	 */
  	static Runtime runtime = Runtime.getRuntime();
  	
  	/*
  	 * Dossiers à utiliser dans les commandes d'arduino-cli
  	 */
  	public static String Main_Repository = "C:\\Users\\Thommes\\Documents\\Arduino\\consigne"; //Repertoire des fichiers Arduino = Sketch
  	public static String programme = "C:\\Users\\Thommes\\Documents\\Arduino\\arduino-cli-0.3.1-alpha.preview-windows.exe"; //Repertoire de l'executable arduino-cli	
  	public static String nom_arduino = "arduino:avr:uno"; //Identifiant de la carte Arduino, ici dans le cas de l'Arduino Uno
  	
	/*
	 * Liste des ports possibles
	 * La troisième case du tableau est vide, elle sera complétée par la méthode definePort()
	 */
	private static String PORT_NAMES[] = {"/dev/tty.usbserial-A9007UX1", // Mac OS X
	        									"/dev/ttyUSB0", // Linux
	        									"", //Complétée par la méthode definePort()
	        									};
	
	
	/*
	 * Trouve le port sur lequel est branché l'Arduino
	 */
	public void definePort() throws IOException {
		System.out.println("Définition du port série connecté à l'Arduino");
		
		CommPortIdentifier serialPortId = null;
	
		@SuppressWarnings("rawtypes")
		Enumeration enumComm;
	
		enumComm = CommPortIdentifier.getPortIdentifiers();
	
		while (enumComm.hasMoreElements()) {
			
			serialPortId = (CommPortIdentifier)enumComm.nextElement();
			
			if (serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				// Apply the right port to the array
				PORT_NAMES[2] = serialPortId.getName();
				System.out.println(separateur);
				System.out.println("Le numéro du port est " + PORT_NAMES[2]);
				System.out.println(separateur);
				
			}


		}

	}


	

	

	/*
	 * Initialise la connexion par port série
	 */
	public void initialize() throws InterruptedException {
	    @SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
	
	    System.out.println("Initialisation de l'écoute du port série " + PORT_NAMES[2]);

	    //First, Find an instance of serial port as set in PORT_NAMES.
	    while (portEnum.hasMoreElements()) {
	    	
	        CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
	        for (String portName : PORT_NAMES) {
	            if (currPortId.getName().equals(portName)) {
	                portId = currPortId; 
	                break;
	            
	            }
	        }
	    }
	    if (portId == null) {
	        System.out.println("Could not find COM port.");
	        return;
	    }
	
	    try {
	        serialPort = (SerialPort) portId.open(this.getClass().getName(),
	                TIME_OUT);
	        serialPort.setSerialPortParams(DATA_RATE,
	                SerialPort.DATABITS_8,
	                SerialPort.STOPBITS_1,
	                SerialPort.PARITY_NONE);
	
	        // open the streams
	        input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
	        output = serialPort.getOutputStream();
	        
	
	        serialPort.addEventListener(this);
	        serialPort.notifyOnDataAvailable(true);
	    } catch (Exception e) {
	        // System.err.println(e.toString());
	        
	    }
	}
	
	
	public void supprimer_port() throws IOException {
		System.out.println("Suppression du port");
	    if (serialPort != null) {
	        serialPort.removeEventListener();
	        serialPort.close();
	        output.close();
	        input.close();
	    }
	}
	
	
	
	/*
	 * (non-Javadoc)
	 * @see gnu.io.SerialPortEventListener#serialEvent(gnu.io.SerialPortEvent)
	 * Ecoute les données envoyées par l'Arduino
	 * Stocke dans la variable "chunks"
	 * Récupère les 3 données séparées par ";"
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
	    if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
	        try {
	            String inputLine=null;
	            if (input.ready()) {
	                inputLine = input.readLine();
	                String [] chunks = inputLine.split(";");
	                //Affichage dans la console des données récéptionnées
	                System.out.println("Humiditée : " + chunks[0] + "% | Température : " + chunks[1] + " | Point de rosée : " + chunks[2]);
	                //Assignement des nouvelles valeurs aux données
	                this.humidite = chunks[0];
	                this.Temperature_mesuree = chunks[1];
	                this.Temperature_rosee = chunks[2];
	                //Conversion des données en double
	                this.determiner_valeur_double();
	                //Calcule les variables d'environnements à partir des données reçues par l'Arduino
	                this.determiner_valeur_environnement();
	                //Prend la décision pour l'Arduino et lui envoi grace à la méthode "writeData" de la classe "Serial"
	                this.supprimer_port();
	                this.action_temperature();
	                //Séparateur pour délimiter les lignes d'informations reçues et traitées
	                System.out.println(separateur);
	                this.initialize();
	            }
	
	        } catch (Exception e) {
	        	
	        }
	    }
	    

	}
	
	
	/*
	 * Méthode qui modifie un fichier .h, .c et .ino
	 * A chaque fois qu'une trame de donnée est reçue
	 * Les fichiers prennent la nouvelle valeur de la consigne à respecter
	 * 
	 */
	public static void modification_fichier(String consigne_a_respecter) throws IOException, InterruptedException {
		
		/*
		 * Création d'objets de type File
		 */
		System.out.println("Modification des fichiers .h, .c et .ino avec la consigne : " + consigne_a_respecter);

		try{
			File ff_1=new File(Full_Path_File_H); // Définir l'arborescence
			File ff_2=new File(Full_Path_File_C); // Définir l'arborescence
			File ff_3=new File(Full_Path_File_Ino); // Définir l'arborescence


			//Création de fichier
			ff_1.createNewFile();
			ff_2.createNewFile();
			ff_3.createNewFile();

			
			/*
			 * création d'objets de type FileWriter
			 */
			FileWriter ffw_1=new FileWriter(ff_1);
			FileWriter ffw_2=new FileWriter(ff_2);
			FileWriter ffw_3=new FileWriter(ff_3);

			/*
			 * Modification du fichier .h consigne.h
			 */
			ffw_1.write("#ifndef consigne");
			ffw_1.write(System.getProperty( "line.separator" ));
			ffw_1.write("#define consigne");
			ffw_1.write(System.getProperty( "line.separator" ));
			ffw_1.write("#include <Arduino.h>");
			ffw_1.write(System.getProperty( "line.separator" ));
			ffw_1.write("extern int consigne_a_respecter;");
			ffw_1.write(System.getProperty( "line.separator" ));
			ffw_1.write("#endif");
			ffw_1.close(); // fermer le fichier à la fin des traitements
			
			
			/*
			 * Modification du fichier .c consigne.c
			 */
			ffw_2.write("#include \"" + Path_File_H + "\"");
			ffw_2.write(System.getProperty( "line.separator" ));
			ffw_2.write("int consigne_a_respecter=" + consigne_a_respecter + ";");
			ffw_2.close(); // fermer le fichier à la fin des traitements
			
			
			/*
			 * Modification du fichier .ino consigne.ino
			 */
			ffw_3.write("#include \"" + Path_File_H + "\"");
			ffw_3.write(System.getProperty( "line.separator" ));
			ffw_3.write("void setup() {");
			ffw_3.write(System.getProperty( "line.separator" ));
			ffw_3.write("consigne_a_respecter = " + consigne_a_respecter + ";");
			ffw_3.write(System.getProperty( "line.separator" ));
			ffw_3.write("Serial.begin(9600);");
			ffw_3.write(System.getProperty( "line.separator" ));
			ffw_3.write("}");
			ffw_3.write(System.getProperty( "line.separator" ));
			ffw_3.write("void loop() {");
			ffw_3.write(System.getProperty( "line.separator" ));
			ffw_3.write("Serial.println(\"30;15;17\");");
			ffw_3.write(System.getProperty( "line.separator" ));
			ffw_3.write("}");
			ffw_3.close(); // fermer le fichier à la fin des traitements
			
			} catch (Exception e) {}
	}
	
	/*
	 * Méthode qui va initialiser la connexion entre windows et l'Arduino
	 * S'execute une seule fois au démarrage de l'application
	 */
	public void ini_exec() throws InterruptedException, IOException {
		
		/*
		 * Verification des ports, de compatibilité de la carte et des branchements usb
		 * Logiciel "arduino-cli" executé
		 */
		
		
		
		/*
		//Mise à jour des index
		Process a = runtime.exec(programme + " core update-index");
		System.out.println(programme + " core update-index");
		a.waitFor();

		//Liste des cartes disponibles
		Process b = runtime.exec(programme + " board list");
		System.out.println(programme + " board list");
		b.waitFor();

		//Recherche les cartes compatibles
		Process c = runtime.exec(programme + " core search arduino");
		System.out.println(programme + " core search arduino");
		c.waitFor();
		
		//Instalaltion des paquets pour la carte Arduino
		Process d = runtime.exec(programme + " core install arduino:avr");
		System.out.println(programme + " core install arduino:avr");
		d.waitFor();
		
		//Liste des cartes disponibles
		Process e = runtime.exec(programme + " board list");
		System.out.println(programme + " board list");
		e.waitFor();
	*/
	}
	
	/*
	 * Méthode qui permet de :
	 * Compiler le dossier dans lequel se situe les fichier .ino
	 * Uploader (=téléverser) les fichiers héxadécimaux dans l'Arduino
	 */
	public static void exec_commande() throws InterruptedException, IOException {
		
		System.out.println("Compilation et téléversement :");
		
		
		//Instalaltion des paquets pour la carte Arduino
		Process h = runtime.exec(programme + " core install arduino:avr");
		System.out.println(programme + " core install arduino:avr");
		String commande_install = programme + " core install arduino:avr";
		h.waitFor();


		
		//Compile les fichiers du dossier selectionné
		Process f = runtime.exec("cmd.exe /c " + programme + " compile --fqbn " + nom_arduino + " " + Main_Repository);
		System.out.println(programme + " compile --fqbn " + nom_arduino + " " + Main_Repository);
		String commande_compiler = programme + " compile --fqbn " + nom_arduino + " " + Main_Repository;
		f.waitFor();




		
		//Téléverse dans la carte Arduino
		Process g = runtime.exec(programme + " upload -p " + PORT_NAMES[2] + " --fqbn " + nom_arduino + " " + Main_Repository); //Port_Names[2] est obtenu grâce à la méthode definePort
		System.out.println(programme + " upload -p " + PORT_NAMES[2] + " --fqbn " + nom_arduino + " " + Main_Repository); //Port_Names[2] est obtenu grâce à la méthode definePort
		String commande_televerse = programme + " upload -p " + PORT_NAMES[2] + " --fqbn " + nom_arduino + " " + Main_Repository;
		g.waitFor();
		

		
	}

    
	/*
	 * Conversion String vers double pour traitement des donnees
	 */
	public void determiner_valeur_double() {
		humidite_double = Double.parseDouble(this.getHumidite());
		temperature_double = Double.parseDouble(this.getTemperature_mesuree());
		rosee_double = Double.parseDouble(this.getTemperature_rosee());
		System.out.println("Valeurs chiffrées; humidite_double = " + humidite_double + " temperature_double = " + temperature_double + " rosee_double = " + rosee_double);
		
	}
	
	
	//Calculs des variables d'environnements à chaque reception de données de l'Arduino
	public void determiner_valeur_environnement() {
		hysteresis = (consigne*precision)/100;
		temperature_mesuree_fahrenheit = Fahrenheit + getTemperature_double();
		temperature_haute = consigne + hysteresis;
		temperature_bas = consigne-hysteresis;
		System.out.println("Valeurs d'environnements : hysteresis = " + hysteresis + " temperature_mesuree_fahrenheit = " + temperature_mesuree_fahrenheit + " temperature_haute = " + temperature_haute + " temperature_bas = " + temperature_bas);
	}
	
	
	
	/*
	 * Envoi les ordres à l'Arduino en fonction des données reçues
	 * Les ordres sont envoyés via la méthode writeData
	 * L'ordre contient la consigne dans le cas où il est autorisé d'atteindre la température spécifié
	 * Sinon aucun ordre n'est spécifié
	 */
		public String action_temperature() throws TooManyListenersException, InterruptedException, IOException {
			difference_temperature_atteindre = this.getConsigne() - this.getTemperature_double();
			System.out.println("Action à faire en fonction des données récupérées de l'Arduino :");
			if(difference_temperature_atteindre > 0) {
				if(this.getConsigne() > this.getRosee_double()) {
					/*
					 * On peut chauffer
					 * Appel de la methode modification_fichier de la classe Serial
					 */
					modification_fichier(this.getConsigne_string());
					//On averti l'utilisateur
					System.out.println(message_normal);
					//On compile puis téléverse les nouveaux fichiers
					exec_commande();
					return message_normal;
				}
			}
			if(difference_temperature_atteindre < 0) {
				if(this.getConsigne() > this.getRosee_double()) {
					
					/*
					 * On peut refroidir
					 */
					modification_fichier(this.getConsigne_string());
					System.out.println(message_normal);
					//On compile puis téléverse les nouveaux fichiers
					exec_commande();
					return message_normal;

				}

			}
			if(difference_temperature_atteindre == 0) {
				if(this.getConsigne() > this.getRosee_double()) {

					/*
					 * Pas besoin de chauffer, bonne température
					 */
					//modification_fichier(this.getConsigne_string()); //Pas besoin de modifier les fichiers
					System.out.println(message_bonne_temperature);
					//exec_commande(); //Pas besoin d'envoyer de nouvelle consigne
					return message_bonne_temperature;
				}
			}
			

			if(this.getTemperature_double() <= this.getRosee_double()) {
				/*
				 * Il faut chauffer pour eviter la condensation
				 */
				modification_fichier(this.getTemp_urgence());
				System.out.println(message_chauffage_urgence);
				//On compile puis téléverse les nouveaux fichiers
				exec_commande();
				return message_chauffage_urgence;
				
			}
			
			if(this.getConsigne() <= this.getRosee_double()) {
				/*
				 * Risque de condensation
				 * L'utilisateur veut atteindre une température dangereuse pour le système
				 * Pas d'ordre envoyé
				 */
				
				modification_fichier(this.getConsigne_string());
				System.out.println(message_alerte);
				exec_commande(); //Pas besoin d'envoyer de nouvelle consigne
				return message_alerte;

			}

			modification_fichier(this.getConsigne_string());
			System.out.println(message_normal);
			//On compile puis téléverse les nouveaux fichiers
			exec_commande();
			return message_normal;
		}
    
	
	/*
	 * Accesseurs
	 */
	public String getPosition() {
		return this.position;
	}
	
	public boolean getFinish() {
		return this.finish;
	}

	public String getHumidite() {
		return humidite;
	}

	public void setHumidite(String humidite) {
		this.humidite = humidite;
	}

	public String getTemperature_mesuree() {
		return Temperature_mesuree;
	}

	public void setTemperature_mesuree(String temperature_mesuree) {
		Temperature_mesuree = temperature_mesuree;
	}

	public String getTemperature_rosee() {
		return Temperature_rosee;
	}

	public void setTemperature_rosee(String temperature_rosee) {
		Temperature_rosee = temperature_rosee;
	}




	public double getHumidite_double() {
		return humidite_double;
	}




	public void setHumidite_double(double humidite_double) {
		this.humidite_double = humidite_double;
	}




	public double getTemperature_double() {
		return temperature_double;
	}




	public void setTemperature_double(double temperature_double) {
		this.temperature_double = temperature_double;
	}




	public double getRosee_double() {
		return rosee_double;
	}




	public void setRosee_double(double rosee_double) {
		this.rosee_double = rosee_double;
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




	public String getType_systeme() {
		return type_systeme;
	}




	public void setType_systeme(String type_systeme) {
		this.type_systeme = type_systeme;
	}




	public double getConsigne() {
		return consigne;
	}




	public void setConsigne(double consigne) {
		this.consigne = consigne;
	}




	public double getPrecision() {
		return precision;
	}




	public void setPrecision(double precision) {
		this.precision = precision;
	}




	public double getFahrenheit() {
		return Fahrenheit;
	}




	public void setFahrenheit(double fahrenheit) {
		Fahrenheit = fahrenheit;
	}




	public double getTemperature_mesuree_fahrenheit() {
		return temperature_mesuree_fahrenheit;
	}




	public void setTemperature_mesuree_fahrenheit(double temperature_mesuree_fahrenheit) {
		this.temperature_mesuree_fahrenheit = temperature_mesuree_fahrenheit;
	}




	public double getHysteresis() {
		return hysteresis;
	}




	public void setHysteresis(double hysteresis) {
		this.hysteresis = hysteresis;
	}




	public double getTemperature_haute() {
		return temperature_haute;
	}




	public void setTemperature_haute(double temperature_haute) {
		this.temperature_haute = temperature_haute;
	}




	public double getTemperature_bas() {
		return temperature_bas;
	}




	public void setTemperature_bas(double temperature_bas) {
		this.temperature_bas = temperature_bas;
	}





	public BufferedReader getInput() {
		return input;
	}




	public void setInput(BufferedReader input) {
		this.input = input;
	}




	public OutputStream getOutput() {
		return output;
	}




	public void setOutput(OutputStream output) {
		this.output = output;
	}




	public SerialPort getSerialPort() {
		return serialPort;
	}




	public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}




	public static String[] getPORT_NAMES() {
		return PORT_NAMES;
	}




	public static void setPORT_NAMES(String[] pORT_NAMES) {
		PORT_NAMES = pORT_NAMES;
	}




	public static int getTimeOut() {
		return TIME_OUT;
	}




	public static int getDataRate() {
		return DATA_RATE;
	}




	public void setPosition(String position) {
		this.position = position;
	}




	public void setFinish(boolean finish) {
		this.finish = finish;
	}




	public String getSeparateur() {
		return separateur;
	}




	public void setSeparateur(String separateur) {
		this.separateur = separateur;
	}




	public String getConsigne_string() {
		return consigne_string;
	}




	public void setConsigne_string(String consigne_string) {
		this.consigne_string = consigne_string;
	}




	public double getRosee_double_elevee() {
		return rosee_double_elevee;
	}




	public void setRosee_double_elevee(double rosee_double_elevee) {
		this.rosee_double_elevee = rosee_double_elevee;
	}




	public String getTemp_urgence() {
		return temp_urgence;
	}




	public void setTemp_urgence(String temp_urgence) {
		this.temp_urgence = temp_urgence;
	}

}