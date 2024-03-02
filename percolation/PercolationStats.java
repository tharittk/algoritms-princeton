public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    // perform independent trials on an n-by-n grid
    private double[] thrsholds;
    private int numTrial;

    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        if (trials <= 0) {
            throw new IllegalArgumentException("Trial must be greater than 0");
        }
        thrsholds = new double[trials];
        numTrial = trials;
        for (int i = 0; i < trials; i++) {
            int rowToOpen;
            int colToOpen;
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {

                rowToOpen = edu.princeton.cs.algs4.StdRandom.uniformInt(n) + 1;
                colToOpen = edu.princeton.cs.algs4.StdRandom.uniformInt(n) + 1;

                while (perc.isOpen(rowToOpen, colToOpen)) {
                    rowToOpen = edu.princeton.cs.algs4.StdRandom.uniformInt(n) + 1;
                    colToOpen = edu.princeton.cs.algs4.StdRandom.uniformInt(n) + 1;
                }

                perc.open(rowToOpen, colToOpen);
            }
            thrsholds[i] = ((double) perc.numberOfOpenSites()) / ((double) n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return edu.princeton.cs.algs4.StdStats.mean(thrsholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return edu.princeton.cs.algs4.StdStats.stddev(thrsholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double mu = mean();
        double std = stddev();
        return mu - (CONFIDENCE_95 * std / Math.sqrt(numTrial));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mu = mean();
        double std = stddev();
        return mu + (CONFIDENCE_95 * std / Math.sqrt(numTrial));
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats percStat = new PercolationStats(Integer.parseInt(args[0]),
                                                         Integer.parseInt(args[1]));
        System.out.println("mean\t=" + percStat.mean());
        System.out.println("stddev\t=" + percStat.stddev());
        System.out.println("95% confidence interval\t=[" + percStat.confidenceLo() + ", " +
                                   percStat.confidenceHi() + "]");


    }
}
