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
        String messageToSend;
        while (true) {
            
            int randomNum = ThreadLocalRandom.current().nextInt(0, 4 + 1); //Zufall 0-4
            int Anzahl = ThreadLocalRandom.current().nextInt(1, 50 + 1); //Zufall 0-4
            double price = ThreadLocalRandom.current().nextDouble(3);
            double p = Math.round(price * 100) / 100.0; //2 Nachkommastellen
            String Artikel="";
            switch (randomNum) {
                case 0:
                    Artikel="Milch";
                    break;
                case 1:
                    Artikel="Butter";
                    break;
                case 2:
                    Artikel="Yoghurt";
                    break;
                case 3:
                    Artikel="Wurst";
                    break;
                case 4:
                    Artikel="Schokolade";
                    break;
                default:

            }
            //OFFER
            messageToSend=CliParameters.getInstance().getProducer()
                            +":"
                            +Artikel
                            +":"
                            +Double.toString(p)
                            +":"
                            +Anzahl;

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
}
