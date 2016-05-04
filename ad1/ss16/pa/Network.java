package ad1.ss16.pa;

import java.util.*;

public class Network {

    private HashMap<Integer, LinkedList<Integer>> A = new HashMap<>();
    private final int n;
    private int m;
    private boolean[][] M;
    private boolean[] discovered;

    /**
     * O(n^2)
     * Organisation in Form einer Adjaszenzliste
     */
    public Network(int n) {
        this.n = n;
        this.m = 0;
        this.M = new boolean[n][n];
        for(int i = 0; i < n; i++) {
            this.A.put(i, new LinkedList<>());
        }
    }

    /**
     * O(1)
     * Liefert die Anzahl der Knoten zurück.
     */
    public int numberOfNodes() {
        return this.n;
    }

    /**
     * O(1)
     * Liefert die Anzahl der Verbindungen zurück.
     */
    public int numberOfConnections() {
        return m;
    }

    /**
     * O(1)
     * Fügt eine Verbindung im Netzwerk zwischen den Knoten v und w ein. Ist diese Verbindung schon
     * vorhanden,dann passiert nichts, d.h. die Verbindung bleibt im Netzwerk erhalten.
     */
    public void addConnection(int v, int w) {
        this.m++;
        this.A.get(v).add(w);
        this.M[v][w] = true;
        this.M[w][v] = true;
    }

    /**
     * Fügt Verbindungen von einem bestimmten Knoten v zu allen anderen Knoten ein. Hatte der Knoten
     * schon Verbindungen, dann bleiben diese erhalten.
     */
    public void addAllConnections(int v) {
        LinkedList<Integer> node = this.A.get(v);
        int c = 0;
        for(int k : this.A.keySet()) {
            if(k == v || node.contains(k)) {
                return;
            }
            c++;
            node.add(k);
        }
        this.m += c;
    }

    /**
     * O(n)
     * Entfernt eine Verbindung zwischen den Knoten v und w aus dem Netzwerk. Ist die Verbindung nicht
     * vorhanden, dann passiert nichts.
     */
    public void deleteConnection(int v, int w) {
        if(this.A.containsKey(v) && this.A.containsKey(w)) {
            this.m--;
            this.A.get(v).remove(w);
            this.M[v][w] = false;
            this.M[w][v] = false;
        }
    }

    /**
     * O(n)
     * Entfernt alle Verbindungen für einen bestimmten Knoten v aus dem Netzwerk. Hatte der Knoten noch
     * keine Verbindungen, dann passiert nichts.
     */
    public void deleteAllConnections(int v) {
        if(this.A.containsKey(v)) {
            this.m -= this.A.get(v).size();
            this.A.get(v).clear();
            this.M[v] = new boolean[this.n];
        }
    }

    /**
     * O(n+m)
     * Liefert die Anzahl der Zusammenhangskomponenten im Netzwerk zurück.
     * Verwendet: DFS
     */
    public int numberOfComponents() {
        this.discovered = new boolean[this.n];
        int c = 0;
        for(int u : this.A.keySet()) {
            if(!discovered[u]) {
                c++;
                numberOfComponentsR(u);
            }
        }
        return c;
    }

    private void numberOfComponentsR(int u) {
        this.discovered[u] = true;
        for(int v : this.A.get(u)) {
            if(!discovered[v]) {
                numberOfComponentsR(v);
            }
        }
    }

