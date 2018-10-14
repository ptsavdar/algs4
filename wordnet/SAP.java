package wordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph graph;
    private final DeluxeBFS bfs;
    /**
     * Constructor
     * @param G a graph not necessarily a DAG
     */
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        graph = new Digraph(G);
        bfs = new DeluxeBFS(G);
    }

    public int length(int v, int w) {
        bfs.runBFS(v, w);

        return bfs.dist();
    }

    public int ancestor(int v, int w) {
        bfs.runBFS(v, w);

        return bfs.ancestor();
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        bfs.runBFS(v, w);

        return bfs.dist();
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        bfs.runBFS(v, w);

        return bfs.ancestor();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
