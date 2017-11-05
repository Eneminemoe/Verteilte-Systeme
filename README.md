# Verteilte-Systeme
VS WS 17/18

Fridge:
Simuliert Sensoren, um Artikel aus dem Kühlschrank zu nehmen, oder hinzuzufügen.

Server:
Verwaltet UDP und TCP Connections.
Derzeit einfache Anfragen über den Browser möglich 127.0.0.1:6544/index.html per TCP
Beantwortet Anfragen einer GUI zum aktuellen Artikelstand.
Parallele Kommunikation mögliche, Server arbeitet mit Threads.

Items:
Ein Objekt wird in Fridge erzeugt, welches den aktuellen Stand der Artikel speichert. 

GUI und Client:
Oberfläche, die die aktuellen Artikel anzeigt. 
Client iefert die Daten für die GUI. Oberfläche und Kommunkation sind getrennt. Kommunkation mittels UDP
