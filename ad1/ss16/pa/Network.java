package ad1.ss16.pa;

import java.util.*;

public class Network {

    private HashMap<Integer, LinkedHashSet<Integer>> G = new HashMap<>();
    private final int n;
    private int m;
    private boolean[] discovered;

    private int[] low;
    private int[] pre;
    private int cnt;
    private boolean[] articulation;

    public Network(int n) {
        this.n = n;
        this.m = 0;
        for (int i = 0; i < n; i++) {
            this.G.put(i, new LinkedHashSet<>());
        }
    }

    public int numberOfNodes() {
        return this.n;
    }

    public int numberOfConnections() {
        return m;
    }

    public void addConnection(int v, int w) {
        if (this.G.containsKey(v) && !this.G.get(v).contains(w) && v != w) {
            this.m++;
            this.G.get(v).add(w);
            this.G.get(w).add(v);
        }
    }

    public void addAllConnections(int v) {
        int c = 0;
        for (int u : this.G.keySet()) {
            if (!this.G.get(v).contains(u) && u != v) {
                c++;
                this.G.get(v).add(u);
                this.G.get(u).add(v);
            }
        }
        this.m += c;
    }

    public void deleteConnection(int v, int w) {
        if (this.G.containsKey(v) && this.G.get(v).contains(w)) {
            this.m--;
            this.G.get(v).remove(w);
            this.G.get(w).remove(v);
        }
    }

    public void deleteAllConnections(int v) {
        if (this.G.containsKey(v) && this.G.get(v).size() > 0) {
            this.m -= this.G.get(v).size();
            this.G.replace(v, new LinkedHashSet<>());
            for (int i = 0; i < this.n; i++) {
                this.G.get(i).remove(v);
            }
        }
    }

    public int numberOfComponents() {
        this.discovered = new boolean[this.n];
        int c = 0;
        for (int u : this.G.keySet()) {
            if (!this.discovered[u]) {
                c++;
                this.numberOfComponentsR(u);
            }
        }
        return c;
    }

    private void numberOfComponentsR(int u) {
        this.discovered[u] = true;
        for (int v : this.G.get(u)) {
            if (!this.discovered[v]) {
                this.numberOfComponentsR(v);
            }
        }
    }

    public boolean hasCycle() {
        this.discovered = new boolean[this.n];
        for (int u : this.G.keySet()) {
            if (!this.discovered[u] && hasCycleR(u, u)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCycleR(int u, int v) {
        this.discovered[u] = true;
        for (int w : this.G.get(u)) {
            if (!this.discovered[w]) {
                if(hasCycleR(w, u)) {
                    return true;
                }
            } else if (this.discovered[w] && v != w) { // changed
                return true;
            }
        }
        return false;
    }

    public int minimalNumberOfConnections(int start, int end) {
        if (this.m < 1) {
            return -1;
        }
        if (start == end) {
            return 0;
        }
        int[] distance = new int[this.n];
        boolean[] discovered = new boolean[this.n];
        Queue<Integer> Q = new LinkedList<>();
        distance[start] = 0;
        discovered[start] = true;
        Q.add(start);
        while (!Q.isEmpty()) {
            int v = Q.poll();
            for (int w : this.G.get(v)) {
                if (!discovered[w]) {
                    discovered[w] = true;
                    distance[w] = distance[v] + 1;
                    if (w == end) {
                        return distance[w];
                    }
                    Q.add(w);
                }
            }
        }
        return -1;
    }

    public List<Integer> criticalNodes() {
        this.low = new int[this.n];
        this.pre = new int[this.n];
        this.articulation = new boolean[this.n];
        for (int v = 0; v < this.n; v++) {
            this.low[v] = -1;
            this.pre[v] = -1;
        }
        for (int v = 0; v < this.n; v++) {
            if (this.pre[v] == -1) {
                criticalNodesR(v, v);
            }
        }
        List<Integer> L = new LinkedList<>();
        for (int i = 0; i < this.articulation.length; i++) {
            if (this.articulation[i]) {
                L.add(i);
            }
        }
        return L;
    }

    private void criticalNodesR(int u, int v) {
        int children = 0;
        this.pre[v] = this.cnt++;
        this.low[v] = this.pre[v];
        for (int w : this.G.get(v)) {
            if (this.pre[w] == -1) {
                children++;
                criticalNodesR(v, w);
                this.low[v] = Math.min(this.low[v], this.low[w]);
                if (this.low[w] >= this.pre[v] && u != v) {
                    this.articulation[v] = true;
                }
            } else if (w != u) {
                this.low[v] = Math.min(this.low[v], this.pre[w]);
            }
        }
        if (u == v && children > 1) {
            this.articulation[v] = true;
        }
    }

}