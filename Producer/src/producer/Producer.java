/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producer;

/**
 *
 * @author Jens
 * 
 * Lieferant der Produkte  
 */
public class Producer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         // Parse the command line.
        CliProcessor.getInstance().parseCliOptions(args);

        // Start the MQTT subscriber.
        Publisher publisher = new Publisher();
        publisher.run();
    }
    
}
