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
    private String topic = "dies-ist-mein-test";
    /**
     * The message that is published.
     */
    private String message = "Hello World";

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
}
