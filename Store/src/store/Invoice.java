/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package store;

import java.text.DecimalFormat;

/**
 *
 * @author Jens
 */
public class Invoice {

    private static final String MILCH_UNIT = "Liter";
    private static final String YOGHURT_UNIT = "250 Gramm";
    private static final String SAUSAGE_UNIT = "100 Gramm";
    private static final String BUTTER_UNIT = "250 Gramm";
    private static final String CHOCOLATE_UNIT = "100 Gramm";

    //Preise der Artikel
    private double sum = 0.00;
    private static final double milk = 0.60;
    private static final double yoghurt = 0.40;
    private static final double sausage = 1.60;
    private static final double butter = 0.99;
    private static final double chocolate = 1.00;

    //Anzahl bestellter Artikel
    private int int_sum = 0;
    private int int_milk = 0;
    private int int_yoghurt = 0;
    private int int_sausage = 0;
    private int int_butter = 0;
    private int int_chocolate = 0;

    DecimalFormat f = new DecimalFormat("0.00"); // 2 Nachkommastellen

    /**
     * Speichert die verkauften Artikel
     * 
     * @param num Anzahl gekaufter Objekte
     * @param type Artikel der gekauft wurde
     */
    public void setOrder(int num, String type) {

        System.out.println(type);

        int_sum += num;
        switch (type) {

            case constants.Constants.MILCH:
                sum = sum + num * milk;
                int_milk += num;
                break;
            case constants.Constants.YOGHURT:
                sum = sum + num * yoghurt;
                int_yoghurt += num;
                break;
            case constants.Constants.WURST:
                sum = sum + num * sausage;
                int_sausage += num;
                break;
            case constants.Constants.BUTTER:
                sum = sum + num * butter;
                int_butter += num;
                break;
            case constants.Constants.SCHOKOLADE:
                sum = sum + num * chocolate;
                int_chocolate += num;
                break;
            default:
        }
    }

    /**
     * Gibt die aktuelle Rechnung als String zurück
     * 
     * @return Rechnung
     */
    public String getOrder() {

        return "Ihre Rechnung:<br/><br/>"
                + sumOfItem(constants.Constants.MILCH, MILCH_UNIT, int_milk, milk)
                + sumOfItem(constants.Constants.YOGHURT, YOGHURT_UNIT, int_yoghurt, yoghurt)
                + sumOfItem(constants.Constants.WURST, SAUSAGE_UNIT, int_sausage, sausage)
                + sumOfItem(constants.Constants.BUTTER, BUTTER_UNIT, int_butter, butter)
                + sumOfItem(constants.Constants.SCHOKOLADE, CHOCOLATE_UNIT, int_chocolate, chocolate)
                + "Summe Artikel: " + int_sum
                + "   Summe:" + f.format(sum) + " EURO\n";
    }

    /**
     * Baut die Rechnung für einen einzelnen Artikel
     */
    private String sumOfItem(String item, String unit, int number, double price) {
        return item + " " + number + " Stck. á " + f.format(price) + " Eu / " + unit + " = "+f.format(number*price)+" EURO"+"<br/>";
    }
}
