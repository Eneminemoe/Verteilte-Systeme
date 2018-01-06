/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt;

import java.util.List;

/**
 *
 * @author Jens
 */
public class CliParameters {

    /**
     * The one and only instance of CLI parameters.
     */
    private static CliParameters instance;

    /**
     * The address of the broker.
     */
    private String brokerAddress = "iot.eclipse.org";
    /**
     * The port of the broker.
     */
    private String brokerPort = "1883";
    /**
     * The port of the protocol.
     */
    private String brokerProtocol = "tcp";
    /**
     * The topic the MQTT client subscribes to.
     */
    private String topic = constants.Constants.TOPIC_MARKETPLACE;
    /**
     * The message that is published.
     */
    private String message = "Hello World";
    /**
     * The producer
     */
    private String producer="defaultProducer";
    /**
     * The Store
     */
    private String store ="defaultStore";
    /**
     * The Thriftport
     */
    private int Thriftport=constants.Constants.THRIFTPORT;
    /**
     * The Port the Server listens to on TCP
     */
    private int Tcp_Listening_Server_Socket_Port=constants.Constants.TCP_LISTENING_SERVER_SOCKET;
    /**
     * The Port the Applications listens to on UDP
     */
    private int Udp_Send_To_Port=constants.Constants.UDP_SEND_TO_PORT;
    /**
     * The Port the Applications sends through UDP
     */
    private int Udp_Listen_To_Port=constants.Constants.UDP_LISTEN_TO_PORT;
    /**
     * The static getter for the CLI parameters instance.
     *
     * @return The CLI parameters instance.
     */
    public static CliParameters getInstance() {
        if (instance == null) {
            instance = new CliParameters();
        }
        return instance;
    }

    //
    // Getter and Setter
    //
    public String getBrokerAddress() {
        return this.brokerAddress;
    }

    public void setBrokerAddress(String brokerAddress) {
        this.brokerAddress = brokerAddress;
    }

    public String getBrokerPort() {
        return this.brokerPort;
    }

    public void setBrokerPort(String brokerPort) {
        this.brokerPort = brokerPort;
    }

    public String getBrokerProtocol() {
        return this.brokerProtocol;
    }

    public void setBrokerProtocol(String brokerProtocol) {
        this.brokerProtocol = brokerProtocol;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage(List<String> args) {
        this.message = "";
        for (String arg : args) {
            this.message += arg + " ";
        }
        this.message = this.message.trim();
    }

    /**
     * A private constructor to avoid instantiation.
     */
    private CliParameters() {
    }

    /**
     * @return the producer
     */
    public String getProducer() {
        return producer;
    }

    /**
     * @param producer the producer to set
     */
    public void setProducer(String producer) {
        this.producer = producer;
    }

    /**
     * @return the store
     */
    public String getStore() {
        return store;
    }

    /**
     * @param store the store to set
     */
    public void setStore(String store) {
        this.store = store;
    }

    /**
     * @return the Thriftport
     */
    public int getThriftport() {
        return Thriftport;
    }

    /**
     * @param Thriftport the Thriftport to set
     */
    public void setThriftport(int Thriftport) {
        this.Thriftport = Thriftport;
    }

    /**
     * @return the Tcp_Listening_Server_Socket_Port
     */
    public int getTcp_Listening_Server_Socket_Port() {
        return Tcp_Listening_Server_Socket_Port;
    }

    /**
     * @param Tcp_Listening_Server_Socket_Port the Tcp_Listening_Server_Socket_Port to set
     */
    public void setTcp_Listening_Server_Socket_Port(int Tcp_Listening_Server_Socket_Port) {
        this.Tcp_Listening_Server_Socket_Port = Tcp_Listening_Server_Socket_Port;
    }

    /**
     * @return the Udp_Listening_Server_Socket_Port
     */
    public int getUdp_Send_To_Port() {
        return Udp_Send_To_Port;
    }

    /**
     * @param Udp_Send_To_Port the Udp_Listening_Server_Socket_Port to set
     */
    public void setUdp_Send_To_Port(int Udp_Send_To_Port) {
        this.Udp_Send_To_Port = Udp_Send_To_Port;
    }

    /**
     * @return the Udp_Listen_To_Port
     */
    public int getUdp_Listen_To_Port() {
        return Udp_Listen_To_Port;
    }

    /**
     * @param Udp_Listen_To_Port the Udp_Listen_To_Port to set
     */
    public void setUdp_Listen_To_Port(int Udp_Listen_To_Port) {
        this.Udp_Listen_To_Port = Udp_Listen_To_Port;
    }
}
