/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Jens
 */
public class TestClass extends Thread {

    Socket socket;
    static int test = 0;
    static DataOutputStream outToClient = null;

    public TestClass() {     
    }

    public static void main(String[] args) {
        
        TestClass t = new TestClass();
        t.start();
        while(true);
    }

    @Override
    public void run() {

        for(int i =0; i<100 ; i++){
            
        try {
            this.socket = new Socket("localhost", 6544);
            System.out.println("Port: "+socket.getLocalSocketAddress());
        } catch (IOException ex) {
            Logger.getLogger(TCPHandling.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TCPHandling h = new TCPHandling(socket);
            System.out.println("Hier 1");
        TCPHandling.sendMessageTCP("GET HTTP index.html");
        System.out.println("Hier 2");    
        System.out.println(h.receiveMessageTCP());
            System.out.println("hier 3");
        }
        
    }

}
