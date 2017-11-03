/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jens
 */
public class Server {

    final static int PAYLOAD = 1024;
    final static int PORTNUMBERUDP = 6542;
    final static int PORNUMBERTCP = 7000;
    static DatagramSocket datagramSocket; //UDP
    private static Items items;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        items = new Items();
        
        try {
            datagramSocket = new DatagramSocket(PORTNUMBERUDP);
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {

            String incomingMessage = receiveMessage();
            handleMessage(incomingMessage, items);
           // System.out.println(incomingMessage);
            System.out.println(items.getMilk().lastElement());
        }
    }

    /**
     * @return String Empfängt UDP-Nachricht und gibt diese als String zurück
     */
    private static String receiveMessage() {

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
     *
     * Server kontrolliert ob Item genommen oder hinzugefügt wurde
     */
    private static void handleMessage(String s, Items items) {

        s=s.trim(); //Comparison fails in ItemToAlter if not trimmed
        if (s.startsWith("-")) {
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
}
