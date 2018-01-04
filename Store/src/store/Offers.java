/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package store;

import java.util.ArrayList;

/**
 *
 * @author Jens
 */
public class Offers {

    private ArrayList<Offer> offers = new ArrayList<>();
    private static Offers instance;

    public static Offers getInstance() {
        if (instance == null) {
            instance = new Offers();
        }
        return instance;
    }

    public boolean addOffer(Offer offer) {
        return offers.add(offer);
    }

    /**
     * Function to determine the best available offer
     *
     * @param item Item to ckeck offer for
     * @return the offer
     */
    public Offer getBestOfferFor(String item) {

        Offer tmp = offers.get(0);
        for (Offer offer : offers) {
            if (item.equals(offer.getArtikel())) {
                if (offer.getPreis() < tmp.getPreis()) {
                    tmp = offer;
                }
            }
        }
        return tmp;
    }
}
