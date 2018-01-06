/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producer;

import mqtt.Publisher;
import mqtt.CliProcessor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import mqtt.CliParameters;
import mqtt.MessageParser;
import organizeoffers.Offers;

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
    private static String messageToSend;
    private static Offers offers;
    
    public static void main(String[] args) {
        
        offers = Offers.getInstance();

        // Parse the command line.
        CliProcessor.getInstance().parseCliOptions(args);

        //PRODUCER needs to subscribed to a reiceive_order topic
        Subscriber subscriber = new Subscriber(CliParameters.getInstance().getProducer() + constants.Constants.TOPIC_RECEIVE_ORDER);
        subscriber.run();
        
        while (true) {
            
            messageToSend = offers.generateOffer();

            // Start the MQTT Publisher and send offer
            Publisher publisher = new Publisher(MessageParser.getInstance().getTopic(), messageToSend);
            publisher.run();
            
            waitSeconds(constants.Constants.PERIDOIC_UPDATE);
        }
        
    }

    /**
     * Time to wait in seconds, cannot be interrupted
     *
     * @param seconds time to wait
     */
    private static void waitSeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException ex) {
            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
