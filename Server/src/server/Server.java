/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jens
 *
 * Server, der Kommunikation mittels UDP und TCP realisiert UDP wird in der main
 * behandelt und TCP im Thread tcp
 *
 */
public class Server extends Thread {



    //THRIFT CONSTANTS
    final static String ORDER = "9";
    
    static DatagramSocket datagramSocket; //UDP
    static ServerSocket welcomeSocket; //TCP-LISTENING
    protected static Server tcp;
    protected static volatile Items items; //beinhaltet den aktuellen Stand der Artikel
    public static HTMLMaker htmlmaker;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Server.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        items = new Items();
        htmlmaker = new HTMLMaker(items.getCurrentItemsArray());

        try {
            datagramSocket = new DatagramSocket(constants.Constants.UDP_SERVER_PORT);
            welcomeSocket = new ServerSocket(constants.Constants.TCP_LISTENING_SERVER_SOCKET);
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        tcp = new Server();
        tcp.start();

        while (true) {

            String incomingMessage = receiveMessageUDP(); //receiveMessageUDP blockiert, bis Nachricht eintrifft
            handleMessage(incomingMessage);
        }
    }

    /**
     * Bearbeitet eingehende TCP-Anfragen
     */
    @Override
    public void run() {

        while (true) {

            try {
                Socket connectionSocket = welcomeSocket.accept();
                TCPHandling newConnection = new TCPHandling(connectionSocket);
                newConnection.start();

            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * @return String Empfängt UDP-Nachricht und gibt diese als String zurück
     */
    private static String receiveMessageUDP() {

        byte[] receivedData = new byte[constants.Constants.PAYLOAD_FOR_UDP];
        DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
        try {
            datagramSocket.receive(receivedPacket); //Daten empfangen
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        String data = new String(receivedPacket.getData()); //Empfangene Daten
        return data;

    }

    /**
     * @param message beihnhaltet Nachricht zum verschicken Sendet String per
     * UDP an den Server
     */
    public static void sendMessageUDP(String message) {

        byte[] sendData = new byte[constants.Constants.PAYLOAD_FOR_UDP];
        try (DatagramSocket sendSocket = new DatagramSocket()) {
            InetAddress inetAddress = InetAddress.getByName("localhost");
            sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, inetAddress, constants.Constants.UDP_CLIENT_PORT);
            sendSocket.send(sendPacket);

        } catch (IOException e) {
        }

    }

    /**
     * Kontrolliert ob Item aus dem Kühlschrank genommen oder hinzugefügt wurde
     * und verarbeitet UDP-Nachrichten
     *
     */
    private static void handleMessage(String s) {

        s = s.trim(); //Comparison fails in ItemToAlter if not trimmed
        if (s.equals("updateitems")) {
            sendMessageUDP(Server.items.currentItems());

        } else if (s.startsWith("-")) {
            s = s.substring(1);

            if (Server.items.takeItemOut(Server.items.ItemToAlter(s)) < 3) { // Wenn weniger als 2 eines Artikels vorhanden, nachbestellen
                //in THREAD auslagern?
                orderItems(s);
            }

        } else if (s.startsWith("+")) {
            s = s.substring(1);
            Server.items.putItemIn(Server.items.ItemToAlter(s));

        } else {
            System.out.println("Wrong Message");
        }
    }

    /**
     * @return the items
     */
    public static Items getItems() {
        return items;
    }

    /**
     * Bestellt Artikel im Store vie THRIFT 
     * Anzahl auf 9 festgelegt
     * @param itemToOrder 
     */
    public static void orderItems(String itemToOrder) {
        String answer = ThriftHandler.establishThriftConnection(itemToOrder, ThriftHandler.ORDERITEMS, 1);
        
        if (answer.contains("lieferbar")) {
            LOGGER.info(answer);
        } else {
            items.changeItems(answer);
        }
    }

    /**
     * @param int test
     *
     * test = 1: Wie lange dauert das Bearbeiten der Anfrage und Versenden der
     * HTML FILE
     */
    private static void testFunction(int test) {

        switch (test) {
            case 1:
                long startTime,
                 endTime,
                 duration;

                try {
                    Socket connectionSocket = welcomeSocket.accept(); //Verbindung aufbauen
                    startTime = System.nanoTime(); //Zeit gemessen 11:02:20341
                    TCPHandling newConnection = new TCPHandling(connectionSocket);
                    newConnection.start();
                    while (newConnection.isAlive()) { // wenn Verbindung zu Ende
                    }
                    endTime = System.nanoTime(); // Zeit messen 11:02:56245
                    duration = endTime - startTime; // Berechnung der Dauer
                    System.out.println("Time elapsed: " + duration + " nanoseconds."); //Zeitmessung
                    System.out.println("Time elapsed: " + ((double) duration / 1000000) + " milliseconds."); //zeitmessung
                    System.out.println("Time elapsed: " + ((double) duration / 1000000000) + " seconds."); //zeitmessung
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
            default:
                System.out.println("Test nicht vorhanden");
        }
    }
}
