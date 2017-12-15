/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import p3.StoreService;

/**
 *
 * @author Jens
 */
public class ThriftHandler {

    //THRIFT CONSTANTS
    final static String ORDERITEMS = "9";
    final static String HOST = "localhost";
    final static int THRIFTPORT = 9090;

    /**
     * Bestellt Items über die Schnittstelle
     * Bestellbare Items: "milk" , "yoghurt"
                         "sausage" , "butter"
                        "chocolate"
     */
    private static String orderItem(StoreService.Client client, String item, String number) throws TException {

        return client.order(number + item);
    }

    private static String getInvoice(StoreService.Client client,String message) throws TException {
        return client.invoice(message);
    }

    /**
     * Baut Verbinung zum Store auf via THRIFT Schnittstelle
     *
     * @param item Item, das bestellt werden soll 
     * @param type 1: Bestellung, 2: Rechnung anfordern
     * @return depending on chosen type
     */
    public static String establishThriftConnection(String item,String number, int type) {

        String answer = "Store offline";
        try {
            TTransport transport;

            transport = new TSocket(HOST, THRIFTPORT);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            StoreService.Client client = new StoreService.Client(protocol);

            switch (type) {
                case 1:
                    answer = orderItem(client, item, number);
                    break;
                case 2:
                    answer = getInvoice(client,item);
                    break;
                default:;
            }

            transport.close();
        } catch (TException x) {
            System.out.println("server.Server.establishThriftConnection()" + "  " + x);
        }
        return answer;
    }

}
