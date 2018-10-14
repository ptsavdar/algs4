package percolation;
/******************************************************************************
 *  Compilation:  javac Percolation.java
 *  Execution:    java Percolation
 *  Dependencies: WeightedQuickUnionUF.java
 *
 *  ADT for Percolation object
 *
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] grid; // Percolation grid
    private final int size; // grid size
    private int openSites;
    private final WeightedQuickUnionUF uf, uff;

    /**
     * Creates a nXn grid, with all sites blocked
     * @param n int
     */
    public Percolation(int n) {
        if (n < 1) throw new IllegalArgumentException("Size needs to be bigger than 0");
        size = n;
        grid = new boolean[n * n + 2];
        grid[0] = true;
        grid[n * n + 1] = true;
        uf = new WeightedQuickUnionUF(n * n + 2);
        uff = new WeightedQuickUnionUF(n * n + 1);
        openSites = 0;
    }

    /**
     * Opens site (row, col) if it is not already open
     * @param row int
     * @param col int
     */
    public void open(int row, int col) {
        checkBounds(row, col);
        int idx = calcIndex(row, col);
        if (!grid[idx]) {
            grid[idx] = true;
            openSites++;
            if (row > 1 && isOpen(row - 1, col)) doubleUnion(idx, calcIndex(row - 1, col));
            if (row < size && isOpen(row + 1, col)) doubleUnion(idx, calcIndex(row + 1, col));
            if (col > 1 && isOpen(row, col - 1)) doubleUnion(idx, calcIndex(row, col - 1));
            if (col < size && isOpen(row, col + 1)) doubleUnion(idx, calcIndex(row, col + 1));
            // Union with top and bottom nodes
            if (row == 1) doubleUnion(idx, 0);
            if (row == size) uf.union(idx, size * size + 1);
        }
    }

    /**
     * Is site (row, col) open?
     *
     * @param row int
     * @param col int
     * @return boolean
     */
    public boolean isOpen(int row, int col) {
        checkBounds(row, col);
        return grid[calcIndex(row, col)];
    }

    /**
     * Is site (row, col) full? (Connected to the top site)
     * @param row int
     * @param col int
     * @return boolean
     */
    public boolean isFull(int row, int col) {
        checkBounds(row, col);
        return isOpen(row, col) && uff.connected(0, calcIndex(row, col));
    }

    /**
     * Number of sites opened in the system
     *
     * @return int
     */
    public int numberOfOpenSites() {
        return openSites;
    }

    /**
     * Does the system percolates?
     *
     * @return boolean
     */
    public boolean percolates() {
        return uf.connected(0, size * size + 1);
    }

    /**
     * Calculates index of site (row, col)
     *
     * @param row int
     * @param col int
     * @return int
     */
    private int calcIndex(int row, int col) {
        return (row - 1) * size + (col - 1) + 1;
    }

    /**
     * Checks if site (row, col) is inside allowed bounds
     *
     * @param row int
     * @param col int
     */
    private void checkBounds(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size)
            throw new IllegalArgumentException("row and col must be in [1, " + size + "]");
    }

    private void doubleUnion(int idx1, int idx2) {
        uf.union(idx1, idx2);
        uff.union(idx1, idx2);
    }
}
