package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;


/*
 * Classe qui initialise la conenxion en trouvant le bon port usb sur lequel est connect� la carte Arduino
 * Puis r�cup�re les informations envoy�es par l'Arduino
 */
public class Serial implements SerialPortEventListener {
	
	
	/*
	 * Compositions
	 */
	Split_Data split_data = new Split_Data();
	ModelFacade modelfacade = new ModelFacade();
	Decision_Fonctionnement decision_fonctionnement = new Decision_Fonctionnement();
	
	
	
	/*
	 * Variables issues de l'Arduino
	 */
	public String humidite;
	public String Temperature_mesuree;
	public String Temperature_rosee;
	
	/*
	 * Variables n�c�ssaires � la reception/emission de donn�es
	 */
	private BufferedReader input;
	@SuppressWarnings("unused")
	private OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;
	private String position = "20";
	private boolean finish = false;
	SerialPort serialPort;
	
	/*
	 * Liste des ports possibles
	 * La troisi�me case du tableau est vide, elle sera compl�t�e par la m�thode definePort()
	 */
	private static String PORT_NAMES[] = {"/dev/tty.usbserial-A9007UX1", // Mac OS X
	        									"/dev/ttyUSB0", // Linux
	        									"", // Windows
	        									};
	
	
	/*
	 * Trouve le port sur lequel est branch� l'Arduino
	 */
	public void definePort() {
		
		CommPortIdentifier serialPortId = null;
	
		@SuppressWarnings("rawtypes")
		Enumeration enumComm;
	
		enumComm = CommPortIdentifier.getPortIdentifiers();
	
		while (enumComm.hasMoreElements()) {
			
			serialPortId = (CommPortIdentifier)enumComm.nextElement();
			
			if (serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				// Apply the right port to the array
				PORT_NAMES[2] = serialPortId.getName();
				System.out.println("\n*********************************************");
				System.out.println("Le num�ro du port est " + PORT_NAMES[2] + "\n");
				System.out.println("*********************************************\n");
				
			}
		}
	}


	

	/*
	 * Initialise la connexion par port s�rie
	 */
	public void initialize() {
	    CommPortIdentifier portId = null;
	    @SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
	
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
	
	
	public synchronized void close() {
	    if (serialPort != null) {
	        serialPort.removeEventListener();
	        serialPort.close();
	    }
	}
	
	/*
	 * (non-Javadoc)
	 * @see gnu.io.SerialPortEventListener#serialEvent(gnu.io.SerialPortEvent)
	 * Ecoute les donn�es envoy�es par l'Arduino
	 * Stocke dans la variable "chunks"
	 * R�cup�re les 3 donn�es s�par�es par ";"
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
	    if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
	        try {
	            String inputLine=null;
	            if (input.ready()) {
	                inputLine = input.readLine();
	                String [] chunks = inputLine.split(";");
	                //Affichage dans la console des donn�es r�c�ptionn�es
	                System.out.println("Humidit�e : " + chunks[0] + "% | Temp�rature : " + chunks[1] + " | Point de ros�e : " + chunks[2]);
	                //Assignement des nouvelles valeurs aux donn�es
	                this.humidite = chunks[0];
	                this.Temperature_mesuree = chunks[1];
	                this.Temperature_rosee = chunks[2];
	                //Conversion des donn�es en double
	                split_data.determiner_valeur_double();
	                //Calcule les variables d'environnements � partir des donn�es re�ues par l'Arduino
	                modelfacade.determiner_valeur_environnement();
	                //Prend la d�cision pour l'Arduino et lui envoi grace � la m�thode "writeData" de la classe "Serial"
	                decision_fonctionnement.action_temperature();
	            }
	
	        } catch (Exception e) {
	        	
	        }
	    }
	    

	}
	
	
	
	/*
	 * Methode pour envoyer des donnees a l'Arduino
	 * On utilise write() qui prend en param�tre des bits
	 */
    public void writeData(int a) {
    	
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
}