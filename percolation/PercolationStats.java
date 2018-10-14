package percolation;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/******************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java PercolationStats
 *  Dependencies: StdRandom.java, StdStats.java
 *
 *  Statistics for percolation ADT
 *
 *
 ******************************************************************************/

public class PercolationStats {
    private double[] results;
    private final double mean, stddev, confLo, confHi;
    private final double CONF_95 = 1.96;

    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) throw new IllegalArgumentException("Size and Trials need to be bigger than 0");
        results = new double[trials];

        for (int i = 0; i < trials; i++) {
            performTrial(i, n);
        }

        this.mean = StdStats.mean(results);
        this.stddev = trials > 1 ? StdStats.stddev(results) : Double.NaN;
        double sqT = Math.sqrt(trials);
        this.confLo = mean - (CONF_95 * stddev / sqT);
        this.confHi = mean + (CONF_95 * stddev / sqT);
    }

    public double mean() {
        return mean;
    }

    public double stddev() { return stddev; }

    public double confidenceLo() {
        return confLo;
    }

    public double confidenceHi() {
        return confHi;
    }

    private void performTrial(int i, int n) {
        Percolation p = new Percolation(n);
        while (!p.percolates()) {
            p.open(StdRandom.uniform(1, n +1), StdRandom.uniform(1, n + 1));
        }

        results[i] = (double) p.numberOfOpenSites() / (n * n);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, t);

        System.out.println("mean                     = " + ps.mean());
        System.out.println("stddev                   = " + ps.stddev());
        System.out.println("95% confidence interval  = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}
