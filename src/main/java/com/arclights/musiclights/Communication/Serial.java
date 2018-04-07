package com.arclights.musiclights.Communication;

import com.arclights.musiclights.monitor.Monitor;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;


public class Serial extends Thread implements SerialPortEventListener {

    SerialPort serialPort;

    private Monitor m;

    private static final String PORT_NAMES[] = {"/dev/tty.usbserial-A9007UX1",
            "dev/ttyUSB0", "COM14"};

    /**
     * A BufferedReader which will be fed by a InputStreamReader converting the
     * bytes into characters making the displayed results codepage independent
     */
    private BufferedReader input;
    /**
     * The output stream to the port
     */
    private OutputStream output;
    /**
     * Milliseconds to block while waiting for port to open
     */
    private static final int TIME_OUT = 2000;
    /**
     * Default bits per second from COM port
     */
    private static final int DATA_RATE = 9600;

    public Serial(Monitor m) {
        this.m = m;
    }

    public void initialize() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        // First, find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
                    .nextElement();
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
            // Open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            // Set port parameters
            serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            // Open the streams
            input = new BufferedReader(new InputStreamReader(
                    serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            // Add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This should be called when you stop using the port. This will prevent
     * port locking on platforms like Linux.
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = input.readLine();
                System.out.println(inputLine);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    @Override
    public void run() {
        while (!interrupted()) {
            int message = m.getMessage();
            try {
                output.write(message);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        close();
    }
}
