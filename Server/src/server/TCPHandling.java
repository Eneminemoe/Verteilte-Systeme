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
 * //HTML Part nicht elegant, muss vielleicht geändert werden, um zukünftigen
 * Anforderungen zu genügen
 */
public class TCPHandling extends Thread {

    static Socket connection;
    static DataOutputStream outToClient = null;
    static BufferedReader inFromClient = null;
    static FileInputStream fis = null;
    static BufferedInputStream bis = null;
    final static String HTTPANSWER = "HTTP/1.1 200 OK\nContent-Type: text/html\n\n";
    File htmlfile;

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
        System.out.println(message);
        
        if(message.contains("request"))System.out.println("Button gedrückt");
        
        if (message.contains("GET")
                && (message.contains("HTTP") || message.contains("HTTPS"))
                && message.contains("index.html")) {

            sendMessageTCP(HTTPANSWER);
            
            if(message.contains("request=receipt")){
            //Rechnung schicken
            }else{
                //sonst normale index.html
                sendFileTCP("index.html");}
            
        }
        try {
            fis.close();
            bis.close();
            inFromClient.close();
            outToClient.close();
        } catch (IOException ex) {
            Logger.getLogger(TCPHandling.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @return String
     * receives Message per TCP an returns the value of it
     *
     */
    private static String receiveMessageTCP() {

        String receivedMessage = "";
        String tmp;
        boolean done=false; 

        try {
            while(!done){
            
                tmp= inFromClient.readLine();
                receivedMessage = receivedMessage+ tmp;
                if(tmp.equals(""))done=true; //End of Message -> done
            }
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

    private void sendFileTCP(String path) {

        try {
            htmlfile = new File(path);
            byte[] filecontent = new byte[(int) htmlfile.length()];
            fis = new FileInputStream(htmlfile);
            bis = new BufferedInputStream(fis);
            bis.read(filecontent, 0, filecontent.length);
            outToClient.write(filecontent);
        } catch (IOException ex) {
            Logger.getLogger(TCPHandling.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
