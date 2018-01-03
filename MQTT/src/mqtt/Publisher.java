/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jens
 */
public class Publisher {
    
/** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Publisher.class);

    /** The global CLI parameters that have been parsed in Main. */
    private CliParameters cliParameters;
    /** The broker URL. */
    private String broker;

    /**
     * Default constructor that initializes
     * various class attributes.
     * @param topic
     * @param message
     */
    public Publisher(String topic,String message) {

        // Get the CLI parameters.
        cliParameters = CliParameters.getInstance();
        //update cliParameters 
        cliParameters.setTopic(topic);
        cliParameters.setMessage(message);
        
        // Create the broker string from command line arguments.
        broker =
                cliParameters.getBrokerProtocol() + "://" +
                cliParameters.getBrokerAddress() + ":" +
                cliParameters.getBrokerPort();

    }

    /**
     * Runs the MQTT client and publishes a message.
     */
    public void run() {

        // Create some MQTT connection options.
        MqttConnectOptions mqttConnectOpts = new MqttConnectOptions();
        mqttConnectOpts.setCleanSession(true);

        try {

            MqttClient client = new MqttClient(broker, MqttClient.generateClientId());

            // Connect to the MQTT broker using the connection options.
            client.connect(mqttConnectOpts);
            LOGGER.info("Connected to MQTT broker: " + client.getServerURI());

            // Create the message and set a quality-of-service parameter.
            MqttMessage message = new MqttMessage(cliParameters.getMessage().getBytes());
            message.setQos(Constants.QOS_EXACTLY_ONCE);
            System.out.println(message); //DEBUG

            // Publish the message.
            client.publish(cliParameters.getTopic(), message);
            LOGGER.info("Published message: " + message);

            // Disconnect from the MQTT broker.
            client.disconnect();
            LOGGER.info("Disconnected from MQTT broker.");

            // Exit the app explicitly.
            //System.exit(Constants.EXIT_CODE_SUCCESS);

        } catch (MqttException e) {
            LOGGER.error("An error occurred: " + e.getMessage());
        }
    }

}