/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package store;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import p3.StoreService;
import org.apache.thrift.TException;
import org.apache.thrift.server.*;
import org.apache.thrift.transport.*;
import mqtt.CliProcessor;
import mqtt.CliParameters;

/**
 *
 * @author Jens
 */
public class Store extends Thread implements StoreService.Iface {

    //TIMETOWAIT in seconds for peridoic ordering of items
    private static final int TIMETOWAIT = 20;
    public static Store store;
    public static StoreService.Processor processor;
    private static Invoice invoice;
    private static Stock stock_instance;
    private static Offers offers;
    private static ArrayList<String> subscribedProducers;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            store = new Store();
            invoice = new Invoice();
            processor = new StoreService.Processor(store);
            stock_instance = Stock.getInstance();
            offers = Offers.getInstance();
            subscribedProducers = new ArrayList<>();

            store.start();

            //MQTT
            CliProcessor.getInstance().parseCliOptions(args);
            System.out.println("Store: " + CliParameters.getInstance().getStore());
            Subscriber subscriber = new Subscriber(constants.Constants.TOPIC_MARKETPLACE);
            subscriber.run();

            /**
             * THRIFT
             */
            simpleServer(processor);

        } catch (Exception e) {
        }

    }

    @Override
    public void run() {
        while (true) {
            //Artikel nachbestellen
            waitSeconds(TIMETOWAIT);
            checkAndSubscribeToProducers();
            waitSeconds(2);
            stock_instance.checkStockandOrder(offers);
            waitSeconds(2);
            offers.showCurrentOffers();
        }
    }

    /**
     * Function processes order and checks Stock
     *
     * @param message Anzahl und Typ Artikel in Form von xitem
     * @return String,
     * @throws TException
     */
    @Override
    public String order(String message) throws TException {

        boolean inStock = false; // Wenn Artikel nicht vorhanden, keinen versenden

        if (Character.isDigit(message.charAt(0))) {
            int tmp = Character.getNumericValue(message.charAt(0)); //Anzahl der bestellten Artikel
            message = message.trim();
            message = message.replaceAll("[^a-zA-Z]", "");

            System.out.println("Bestellung eingegangen: " + tmp + " " + message);

            switch (message) { //schauen ob auf Lager und Lager updaten, falls versendbar

                case constants.Constants.MILCH:
                    if (stock_instance.getMilk() < tmp) {
                        inStock = false;
                        break;
                    }
                    inStock = true;
                    stock_instance.setMilk(-tmp);
                    break;
                case constants.Constants.YOGHURT:
                    if (stock_instance.getYoghurt() < tmp) {
                        inStock = false;
                        break;
                    }
                    inStock = true;
                    stock_instance.setYoghurt(-tmp);
                    break;
                case constants.Constants.WURST:
                    if (stock_instance.getSausage() < tmp) {
                        inStock = false;
                        break;
                    }
                    inStock = true;
                    stock_instance.setSausage(-tmp);
                    break;
                case constants.Constants.BUTTER:
                    if (stock_instance.getButter() < tmp) {
                        inStock = false;
                        break;
                    }
                    inStock = true;
                    stock_instance.setButter(-tmp);
                    break;
                case constants.Constants.SCHOKOLADE:
                    if (stock_instance.getChocolate() < tmp) {
                        inStock = false;
                        break;
                    }
                    inStock = true;
                    stock_instance.setChocolate(-tmp);
                    break;
                default:
                    System.out.println("Artikel nicht vorhanden: " + message);
                    return "Gesendete Nachricht: " + message + " Artikel nicht vorhanden";
            }

            if (inStock) {
                makeInvoice(message, tmp);
                System.out.println("Bestellung versendet: " + tmp + " " + message);
                return tmp + " " + message + " bestellt.";
            } else {
                //Bestellung nicht verabeitbar, weil nicht vorrätig
                System.out.println("Artikel derzeit nicht vorhanden. Kann nicht versendet werden");
                return "Artikel derzeit nicht vorhanden.";
            }

        } else {
            System.out.println("Nachricht konnte nicht verarbeitet werden.");
            System.out.print("Gesendete Nachricht: ");
            System.out.print(message);
            return "Gesendete Nachricht: " + message + "Nachricht konnte nicht verarbeitet werden";
        }

    }

    /**
     *
     * @param message Fehler in THRIFT, message nicht benötigt
     * @return String mit Rechnung
     * @throws TException
     */
    @Override
    public String invoice(String message) throws TException {

        System.out.println("Rechnung angefordert.");
        return invoice.getOrder();
    }

    /**
     * Rechnung erstellen
     *
     * @param order bestellter Artikel
     * @param number Anzahl des Artikels
     */
    private void makeInvoice(String order, int number) {

        invoice.setOrder(number, order);
    }

    /**
     * THRIFT Connection
     *
     * @param processor
     */
    public static void simpleServer(StoreService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(constants.Constants.THRIFTPORT);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

            System.out.println("Starting the Store server...");
            server.serve();
        } catch (TTransportException e) {
        }
    }

    /**
     * Time to wait in seconds, cannot be interrupted
     */
    private static void waitSeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException ex) {
            Logger.getLogger(Store.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Kontrolliert, ob bei allen Producern auf CONFIRMATION_Topic subscribed
     * Wenn nicht, wird beim jeweiligen subscribed und der Liste hinzugefügt
     */
    private static void checkAndSubscribeToProducers() {
        boolean alreadySubscribed;

        for (String producer : offers.getProducers()) {
            alreadySubscribed = false; //Zurücksetzen für nächste Iteration
            for (String subscribedProducer : subscribedProducers) {
                if (producer.equals(subscribedProducer)) {  //Bereits subscribed
                    alreadySubscribed = true;

                }
            }

            if (!alreadySubscribed) {
                subscribedProducers.add(producer);
                Subscriber subscriber = new Subscriber(
                        CliParameters.getInstance().getStore()
                        + constants.Constants.TOPIC_CONFIRMATION);
                subscriber.run();
            }
        }
    }

}
