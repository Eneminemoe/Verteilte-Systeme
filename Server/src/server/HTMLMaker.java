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
        
        if(makeFile()){
            System.out.println("HTMLMAKER: File created");
        }else{
            System.out.println("File could not be created!");
        }
    }

    /**
     * Erstellt eine HTMLFile mit dem aktuellen Kühlschrankinhalt und schreibt diese auf die HDD
     */
    private boolean makeFile() {

        Writer writer = null;
        String listOfItems = "<ul>";

        for (String s : items) {
            listOfItems += "<li>" + s + "<input type=\"submit\" value=\"nachbestellen\" name =\"";
            s = s.replaceAll("[^A-Za-z]", "");
            listOfItems += s;
            listOfItems += "\" onClick=\"return buttonClick(this)\">" + "</li>";
        }
        listOfItems += "</ul>";

        String html = "<!doctype html>\n"
                + "<html lang=\"de\">\n"
                + "  <head>\n"
                + "    <meta charset=\"utf-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                // + "     <meta http-equiv=\"refresh\" content=\"2\" />\n"
                + "    <title>Kühlschrank</title>\n"
                + "         <script>function buttonClick(theButton){\n"
                + "             document.getElementById('clicked_button').value = theButton.name;\n"
                + "             return true;}\n"
                + "     </script>"
                + "  </head>\n"
                + "  <body>\n"
                + " <h1>Ihre Artikel:</h1>"
                + "<form action=\"\" method=\"get\">"
                + listOfItems
                + "<input type=\"submit\" value=\"Aktualisieren\" name=\"refresh\">"
                + "<input type=\"hidden\" value=\"\" name=\"request\">"
                + "<input type=\"submit\" name=\"invoice\" value=\"Rechnung anzeigen\" formtarget=\"_blank\">"
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
            } catch (IOException ex) {/*ignore*/
            }

        }
        return true;

    }

}
