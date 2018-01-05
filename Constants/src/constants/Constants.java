/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constants;

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
     * The RECEIVE_BROKER for Producers to be expanded by Producername
     * like Producer + TOPIC_RECEIVE_ORDER
     */
    public static final String TOPIC_RECEIVE_ORDER = "_RECEIVE_ORDER";
    /**
     * THE CONFIRMATION_BROKER for PRODUCERS and Store. to be expanded by
     * Producer and Store like Store + TOPIC_CONFIRAMTION
     */
    public static final String TOPIC_CONFIRMATION = "_CONFIRAMTION";
    /**
     * Time to deliver a new Offer in milliseconds
     */
    public static final int PERIDOIC_UPDATE = 5*1000;

    /**
     * The type of message for the Parser
     */
    public enum messagetype {
        OFFER, ORDER, CONFIRMATION
    }
    public static final String OFFER="Offer:";
    public static final String ORDER="Order:";
    public static final String CONFIRMATION="Confirmation";
    
    /**
     * The Itmes available which can be bought, selled and held
     */
    public enum Items{Milch,Butter,Wurst,Schokolade,Yoghurt}
    public static final String MILCH = "Milch";
    public static final String BUTTER = "Butter";
    public static final String WURST = "Wurst";
    public static final String YOGHURT = "Yoghurt";
    public static final String SCHOKOLADE = "Schokolade";

    /**
     * UDP Communication Constants
     */
    public static final int PAYLOAD_FOR_UDP = 1024;
    public static final int UDP_SERVER_PORT = 6542;
    public static final int UDP_CLIENT_PORT = 6543;
    
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
