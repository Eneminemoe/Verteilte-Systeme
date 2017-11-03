/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fridge;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jens
 */
public class Fridge {

    final static int PAYLOAD = 1024;
    final static int UDP_SERVER_PORT = 6542;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Hier kann man nun Sensoren simulieren, um Gegenstände rauszunehmen und einzufügen
        while (true) {

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ex) {
                Logger.getLogger(Fridge.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            takeItemOut("Milch");
        }
    }

    /**
     * Sendet String per UDP an den Server
     */
    private static void sendMessage(String message) {

        byte[] sendData = new byte[PAYLOAD];
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
            InetAddress inetAddress = InetAddress.getByName("localhost");
            sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, inetAddress, UDP_SERVER_PORT);
            datagramSocket.send(sendPacket);

        } catch (IOException e) {
        } finally {
            datagramSocket.close();
        }

    }

    /**
     * @param type entspricht dem Item Hängt ein Minus vor den String zur
     * Signaliesirung, dass ein Artikel rausgenommen wurde
     *
     */
    private static void takeItemOut(String type) {
        sendMessage("-" + type);
    }

    /**
     * @param type entspricht Item Hängt ein Plus vor den String zur
     * Signaliesierung, dass ein Artikel hinzugefügt wurde
     *
     */
    private static void putItemIn(String type) {
        sendMessage("+" + type);
    }
}
