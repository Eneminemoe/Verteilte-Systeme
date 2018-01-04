/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package store;

/**
 *
 * @author Jens
 */
public class Offer {

    private final String Producer;
    private final String Artikel;
    private final float Preis;
    private int Anzahl;
    
    public Offer(String producer,String item,float preis,int number){
        this.Producer=producer;
        this.Anzahl=number;
        this.Preis=preis;
        this.Artikel=item;
    }

    /**
     * @return the Producer
     */
    public String getProducer() {
        return Producer;
    }

    /**
     * @return the Artikel
     */
    public String getArtikel() {
        return Artikel;
    }

    /**
     * @return the Preis
     */
    public float getPreis() {
        return Preis;
    }

    /**
     * @return the Anzahl
     */
    public int getAnzahl() {
        return Anzahl;
    }

    /**
     * @param Anzahl the Anzahl to set
     */
    public void setAnzahl(int Anzahl) {
        this.Anzahl = Anzahl;
    }
    
    
}
