import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // creates n-by-n grid, with all sites initially blocked
    private int count;
    private int nCol;
    private WeightedQuickUnionUF UF;
    private int[] grid;
    private int virtualTopIndex;
    private int virtualBottomIndex;


    public Percolation(int n) {
        count = 0;
        nCol = n;
        UF = WeightedQuickUnionUF(n * n + 2);
        virtualTopIndex = (n * n + 2) - 2;
        virtualBottomIndex = (n * n + 2) - 1;

        grid = new int[n * n];
        for (int i = 0; i < n * n; i++ ){
            grid[i] = 0;
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int idx;
        idx = (col - 1) + nCol * (row - 1);
        if (!isOpen(row, col) ){
            grid[idx] = 1;
            // union block

            //
            count ++;
        }

    }
    if (p < 0 || p >= n) {
        throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n-1));
    }
    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int idx;
        idx = (col - 1) + nCol * (row - 1);

        return grid[idx] == 1;

    }
    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int idx;

        idx = (col - 1) + nCol * (row - 1);
        return UF.find(idx) == UF.find(virtualTopIndex);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates(){
        return UF.find(virtualTopIndex) == UF.find(virtualBottomIndex);
    }

    // test client (optional)
    public static void main(String[] args){

    }
}
