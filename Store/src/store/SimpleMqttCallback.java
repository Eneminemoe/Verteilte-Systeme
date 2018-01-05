package store;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import organizeoffers.Offer;
import organizeoffers.Offers;
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
    private static final Offers OFFERS = Offers.getInstance();

    @Override
    public void connectionLost(Throwable throwable) {
        LOGGER.error("Connection to MQTT broker lost!");
    }

    /**
     * Get message via MQTT
     *
     * @param s
     * @param mqttMessage
     * @throws java.lang.Exception
     */
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        
        //Nachricht verarbeiten: einlesen und speichern
        MESSAGEPARSER.parseMessage(new String(mqttMessage.getPayload()));
        
        switch (MESSAGEPARSER.getMessagetype()) {
            
            case CONFIRMATION:
                //Wenn keine Artikel bekommen
                if(MESSAGEPARSER.getItemsSent()== constants.Constants.NOITEMSENT){
                LOGGER.info("Angebot ausverkauft: " + MESSAGEPARSER.getConfirmation_message());
                }
                else{
                //Angebot bestellt und geliefert 
                LOGGER.info("Angebot erfolgreich bestellt: " + MESSAGEPARSER.getConfirmation_message());
                 //->Stock auffüllen
                LOGGER.info("Neuer Artikelstand: "
                        + MESSAGEPARSER.getArtikel()
                        + " "
                        + Stock.getInstance().updateStock(
                                MESSAGEPARSER.getArtikel(),
                                MESSAGEPARSER.getItemsSent()));
                }
                //aus Liste der Angebote löschen, egal ob bekommen oder nicht -> gleiche Konsequenz
                OFFERS.deleteOfferIfEqualTo(new Offer(MESSAGEPARSER.getProducer(),
                        MESSAGEPARSER.getArtikel(),
                        MESSAGEPARSER.getPreis(),
                        MESSAGEPARSER.getAnzahl()));
                break;

            case OFFER:
                //Wenn Angebot erhalten wird gespeichert
                if (OFFERS.addOffer(new Offer(MESSAGEPARSER.getProducer(),
                        MESSAGEPARSER.getArtikel(),
                        MESSAGEPARSER.getPreis(),
                        MESSAGEPARSER.getAnzahl()))) {
                    System.out.println("Angebot erhalten: " + MESSAGEPARSER.getOffer_message());
                }else{
                    //Falls Fehler beim speichern INFO
                System.out.println("Angebot konnte nicht gespeichert werden: " + MESSAGEPARSER.getOffer_message());
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
}
