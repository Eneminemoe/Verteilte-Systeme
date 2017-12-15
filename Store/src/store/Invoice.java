/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package store;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jens
 */
public class Invoice {

    private String Milch = "Milch : 0,60 EURO / Liter";
    private String Yoghurt = "Yoghurt: 0,30 EURO / 100 Gramm";
    private String Sausage = "Wurst: 1,60 EURO / 100 Gramm";
    private String Butter = "Butter: 0,30 EURO / 100 Gramm";
    private String Chocolate = "Schokolade: 1,00 EURO /100 Gramm";

    //Preise der Artikel
    private double sum = 0;
    private double milk = 0.6;
    private double yoghurt = 0.3;
    private double sausage = 1.6;
    private double butter = 0.3;
    private double chocolate = 1;

    //Anzahl bestellter Artikel
    private int int_sum = 0;
    private int int_milk = 0;
    private int int_yoghurt = 0;
    private int int_sausage = 0;
    private int int_butter = 0;
    private int int_chocolate = 0;

    public void setOrder(int num, String type) {

        int_sum+=num;
        switch (type) {

            case "milk":
                sum = sum + num * milk;
                int_milk+=num;
                break;
            case "yoghurt":
                sum = sum + num * yoghurt * 2.5;
                int_yoghurt+=num;
                break;
            case "sausage":
                sum = sum + num * sausage;
                int_sausage+=num;
                break;
            case "butter":
                sum = sum + num * butter * 2.5;
                int_butter+=num;
                break;
            case "chocolate":
                sum = sum + num * chocolate;
                int_chocolate+=num;
                break;
            default:
        }
    }
    
    public String getOrder(){
    
        return "Ihre Rechnung:\n\n"
                +Milch +" * "+int_milk+" Stck.\n"
                +Yoghurt +" * "+int_yoghurt+" Stck.\n"
                +Sausage +" * "+int_sausage+" Stck.\n"
                +Butter +" * "+int_butter+" Stck.\n"
                +Chocolate +" * "+int_chocolate+" Stck.\n\n"
                +"Summe:" + int_sum +" EURO";
    }
}
