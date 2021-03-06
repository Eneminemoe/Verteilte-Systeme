/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package store;

import java.util.logging.Level;
import java.util.logging.Logger;
import organizeoffers.Offer;
import organizeoffers.Offers;
import mqtt.Publisher;

/**
 *
 * @author Jens
 */
public class Stock {

    private static Stock instance;

    //public static final int MAXNUMBEROFEACHITEM = 2000;
    //Anzahl lagernder Artikel
    private int sum = 1250;
    private int milk = 250;
    private int yoghurt = 250;
    private int sausage = 250;
    private int butter = 250;
    private int chocolate = 250;

    public static Stock getInstance() {
        if (instance == null) {
            instance = new Stock();
        }
        return instance;
    }

    //Prevent Instantiation
    private Stock() {
    }

    /**
     * Updates the item
     *
     * @param item to update
     * @param number
     * @return bool if succesfull
     */
    public int updateStock(String item, int number) {

        switch (item) {
            case constants.Constants.BUTTER:
                butter += number;
                return butter;
            case constants.Constants.MILCH:
                milk += number;
                return milk;
            case constants.Constants.SCHOKOLADE:
                chocolate += number;
                return chocolate;
            case constants.Constants.WURST:
                sausage += number;
                return sausage;
            case constants.Constants.YOGHURT:
                yoghurt += number;
                return yoghurt;
            default:
                return 0;
        }
    }

    /**
     * @return the sum
     */
    public int getSum() {
        return sum;
    }

    /**
     * Calculate the sum
     */
    public void CalculateSum() {
        this.sum = milk + butter + yoghurt + chocolate + sausage;
    }

    /**
     * @return the milk
     */
    public int getMilk() {
        return milk;
    }

    /**
     * @param milk the milk to set
     */
    public void setMilk(int milk) {
        this.milk += milk;
    }

    /**
     * @return the yoghurt
     */
    public int getYoghurt() {
        return yoghurt;
    }

    /**
     * @param yoghurt the yoghurt to set
     */
    public void setYoghurt(int yoghurt) {
        this.yoghurt += yoghurt;
    }

    /**
     * @return the sausage
     */
    public int getSausage() {
        return sausage;
    }

    /**
     * @param sausage the sausage to set
     */
    public void setSausage(int sausage) {
        this.sausage += sausage;
    }

    /**
     * @return the butter
     */
    public int getButter() {
        return butter;
    }

    /**
     * @param butter the butter to set
     */
    public void setButter(int butter) {
        this.butter += butter;
    }

    /**
     * @return the chocolate
     */
    public int getChocolate() {
        return chocolate;
    }

    /**
     * @param chocolate the chocolate to set
     */
    public void setChocolate(int chocolate) {
        this.chocolate += chocolate;
    }

    /**
     * Function to run through all offers and decide to order items if fitting
     * offer
     *
     * @param offers Object Offers with every offer via MQTT
     */
    public void checkStockandOrder(Offers offers) {

        Offer offer = null;
        for (constants.Constants.Items i : constants.Constants.Items.values()) {

            switch (i) {

                case Butter:
                    offer = offers.getBestOfferFor(constants.Constants.BUTTER);
                    if (offer != null) {
                        order(offer);
                    }
                    break;
                case Milch:
                    offer = offers.getBestOfferFor(constants.Constants.MILCH);
                    if (offer != null) {
                        order(offer);
                    }
                    break;
                case Schokolade:
                    offer = offers.getBestOfferFor(constants.Constants.SCHOKOLADE);
                    if (offer != null) {
                        order(offer);
                    }
                    break;
                case Wurst:
                    offer = offers.getBestOfferFor(constants.Constants.WURST);
                    if (offer != null) {
                        order(offer);
                    }
                    break;
                case Yoghurt:
                    offer = offers.getBestOfferFor(constants.Constants.YOGHURT);
                    if (offer != null) {
                        order(offer);
                    }
                    break;
                default:
            }
        }
    }

    /**
     * Function to order the best offer for one Item
     *
     * @param offers Object Offers with every offer via MQTT
     * @param item to check offer for
     * @return if order succesfull
     */
    public synchronized boolean checkStockandOrder(Offers offers, constants.Constants.Items item) {

        Offer offer = null;

        if (offers == null) {
            return false;
        }

        switch (item) {

            case Butter:
                offer = offers.getBestOfferFor(constants.Constants.BUTTER);
                if (offer != null) {
                    order(offer);
                    return true;
                }
                break;
            case Milch:
                offer = offers.getBestOfferFor(constants.Constants.MILCH);
                if (offer != null) {
                    order(offer);
                    return true;
                }
                break;
            case Schokolade:
                offer = offers.getBestOfferFor(constants.Constants.SCHOKOLADE);
                if (offer != null) {
                    order(offer);
                    return true;
                }
                break;
            case Wurst:
                offer = offers.getBestOfferFor(constants.Constants.WURST);
                if (offer != null) {
                    order(offer);
                    return true;
                }
                break;
            case Yoghurt:
                offer = offers.getBestOfferFor(constants.Constants.YOGHURT);
                if (offer != null) {
                    order(offer);
                    return true;
                }
                break;
            default:
                return false;
        }
        return false;
    }

    private void order(Offer offer) {
        // Start the MQTT Publisher and send an order

        Publisher publisher = new Publisher(offer.getTopic(), offer.orderOffer());
        publisher.run();

    }
}
