/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import mqtt.CliParameters;
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

    /**
     * Bestellt Items über die Schnittstelle Bestellbare Items: public static
     * final String MILCH = "Milch"; public static final String BUTTER =
     * "Butter"; public static final String WURST = "Wurst"; public static final
     * String YOGHURT = "Yoghurt"; public static final String SCHOKOLADE =
     * "Schokolade";
     *
     * @return
     */
    private static String orderItem(StoreService.Client client, String item, String number) throws TException {

        return client.order(number + item);
    }

    /**
     * Rechnung anfordern
     */
    private static String getInvoice(StoreService.Client client, String message) throws TException {
        return client.invoice(message);
    }

    /**
     * Baut Verbinung zum Store auf via THRIFT Schnittstelle
     *
     * @param item Item, das bestellt werden soll
     * @param number Anzahl der zu bestellenden Artikel
     * @param type 1: Bestellung, 2: Rechnung anfordern
     * @param port the Port to send the message to.
     * @return depending on chosen type type 1: xitem x=Anzahl der gelieferten
     * Artikel item: Artikeltyp type 2: Rechung der bestellten Artikel
     */
    public static String establishThriftConnection(String item, String number, int type, int port) {

        
        String answer = "Store offline";
        try {
            TTransport transport;

            transport = new TSocket(constants.Constants.THRIFT_HOST, port);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            StoreService.Client client = new StoreService.Client(protocol);

            switch (type) {
                case 1:
                    answer = orderItem(client, item, number);
                    System.out.println(ThriftHandler.class + ", Answer: " + answer);
                    break;
                case 2:
                    answer = getInvoice(client, item);
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
