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
public class Stock {
    
    private static Stock instance;
    
    //Anzahl lagernder Artikel
    private int sum = 1250;
    private int milk = 250;
    private int yoghurt = 250;
    private int sausage = 250;
    private int butter = 250;
    private int chocolate = 250;
    
        public static Stock getInstance() {
        if (instance == null) {
            instance = new Stock();
        }
        return instance;
    }
    
    //Prevent Instantiation
    private Stock(){}

    /**
     * @return the sum
     */
    public int getSum() {
        return sum;
    }

    /**
     *Calculate the sum
     */
    public void CalculateSum() {
        this.sum = milk+butter+yoghurt+chocolate+sausage;
    }

    /**
     * @return the milk
     */
    public int getMilk() {
        return milk;
    }

    /**
     * @param milk the milk to set
     */
    public void setMilk(int milk) {
        this.milk += milk;
    }

    /**
     * @return the yoghurt
     */
    public int getYoghurt() {
        return yoghurt;
    }

    /**
     * @param yoghurt the yoghurt to set
     */
    public void setYoghurt(int yoghurt) {
        this.yoghurt += yoghurt;
    }

    /**
     * @return the sausage
     */
    public int getSausage() {
        return sausage;
    }

    /**
     * @param sausage the sausage to set
     */
    public void setSausage(int sausage) {
        this.sausage += sausage;
    }

    /**
     * @return the butter
     */
    public int getButter() {
        return butter;
    }

    /**
     * @param butter the butter to set
     */
    public void setButter(int butter) {
        this.butter += butter;
    }

    /**
     * @return the chocolate
     */
    public int getChocolate() {
        return chocolate;
    }

    /**
     * @param chocolate the chocolate to set
     */
    public void setChocolate(int chocolate) {
        this.chocolate += chocolate;
    }
}
