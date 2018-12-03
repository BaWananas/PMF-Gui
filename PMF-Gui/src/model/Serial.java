package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/*
 * Classe qui initialise la conenxion en trouvant le bon port usb sur lequel est connect� la carte Arduino
 * Puis r�cup�re les informations envoy�es par l'Arduino
 * Peut envoyer des consignes � l'Arduino
 * Calcule de variables d'environnements
 */
public class Serial implements SerialPortEventListener {
	
	public String separateur = "\n-----------------------\n";

	/*
	 * Variables entrantes r�cup�r�es de l'Arduino
	 */
	public String humidite;
	public String Temperature_mesuree;
	public String Temperature_rosee;
	
	/*
	 * Variables issues de l'Arduino traduites en double
	 */
	public double humidite_double;
	public double temperature_double;
	public double rosee_double;
	public double rosee_double_elevee;
	
	
	/*
	 * Type de syst�me, donn�e affich�e graphiquement
	 */
	public String type_systeme = "Ferm�";

	
	/*
	 * Donn�es modifiables depuis l'interface
	 */
	public double consigne = 15.2; //Consigne � envoyer � l'Arduino si l'utilisateur veut changer sa valeur depuis l'interface
	public double precision = 1; //Valeur en %
	public double Fahrenheit = 273.15;

	/*
	 * Donn�es calcul�es � chaque reception de donn�es de l'Arduino
	 */
	public double temperature_mesuree_fahrenheit;
	public double hysteresis;
	public double temperature_haute;
	public double temperature_bas;
	
	/*
	 * Consigne en String
	 * Temperature de rosee �lev�e de 1 �C (en cas de temp�rature du frigo insuffisante)
	 */
	public String consigne_string = Double.toString(consigne);
	
	/*
	 * Consigne en int
	 * Permet d'etre utilis� par la m�thode writeData(inta, int b)
	 */
	public int consigne_int = (int) consigne;
	
	/*
	 * Variables n�c�ssaires � la reception/emission de donn�es
	 */
	public BufferedReader input;
	public OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;
	private String position = "20";
	private boolean finish = false;
	
	/*
	 * Objets pour la recpetion et l'emission de donn�es
	 */
	public SerialPort serialPort;
    public CommPortIdentifier portId = null;

  	
    /*
     * Variables ASCII avec leurs valeurs en int pour l'emission de donn�es
     */
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;
  	
	/*
	 * Liste des ports possibles
	 * La troisi�me case du tableau est vide, elle sera compl�t�e par la m�thode definePort()
	 */
	private static String PORT_NAMES[] = {"/dev/tty.usbserial-A9007UX1", // Mac OS X
	        									"/dev/ttyUSB0", // Linux
	        									"", //Compl�t�e par la m�thode definePort()
	        									};
	
	
	/*
	 * Trouve le port sur lequel est branch� l'Arduino
	 */
	public void definePort() throws IOException {
		System.out.println("D�finition du port s�rie connect� � l'Arduino");
	
		//Enum�ration des ports identifi�s
		Enumeration enumComm = CommPortIdentifier.getPortIdentifiers();
	
		//On parcours tous les ports identifi�s
		while (enumComm.hasMoreElements()) {
			
			CommPortIdentifier serialPortId = (CommPortIdentifier)enumComm.nextElement();
			
			if (serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				//Le bon port est appliqu�
				PORT_NAMES[2] = serialPortId.getName();
				
			}


		}

	}

	

	/*
	 * Initialise la connexion par port s�rie
	 */
	public void initialize() throws InterruptedException {
	    @SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
	
	    System.out.println("Initialisation de l'�coute du port s�rie " + PORT_NAMES[2]);

	    //Instance des ports s�ries d'apr�s la variable PORT_NAMES[] 
	    while (portEnum.hasMoreElements()) {
	    	
	        CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
	        for (String portName : PORT_NAMES) { //Parcourir l'ensemble des ports s�ries
	            if (currPortId.getName().equals(portName)) {
	                portId = currPortId; 
	                break;
	            
	            }
	        }
	    }
	    //Arret du programme si pas de connexion avec la carte Arduino
	    if (portId == null) {
	        System.out.println("Could not find COM port.");
	        return;
	    }
	    
	    System.out.println("Creation de serialPort");
	    try {
	    	//Param�trage du port s�rie
	        serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
	        serialPort.setSerialPortParams(DATA_RATE,
	                SerialPort.DATABITS_8,
	                SerialPort.STOPBITS_1,
	                SerialPort.PARITY_NONE);
	        //Appel de la m�thode qui active les input et output stream et les eventListener
	        this.doListen();
	    } catch (Exception e) {
	         e.printStackTrace();
	    }
	}
	