    /**
     * O(n+m)
     * Überprüft, ob das Netzwerk einen Kreis enthält. Wenn dies der Fall ist, wird true zurückgeliefert,
     * ansonsten false.
     * Verwendet: BFS
     */
    public boolean hasCycle() {
        boolean[] discovered = new boolean[this.n];
        discovered[0] = true;
        LinkedList<Integer> Q = new LinkedList<>();
        Q.add(0);
        while(!Q.isEmpty()) {
            int u = Q.pop();
            for(int v : this.A.get(u)) {
                if(!discovered[v]) {
                    discovered[v] = true;
                    Q.add(v);
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Liefert die kleinste Anzahl an Verbindungen, die durchlaufen werden muss, um von einem Startknoten
     * start zu einem Endknoten end zu gelangen. Sind start und end gleich, dann soll 0 zurückgeliefert
     * werden. Sind start und end nicht u ̈ber einen Pfad miteinander verbunden, dann wird -1 zurück-
     * geliefert.
     * Verwendet: Dijkstra's Algorithm
     * http://algs4.cs.princeton.edu/41graph/BreadthFirstPaths.java.html
     */
    public int minimalNumberOfConnections(int start, int end) {
        if(start == end) {
            return 0;
        }
        int c = this.numberOfConnections();
        int[] distance = new int[c];
        discovered = new boolean[this.n];
        // BFS
        LinkedList<Integer> Q = new LinkedList<>();
        distance[start] = 0;
        discovered[start] = true;
        Q.add(start);
        while (!Q.isEmpty()) {
            int v = Q.pop();
            for (int w : this.A.get(v)) {
                if (!discovered[w]) {
                    distance[w] = distance[v] + 1;
                    if(w == end) {
                        return distance[w];
                    }
                    discovered[w] = true;
                    Q.add(w);
                }
            }
        }
        return -1;
    }

    /**
     * Liefert eine Liste jener Knoten zurück, die als kritisch eingestuft werden. Ein Knoten ist kritisch, wenn
     * das Entfernen aller Verbindungen zu diesem Knoten nicht nur diesen Knoten isoliert, sondern auch seine
     * ursprüngliche Zusammenhangskomponente in drei oder mehr Zusammenhangskom- ponenten zerfallen lässt.
     */
    public List<Integer> criticalNodes() {
        List<Integer> critical = new LinkedList<>();
        int in = 0, out = 0, u = 0;
        return critical;
    }

//    /**
//     * O(n+m)
//     * Reverses the current graph
//     * @return HashMap
//     */
//    private HashMap<Integer, LinkedList<Integer>> reverse() {
//        HashMap<Integer, LinkedList<Integer>> rev = new HashMap<>();
//        int k = 0;
//        for(int i = 0; i < this.A.size(); i++) {
//            rev.put(i, new LinkedList<>());
//        }
//        for(LinkedList<Integer> L : this.A.values()) {
//            for(int u : L) {
//                rev.get(u).add(k);
//            }
//            k++;
//        }
//        return rev;
//    }

//    /**
//     * Does a Breath-First-Search on the given Graph
//     * @param s: Node
//     * @param G: Graph
//     * @return discovered: Array
//     */
//    private boolean[] BFS(HashMap<Integer, LinkedList<Integer>> G, int s) {
//        // BFS
//        boolean[] discovered = new boolean[G.size()];
//        discovered[s] = true;
//        LinkedList<Integer> Q = new LinkedList<>();
//        Q.add(s);
//        while(!Q.isEmpty()) {
//            int u = Q.pop();
//            for(int v : G.get(u)) {
//                if(!discovered[v]) {
//                    discovered[v] = true;
//                    Q.add(v);
//                }
//            }
//        }
//        return discovered;
//    }

    /**
     * Development
     */

    private void printConnections(HashMap<Integer, LinkedList<Integer>> G) {
        for(int u : G.keySet()) {
            for(int v: G.get(u)) {
                System.out.println(u + " -> " + v);
            }
        }
    }

    public static void main(String[] args) {
        Network network = new Network(13);
        network.addConnection(0,1);
        network.addConnection(0,2);
        network.addConnection(0,6);
        network.addConnection(1,2);
        network.addConnection(1,3);
        network.addConnection(1,4);
        network.addConnection(2,4);
        network.addConnection(2,6);
        network.addConnection(2,7);
        network.addConnection(3,4);
        network.addConnection(4,5);
        network.addConnection(6,7);
        network.addConnection(8,9);
        network.addConnection(8,10);
        network.addConnection(9,10);
        network.addConnection(10,11);
        network.addConnection(11,12);

//        Network network = new Network(7);
//        network.addConnection(0,1);
//        network.addConnection(1,2);
//        network.addConnection(2,3);
//        network.addConnection(3,4);
//        network.addConnection(4,5);
//        network.addConnection(5,6);

        System.out.println("numberOfNodes(): " + network.numberOfNodes());
        System.out.println("numberOfConnections(): " + network.numberOfConnections());
        System.out.println("numberOfComponents(): " + network.numberOfComponents());
        System.out.println("hasCycle(): " + network.hasCycle());
        System.out.println("minimalNumberOfConnections(0,7): " + network.minimalNumberOfConnections(0,7));
//        System.out.println(network.minimalNumberOfConnections(0,9));

    }
}



