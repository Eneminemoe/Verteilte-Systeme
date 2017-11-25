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
 *
 * Simuliert einen Kühlschrank mit Sensoren, der Artikel erfasst.
 * 
 * Artikel die derzeit simuliert werden können:
 * milk, yoghurt, chocolate, butter, sausage
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

            
            testFunction(1);
            putItemIn("milk");
            waitSeconds(2);

            takeItemOut("milk");
            waitSeconds(2);
        }
    }

    /**
     * @param message beihnhaltet Nachricht zum verschicken Sendet String per
     * UDP an den Server
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

    private static void waitSeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException ex) {
            Logger.getLogger(Fridge.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param int test
     *
     * test = 1: Testen, ob schnelle abwechselnde ein- und ausnahme erkannt wird
     * test = 2: Wie lange dauert das Senden einer Nachricht
     */
    private static void testFunction(int test) {
        int i = 0;
        switch (test) {
            case 1:
                while (i < 1) {
                    putItemIn("milk");
                    takeItemOut("milk");
                }
                break;
            case 2:
                long startTime,
                 endTime,
                 duration;

                startTime = System.nanoTime(); // Zeit messen
                putItemIn("milk"); // Nachricht versenden 
                endTime = System.nanoTime(); // Zeit messen
                duration = endTime - startTime;
                System.out.println("Time elapsed: " + duration + " nanoseconds."); //Zeitmessung
                System.out.println("Time elapsed: " + ((double) duration / 1000000) + " milliseconds."); //zeitmessung
                System.out.println("Time elapsed: " + ((double) duration / 1000000000) + " seconds."); //zeitmessung
                break;

            default:System.out.println("Test nicht vorhanden");;
        }
    }

}
