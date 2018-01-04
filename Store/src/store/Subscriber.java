package store;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import mqtt.CliParameters;
import mqtt.MessageParser;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jens
 */
public class Subscriber {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Subscriber.class);

    /**
     * The global CLI parameters that have been parsed in Main.
     */
    private CliParameters cliParameters;
    /**
     * The broker URL.
     */
    private String broker;

    /**
     * Default constructor that initializes various class attributes.
     *
     * @param topic
     */
    public Subscriber(String topic) {

        // Get the CLI parameters.
        cliParameters = CliParameters.getInstance();
        cliParameters.setTopic(topic);
        // Create the broker string from command line arguments.
        broker
                = cliParameters.getBrokerProtocol() + "://"
                + cliParameters.getBrokerAddress() + ":"
                + cliParameters.getBrokerPort();

    }

    /**
     * Runs the MQTT client.
     */
    public void run() {
        try {
            MqttClient client = new MqttClient(broker, MqttClient.generateClientId());
            client.setCallback(new SimpleMqttCallback());

            // Connect to the MQTT broker.
            client.connect();
            LOGGER.info("Connected to MQTT broker: " + client.getServerURI());

            // Subscribe to a topic.
            client.subscribe(cliParameters.getTopic());
            LOGGER.info("Subscribed to topic: " + client.getTopic(cliParameters.getTopic()));
        } catch (MqttException e) {
            LOGGER.error("An error occurred: " + e.getMessage());
        }
    }

}
