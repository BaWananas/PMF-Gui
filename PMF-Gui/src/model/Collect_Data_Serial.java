package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class Collect_Data_Serial implements SerialPortEventListener {
	SerialPort serialPort;
	private String data;
	
    /** The port we're normally going to use. */
private static final String PORT_NAMES[] = {
		/*
		"/dev/tty.usbserial-A9007UX1", // Mac OS X
                    "/dev/ttyACM0", // Raspberry Pi
		"/dev/ttyUSB0", // Linux
		*/
		"", // Windows 1
		//Or
		"COM4", //Windows 2
};

/**
* A BufferedReader which will be fed by a InputStreamReader 
* converting the bytes into characters 
* making the displayed results codepage independent
*/
private BufferedReader input;
/** The output stream to the port */
private OutputStream output;
/** Milliseconds to block while waiting for port open */
private static final int TIME_OUT = 2000;
/** Default bits per second for COM port. */
private static final int DATA_RATE = 9600;

	/*
	 * Recuperer le port où l'Arduino est branchee
	 * Permet de gagner en flexibilite
	 */
public void definePort() {
	
	CommPortIdentifier serialPortId = null;

	Enumeration enumComm;

	enumComm = CommPortIdentifier.getPortIdentifiers();

	while (enumComm.hasMoreElements()) {
		
		serialPortId = (CommPortIdentifier)enumComm.nextElement();
		
		if (serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
			// Apply the right port to the array
			PORT_NAMES[2] = serialPortId.getName();			
			System.out.println("\n*********************************************");
			System.out.println("Le numéro du port est " + PORT_NAMES[2] + "\n");
			System.out.println("*********************************************\n");
			
		}
	}
}

public void initialize() {
            // the next line is for Raspberry Pi and 
            // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
            System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

	CommPortIdentifier portId = null;
	Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
	this.definePort();
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
		// open serial port, and use class name for the appName.
		serialPort = (SerialPort) portId.open(this.getClass().getName(),
				TIME_OUT);

		// set port parameters
		serialPort.setSerialPortParams(DATA_RATE,
				SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);

		// open the streams
		input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
		output = serialPort.getOutputStream();

		// add event listeners
		serialPort.addEventListener(this);
		serialPort.notifyOnDataAvailable(true);
	} catch (Exception e) {
		System.err.println(e.toString());
	}
}

/**
 * This should be called when you stop using the port.
 * This will prevent port locking on platforms like Linux.
 */
public synchronized void close() {
	if (serialPort != null) {
		serialPort.removeEventListener();
		serialPort.close();
	}
}

/**
 * Methode qui lit les entrees consoles de l'Arduino
 * Les entrees proviennent des capteurs
 */
public synchronized void serialEvent(SerialPortEvent oEvent) {
	if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
		try {
			String inputLine=input.readLine();
			this.data=inputLine;
			//System.out.println(inputLine);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	// Ignore all the other eventTypes, but you should consider the other ones.
}



public void initialisation() throws Exception {
	Collect_Data_Serial main = new Collect_Data_Serial();
	main.initialize();
	Thread t=new Thread() {
		public void run() {
			//the following line will keep this app alive for 1000 seconds,
			//waiting for events to occur and responding to them (printing incoming messages to console).
			try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
		}
	};
	t.start();
	System.out.println("Started");
}

public String getData() {
	return data;
}

public void setData(String data) {
	this.data = data;
}

	
}
