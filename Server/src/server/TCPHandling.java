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
 * 
 * Übernimmt Kommunkation mittels TCP
 * 
 * //HTML Part nicht elegant, muss vielleicht geändert werden, um zukünftigen Anforderungen zu genügen
 */
public class TCPHandling extends Thread {

    static Socket connection;
    static DataOutputStream outToClient;
    static BufferedReader inFromClient;
    final static String HTTPANSWER = "HTTP/1.1 200 OK\nContent-Type: text/html\n\n";

    public TCPHandling(Socket incomingConnection) {

        connection = incomingConnection;

        try {
            inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            outToClient = new DataOutputStream(connection.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(TCPHandling.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {

        String message = receiveMessageTCP();
        if (message.contains("GET")
                && (message.contains("HTTP") || message.contains("HTTPS"))
                && message.contains("index.html")) {

            sendMessageTCP(HTTPANSWER + Server.getItems().currentItems().replace("\n", "<br/>"));

        }
        try {
            inFromClient.close();
            outToClient.close();
        } catch (IOException ex) {
            Logger.getLogger(TCPHandling.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @return String
     * 
     * 
     */
    private static String receiveMessageTCP() {

        String receivedMessage = "";

        try {
            receivedMessage = inFromClient.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        return receivedMessage;
    }

    private static void sendMessageTCP(String message) {

        try {
            outToClient.writeBytes(message); //Ziemlich hässlich, funktioniert aber
        } catch (IOException ex) {
            Logger.getLogger(TCPHandling.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
