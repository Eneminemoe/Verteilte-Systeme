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

            simpleServer(processor);
        } catch (Exception e) {
        }
    }

    @Override
    public String order(String message) throws TException {

        
        System.out.println(message);
        
        message = message.toLowerCase();
        if (Character.isDigit(message.charAt(0))) {
            int tmp = Character.getNumericValue(message.charAt(0));
            message = message.trim();
            message = message.replaceAll("[^a-z]", "");
            
            switch (message) {
                
                case "milk":
                case "yoghurt":
                case "sausage":
                case "butter":
                case "chocolate":
                    makeInvoice(message, tmp);
                    return tmp + " " + message + " bestellt.";
                
                default:
                    return "Artikel nicht vorhanden";
            }

        } else {
            return "Artikel nicht vorhanden";
        }

    }

    @Override
    public String invoice(String message) throws TException {

        return invoice.getOrder();
    }

    private void makeInvoice(String order, int number) {

        invoice.setOrder(number, order);
    }

    public static void simpleServer(StoreService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(PORT);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

            System.out.println("Starting the simple server...");
            server.serve();
        } catch (TTransportException e) {
        }
    }

}
