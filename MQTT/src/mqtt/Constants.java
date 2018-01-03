/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt;

/**
 *
 * @author Jens
 */
public class Constants {
    
     /** The exit code if the procedure succeeded. */
    public static final int EXIT_CODE_SUCCESS = 0;
    /** The exit code of the procedure failed. */
    public static final int EXIT_CODE_ERROR = 1;
    /** The at-most-once QoS parameter of MQTT: */
    public static final int QOS_AT_MOST_ONCE = 0;
    /** The at-least-once QoS parameter of MQTT: */
    public static final int QOS_AT_LEAST_ONCE = 1;
    /** The exactly-once QoS parameter of MQTT: */
    public static final int QOS_EXACTLY_ONCE = 2;
    /** The Market every Producer subscribes to:*/
    public static final String TOPIC_MARKETPLACE= "MARKET";
    /** The RECEIVE_BROKER for Producers*/
    public static final String RECEIVE_ORDER = "_RECEIVED_ORDER";
    /** Time to deliver a new Offer in milliseconds*/
    public static final int PERIDOIC_UPDATE = 20000;
    /** The type of message for the Parser*/
    public enum messagetype{OFFER,ORDER,CONFIRMATION}
    /**
     * A private constructor to avoid
     * instantiation.
     */
    private Constants() {}
}
