import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {

    // constant for 95% confidence interval
    private static final double CONFIDENCE_95 = 1.96;
    // array of percolation thresholds
    // (ratio of open/total sites at which grid percolates)
    private double[] thresholds;
    // number of trials
    private int trials;
    // total time elapsed for all trials
    private double elapsedTime;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        // throw illegal argument exception if n or trials are not positive
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException(
                    "grid size and trials must be above zero");
        }
        this.trials = trials;
        // set threshold array length to number of trials
        thresholds = new double[trials];

        Stopwatch stopwatch = new Stopwatch();
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            // find and open random blocked site until grid percolates
            while (!percolation.percolates()) {
                int randRow, randCol;
                do {
                    randRow = StdRandom.uniformInt(n);
                    randCol = StdRandom.uniformInt(n);
                } while (percolation.isOpen(randRow, randCol));
                percolation.open(randRow, randCol);
            }
            double numOpenSites = percolation.numberOfOpenSites();
            // add percolation threshold to array
            thresholds[i] = numOpenSites / (n * n);
        }
        elapsedTime = stopwatch.elapsedTime();
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return (mean() - (CONFIDENCE_95 * stddev() / Math.sqrt(trials)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return (mean() + (CONFIDENCE_95 * stddev() / Math.sqrt(trials)));
    }

    // returns time elapsed through all trials
    private double elapsedTime() {
        return elapsedTime;
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length == 2) {
            int n = Integer.parseInt(args[0]);
            int trials = Integer.parseInt(args[1]);
            PercolationStats test = new PercolationStats(n, trials);
            System.out.println("mean() = " + test.mean());
            System.out.println("stddev() = " + test.stddev());
            System.out.println("confidenceLow() = " + test.confidenceLow());
            System.out.println("confidenceHigh() = " + test.confidenceHigh());
            System.out.println("elapsed time = " + test.elapsedTime());
        }
    }
}