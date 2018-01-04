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
public class TCPHandling extends Server {

    static Socket connection;
    static DataOutputStream outToClient = null;
    static BufferedReader inFromClient = null;
    static FileInputStream fis = null;
    static BufferedInputStream bis = null;
    final static String HTTPANSWER = "HTTP/1.1 200 OK\nContent-Type: text/html\n\n";
    final String FAVICON = "favicon.ico";
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

        if (message.contains(FAVICON)) {
        } //favicon requests ignore
        else if (message.contains("GET")
                && (message.contains("HTTP") || message.contains("HTTPS"))
                && message.contains("index.html")) {

            sendMessageTCP(HTTPANSWER);

            if (message.contains("request") && message.contains("invoice")) {
                sendMessageTCP(ThriftHandler.establishThriftConnection("", "0", 2));
            } else if (message.contains("refresh=Aktualisieren")) {
                htmlmaker.setItems(Server.items.getCurrentItemsArray());
                sendFileTCP("index.html");
            } else if (message.contains("=nachbestellen")) {

                message = message.substring(message.indexOf("?") + 1, message.indexOf("="));
                System.out.println("Artikel nachbestellen: " + message);
                Server.orderItems(message);
                sendFileTCP("index.html");
            } else {
                //sonst normale index.html
                sendFileTCP("index.html");
            }

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
     *
     *
     */
    private static String receiveMessageTCP() {

        String receivedMessage = "";
        String tmp;
        boolean done = false;

        try {
            while (!done) {

                tmp = inFromClient.readLine();
                receivedMessage += tmp;
                if (tmp.equals("")) {
                    done = true;
                }
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
