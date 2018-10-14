package wordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import java.util.HashSet;

public class DeluxeBFS {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph G;
    private boolean[][] marked;  // marked[v] = is there an s->v path?
    private int[][] distTo;      // distTo[v] = length of shortest s->v path
    private int minDist, minAncestor;
    private HashSet<Integer> lastChange;
    private HashSet<Integer> prevV, prevW;

    public DeluxeBFS(Digraph G) {
        this.G = new Digraph(G);
        marked = new boolean[2][G.V()];
        distTo = new int[2][G.V()];
        minAncestor = -1;
        minDist = INFINITY;
        lastChange = new HashSet<>();
        prevV = new HashSet<>();
        prevW = new HashSet<>();
        for (int v = 0; v < G.V(); v++)
            distTo[0][v] = distTo[1][v] = INFINITY;
    }

    /**
     * Computes the shortest path between v and w
     * @param v the first vertex
     * @param w the second vertex
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public void runBFS(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        if (isPrev(v, w)) return;
        initializeRun(v, w);
        bfs(v, w);
    }

    /**
     * Computes shortest path between two vertices list
     * @param v the first vertices
     * @param w the second vertices
     * @throws IllegalArgumentException unless each vertex {@code v} in
     *         {@code sources} satisfies {@code 0 <= v < V}
     */
    public void runBFS(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        if (isPrev(v, w)) return;
        initializeRun(v, w);
        bfs(v, w);
    }

    // BFS from single source
    private void bfs(int v, int w) {
        if (v == w) {
            minAncestor = v;
            minDist = 0;
        }
        // Initialize 2 queues one for v
        Queue<Integer> qv = new Queue<>();
        initializeVertex(0, v, qv);
        // and one for w
        Queue<Integer> qw = new Queue<>();
        initializeVertex(1, w, qw);
        // Perform bfs in both queues
        boolean found = false;
        while (!found && (!qv.isEmpty() || !qw.isEmpty())) {
            // Bfs for v
            if (!qv.isEmpty()) {
                found |= lockstep(0, qv);
            }
            // Bfs for w
            if (!qw.isEmpty()) {
                found |= lockstep(1, qw);
            }
        }
    }

    // BFS from multiple sources
    private void bfs(Iterable<Integer> v, Iterable<Integer> w) {
        // Initialize 2 queues one for v
        Queue<Integer> qv = new Queue<>();
        initializeVertices(0, v, qv);
        // and one for w
        Queue<Integer> qw = new Queue<>();
        initializeVertices(1, w, qw);
        // Perform bfs in both queues
        boolean found = false;
        while (!found && (!qv.isEmpty() || !qw.isEmpty())) {
            // Bfs for v
            if (!qv.isEmpty()) {
                found |= lockstep(0, qv);
            }
            // Bfs for w
            if (!qw.isEmpty()) {
                found |= lockstep(1, qw);
            }
        }
    }

    public int dist()
    {
        return this.minAncestor == -1 ? -1 : this.minDist;
    }

    public int ancestor()
    {
        return this.minAncestor;
    }

    private void initializeVertex(int idx, int v, Queue<Integer> q) {
        marked[idx][v] = true;
        distTo[idx][v] = 0;
        q.enqueue(v);
        lastChange.add(v);
    }

    private void initializeVertices(int idx, Iterable<Integer> v, Queue<Integer> q) {
        for (int s : v) {
            marked[idx][s] = true;
            distTo[idx][s] = 0;
            q.enqueue(s);
            lastChange.add(s);
        }
    }

    private boolean lockstep(int idx, Queue<Integer> q) {
        int y = q.dequeue();
        if (marked[(idx + 1) % 2][y]) {
            int dist = distTo[0][y] + distTo[1][y];
            if (dist < this.minDist) {
                this.minDist = dist;
                this.minAncestor = y;
            }
        }
        for (int x : G.adj(y)) {
            if (!marked[idx][x]) {
                distTo[idx][x] = distTo[idx][y] + 1;
                marked[idx][x] = true;
                q.enqueue(x);
                lastChange.add(x);
            }
        }

        return false;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = marked[0].length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int V = marked[0].length;
        for (int v : vertices) {
            if (v < 0 || v >= V) {
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
            }
        }
    }

    private boolean isPrev(int v, int w) {
        return prevW.size() == 1 && prevW.contains(w) && prevV.size() == 1 && prevV.contains(v);
    }

    private boolean isPrev(Iterable<Integer> v, Iterable<Integer> w) {
        int sizeV = 0;
        for (int i : v) {
            sizeV++;
            if (!prevV.contains(i)) return false;
        }
        int sizeW = 0;
        for (int i : w) {
            sizeW++;
            if (!prevW.contains(i)) return false;
        }

        return sizeV == prevV.size() && sizeW == prevW.size();
    }

    private void initializeRun(int v, int w) {
        this.initializeRun();
        prevV = new HashSet<>();
        prevV.add(v);
        prevW = new HashSet<>();
        prevW.add(w);
    }

    private void initializeRun(Iterable<Integer> v, Iterable<Integer> w) {
        this.initializeRun();
        prevV = new HashSet<>();
        for (int i : v) {
            prevV.add(i);
        }
        prevW = new HashSet<>();
        for (int i : w) {
            prevW.add(i);
        }
    }

    private void initializeRun() {
        for (int i : lastChange) {
            marked[0][i] = marked[1][i] = false;
            distTo[0][i] = distTo[1][i] = INFINITY;
        }
        minDist = INFINITY;
        minAncestor = -1;
        lastChange = new HashSet<>();
    }
}
