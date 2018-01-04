/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package store;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import p3.StoreService;
import org.apache.thrift.TException;
import org.apache.thrift.server.*;
import org.apache.thrift.transport.*;
import mqtt.CliProcessor;

/**
 *
 * @author Jens
 */
public class Store implements StoreService.Iface {

    public static Store store;
    public static StoreService.Processor processor;
    private static Invoice invoice;
    private static Stock stock_instance;
    private static Offers offers;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            store = new Store();
            invoice = new Invoice();
            processor = new StoreService.Processor(store);
            stock_instance = Stock.getInstance();
            offers=Offers.getInstance();

            //MQTT
            CliProcessor.getInstance().parseCliOptions(args);
            Subscriber subscriber = new Subscriber(constants.Constants.TOPIC_MARKETPLACE);
            subscriber.run();

            /**
             * THRIFT
             */
            simpleServer(processor);

            while (true) {
                //Artikel nachbestellen
                waitSeconds(30);
                stock_instance.checkStockandOrder(offers);
            }

        } catch (Exception e) {
        }
    }

    /**
     * Function processes prder and checks Stock after an order has been sent
     * Eventually orders items if offer from producer is available
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

            switch (message) {

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

}
