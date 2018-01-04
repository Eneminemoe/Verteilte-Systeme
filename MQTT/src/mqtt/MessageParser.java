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
 * @author Jens Class to create and read messages made by MQTT
 *
 */
public class MessageParser {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageParser.class);
    /**
     * The one and only instance of MessageParser.
     */
    private static MessageParser instance;
    private String Producer;
    private String Artikel;
    private float Preis;
    private int Anzahl;
    private String offer_message;
    private String order_message;
    private String confirmation_message;
    private Constants.messagetype messagetype;

    public static MessageParser getInstance() {
        if (instance == null) {
            instance = new MessageParser();
        }
        return instance;
    }

    /**
     * Parses the given String
     *
     * @param message in form of "OFFER:producer:artikel:price:number"
     * or "ORDER:artikel:price:number"
     * or "CONFIRMATION:Order_received_and_sent"
     */
    public void parseMessage(String message) {

        message = message.trim();
        String[] words = {"", ""};

        if (message.contains(":")) {
            words = message.split(":");
            switch (words[0]) {
                case "OFFER":
                    this.messagetype = Constants.messagetype.OFFER;
                    break;
                case "ORDER":
                    this.messagetype = Constants.messagetype.ORDER;
                    break;
                case "CONFIRMATION":
                    this.messagetype = Constants.messagetype.CONFIRMATION;
                    break;
                default:
            }
        }

        switch (messagetype) {
            case OFFER:
                this.offer_message = message;
                Producer = words[1];  // intended to start at 1 -> [0] = "OFFER" / "ORDER"
                Artikel = words[2];
                Preis = Float.valueOf(words[3]);
                Anzahl = Integer.valueOf(words[4]);
                break;
            case ORDER:
                this.order_message = message;
                Artikel = words[1]; // intended to start at 1 -> [0] = "OFFER" / "ORDER"
                Preis = Float.valueOf(words[2]);
                Anzahl = Integer.valueOf(words[3]);
                break;
            case CONFIRMATION:
                this.confirmation_message = message;
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

    /**
     * @return the offer_message
     */
    public String getOffer_message() {
        return offer_message;
    }

    /**
     * @return the order_message
     */
    public String getOrder_message() {
        return order_message;
    }

    /**
     * @return the confirmation_message
     */
    public String getConfirmation_message() {
        return confirmation_message;
    }

    /**
     * Function that retunrs a String in form of:
     * "OFFER:producer:artikel:price:number"
     *
     * @param producer The producer
     * @param artikel the artikel to offer
     * @param price the price for the offer in EURO
     * @param number the numnber to offer
     * @return String "OFFER:producer:artikel:price:number"
     */
    public String makeOffermessage(String producer, String artikel, double price, int number) {
        return "OFFER:"
                + producer
                + ":"
                + artikel
                + ":"
                + Double.toString(price)
                + ":"
                + Integer.toString(number);
    }

    /**
     * Function that retunrs a String in form of: "ORDER:artikel:price:number"
     *
     * @param artikel the artikel from the offer
     * @param price the price from the offer in EURO
     * @param number the numnber to offer
     * @return String "ORDER:artikel:price:number"
     */
    public String makeOrder_message(String artikel, double price, int number) {
        return "ORDER"
                + artikel
                + ":"
                + Double.toString(price)
                + ":"
                + Integer.toString(number);
    }

    /**
     *
     * @return String "CONFIRMATION:Order_received_and_sent"
     */
    public String makeConfirmation_message() {
        return "CONFIRMATION:Order_received_and_sent";
    }

    /**
     * @return the messagetype
     */
    public Constants.messagetype getMessagetype() {
        return messagetype;
    }
}
