package boggle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.TreeSet;

public class BoggleSolver {
    // TODO: Try TST instead of R-way ST
    private final TrieST dic;
    private int prevM, prevN;
    private ArrayList<ArrayList<Integer>> prevGraph;
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException();
        dic = new TrieST();
        for (int i = 0; i < dictionary.length; i++) {
            if (dictionary[i] == null) throw new IllegalArgumentException();
            dic.put(dictionary[i], scoreOf(dictionary[i].length()));
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) throw new IllegalArgumentException();
        TreeSet<String> validWords = new TreeSet<>();
        buildGraph(board.rows(), board.cols());

        for (int i = 0; i < board.rows() * board.cols(); i++) {
            dfs(i, board, validWords);
        }

        return validWords;
    }

    private void buildGraph(int m, int n) {
        if (m == prevM && n == prevN) return;
        prevM = m;
        prevN = n;
        prevGraph = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int idx = i * n + j;
                ArrayList<Integer> adj = new ArrayList<>();
                // Top neighbor
                if (i > 0) adj.add(idx - n);
                // Left neighbor
                if (j > 0) adj.add(idx - 1);
                // Bottom neighbor
                if (i < m - 1) adj.add(idx + n);
                // Right neighbor
                if (j < n - 1) adj.add(idx + 1);
                // Top Left neighbor
                if (i > 0 && j > 0) adj.add(idx - n - 1);
                // Top Right neighbor
                if (i > 0 && j < n - 1) adj.add(idx - n + 1);
                // Bottom Left neighbor
                if (i < m - 1 && j > 0) adj.add(idx + n - 1);
                // Bottom Right neighbor
                if (i < m - 1 && j < n - 1) adj.add(idx + n + 1);
                prevGraph.add(adj);
            }
        }
    }

    private void dfs(int i, BoggleBoard board, TreeSet<String> validWords) {
        char c = board.getLetter(i / board.cols(), i % board.cols());
        StringBuilder sb = new StringBuilder((c == 'Q') ? "QU" : Character.toString(c));
        Node start = this.dic.root.next[c - TrieST.alpha];
        if (start != null && c == 'Q') start = start.next['U' - TrieST.alpha];
        if (start != null)
            dfs(i, new ArrayList<>(), sb, start, board, validWords);
    }

    private void dfs(int idx, ArrayList<Integer> visited, StringBuilder prefix, Node prevNode, BoggleBoard board, TreeSet<String> validWords) {
        visited.add(idx);
        if (prevNode.val > 0) validWords.add(prefix.toString());
        for (int adj : prevGraph.get(idx)) {
            int i = adj / board.cols(), j = adj % board.cols();
            char chr = board.getLetter(i, j);
            Node newNode = prevNode.next[chr - TrieST.alpha];
            if (newNode != null && chr == 'Q') newNode = newNode.next['U' - TrieST.alpha];
            if (newNode != null && !visited.contains(adj))
                dfs(adj, new ArrayList<>(visited), new StringBuilder(prefix).append(chr == 'Q' ? "QU" : chr), newNode, board, validWords);
        }
    }

    public int scoreOf(String word) {
        if (word == null) throw new IllegalArgumentException();
        Node nd = dic.get(this.dic.root, word, 0);
        return nd == null || nd.val < 0 ? 0 : nd.val;
    }

    private int scoreOf(int l) {
        if (l < 3) return 0;
        if (l < 5) return 1;
        if (l == 5) return 2;
        if (l == 6) return 3;
        if (l == 7) return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

    // 26-way trie node
    private static class Node {
        private Integer val;
        private Node[] next = new Node[TrieST.R];

        private Node() {
            val = -1;
        }
    }

    private class TrieST {
        private static final int R = 26;        // A-Z, 26 capital letters
        private static final int alpha = 'A';


        private Node root;      // root of trie

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[c - alpha], key, d+1);
        }

        /**
         * Inserts the key-value pair into the symbol table, overwriting the old value
         * with the new value if the key is already in the symbol table.
         * If the value is {@code null}, this effectively deletes the key from the symbol table.
         * @param key the key
         * @param val the value
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public void put(String key, Integer val) {
            if (key == null) throw new IllegalArgumentException("first argument to put() is null");
            if (val != null) root = put(root, key, val, 0);
        }

        private Node put(Node x, String key, Integer val, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                x.val = val;
                return x;
            }
            char c = key.charAt(d);
            x.next[c - alpha] = put(x.next[c - alpha], key, val, d+1);
            return x;
        }
    }
}
