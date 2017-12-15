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
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import p3.StoreService;

/**
 *
 * @author Jens
 *
 * Server, der Kommunikation mittels UDP und TCP realisiert UDP wird in der main
 * behandelt und TCP im Thread tcp
 *
 */
public class Server extends Thread {

    final static int PAYLOAD = 1024;
    final static int UDP_SERVER_PORT = 6542;
    final static int UDP_CLIENT_PORT = 6543;
    final static int TCP_LISTENING_SOCKET = 6544;
    //THRIFT CONSTANTS
    final static String ORDER = "9";
    final static String HOST = "localhost";
    final static int THRIFTPORT = 9090;
    static DatagramSocket datagramSocket; //UDP
    static ServerSocket welcomeSocket; //TCP-LISTENING
    static Server tcp;
    private static Items items; //beinhaltet den aktuellen Stand der Artikel
    private static HTMLMaker htmlmaker;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        items = new Items();
        htmlmaker = new HTMLMaker(items.getCurrentItemsArray());

        try {
            datagramSocket = new DatagramSocket(UDP_SERVER_PORT);
            welcomeSocket = new ServerSocket(TCP_LISTENING_SOCKET);
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        tcp = new Server();
        tcp.start();

        while (true) {

            String incomingMessage = receiveMessageUDP(); //receiveMessageUDP blockiert, bis Nachricht eintrifft
            handleMessage(incomingMessage, getItems());
        }
    }

    /**
     * Bearbeitet eingehende TCP-Anfragen
     */
    @Override
    public void run() {

        //testFunction(1); //testet die Dauer einer Verbindung
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

        byte[] receivedData = new byte[PAYLOAD];
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

        byte[] sendData = new byte[PAYLOAD];
        DatagramSocket sendSocket = null;
        try {
            sendSocket = new DatagramSocket();
            InetAddress inetAddress = InetAddress.getByName("localhost");
            sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, inetAddress, UDP_CLIENT_PORT);
            sendSocket.send(sendPacket);

        } catch (IOException e) {
        } finally {
            sendSocket.close();
        }

    }

    /**
     * Bestellt Items über die Schnittstelle
     */
    private static String orderItem(StoreService.Client client, String item) throws TException {

        return client.order(ORDER + item);
    }

    /**
     * Baut Verbinung zum Store auf via THRIFT Schnittstelle
     */
    private static String establishThriftConnection(String item) {

        String answer = "no answer received";
        try {
            TTransport transport;

            transport = new TSocket(HOST, THRIFTPORT);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            StoreService.Client client = new StoreService.Client(protocol);

            answer = orderItem(client, item);

            transport.close();
        } catch (TException x) {
            System.out.println("server.Server.establishThriftConnection()" + "  " + x);
        }
        return answer;
    }

    /**
     * Kontrolliert ob Item genommen oder hinzugefügt wurde und verarbeitet
     * UDP-Nachrichten
     */
    private static void handleMessage(String s, Items items) {

        s = s.trim(); //Comparison fails in ItemToAlter if not trimmed

        if (s.equals("updateItems")) {
            sendMessageUDP(items.currentItems());
        } else if (s.startsWith("-")) {
            s = s.substring(1);
            s = s.toLowerCase();
            if (items.takeItemOut(items.ItemToAlter(s)) < 3) { // Wenn weniger als 2 eines Artikels vorhanden, nachbestellen
                //in THREAD auslagern?
                items.changeItems(establishThriftConnection(s)); 
            }
            htmlmaker.setItems(items.getCurrentItemsArray());

        } else if (s.startsWith("+")) {
            s = s.substring(1);
            s = s.toLowerCase();
            items.putItemIn(items.ItemToAlter(s));
            htmlmaker.setItems(items.getCurrentItemsArray());
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
                    Socket connectionSocket = welcomeSocket.accept();
                    startTime = System.nanoTime();
                    TCPHandling newConnection = new TCPHandling(connectionSocket);
                    newConnection.start();
                    while (newConnection.isAlive()) {
                    }
                    endTime = System.nanoTime();
                    duration = endTime - startTime;
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
