/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package organizeoffers;

import constants.Constants;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import mqtt.CliParameters;
import mqtt.MessageParser;

/**
 *
 * @author Jens
 */
public class Offers {

    //One and only instance of Offers
    private static Offers instance;
    private ArrayList<Offer> offers = new ArrayList<>();
    private ArrayList<String> producers = new ArrayList<>();

    public static Offers getInstance() {
        if (instance == null) {
            instance = new Offers();
        }
        return instance;
    }

    /**
     * This function adds a new Offer and checks if offer is from new Producer
     * If so the new Producer will be added
     *
     * @param offer The new offer to add
     * @return boolean if adding was succesfull or not
     */
    public boolean addOffer(Offer offer) {

        if (!checkIfProducerAlreadyExists(offer)) {
            addProducer(offer);
        }
        return offers.add(offer);
    }

    /**
     * Function to determine the best available offer
     *
     * @param item Item to ckeck offer for
     * @return the offer
     */
    public Offer getBestOfferFor(String item) {

        if (offers.isEmpty()) {
            return null;
        }
        Offer tmp = null;
        for (Offer offer : offers) {
            if (item.equals(offer.getArtikel())) {
                if (tmp == null) {
                    tmp = offer;
                }
                if (offer.getPreis() < tmp.getPreis()) {
                    tmp = offer;
                }
            }
        }
        return tmp;
    }

    /**
     * @return the producers
     */
    public ArrayList<String> getProducers() {
        return producers;
    }

    /**
     * Adds a producer to the List
     */
    private void addProducer(Offer offer) {

        producers.add(offer.getProducer());

    }

    /**
     * Checks if the Offer is from a new Producer.
     */
    private boolean checkIfProducerAlreadyExists(Offer offer) {

        String newOfferProducer = offer.getProducer();
        for (String existingProducer : producers) {
            if (newOfferProducer.equals(existingProducer)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes Offer if it is equal to an existing
     *
     * @param offer The offer to check
     * @return true if existing
     */
    public boolean deleteOfferIfEqualTo(Offer offer) {

        for (int i = 0; i < offers.size(); i++) {
            if (offers.get(i).equals(offer)) {
                offers.remove(i);
                return true;
            }
        }

        return false;
    }

    /**
     * Display all current Offers
     */
    public void showCurrentOffers() {

        offers.forEach((o) -> {
            System.out.println(
                    "Angebot: "
                            + o.getProducer()
                            + ": "
                            + o.getArtikel()
                            + ": "
                            + o.getPreis()
                            + ": "
                            + o.getAnzahl());
        });
    }

    /**
     * Function which generates an offer and stores it.
     *
     * @param item to offer
     * @param number to offer
     * @param price of offer
     * @return String with offer made by MESSAGEPARSER.makeOffermessage
     */
    public String generateOffer(constants.Constants.Items item,
            int number,
             double price
    ) {

        
        int Anzahl = number;
        double p = price;
        double price_rounded = Math.round(p * 100) / 100.0; //2 Nachkommastellen
        String Artikel = "";
        switch (item) {
            case Milch:
                Artikel = constants.Constants.MILCH;
                break;
            case Butter:
                Artikel = constants.Constants.BUTTER;
                break;
            case Yoghurt:
                Artikel = constants.Constants.YOGHURT;
                break;
            case Wurst:
                Artikel = constants.Constants.WURST;
                break;
            case Schokolade:
                Artikel = constants.Constants.SCHOKOLADE;
                break;
            default:

        }
        //Angebot merken
        addOffer(new Offer(CliParameters.getInstance().getProducer(), Artikel, price_rounded, Anzahl));

        //create Offer and Topic is set within makeOffermessage
        return MessageParser.getInstance().makeOffermessage(
                CliParameters.getInstance().getProducer(),
                Artikel,
                price_rounded,
                Anzahl);

    }
    
    /**
     * Function which generates an offer and stores it. available: Milch,
     * Yoghurt, Butter, Wurst, Schokolade
     *
     * @return String with generated offer made by MESSAGEPARSER.makeOffermessage
     */
    public String generateOffer() {

        Constants.RandomEnum<Constants.Items> i = new Constants.RandomEnum<>(Constants.Items.class);
        
        
        int Anzahl = ThreadLocalRandom.current().nextInt(1, 50 + 1); //Zufall 1-50
        double p = ThreadLocalRandom.current().nextDouble(0.01, 3);
        double price_rounded = Math.round(p * 100) / 100.0; //2 Nachkommastellen
        String Artikel = "";
        switch (i.random()) {
            case Milch:
                Artikel = constants.Constants.MILCH;
                break;
            case Butter:
                Artikel = constants.Constants.BUTTER;
                break;
            case Yoghurt:
                Artikel = constants.Constants.YOGHURT;
                break;
            case Wurst:
                Artikel = constants.Constants.WURST;
                break;
            case Schokolade:
                Artikel = constants.Constants.SCHOKOLADE;
                break;
            default:

        }
        //Angebot merken
        addOffer(new Offer(CliParameters.getInstance().getProducer(), Artikel, price_rounded, Anzahl));

        //create Offer and Topic is set within makeOffermessage
        return MessageParser.getInstance().makeOffermessage(
                CliParameters.getInstance().getProducer(),
                Artikel,
                price_rounded,
                Anzahl);

    }
    
    public String getLastOffer(){
    
    return offers.get(offers.size()-1).getArtikel()+":"+offers.get(offers.size()-1).getAnzahl()+":"+offers.get(offers.size()-1).getPreis();
    }
}
