public class PercolationStats {
    // perform independent trials on an n-by-n grid
    private double[] thrsholds;
    private int T;

    public PercolationStats(int n, int trials) {
        thrsholds = new double[trials];
        T = trials;
        for (int i = 0; i < trials; i++) {
            int idxToOpen;

            int iRow, iCol;
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                idxToOpen = edu.princeton.cs.algs4.StdRandom.discrete(perc.grid);
                iRow = (idxToOpen / n) + 1;
                iCol = idxToOpen - ((n) * (iRow - 1)) + 1;
                perc.open(iRow, iCol);
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
        return mu - (1.96 * std / Math.sqrt(T));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mu = mean();
        double std = stddev();
        return mu + (1.96 * std / Math.sqrt(T));
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
