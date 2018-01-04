/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package store;

import p3.StoreService;
import org.apache.thrift.TException;
import org.apache.thrift.server.*;
import org.apache.thrift.transport.*;
import mqtt.CliProcessor;
import mqtt.Constants;

/**
 *
 * @author Jens
 */
public class Store implements StoreService.Iface {

    public static Store store;
    public static StoreService.Processor processor;
    public static final int PORT = 9090;
    private static Invoice invoice;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            store = new Store();
            invoice = new Invoice();
            processor = new StoreService.Processor(store);

            //MQTT
            CliProcessor.getInstance().parseCliOptions(args);
            Subscriber subscriber = new Subscriber(Constants.TOPIC_MARKETPLACE);
            subscriber.run();

            simpleServer(processor);

        } catch (Exception e) {
        }
    }

    /**
     *
     * @param message Anzahl und Typ Artikel in Form von xitem
     * @return String,
     * @throws TException
     */
    @Override
    public String order(String message) throws TException {

        boolean inStock = false; // Wenn Artikel nicht vorhanden, keinen versenden
        message = message.toLowerCase();
        if (Character.isDigit(message.charAt(0))) {
            int tmp = Character.getNumericValue(message.charAt(0));
            message = message.trim();
            message = message.replaceAll("[^a-z]", "");

            System.out.println("Bestellung eingegangen: " + tmp + " " + message);

            switch (message) {

                case "milk":
                    if (Stock.getInstance().getMilk() < tmp) {
                        inStock = false;
                        break;
                    }
                    inStock = true;
                    Stock.getInstance().setMilk(-tmp);
                    break;
                case "yoghurt":
                    if (Stock.getInstance().getYoghurt()< tmp) {
                        inStock = false;
                        break;
                    }
                    inStock=true;
                    Stock.getInstance().setYoghurt(-tmp);
                    break;
                case "sausage":
                    if (Stock.getInstance().getSausage()< tmp) {
                        inStock = false;
                        break;
                    }
                    inStock = true;
                    Stock.getInstance().setSausage(-tmp);
                    break;
                case "butter":
                    if (Stock.getInstance().getButter()< tmp) {
                        inStock = false;
                        break;
                    }
                    inStock = true;
                    Stock.getInstance().setButter(-tmp);
                    break;
                case "chocolate":
                    if (Stock.getInstance().getChocolate()< tmp) {
                        inStock = false;
                        break;
                    }
                    inStock = true;
                    Stock.getInstance().setChocolate(-tmp);
                    break;
                default:
                    System.out.println("Artikel nicht vorhanden: " + message);
                    return "Gesendete Nachricht: " + message + " Artikel nicht vorhanden";
            }

            if(inStock){
            makeInvoice(message, tmp);
            System.out.println("Bestellung versendet: " + tmp + " " + message);

            return tmp + " " + message + " bestellt.";
            }else{
                //Bestellung nicht verabeitbar, weil nicht vorrätig
                System.out.println("Artikel derzeit nicht vorhanden. Kann nicht versendet werden");
            return "Artikel derzeit nicht vorhanden.";
            }
                 
        } else {
            System.out.println("Nachricht konnte nicht verarbeitet werden.");
            System.out.println("Gesendete Nachricht:");
            System.out.println(message);
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

    public static void simpleServer(StoreService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(PORT);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

            System.out.println("Starting the Store server...");
            server.serve();
        } catch (TTransportException e) {
        }
    }

}