	/*
	 * M�thode qui est appel�e lors de l'initialisation (m�thode initialize())
	 * Permet d'activer les flux d'entr�e et de sortie (input and output streams)
	 * Ajoute un eventListener au port s�rie
	 */
	public void doListen() throws IOException, TooManyListenersException
	{
		//Avertir l'utilisateur
		System.out.println("Active I/O streams");
		
		//I/O
        input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
        output = serialPort.getOutputStream();
        
        //Envoi de donn�es � l'Arduino
        writeData("0");
        
        //Ajout des eventListener et des notifications quand les donn�es sont re�ues
        System.out.println("Add event listener");
        serialPort.addEventListener(this);
        serialPort.notifyOnDataAvailable(true);
	}
	
	
	/*
	 * M�thode qui permet de stopper l'�coute et de fermer les flux
	 */
	public void exitListening() throws IOException {
		//Si le port s�rie utilis� n'est pas null
	    if (serialPort != null) {
			System.out.println("Suppression du port");
			//Envoi de 2 donn�es � l'Arduino
			writeData("0");
			//Suppresion de l'eventListener reli� au port s�rie
	        serialPort.removeEventListener();
	        //Fermeture du port s�rie
	        serialPort.close();
	        //Fermeture des flux d'entr�e et de sortie (input and output streams)
	        output.close();
	        input.close();
	        //Mise � null des flux et de l'objet de type SerialPort
	        output = null;
	        input = null;
	        serialPort = null;
	    }
	}
	
	
	
	/*
	 * (non-Javadoc)
	 * @see gnu.io.SerialPortEventListener#serialEvent(gnu.io.SerialPortEvent)
	 * Ecoute les donn�es envoy�es par l'Arduino
	 * Stocke dans la variable "chunks"
	 * R�cup�re les 3 donn�es s�par�es par ";"
	 */
	public void serialEvent(SerialPortEvent oEvent) {
	    if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
	        try {
	        	//Mise � null
	            String inputLine=null;
	            if (input.ready()) { //Si les donn�es sont pr�tes � �tre r�ceptionn�s
	                inputLine = input.readLine();
	                String [] chunks = inputLine.split(";"); //Tableau de String qui stock les donn�es
	                if (chunks.length == 3) //Si il y a 3 donn�es re�ues en provenance de l'Arduino
	                {
	                	//Affichage dans la console des donn�es r�c�ptionn�es
		                System.out.println("Humidit�e : " + chunks[0] + "% | Temp�rature : " + chunks[1] + " | Point de ros�e : " + chunks[2]);
		                //Assignement des nouvelles valeurs aux donn�es du syst�me
		                this.humidite = chunks[0];
		                this.Temperature_mesuree = chunks[1];
		                this.Temperature_rosee = chunks[2];
		                //Conversion des donn�es en double
		                this.determiner_valeur_double();
		                //Calcule les variables d'environnements � partir des donn�es re�ues par l'Arduino
		                this.determiner_valeur_environnement();
	                }
	                //S�pare les trames de donn�es
	                System.out.println(separateur);

	            }
	
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }
	}
	
	/*
	 * M�thode pour envoyer des donn�es de type String
	 */
    public void writeData(String consigne)    {
        try
        {
        	//Emission de la premi�re donn�e
            output.write(consigne.getBytes());
            output.flush();
            //D�limiteur pour les donn�es
            //output.write(DASH_ASCII);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
	
    
	/*
	 * Conversion String vers double pour traitement des donnees
	 */
	public void determiner_valeur_double() {
		humidite_double = Double.parseDouble(this.getHumidite());
		temperature_double = Double.parseDouble(this.getTemperature_mesuree());
		rosee_double = Double.parseDouble(this.getTemperature_rosee());
		System.out.println("Valeurs chiffr�es : humidite_double = " + humidite_double + " temperature_double = " + temperature_double + " rosee_double = " + rosee_double);
	}
	

	/*
	 * Calculs des variables d'environnements � chaque reception de donn�es de l'Arduino
	 */
	public void determiner_valeur_environnement() {
		hysteresis = (consigne*precision)/100;
		temperature_mesuree_fahrenheit = Fahrenheit + getTemperature_double();
		temperature_haute = consigne + hysteresis;
		temperature_bas = consigne-hysteresis;
		System.out.println("Valeurs d'environnements du syst�me : hysteresis = " + hysteresis + " temperature_mesuree_fahrenheit = " + temperature_mesuree_fahrenheit + " temperature_haute = " + temperature_haute + " temperature_bas = " + temperature_bas);

	}
	
	

	
	/*
	 * Ensemble des accesseurs n�c�ssaires au fonctionnement du mod�le
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



	public int getConsigne_int() {
		return consigne_int;
	}



	public void setConsigne_int(int consigne_int) {
		this.consigne_int = consigne_int;
	}

}