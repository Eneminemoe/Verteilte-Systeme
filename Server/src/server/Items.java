/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.Vector;

/**
 *
 * @author Jens
 *
 * Beihnhaltet Informationen zum aktuellen Artikelstand
 */
public class Items {

    private Vector milk = new Vector();
    private Vector yoguhrt = new Vector();
    private Vector sausage = new Vector();
    private Vector butter = new Vector();
    private Vector chocolate = new Vector();
    private String[] cI = new String[5];

    public Items() {
        milk.add(10);
        yoguhrt.add(10);
        sausage.add(10);
        butter.add(10);
        chocolate.add(10);
    }

    /**
     * @param item Name of Item which is to be changed
     * @return Vector gibt den zu ändernden Artikel zurück
     */
    public Vector ItemToAlter(String item) {
        switch (item) {
            case constants.Constants.MILCH:
                return getMilk();
            case constants.Constants.YOGHURT:
                return getYoguhrt();
            case constants.Constants.WURST:
                return getSausage();
            case constants.Constants.BUTTER:
                return getButter();
            case constants.Constants.SCHOKOLADE:
                return getChocolate();
            default:
                return null;
        }
    }

    /**
     * @param vec zu ändernder Artikel Nimmt Artikel aus dem Kühlschrank und
     * speichert die neue Anzahl
     * @return gibt aktualisierte Anzahl des Items im Kühlschrank zurück
     */
    public int takeItemOut(Vector vec) {
        int x = (int) vec.lastElement();
        if (x > 0) { //wenn Kühlschrank leer kann nichts entfernt werden
            x--;
            vec.add(x);
            return x;
        }
        return 0;
    }

    /**
     * @param vec zu ändernder Artikel Legt Artikel in den Kühlschrank und
     * speichert neue Anzahl
     */
    public void putItemIn(Vector vec) {
        int x = (int) vec.lastElement();
        x++;
        vec.add(x);
    }

    /**
     * @return the current amount of each Item
     *
     */
    public String currentItems() {

        cI[0] = constants.Constants.MILCH + ": " + getMilk().lastElement();
        cI[1] = constants.Constants.YOGHURT + ": " + getYoguhrt().lastElement();
        cI[2] = constants.Constants.WURST + ": " + getSausage().lastElement();
        cI[3] = constants.Constants.BUTTER + ": " + getButter().lastElement();
        cI[4] = constants.Constants.SCHOKOLADE + ":" + getChocolate().lastElement();
        return cI[0] + "\n" + cI[1] + "\n" + cI[2] + "\n" + cI[3] + "\n" + cI[4];
    }

    public String[] getCurrentItemsArray() {

        cI[0] = constants.Constants.MILCH + ": " + getMilk().lastElement();
        cI[1] = constants.Constants.YOGHURT + ": " + getYoguhrt().lastElement();
        cI[2] = constants.Constants.WURST + ": " + getSausage().lastElement();
        cI[3] = constants.Constants.BUTTER + ": " + getButter().lastElement();
        cI[4] = constants.Constants.SCHOKOLADE + ": " + getChocolate().lastElement();

        return cI;
    }

    /**
     * @return the milk
     */
    public Vector getMilk() {
        return milk;
    }

    /**
     * @return the yoguhrt
     */
    public Vector getYoguhrt() {
        return yoguhrt;
    }

    /**
     * @return the sausage
     */
    public Vector getSausage() {
        return sausage;
    }

    /**
     * @return the butter
     */
    public Vector getButter() {
        return butter;
    }

    /**
     * @return the chocolate
     */
    public Vector getChocolate() {
        return chocolate;
    }

    /**
     * Nachricht: xitem Funktion parst den String in int Anzahl und string item
     * und ändert dementspredhend den Wert eines Artikels
     *
     * @param s xitem
     */
    public void changeItems(String s) {

        int tmp = 0; //Anzahl der Artikel

        if (s.equals("")) { //Wenn leere Nachhricht nichts ändern.
            return;
        }

        s = s.replaceAll("\\s", "");
        if (Character.isDigit(s.charAt(0))) {

            tmp = Character.getNumericValue(s.charAt(0)); //Anzahl holen
            s = s.substring(1); //Anzahl löschen
            s = s.substring(0, s.length() - 9); 
            try {
                ItemToAlter(s).add((int) ItemToAlter(s).lastElement() + tmp);
            } catch (Exception e) {
            }
        }        
    }
}
