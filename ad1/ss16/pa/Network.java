package ad1.ss16.pa;

import java.util.*;

public class Network {

    private HashMap<Integer, LinkedHashSet<Integer>> G = new HashMap<>();
    private HashMap<Integer, LinkedHashSet<Integer>> Gu = new HashMap<>();
    private final int n;
    private int m;
    private boolean[] discovered;

    private int[] low;
    private int[] pre;
    private int cnt;
    private boolean[] articulation;

    /**
     * O(n)
     * Organisation in Form einer Adjaszenzliste
     */
    public Network(int n) {
        this.n = n;
        this.m = 0;
        for(int i = 0; i < n; i++) {
            this.G.put(i, new LinkedHashSet<>());
            this.Gu.put(i, new LinkedHashSet<>());
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
        this.G.get(v).add(w);
        this.Gu.get(v).add(w);
        this.Gu.get(w).add(v);
    }

    /**
     * O(n)
     * Fügt Verbindungen von einem bestimmten Knoten v zu allen anderen Knoten ein. Hatte der Knoten
     * schon Verbindungen, dann bleiben diese erhalten.
     */
    public void addAllConnections(int v) {
        for(int u : this.G.keySet()) {
            if(!this.G.get(v).contains(u) && u != v) {
                this.G.get(v).add(u);
                this.Gu.get(v).add(u);
                this.Gu.get(u).add(v);
            }
        }
    }

    /**
     * O(n)
     * Entfernt eine Verbindung zwischen den Knoten v und w aus dem Netzwerk. Ist die Verbindung nicht
     * vorhanden, dann passiert nichts.
     */
    public void deleteConnection(int v, int w) {
        if(this.G.containsKey(v) && this.G.get(v).contains(w)) {
            this.m--;
            this.G.get(v).remove(w);
            this.Gu.get(v).remove(w);
            this.Gu.get(w).remove(v);
        }
    }

    /**
     * O(n)
     * Entfernt alle Verbindungen für einen bestimmten Knoten v aus dem Netzwerk. Hatte der Knoten noch
     * keine Verbindungen, dann passiert nichts.
     */
    public void deleteAllConnections(int v) {
        if(this.G.get(v).size() > 0) {
            this.m -= this.G.get(v).size();
            this.G.replace(v,new LinkedHashSet<>());
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
        for (int u : this.Gu.keySet()) {
            if (!this.discovered[u]) {
                c++;
                this.numberOfComponentsR(u);
            }
        }
        return c;
    }

    private void numberOfComponentsR(int u) {
        this.discovered[u] = true;
        for(int v : this.Gu.get(u)) {
            if(!this.discovered[v]) {
                this.numberOfComponentsR(v);
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
            for(int v : this.G.get(u)) {
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
        if(this.m < 1) {
            return -1;
        }
        if(start == end) {
            return 0;
        }
        int[] distance = new int[this.n];
        boolean[] discovered = new boolean[this.n];
        // BFS
        LinkedList<Integer> Q = new LinkedList<>();
        distance[start] = 0;
        discovered[start] = true;
        Q.add(start);
        while (!Q.isEmpty()) {
            int v = Q.pop();
            for (int w : this.G.get(v)) {
                if (!discovered[w]) {
                    distance[w] = distance[v] + 1;
                    if (w == end) {
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
     * ursprüngliche Zusammenhangskomponente in drei oder mehr Zusammenhangskomponenten zerfallen lässt.
     */
    public List<Integer> criticalNodes() {
        low = new int[this.n];
        pre = new int[this.n];
        articulation = new boolean[this.n];
        for (int v = 0; v < this.n; v++) {
            low[v] = -1;
            pre[v] = -1;
        }
        for(int v = 0; v < this.n; v++) {
            if (pre[v] == -1) {
                dfs(v, v);
            }
        }
        List<Integer> L = new LinkedList<>();
        for(int i = 0; i < this.articulation.length; i++) {
            if(this.articulation[i]) {
                L.add(i);
            }
        }
        return L;
    }

    private void dfs(int u, int v) {
        int children = 0;
        pre[v] = cnt++;
        low[v] = pre[v];
        for(int w : this.Gu.get(v)) {
            if(pre[w] == -1) {
                children++;
                dfs(v, w);
                // update low number
                low[v] = Math.min(low[v], low[w]);
                // non-root of DFS is an articulation point if low[w] >= pre[v]
                if(low[w] >= pre[v] && u != v) {
                    articulation[v] = true;
                }
            } else if(w != u) { // update low number - ignore reverse of edge leading to v
                low[v] = Math.min(low[v], pre[w]);
            }
        }

        // root of DFS is an articulation point if it has more than 1 child
        if (u == v && children > 1) {
            articulation[v] = true;
        }
    }

    public static void main(String[] args) {
//        Network network = new Network(13);
//        network.addConnection(0,1);
//        network.addConnection(0,2);
//        network.addConnection(0,6);
//        network.addConnection(1,2);
//        network.addConnection(1,3);
//        network.addConnection(1,4);
//        network.addConnection(2,4);
//        network.addConnection(2,6);
//        network.addConnection(2,7);
//        network.addConnection(3,4);
//        network.addConnection(4,5);
//        network.addConnection(6,7);
//        network.addConnection(8,9);
//        network.addConnection(8,10);
//        network.addConnection(9,10);
//        network.addConnection(10,11);
//        network.addConnection(11,12);

        Network network = new Network(100);
        network.addConnection(1,6);
        network.addAllConnections(0);


        System.out.println("numberOfNodes(): " + network.numberOfNodes());
        System.out.println("numberOfConnections(): " + network.numberOfConnections());
        System.out.println("numberOfComponents(): " + network.numberOfComponents());
        System.out.println("hasCycle(): " + network.hasCycle());
        System.out.println("minimalNumberOfConnections(0,5): " + network.minimalNumberOfConnections(0,5));
        System.out.println("minimalNumberOfConnections(0,9): " + network.minimalNumberOfConnections(0,9));
        System.out.println("criticalNodes(): " + network.criticalNodes());

    }
}



