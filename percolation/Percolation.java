import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    // creates n-by-n grid, with all sites initially blocked
    private int count;
    private int nCol;
    private WeightedQuickUnionUF uf;
    private boolean[] isClosedGrid;
    private int virtualTopIndex;
    private int virtualBottomIndex;


    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        count = 0;
        nCol = n;
        uf = new edu.princeton.cs.algs4.WeightedQuickUnionUF(n * n + 2);
        virtualTopIndex = (n * n + 2) - 2;
        virtualBottomIndex = (n * n + 2) - 1;

        isClosedGrid = new boolean[n * n];
        for (int i = 0; i < n * n; i++) {
            isClosedGrid[i] = true;
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row > nCol || col > nCol || row <= 0 || col <= 0) {
            throw new IllegalArgumentException("Row or Column exceeds range");
        }
        boolean leftBlockExist = true;
        boolean rightBlockExist = true;
        boolean topBlockExist = true;
        boolean bottomBlockExist = true;
        int idx;
        int idxTopBlock, idxRightBlock, idxLeftBlock, idxBottomBlock;

        if (!isOpen(row, col)) {

            idx = convertRowColToIndex(row, col);

            // edge case: top-left corner
            if (row == 1 && col == 1) {
                leftBlockExist = false;
                topBlockExist = false;
            }
            // edge case: top-right corner
            if (row == 1 && col == nCol) {
                rightBlockExist = false;
                topBlockExist = false;
            }
            // edge case: bottom-left corner
            if (row == nCol && col == 1) {
                leftBlockExist = false;
                bottomBlockExist = false;
            }
            // edge case: bottom-right corner
            if (row == nCol && col == nCol) {
                rightBlockExist = false;
                bottomBlockExist = false;
            }
            // edge case: top-row, left-edge, right-edge, bottom-row
            if (col == 1) leftBlockExist = false;
            if (col == nCol) rightBlockExist = false;

            if (row == 1) {
                topBlockExist = false;
                uf.union(virtualTopIndex, idx);
            }
            if (row == nCol) {
                bottomBlockExist = false;
                uf.union(virtualBottomIndex, idx);
            }

            // union with 4 neighbors
            isClosedGrid[idx] = false;

            if (topBlockExist) {
                idxTopBlock = convertRowColToIndex(row - 1, col);
                if (isOpen(row - 1, col)) uf.union(idxTopBlock, idx);
            }
            if (leftBlockExist) {
                idxLeftBlock = convertRowColToIndex(row, col - 1);
                if (isOpen(row, col - 1)) uf.union(idxLeftBlock, idx);
            }
            if (rightBlockExist) {
                idxRightBlock = convertRowColToIndex(row, col + 1);
                if (isOpen(row, col + 1)) uf.union(idxRightBlock, idx);
            }
            if (bottomBlockExist) {
                idxBottomBlock = convertRowColToIndex(row + 1, col);
                if (isOpen(row + 1, col)) uf.union(idxBottomBlock, idx);
            }

            // last row -- O(n) SLOW
            // for (int iCol = 0; iCol < nCol; iCol++) {
            //     int tmp = (iCol - 1) + nCol * (nCol - 1);
            //     if (grid[tmp] == 0) {
            //         if (uf.find(virtualTopIndex) == uf.find(tmp)) {
            //             uf.union(virtualBottomIndex, tmp);
            //         }
            //     }
            // }
            // connect to virtual bottom
            // this should also trigger if click any block in between and then make the grid percolate
            // if (row == nCol) {
            //     if (uf.find(virtualTopIndex) == uf.find(idx)) {
            //         uf.union(virtualBottomIndex, idx);
            //     }
            // }
            count++;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > nCol || col > nCol || row <= 0 || col <= 0) {
            throw new IllegalArgumentException("Row or Column exceeds range");
        }
        int idx;
        idx = convertRowColToIndex(row, col);
        return !isClosedGrid[idx];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > nCol || col > nCol || row <= 0 || col <= 0) {
            throw new IllegalArgumentException("Row or Column exceeds range");
        }
        int idx;
        idx = convertRowColToIndex(row, col);

        return uf.find(idx) == uf.find(virtualTopIndex);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        // fix graphic

        return uf.find(virtualTopIndex) == uf.find(virtualBottomIndex);
    }

    private int convertRowColToIndex(int row, int col) {
        return (col - 1) + nCol * (row - 1);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
