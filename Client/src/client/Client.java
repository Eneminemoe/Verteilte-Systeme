/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jens
 */
public class Client {

    final static int UDP_SERVER_PORT = 6542; //SERVERS UDP PORT FOR RECEIVING PACKETS
    final static int UDP_CLIENT_PORT = 6543; //This Clients UDP PORT FOR RECEIVING PACKETS
    final static int PAYLOAD = 1024;
    final static int TIMEOUT = 3000;
    static DatagramSocket datagramSocket;
    static String answerFromServer;

    public Client() {

    }

    /**
     * @return String Gibt empfangene Nachricht zurück Empfängt Nachricht UDP
     */
    private String receiveMessage() {

        try {
            datagramSocket = new DatagramSocket(UDP_CLIENT_PORT);
            datagramSocket.setSoTimeout(TIMEOUT);
            byte[] receivedData = new byte[PAYLOAD];
            DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
            datagramSocket.receive(receivedPacket); //Daten empfangen
            String data = new String(receivedPacket.getData()); //Empfangene Daten
            return data;
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            datagramSocket.close();
        }finally{
        datagramSocket.close();
        }
        return "Verbindung zum Server nicht möglich";
    }

    /**
     * @param message beihnhaltet Nachricht zum verschicken Sendet String per
     * UDP an den Server
     */
    public boolean sendMessage(String message) {

        byte[] sendData = new byte[PAYLOAD];
        DatagramSocket sendSocket = null;
        try {
            sendSocket = new DatagramSocket();
            InetAddress inetAddress = InetAddress.getByName("localhost");
            sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, inetAddress, UDP_SERVER_PORT);
            sendSocket.send(sendPacket);
            //Message sent  

            answerFromServer = receiveMessage();

        } catch (IOException e) {
            return false;
        } finally {
            sendSocket.close();
        }
        return true;
    }

    public String[] getAnswer() {

        String[] answer = answerFromServer.split("\\n");
        return answer;
    }
}
