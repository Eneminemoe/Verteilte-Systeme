/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package organizeoffers;

import mqtt.CliParameters;
import mqtt.MessageParser;

/**
 *
 * @author Jens
 */
public class Offer {

    private final String Topic;
    private final String Producer;
    private final String Artikel;
    private final double Preis;
    private int Anzahl;

    /**
     * Constructor
     * @param producer
     * @param item
     * @param preis
     * @param number
     */
    public Offer(String producer, String item, double preis, int number) {
        this.Producer = producer;
        this.Anzahl = number;
        this.Preis = preis;
        this.Artikel = item;
        this.Topic = producer + constants.Constants.TOPIC_RECEIVE_ORDER;
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

    /**
     * @param Anzahl the Anzahl to set
     */
    public void setAnzahl(int Anzahl) {
        this.Anzahl = Anzahl;
    }

    public String orderOffer() {
        return MessageParser.getInstance().makeOrder_message(CliParameters.getInstance().getStore(), Artikel, Preis, Anzahl);
    }

    /**
     * @return the Topic
     */
    public String getTopic() {
        return Topic;
    }

    /**
     * Compare Offer-Objects
     * 
     * @param offer to compare with
     * @return true if same 
     */
    public boolean equals(Offer offer) {

        return (this.Producer == null ? offer.getProducer() == null : this.Producer.equals(offer.getProducer()))
                && this.Anzahl == offer.getAnzahl()
                && this.Preis == offer.getPreis()
                && (this.Artikel == null ? offer.getArtikel() == null : this.Artikel.equals(offer.getArtikel()));
    }

}
