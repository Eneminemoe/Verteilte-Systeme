/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constants;

import java.util.Random;

/**
 *
 * @author Jens
 */
public class Constants {

    /**
     * The exit code if the procedure succeeded.
     */
    public static final int EXIT_CODE_SUCCESS = 0;
    /**
     * The exit code of the procedure failed.
     */
    public static final int EXIT_CODE_ERROR = 1;
    /**
     * The at-most-once QoS parameter of MQTT:
     */
    public static final int QOS_AT_MOST_ONCE = 0;
    /**
     * The at-least-once QoS parameter of MQTT:
     */
    public static final int QOS_AT_LEAST_ONCE = 1;
    /**
     * The exactly-once QoS parameter of MQTT:
     */
    public static final int QOS_EXACTLY_ONCE = 2;
    /**
     * The Market every Producer subscribes to:
     */
    public static final String TOPIC_MARKETPLACE = "MARKET";
    /**
     * The RECEIVE_BROKER for Producers to be expanded by Producername like
     * Producer + TOPIC_RECEIVE_ORDER
     */
    public static final String TOPIC_RECEIVE_ORDER = "_RECEIVE_ORDER";
    /**
     * THE CONFIRMATION_BROKER for PRODUCERS and Store. to be expanded by
     * Producer and Store like Store + TOPIC_CONFIRAMTION
     */
    public static final String TOPIC_CONFIRMATION = "_CONFIRMATION";
    /**
     * Time to deliver a new Offer in seconds
     */
    public static final int PERIDOIC_UPDATE = 5;
    /**
     * Maximum time to wait for a notify in milliseconds 
     */
    public static final int MAX_WAIT_ON_THREAD= 5*1000;
    /**
     *
     */
    public static final int NOITEMSENT = 0;
    /**
     *
     */
    public static final int MINIMUMVALUEOFITEMSUNTILREORDER = 3;

    /**
     * The type of message for the Parser
     */
    public enum messagetype {
        OFFER, ORDER, CONFIRMATION
    }
    public static final String OFFER = "Offer:";
    public static final String ORDER = "Order:";
    public static final String CONFIRMATION = "Confirmation";

    /**
     * The Itmes available which can be bought, selled and held
     */
    public enum Items {
        Milch, Butter, Wurst, Schokolade, Yoghurt
    }

    /**
     * Get randon enum
     *
     * @param <E> The Emum to get a Random from
     */
    public static class RandomEnum<E extends Enum> {

        private static final Random RND = new Random();
        private final E[] values;

        public RandomEnum(Class<E> token) {
            values = token.getEnumConstants();
        }

        public E random() {
            return values[RND.nextInt(values.length)];
        }
    }

    /**
     * Returns an Items Enum represendet as by the given String
     * 
     * @param i String to Parse
     * @return Item Represented Item
     */
    public static Items parseStringToItem(String i) {
        switch (i) {
            case BUTTER:
                return Items.Butter;
            case MILCH:
                return Items.Milch;
            case SCHOKOLADE:
                return Items.Schokolade;
            case WURST:
                return Items.Wurst;
            case YOGHURT:
                return Items.Yoghurt;
            default:
                return null;
        }
    }
    
    /**
     * Parses the Item to a String
     * 
     * @param i Item to parse
     * @return the representing String
     */
       public static String parseItemToString(Items i) {
        switch (i) {
            case Butter:
                return BUTTER;
            case Milch:
                return MILCH;
            case Schokolade:
                return SCHOKOLADE;
            case Wurst:
                return WURST;
            case Yoghurt:
                return YOGHURT;
            default:
                return null;
        }
    }

    public static final String MILCH = "Milch";
    public static final String BUTTER = "Butter";
    public static final String WURST = "Wurst";
    public static final String YOGHURT = "Yoghurt";
    public static final String SCHOKOLADE = "Schokolade";

    /**
     * UDP Communication Constants
     */
    public static final int PAYLOAD_FOR_UDP = 1024;
    public static final int UDP_SEND_TO_PORT = 6543;
    public static final int UDP_LISTEN_TO_PORT = 6543;

    /**
     * TCP Communication Constants
     */
    public static final int TCP_LISTENING_SERVER_SOCKET = 6544;

    /**
     * THRIFT Constants
     */
    public static final int THRIFTPORT = 9090;
    public static final String THRIFT_HOST = "localhost";

    /**
     * A private constructor to avoid instantiation.
     */
    private Constants() {
    }
}
