/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producer;

import mqtt.Publisher;
import mqtt.CliProcessor;
import mqtt.Constants;
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
         Subscriber subscriber = new Subscriber(CliParameters.getInstance().getProducer()+Constants.TOPIC_RECEIVE_ORDER);
         subscriber.run();
        
        while (true) {

            generateOffer();
            
            // Start the MQTT Publisher.
            Publisher publisher = new Publisher(Constants.TOPIC_MARKETPLACE, messageToSend);
            publisher.run();

            try {
                Thread.sleep(Constants.PERIDOIC_UPDATE);
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
    private static void generateOffer() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 4 + 1); //Zufall 0-4
        int Anzahl = ThreadLocalRandom.current().nextInt(1, 50 + 1); //Zufall 1-50
        double p = ThreadLocalRandom.current().nextDouble(0.01,3);
        double price = Math.round(p * 100) / 100.0; //2 Nachkommastellen
        String Artikel = "";
        switch (randomNum) {
            case 0:
                Artikel = "Milch";
                break;
            case 1:
                Artikel = "Butter";
                break;
            case 2:
                Artikel = "Yoghurt";
                break;
            case 3:
                Artikel = "Wurst";
                break;
            case 4:
                Artikel = "Schokolade";
                break;
            default:

        }
        //OFFER
        messageToSend
                = MessageParser.getInstance().makeOffermessage(
                        CliParameters.getInstance().getProducer(),
                         Artikel,
                         price,
                         Anzahl);

    }
}
