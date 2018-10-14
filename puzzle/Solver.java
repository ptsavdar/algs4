package puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private boolean solvable;
    private int moves;
    private Stack<Board> solution;

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        solvable = false;
        moves = -1;
        solution = null;
        MinPQ<Node> pq1 = new MinPQ<>();
        MinPQ<Node> pq2 = new MinPQ<>();
        pq1.insert(new Node(initial, null));
        pq2.insert(new Node(initial.twin(), null));
        Node node = null;
        while (!pq1.isEmpty() && !pq2.isEmpty()) {
            Node nd1 = pq1.delMin();
            if (nd1.board.isGoal()) {
                node = nd1;
                solvable = true;
                break;
            }
            Node nd2 = pq2.delMin();
            if (nd2.board.isGoal()) {
                break;
            }
            for (Board neighbor : nd1.board.neighbors()) {
                if (nd1.prev == null || !neighbor.equals(nd1.prev.board)) {
                    pq1.insert(new Node(neighbor, nd1));
                }
            }

            for (Board neighbor : nd2.board.neighbors()) {
                if (nd2.prev == null || !neighbor.equals(nd2.prev.board)) {
                    pq2.insert(new Node(neighbor, nd2));
                }
            }
        }
        if (solvable && node != null) {
            solution = new Stack<>();
            moves = node.moves;
            while (node != null) {
                solution.push(node.board);
                node = node.prev;
            }
        }
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return solvable ? moves : -1;
    }

    public Iterable<Board> solution() {
        return solution;
    }

    private class Node implements Comparable<Node>{
        private final Board board;
        private final int moves;
        private final int priority;
        private final Node prev;

        public Node(Board board, Node prev) {
            this.board = board;
            this.prev = prev;
            this.moves = prev == null ? 0 : prev.moves + 1;
            this.priority = moves + board.manhattan();
        }

        @Override
        public int compareTo(Node node) {
            return this.priority - node.priority;
        }
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
