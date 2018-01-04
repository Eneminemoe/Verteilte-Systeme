package store;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import mqtt.MessageParser;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jens
 */
public class SimpleMqttCallback implements MqttCallback {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMqttCallback.class);
    private static final MessageParser MESSAGEPARSER = MessageParser.getInstance();

    @Override
    public void connectionLost(Throwable throwable) {
        LOGGER.error("Connection to MQTT broker lost!");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        LOGGER.info("Message received: " + new String(mqttMessage.getPayload()));
        //Nachricht verarbeiten
        MESSAGEPARSER.parseMessage(new String(mqttMessage.getPayload()));
        switch (MESSAGEPARSER.getMessagetype()) {
            case CONFIRMATION:
                LOGGER.info(MESSAGEPARSER.getConfirmation_message());
                break;
            case OFFER:

                break;
            default:
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken mqttDeliveryToken) {
        try {
            LOGGER.info("Delivery completed: " + mqttDeliveryToken.getMessage());
        } catch (MqttException e) {
            LOGGER.error("Failed to get delivery token message: " + e.getMessage());
        }
    }
}
