/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package organizeoffers;

import java.util.ArrayList;

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

        for (Offer o : offers) {

            System.out.println(
                    "Angebot: "
                    + o.getProducer()
                    + ": "
                    + o.getArtikel()
                    + ": "
                    + o.getPreis()
                    + ": "
                    + o.getAnzahl());
        }
    }
}
