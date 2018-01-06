# Verteilte-Systeme
VS WS 17/18

Um die Kommunikation zwischen mehreren Producern, Stores und Kühlschränken zu gewährleisten müssen Parameter bei Programmstart angegeben werden.

* Kühlschrank und Server müssen angepasst sein: UDP - Ports
* Server muss TCP-Port zugewiesen werden
* Server muss THRIFT - Port des Empfängers kennen
* Store muss THRIFT - Port zugewiesen werden zum Empfangen
* Store und Producer benötigen jeweils einen eindeutigen Namen.
* Parameter für MQTT müssen nicht geändert werden. DEFAULT-Werte zum Test ausreichend.


HINWEIS: Nicht alle Parameter werden durchweg von allen Programmen genutzt.


 * -b,--broker <ADDRESS>          The broker address.
 * -h,--help                      Give this help list.
 * -p,--port <PORT>               The broker port.
 * -P,--protocol <PROTO>          The broker protocol.
 * -s,--Store <STORE>             The Name of the Store.
 * -t,--topic <TOPIC>             The topic to listen to.
 * -T,--TcpListen <TCPLISTEN>     The Port used to receive messages via TCP.
 * -U,--UdpListen <UDPLISTEN>     The Port used to receive messages via UDP.
 * -u,--UdpSend <UDPSEND>         The Port used to send messages via UDP.
 * -x,--Producer <PRODUCER>       The Name of the Producer.
 * -y,--Thriftport <THRIFTPORT>   The Thriftport to use for communication.

**Fridge:**

Simuliert Sensoren, um Artikel aus dem Kühlschrank zu nehmen, oder hinzuzufügen.
Sensoren senden Daten per UDP an den Server, der den Kühlschrankinhalt verwaltet

**Server:**

Verwaltet UDP und TCP Connections.
Derzeit einfache Anfragen über den Browser möglich 127.0.0.1:6544/index.html per TCP
Beantwortet Anfragen einer GUI zum aktuellen Artikelstand.
Parallele Kommunikation möglich, Server arbeitet mit Threads.
Server bestellt bei Store nach, sobald Artikel unterhalb bestimmter Menge(Derzeit 3)

**Items:**

Ein Objekt wird in Fridge erzeugt, welches den aktuellen Stand der Artikel speichert. 

**Not Updated as of Nov17** **Nicht Funktionsfähig**

GUI und Client:
Oberfläche, die die aktuellen Artikel anzeigt. 
Client iefert die Daten für die GUI. Oberfläche und Kommunkation sind getrennt. Kommunkation mittels UDP

**Constants:**

Enthält Konstanten die in den Projekten notwendig sind

**MQTT:**

Enthält Klasse um Paramter bei Programmstart zu übergeben
Enthält Klasse um Nachrichten die per MQTT empfangen und gesendet werden sollen zu parsen
Enthält Klasse um Nachrichten zu per MQTT zu versenden

**OrganizeOffers:**

Enthält Klasse um Angebote zu erstellen und speichern

**Producer:**

Generiert Angebote und sendet diese per MQTT an die Stores
Kommunikation mittels MQTT läuft über iot.eclipse.org

**Store:**

Geschäft verkauft Dinge an Server(Kühlschrank) wenn auf Lager
Kontrolliert periodisch die Lagerbestände und kauft automatisch bei Producern ein
Kommunikation mittels MQTT läuft über iot.eclipse.org
