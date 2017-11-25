/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.*;

/**
 *
 * @author Jens
 */
public class HTMLMaker {

    private String[] items;

    public HTMLMaker(String[] items) {

        setItems(items);
    }

    /**
     * @param items the items to set
     */
    public final void setItems(String[] items) {
        this.items = items;
        makeFile();
    }

    private boolean makeFile() {

        Writer writer = null;
        String listOfItems = "<ul>";

        for (String s : items) {
            listOfItems += "<li>" + s + "</li>";
            // listOfItmes = listofItems + "<li>" + s + "</li>";
        }
        listOfItems += "</ul>";

        String html = "<!doctype html>\n"
                + "<html lang=\"de\">\n"
                + "  <head>\n"
                + "    <meta charset=\"utf-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>Kühlschrank</title>\n"
                + "  </head>\n"
                + "  <body>\n"
                + " <h1>Ihre Artikel:</h1>"
                + listOfItems
                + "<input type=\"button\" value=\"Aktualisieren\" onClick=\"location.href=location.href\">"
                + "<form action=\"\" method=\"get\">"
                + "<input type=\"hidden\" value=\"receipt\" name=\"request\">"
                + "<input type=\"submit\" value=\"Letzte Bestellung anzeigen\" formtarget=\"_blank\">"
                + "  </body>\n"
                + "</html>";

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("index.html"), "utf-8"));
            writer.write(html);  //Datei schreiben
        } catch (IOException ex) {
            // report
            return false;
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/
            }
            return true;
        }

    }

}
