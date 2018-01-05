/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt;


/**
 *
 * @author Jens Class to create and read messages made by MQTT
 *
 */
public class MessageParser {

    /**
     * The one and only instance of MessageParser.
     */
    private static MessageParser instance;
    private String Producer;
    private String Store;
    private String Artikel;
    private double Preis;
    private int Anzahl;
    /**
     * Topic to publish to can be:
     * for Offer: TOPIC_MARKETPLACE
     * for Order: producer + TOPIC_TOPIC_RECEIVE_ORDER
     * for Confirmation: store+TOPIC_CONFIRMATION 
     * 
     */
    private String Topic;
    private String offer_message;
    private String order_message;
    private String confirmation_message;
    private constants.Constants.messagetype messagetype;

    public static MessageParser getInstance() {
        if (instance == null) {
            instance = new MessageParser();
        }
        return instance;
    }

    /**
     * Parses the given String
     *
     * @param message in form of "OFFER:producer:artikel:price:number" or
     * "ORDER:artikel:price:number" or "CONFIRMATION:Order_received_and_sent"
     */
    public void parseMessage(String message) {

        message = message.trim();
        String[] words = {"", ""};
        double p;

        if (message.contains(":")) {
            words = message.split(":");
            switch (words[0]) {
                case "OFFER":
                    this.messagetype = constants.Constants.messagetype.OFFER;
                    break;
                case "ORDER":
                    this.messagetype = constants.Constants.messagetype.ORDER;
                    break;
                case "CONFIRMATION":
                    this.messagetype = constants.Constants.messagetype.CONFIRMATION;
                    break;
                default:
            }
        }

        switch (messagetype) {
            case OFFER:
                this.offer_message = message;
                Producer = words[1];  // intended to start at 1 -> [0] = "OFFER" / "ORDER"
                Artikel = words[2];
                p = Float.valueOf(words[3]);
                Preis = Math.round(p * 100) / 100.0; //2 Nachkommastellen
                Anzahl = Integer.valueOf(words[4]);
                break;
            case ORDER:
                this.order_message = message;
                Store = words[1]; // intended to start at 1 -> [0] = "OFFER" / "ORDER"
                Artikel = words[2];
                p = Double.valueOf(words[3]);
                Preis = Math.round(p * 100) / 100.0; //2 Nachkommastellen
                Anzahl = Integer.valueOf(words[4]);
                break;
            case CONFIRMATION:
                this.confirmation_message = message;
                Producer = words[1];
                Store = words[2]; // intended to start at 1 -> [0] = "OFFER" / "ORDER"
                Artikel = words[3];
                p = Double.valueOf(words[4]);
                Preis= Math.round(p * 100) / 100.0; //2 Nachkommastellen
                Anzahl = Integer.valueOf(words[5]);
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
    public double getPreis() {
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
     * Function that returns a String in form of:
     * "OFFER:producer:artikel:price:number"
     *
     * @param producer The producer
     * @param artikel the artikel to offer
     * @param price the price for the offer in EURO
     * @param number the numnber to offer
     * @return String "OFFER:producer:artikel:price:number"
     */
    public String makeOffermessage(String producer, String artikel, double price, int number) {
        Topic=constants.Constants.TOPIC_MARKETPLACE;
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
     * Function that retunrs a String in form of:
     * "ORDER:store:artikel:price:number"
     *
     * @param store The store which orders
     * @param artikel the artikel from the offer
     * @param price the price from the offer in EURO
     * @param number the numnber to offer
     * @return String "ORDER:artikel:price:number"
     */
    public String makeOrder_message(String store, String artikel, double price, int number) {
        Topic=Producer+constants.Constants.TOPIC_RECEIVE_ORDER;
        return "ORDER:"
                + store
                + ":"
                + artikel
                + ":"
                + Double.toString(price)
                + ":"
                + Integer.toString(number);
    }

    /**
     * Function that retunrs a String in form of:
     * "ORDER:producer:store:artikel:price:number"
     *
     * @param producer
     * @param store
     * @param artikel
     * @param price
     * @param number
     * @return String "CONFIRMATION:producer:store:artikel:price:number"
     */
    public String makeConfirmation_message(String producer, String store, String artikel, double price, int number) {
        Topic=Store+constants.Constants.TOPIC_CONFIRMATION;
        return "CONFIRMATION:"
                + producer
                + ":"
                + store
                + ":"
                + artikel
                + ":"
                + Double.toString(price)
                + ":"
                + Integer.toString(number);
    }

    /**
     * @return the messagetype
     */
    public constants.Constants.messagetype getMessagetype() {
        return messagetype;
    }

    /**
     * @return the Store
     */
    public String getStore() {
        return Store;
    }

    /**
     * @return the Topic
     */
    public String getTopic() {
        return Topic;
    }
}
