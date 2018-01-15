/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package store;

import constants.Constants;
import constants.SharedVariablesBetweenThreads;
import organizeoffers.Offers;
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
import mqtt.Publisher;

/**
 *
 * @author Jens
 */
public class Store extends Thread implements StoreService.Iface {

    //TIMETOWAIT in seconds for peridoic ordering of items
    private static final int TIMETOWAIT = 20;
    private static Store store;
    public static StoreService.Processor processor;
    private static Invoice invoice;
    private static Stock stock_instance;
    private static Offers offers;
    private static ArrayList<String> subscribedProducers;
    private static SharedVariablesBetweenThreads shared;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

            CliProcessor.getInstance().parseCliOptions(args);
            store = new Store();
            invoice = new Invoice();
            processor = new StoreService.Processor(store);
            stock_instance = Stock.getInstance();
            shared = SharedVariablesBetweenThreads.getInstance();
            offers = Offers.getInstance();
            subscribedProducers = new ArrayList<>();
            //Zeigt den Store-Namen
            System.out.println("Store: " + CliParameters.getInstance().getStore());

            store.start();

            //MQTT
            Subscriber subscriber = new Subscriber(constants.Constants.TOPIC_MARKETPLACE);
            subscriber.run();

            Subscriber subscriberb = new Subscriber(CliParameters.getInstance().getStore()
                    + constants.Constants.TOPIC_CONFIRMATION);
            subscriberb.run();

            testFunction(2);

            /**
             * THRIFT
             */
            simpleServer(processor);

        } catch (Exception e) {
        }

    }

    @Override
    public synchronized void run() {

        while (true) {

            waitSeconds(TIMETOWAIT);
            //stock_instance.checkStockandOrder(offers);  //Artikel nachbestellen

            if (offers != null) {
                for (constants.Constants.Items i : constants.Constants.Items.values()) {

                    //Bestellen, wenn kein Angebot vorhanden, nächstes Item
                    if (!stock_instance.checkStockandOrder(offers, i)) {
                        continue;
                    }
                    try { //warten auf Antwort
                        wait(constants.Constants.MAX_WAIT_ON_THREAD);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Store.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //wenn Angebot abgelaufen, nächstes in Anspruch nehmen
                    while (!(shared.lastDeniedOrderedItem == null)) {
                        //Kein weiteres Angebot vorhanden, Varibale zurücksetzten
                        if (!stock_instance.checkStockandOrder(offers, i)) {
                            System.out.println("Kein weiteres Angebot bekannt für " + i);
                            shared.lastDeniedOrderedItem = null;
                        } else {
                            //Wenn weiteres Angebot vorhanden warte auf Antwort
                            try {
                                wait(constants.Constants.MAX_WAIT_ON_THREAD);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Store.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }
                }
            }
            waitSeconds(2);
            offers.showCurrentOffers();
        }
    }

    /**
     * Function processes order between Store and Fridge via Thrift
     *
     * @param message Anzahl und Typ Artikel in Form von xitem
     * @return String Antwort auf Anfrage
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
                System.out.println("Bestellung wird verarbeitet!");
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
     * THRIFT Connection Listents to Thrift requests
     *
     * @param processor
     */
    public static void simpleServer(StoreService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(CliParameters.getInstance().getThriftport());
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

            System.out.println("Starting the Store server...");
            server.serve();
        } catch (TTransportException e) {
        }
    }

    /**
     * Time to wait in seconds, cannot be interrupted
     *
     * @param seconds time to wait
     */
    private static void waitSeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException ex) {
            Logger.getLogger(Store.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @deprecated Nicht notwendig Kontrolliert, ob bei allen Producern auf
     * CONFIRMATION_Topic subscribed Wenn nicht, wird beim jeweiligen subscribed
     * und der Liste hinzugefügt
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

    public static Store getInstance() {
        return store;
    }

    //TEST
    private static void testFunction(int test) {

        switch (test) {
            case 1:

                //DOES ordering via mqtt work
                offers.generateOffer(Constants.Items.Milch, 20, 0.30); //Anegbot generieren, damit offer != null
                stock_instance.checkStockandOrder(offers);
                break;
            case 2:
                //How long does it take to order an item?
                long startTime,
                 endTime,
                 duration;
                offers.generateOffer(Constants.Items.Milch, 20, 0.30); //Anegbot generieren, damit offer != null
                startTime = System.nanoTime(); // Zeit messen
                stock_instance.checkStockandOrder(offers);
                endTime = System.nanoTime(); // Zeit messen
                duration = endTime - startTime;
                System.out.println("Time elapsed: " + duration + " nanoseconds."); //Zeitmessung
                System.out.println("Time elapsed: " + ((double) duration / 1000000) + " milliseconds."); //zeitmessung
                System.out.println("Time elapsed: " + ((double) duration / 1000000000) + " seconds."); //zeitmessung

                break;
            default:
                System.out.println("Test nicht vorhanden");
        }
    }

}
