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
 * Classe qui initialise la conenxion en trouvant le bon port usb sur lequel est connecté la carte Arduino
 * Puis récupère les informations envoyées par l'Arduino
 * Peut envoyer des consignes à l'Arduino
 * Calcule de variables d'environnements
 */
public class Serial implements SerialPortEventListener {
	
	public String separateur = "\n-----------------------\n";

	/*
	 * Variables entrantes récupérées de l'Arduino
	 */
	public Double humidite;
	public Double Temperature_mesuree;
	public Double Temperature_rosee;

	/*
	 * Données modifiables depuis l'interface
	 */
	public int consigne = 0;
	public double precision = 1; //Valeur en %

	/*
	 * Données calculées à chaque reception de données de l'Arduino
	 */
	public double hysteresis;
	public double temperature_haute;
	public double temperature_bas;
	
	/*
	 * Variables nécéssaires à la reception/emission de données
	 */
	public BufferedReader input;
	public OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;
	private String position = "20";
	private boolean finish = false;
	
	/*
	 * Objets pour la recpetion et l'emission de données
	 */
	public SerialPort serialPort;
    public CommPortIdentifier portId = null;
  	
	/*
	 * Liste des ports possibles
	 * La troisième case du tableau est vide, elle sera complétée par la méthode definePort()
	 */
	private static String PORT_NAMES[] = {"/dev/tty.usbserial-A9007UX1", // Mac OS X
	        									"/dev/ttyUSB0", // Linux
	        									"", //Complétée par la méthode definePort()
	        									};

	private ModelFacade model;
	
	public Serial(ModelFacade model)
	{
		this.setModel(model);
	}
	
