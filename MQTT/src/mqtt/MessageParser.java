/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jens
 */
public class MessageParser {

        /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageParser.class);
    /**
     * The one and only instance of MessageParser.
     */
    private static MessageParser instance;

    private String Producer;
    private String Artikel;
    private float Preis;
    private int Anzahl;

    public static MessageParser getInstance() {
        if (instance == null) {
            instance = new MessageParser();
        }
        return instance;
    }

    /**
     * Parses the given String
     *
     * @param messagetype 
     * @param message in form of PRODUCER:ARTIKEL:PREIS:ANZAHL
     */
    public void parseMessage(Constants.messagetype messagetype, String message) {

        LOGGER.info(message);
        
        String[] words;
        words = message.split(":");
        switch (messagetype) {
            case OFFER:
            case ORDER:
                Producer = words[0];
                Artikel = words[1];
                Preis = Float.valueOf(words[2]);
                Anzahl = Integer.valueOf(words[3]);
                break;
            case CONFIRMATION:
                break;
            default:;
        }
        
    }

    /**
     * @return the Producer
     */
    public String getProducer() {
        return Producer;
    }

    /**
     * @return the Artikel
     */
    public String getArtikel() {
        return Artikel;
    }

    /**
     * @return the Preis
     */
    public float getPreis() {
        return Preis;
    }

    /**
     * @return the Anzahl
     */
    public int getAnzahl() {
        return Anzahl;
    }

    private MessageParser() {
    }
}
