package producer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import mqtt.CliParameters;
import mqtt.MessageParser;
import mqtt.Publisher;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import organizeoffers.Offer;
import organizeoffers.Offers;

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
        MESSAGEPARSER.parseMessage(new String(mqttMessage.getPayload()));
        switch (MESSAGEPARSER.getMessagetype()) {
            case ORDER:
                System.out.println("Bestellung erhalten: " + MESSAGEPARSER.getOrder_message());

                //Wenn Angebot vorhanden löschen und Ware versenden
                if (Offers.getInstance().deleteOfferIfEqualTo(
                        new Offer(
                                CliParameters.getInstance().getProducer(),
                                MESSAGEPARSER.getArtikel(), MESSAGEPARSER.getPreis(),
                                MESSAGEPARSER.getAnzahl()))) {
                    sendAnswerToOrder(true);
                } else {
                    //Wenn nicht vorhanden, Nachricht mit Ablehnungn senden
                    sendAnswerToOrder(false);
                }
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

    /**
     * Sendet Nachricht via MQTT
     */
    private void sendAnswerToOrder(boolean sentOrder) {

        int anzahl = 0; //Wenn Artikel nicht mehr vorhanden sende 0
        if (sentOrder) {
            anzahl = MESSAGEPARSER.getAnzahl();
        }
        String messageToSend = MESSAGEPARSER.makeConfirmation_message(CliParameters.getInstance().getProducer(),
                MESSAGEPARSER.getStore(),
                MESSAGEPARSER.getArtikel(),
                MESSAGEPARSER.getPreis(),
                MESSAGEPARSER.getAnzahl(),
                anzahl);
        String topic = MESSAGEPARSER.getTopic();
        // Start the MQTT Publisher.
        Publisher publisher = new Publisher(topic, messageToSend);
        publisher.run();
    }
}
