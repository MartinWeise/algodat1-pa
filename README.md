# 186.813 Algorithmen und Datenstrukturen 1 VU 6.0
## Programmieraufgabe

Sie sind Teil eines Teams, das eine Software für die Verwaltung von Daten eines Kommunikationsnetzwerks erstellen soll. Neben dem Sammeln und Aufbereiten von Daten aus externen Quellen müssen in diesem Programm Abfragen zur Netzwerkinfrastruktur implementiert werden. Dazu müssen die Daten in einer geeigneten Datenstruktur abgelegt werden und darauf die Abfragen effizient ermöglicht werden.

## Beschreibung

Das Netzwerk wird vereinfacht als Sammlung von Netzwerkknoten und den Verbindungen zwischen diesen Netzwerkknoten gesehen. Dabei gilt:

- Es wird am Anfang jedes Tests einer Instanz die Anzahl `n` der Knoten festgelegt. Diese Anzahl bleibt immer gleich, d.h. es werden keine neuen Knoten hinzugefügt bzw. gelöscht. Es wird angenommen, dass `n > 0` ist.
- Die Netzwerkknoten werden von `0` beginnend bis `n − 1` durchnummeriert.
- Zwischen zwei Netzwerkknoten kann eine Verbindung bestehen, über die Daten ausgetauscht werden. Die Daten können in beide Richtungen fließen. Für die Lösung der Aufgaben ist die Richtung nicht von Bedeutung.
- Ein Knoten hat keine Verbindung zu sich selbst (Schleife).
- Die erwarteten Netzwerke sind dünn besetzt.

Ihre Aufgabe besteht aus zwei Teilen. Einerseits müssen Sie eine passende Datenstruktur für das Speichern der Daten implementieren. Andererseits müssen Sie folgende Operationen auf dieser Datenstruktur unterstützen:

- `int numberOfNodes()`: Liefert die Anzahl der Knoten zurück.
- `int numberOfConnections()`: Liefert die Anzahl der Verbindungen zurück.
- `void addConnection(int v, int w)`: Fügt eine Verbindung im Netzwerk zwischen den Knoten `v` und `w` ein. Ist diese Verbindung schon vorhanden, dann passiert nichts, d.h. die Verbindung bleibt im Netzwerk erhalten.
- `void addAllConnections(int v)`: Fügt Verbindungen von einem bestimmten Knoten `v` zu allen anderen Knoten ein. Hatte der Knoten schon Verbindungen, dann bleiben diese erhalten.
- `void deleteConnection(int v, int w)`: Entfernt eine Verbindung zwischen den Knoten `v` und `w` aus dem Netzwerk. Ist die Verbindung nicht vorhanden, dann passiert nichts.
- `void deleteAllConnections(int v)`: Entfernt alle Verbindungen für einen bestimmten Knoten `v` aus dem Netzwerk. Hatte der Knoten noch keine Verbindungen, dann passiert nichts.
- `int numberOfComponents()`: Liefert die Anzahl der Zusammenhangskomponenten im Netzwerk zurück.
- `booleanhasCycle()`: Überprüft, ob das Netzwerk einen Kreis enthält. Wenn dies der Fall ist, wird `true` zurück geliefert, ansonsten `false`.
- `int minimalNumberOfConnections(int start, int end)`: Liefert die kleinste Anzahl an Verbindungen, die durchlaufen werden muss, um von einem Startknoten `start` zu einem Endknoten `end` zu gelangen. Sind `start` und end gleich, dann soll 0 zurückgeliefert werden. Sind `start` und `end` nicht über einen Pfad miteinander verbunden, dann wird `-1` zurück geliefert.
- `List<Integer> criticalNodes()`: Liefert eine Liste jener Knoten zurück, die als kritisch eingestuft werden. Ein Knoten ist kritisch, wenn das Entfernen aller Verbindungen zu diesem Knoten nicht nur diesen Knoten isoliert, sondern auch seine ursprüngliche Zusammenhangskomponente in drei oder mehr Zusammenhangskomponenten zerfallen lässt.

## Beispiel

Es sei folgendes Netzwerk gegeben:

![Graph](/resources/graph.png)

Es ergeben sich dann z.B. folgende Rückgabewerte:

- `numberOfNodes()`: 13
- `numberOfConnections()`: 16
- `numberOfComponents()`: 2
- `hasCycle()`: true
- `minimalNumberOfConnections(0, 5)`: 3
- `minimalNumberOfConnections(0, 9)`: -1
- `criticalNodes()`: [4, 11, 10]

TODO: English version