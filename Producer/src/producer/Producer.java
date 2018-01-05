/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producer;

import mqtt.Publisher;
import mqtt.CliProcessor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import mqtt.CliParameters;
import mqtt.MessageParser;

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

    public static void main(String[] args) {
        // Parse the command line.
        CliProcessor.getInstance().parseCliOptions(args);
        

        
        //PRODUCER needs to subscribed to a reiceive_order topic
         Subscriber subscriber = new Subscriber(CliParameters.getInstance().getProducer()+constants.Constants.TOPIC_RECEIVE_ORDER);
         subscriber.run();
        
        while (true) {

            generateOffer();
            
            // Start the MQTT Publisher and send offer
            Publisher publisher = new Publisher(MessageParser.getInstance().getTopic(), messageToSend);
            publisher.run();

            try {
                Thread.sleep(constants.Constants.PERIDOIC_UPDATE);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Function which generates the offer and stores it in the String messageToSend
     * generates An article with price and number to offer
     * article available: Milch, Yoghurt, Butter, Wurst, Schokolade
     * number to offer between 1 and 50 
     * price between 0.01 and 3 EURO
     */
    private static String generateOffer() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 4 + 1); //Zufall 0-4
        int Anzahl = ThreadLocalRandom.current().nextInt(1, 50 + 1); //Zufall 1-50
        double p = ThreadLocalRandom.current().nextDouble(0.01,3);
        double price = Math.round(p * 100) / 100.0; //2 Nachkommastellen
        String Artikel = "";
        switch (randomNum) {
            case 0:
                Artikel = constants.Constants.MILCH;
                break;
            case 1:
                Artikel = constants.Constants.BUTTER;
                break;
            case 2:
                Artikel = constants.Constants.YOGHURT;
                break;
            case 3:
                Artikel = constants.Constants.WURST;
                break;
            case 4:
                Artikel = constants.Constants.SCHOKOLADE;
                break;
            default:

        }
        //create Offer and Topic is set within makeOffermessage
       return messageToSend
                = MessageParser.getInstance().makeOffermessage(
                        CliParameters.getInstance().getProducer(),
                         Artikel,
                         price,
                         Anzahl);

    }
}
