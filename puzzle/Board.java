package puzzle;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int dimension;
    private final int[] tiles;
    private final int hamming;
    private final int manhattan;

    public Board(int[][] blocks) {
        this.dimension = blocks.length;
        this.tiles = new int[dimension * dimension];
        int hamming = 0;
        int manhattan = 0;
        for(int i = 0; i < dimension; i++) {
            for(int j = 0; j < dimension; j++) {
                int idx = i * dimension + j;
                tiles[idx] = blocks[i][j];
                if (tiles[idx] != 0 && tiles[idx] != idx + 1) {
                    hamming++;
                    manhattan += Math.abs(i - (tiles[idx] - 1) / dimension) + Math.abs(j - (tiles[idx] - 1) % dimension);
                }
            }
        }
        this.hamming = hamming;
        this.manhattan = manhattan;
    }

    public int dimension() {
        return dimension;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return hamming == 0;
    }

    public Board twin() {
        int[][] twin = new int[dimension][dimension];
        // Copy board
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int idx = i * dimension + j;
                twin[i][j] = tiles[idx];
            }
        }
        if (dimension == 1) return new Board(twin);
        // Swap pair
        int i = 0;
        if (twin[0][0] == 0 || twin[0][1] == 0) i = 1;
        int tmp = twin[i][0];
        twin[i][0] = twin[i][1];
        twin[i][1] = tmp;

        return new Board(twin);
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension != this.dimension || that.hamming != this.hamming || that.manhattan != this.manhattan) return false;
        for (int i = 0; i < dimension * dimension; i++) {
            if (that.tiles[i] != this.tiles[i]) return false;
        }

        return true;
    }

    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<>();
        int[][] board = new int[dimension][dimension];
        int zi = -1, zj = -1;
        // Copy board
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int idx = i * dimension + j;
                if (tiles[idx] == 0) {
                    zi = i;
                    zj = j;
                }
                board[i][j] = tiles[idx];
            }
        }
        // Top neighbor
        if (zi > 0) {
            board[zi][zj] = board[zi - 1][zj];
            board[zi - 1][zj] = 0;
            neighbors.push(new Board(board));
            board[zi - 1][zj] = board[zi][zj];
            board[zi][zj] = 0;
        }
        // Bottom neighbor
        if (zi < dimension - 1) {
            board[zi][zj] = board[zi + 1][zj];
            board[zi + 1][zj] = 0;
            neighbors.push(new Board(board));
            board[zi + 1][zj] = board[zi][zj];
            board[zi][zj] = 0;
        }
        // Left neighbor
        if (zj > 0) {
            board[zi][zj] = board[zi][zj - 1];
            board[zi][zj - 1] = 0;
            neighbors.push(new Board(board));
            board[zi][zj - 1] = board[zi][zj];
            board[zi][zj] = 0;
        }
        // Right neighbor
        if (zj < dimension - 1) {
            board[zi][zj] = board[zi][zj + 1];
            board[zi][zj + 1] = 0;
            neighbors.push(new Board(board));
        }

        return neighbors;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension);
        for (int i = 0; i < dimension * dimension; i++) {
            if (i % dimension == 0) s.append("\n");
            s.append(String.format("%2d ", tiles[i]));
        }

        return s.toString();
    }

    public static void main(String[] args) {
        int[][] blocks = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        Board board = new Board(blocks);
        StdOut.println(board);
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        StdOut.println(board.twin());
        //for (Board neighbor : board.neighbors()) StdOut.println(neighbor);
    }
}