	/*
	 * Trouve le port sur lequel est branché l'Arduino
	 */
	@SuppressWarnings("rawtypes")
	public void definePort() throws IOException {
		System.out.println("Définition du port série connecté à l'Arduino");
	
		//Enumération des ports identifiés
		Enumeration enumComm = CommPortIdentifier.getPortIdentifiers();
	
		//On parcours tous les ports identifiés
		while (enumComm.hasMoreElements()) {
			
			CommPortIdentifier serialPortId = (CommPortIdentifier)enumComm.nextElement();
			
			if (serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				//Le bon port est appliqué
				PORT_NAMES[2] = serialPortId.getName();
				
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

	    //Instance des ports séries d'après la variable PORT_NAMES[] 
	    while (portEnum.hasMoreElements()) {
	    	
	        CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
	        for (String portName : PORT_NAMES) { //Parcourir l'ensemble des ports séries
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
	    	//Paramétrage du port série
	        serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
	        serialPort.setSerialPortParams(DATA_RATE,
	                SerialPort.DATABITS_8,
	                SerialPort.STOPBITS_1,
	                SerialPort.PARITY_NONE);
	        //Appel de la méthode qui active les input et output stream et les eventListener
	        this.doListen();
	    } catch (Exception e) {
	         e.printStackTrace();
	    }
	}
	
	/*
	 * Méthode qui est appelée lors de l'initialisation (méthode initialize())
	 * Permet d'activer les flux d'entrée et de sortie (input and output streams)
	 * Ajoute un eventListener au port série
	 */
	public void doListen() throws IOException, TooManyListenersException
	{
		//Avertir l'utilisateur
		System.out.println("Active I/O streams");
		
		//I/O
        input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
        output = serialPort.getOutputStream();
        
        //Ajout des eventListener et des notifications quand les données sont reçues
        System.out.println("Add event listener");
        serialPort.addEventListener(this);
        serialPort.notifyOnDataAvailable(true);
	}
	
	
	/*
	 * Méthode qui permet de stopper l'écoute et de fermer les flux
	 */
	public void exitListening() throws IOException {
		//Si le port série utilisé n'est pas null
	    if (serialPort != null) {
			System.out.println("Suppression du port");
			
			//Suppresion de l'eventListener relié au port série
	        serialPort.removeEventListener();
	        
	        //Fermeture du port série
	        serialPort.close();
	        
	        //Fermeture des flux d'entrée et de sortie (input and output streams)
	        output.close();
	        input.close();
	        
	        //Mise à null des flux et de l'objet de type SerialPort
	        output = null;
	        input = null;
	        serialPort = null;
	    }
	}
	
	/*
	 * (non-Javadoc)
	 * @see gnu.io.SerialPortEventListener#serialEvent(gnu.io.SerialPortEvent)
	 * Ecoute les données envoyées par l'Arduino
	 * Stocke dans la variable "chunks"
	 * Récupère les 3 données séparées par ";"
	 */
	public void serialEvent(SerialPortEvent oEvent) {
		if (input != null && output != null)
		{
			if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
		        try {
		        	//Mise à null
		            String inputLine=null;
		            if (input.ready()) { //Si les données sont prêtes à être réceptionnés
		                if (input !=null) inputLine = input.readLine();
		                String [] chunks = inputLine.split(";"); //Tableau de String qui stock les données
		                if (chunks.length == 4) //Si il y a 4 données reçues en provenance de l'Arduino
		                {			                
			                //Assignement des nouvelles valeurs aux données du système
			                if (!chunks[0].isEmpty()) this.humidite = Double.parseDouble(chunks[0]);
			                if (!chunks[1].isEmpty()) this.Temperature_mesuree = Double.parseDouble(chunks[1]);
			                if (!chunks[2].isEmpty()) this.Temperature_rosee = Double.parseDouble(chunks[2]);
			                
			                if (consigne == 0)
			                {
			                	consigne = (int) (Temperature_rosee + 1);
			                }
			                else if (!chunks[2].isEmpty() && Double.parseDouble(chunks[3]) != consigne)
			                {
			                	System.out.println("Setting consigne : " + consigne);
			                	writeData(consigne);
			                }
			                
			                //Calcule les variables d'environnements à partir des données reçues par l'Arduino
			                this.determiner_valeur_environnement();
			                
			                //Update the view informations
			                if (this.model.getControler() != null)
			                {
			                	this.model.getControler().setTemp(this.Temperature_mesuree);
			                	this.model.getControler().setHumidity(this.humidite.intValue());
			                	this.model.getControler().setTr(this.Temperature_rosee);
			                }
		                }
		            }
		
		        } catch (Exception e) {
		        	e.printStackTrace();
		        }
		    }
		}
	}
	
	/*
	 * Méthode pour envoyer des données de type int
	 */
    public void writeData(int consigne)    {
        try
        {
        	//Emission de la première donnée
            output.write(consigne);
            output.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    

	/*
	 * Calculs des variables d'environnements à chaque reception de données de l'Arduino
	 */
	public void determiner_valeur_environnement() {
		hysteresis = (consigne*precision)/100;
		temperature_haute = consigne + hysteresis;
		temperature_bas = consigne-hysteresis;
	}


	//Ensemble des accesseurs nécéssaires au fonctionnement du modèle//	 
	/**
	 * @return the separateur
	 */
	public String getSeparateur() {
		return separateur;
	}



	/**
	 * @param separateur the separateur to set
	 */
	public void setSeparateur(String separateur) {
		this.separateur = separateur;
	}



	/**
	 * @return the humidite
	 */
	public Double getHumidite() {
		return humidite;
	}



	/**
	 * @param humidite the humidite to set
	 */
	public void setHumidite(Double humidite) {
		this.humidite = humidite;
	}



	/**
	 * @return the temperature_mesuree
	 */
	public Double getTemperature_mesuree() {
		return Temperature_mesuree;
	}



	/**
	 * @param temperature_mesuree the temperature_mesuree to set
	 */
	public void setTemperature_mesuree(Double temperature_mesuree) {
		Temperature_mesuree = temperature_mesuree;
	}



	/**
	 * @return the temperature_rosee
	 */
	public Double getTemperature_rosee() {
		return Temperature_rosee;
	}



	/**
	 * @param temperature_rosee the temperature_rosee to set
	 */
	public void setTemperature_rosee(Double temperature_rosee) {
		Temperature_rosee = temperature_rosee;
	}



	/**
	 * @return the consigne
	 */
	public double getConsigne() {
		return consigne;
	}



	/**
	 * @param consigne the consigne to set
	 */
	public void setConsigne(int consigne) {
		this.consigne = consigne;
	}



	/**
	 * @return the precision
	 */
	public double getPrecision() {
		return precision;
	}



	/**
	 * @param precision the precision to set
	 */
	public void setPrecision(double precision) {
		this.precision = precision;
	}



	/**
	 * @return the hysteresis
	 */
	public double getHysteresis() {
		return hysteresis;
	}



	/**
	 * @param hysteresis the hysteresis to set
	 */
	public void setHysteresis(double hysteresis) {
		this.hysteresis = hysteresis;
	}



	/**
	 * @return the temperature_haute
	 */
	public double getTemperature_haute() {
		return temperature_haute;
	}



	/**
	 * @param temperature_haute the temperature_haute to set
	 */
	public void setTemperature_haute(double temperature_haute) {
		this.temperature_haute = temperature_haute;
	}



	/**
	 * @return the temperature_bas
	 */
	public double getTemperature_bas() {
		return temperature_bas;
	}



	/**
	 * @param temperature_bas the temperature_bas to set
	 */
	public void setTemperature_bas(double temperature_bas) {
		this.temperature_bas = temperature_bas;
	}



	/**
	 * @return the input
	 */
	public BufferedReader getInput() {
		return input;
	}



	/**
	 * @param input the input to set
	 */
	public void setInput(BufferedReader input) {
		this.input = input;
	}



	/**
	 * @return the output
	 */
	public OutputStream getOutput() {
		return output;
	}



	/**
	 * @param output the output to set
	 */
	public void setOutput(OutputStream output) {
		this.output = output;
	}



	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}



	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}



	/**
	 * @return the finish
	 */
	public boolean isFinish() {
		return finish;
	}



	/**
	 * @param finish the finish to set
	 */
	public void setFinish(boolean finish) {
		this.finish = finish;
	}



	/**
	 * @return the serialPort
	 */
	public SerialPort getSerialPort() {
		return serialPort;
	}



	/**
	 * @param serialPort the serialPort to set
	 */
	public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}



	/**
	 * @return the portId
	 */
	public CommPortIdentifier getPortId() {
		return portId;
	}



	/**
	 * @param portId the portId to set
	 */
	public void setPortId(CommPortIdentifier portId) {
		this.portId = portId;
	}



	/**
	 * @return the pORT_NAMES
	 */
	public static String[] getPORT_NAMES() {
		return PORT_NAMES;
	}



	/**
	 * @param pORT_NAMES the pORT_NAMES to set
	 */
	public static void setPORT_NAMES(String[] pORT_NAMES) {
		PORT_NAMES = pORT_NAMES;
	}



	/**
	 * @return the timeOut
	 */
	public static int getTimeOut() {
		return TIME_OUT;
	}



	/**
	 * @return the dataRate
	 */
	public static int getDataRate() {
		return DATA_RATE;
	}

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


}