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
 */
public class Items {

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
    private Vector milk = new Vector();
    private Vector yoguhrt = new Vector();
    private Vector sausage = new Vector();
    private Vector butter = new Vector();
    private Vector chocolate = new Vector();

    public Items() {
        milk.add(10);
        yoguhrt.add(10);
        sausage.add(10);
        butter.add(10);
        chocolate.add(10);
    }

    public Vector ItemToAlter(String item) {
        switch (item) {
            case "milk":
                return getMilk();
            case "yoghurt":
                return getYoguhrt();
            case "sausage":
                return getSausage();
            case "butter":
                return getButter();
            case "chocolate":
                return getChocolate();
            default:
                return null;
        }
    }

    public void takeItemOut(Vector vec) {
        int x = (int) vec.lastElement();
        if (x > 0) { //wenn Kühlschrank leer kann nichts entfernt werden
            x--;
            vec.add(x);
        }
    }

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
        String[] cI = new String[5];
        cI[0] = "Milch: " + getMilk().lastElement();
        cI[1] = "Yoghurt: " + getYoguhrt().lastElement();
        cI[2] = "Sausage: " + getSausage().lastElement();
        cI[3] = "Butter: " + getButter().lastElement();
        cI[4] = "Chocolate:" +getChocolate().lastElement();
        return cI[0]+"\n"+cI[1]+"\n"+cI[2]+"\n"+cI[3]+"\n"+cI[4];
    }
}
