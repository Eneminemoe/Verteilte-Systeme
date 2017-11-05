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

/**
 *
 * @author Jens
 */
public class Server extends Thread {

    final static int PAYLOAD = 1024;
    final static int UDP_SERVER_PORT = 6542;
    final static int UDP_CLIENT_PORT = 6543;
    final static int TCP_LISTENING_SOCKET = 6544;
    static DatagramSocket datagramSocket; //UDP
    static ServerSocket welcomeSocket; //TCP-LISTENING
    static Server tcp;
    private static Items items;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        items = new Items();

        try {
            datagramSocket = new DatagramSocket(UDP_SERVER_PORT);
            welcomeSocket= new ServerSocket(TCP_LISTENING_SOCKET);
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tcp = new Server();
        tcp.start();

        while (true) {

            String incomingMessage = receiveMessageUDP(); //receive blocks, until data is reiceved 
            handleMessage(incomingMessage, getItems());
        }
    }


    public void run(){
    
        
        while(true){
            
        try {
                Socket connectionSocket = welcomeSocket.accept(); //accept blocks, until connection establishes
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
     *
     * Server kontrolliert ob Item genommen oder hinzugefügt wurde
     */
    private static void handleMessage(String s, Items items) {

        s = s.trim(); //Comparison fails in ItemToAlter if not trimmed

        if (s.equals("updateItems")) {
            sendMessageUDP(items.currentItems());
        } else if (s.startsWith("-")) {
            s = s.substring(1);
            s = s.toLowerCase();
            items.takeItemOut(items.ItemToAlter(s));

        } else if (s.startsWith("+")) {
            s = s.substring(1);
            s = s.toLowerCase();
            items.putItemIn(items.ItemToAlter(s));
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
}
