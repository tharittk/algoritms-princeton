/* *****************************************************************************
 *  Name: Tharit T
 *  Date: March 16, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class Board {
    private int[][] board;
    private int dim;

    // private int rowIndexOfZero, colIndexOfZero;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException("null tiles");
        dim = tiles.length;
        board = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                board[i][j] = tiles[i][j];
            }
        }


    }

    // string representation of this board
    public String toString() {
        String out = Integer.toString(dim) + "\n";
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                out = out + Integer.toString(board[i][j]);
                if (j != dim - 1) out = out + "\t";
            }
            if (i != dim - 1) out = out + "\n";
        }
        return out;
    }

    // board dimension n
    public int dimension() {
        return this.dim;
    }

    // number of tiles out of place
    public int hamming() {
        int idealTile;
        int hammingDistance = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                // ideal tile
                if (i == dim - 1 && j == dim - 1) idealTile = 0;
                else idealTile = (j + 1) + (i) * dim;
                // if out-of-place and don't count 0
                if (idealTile != board[i][j] && board[i][j] != 0) {
                    hammingDistance++;
                }
            }
        }
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int stepToIdeal, rowIdeal, colIdeal;
        int mahattanDistance = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                rowIdeal = (board[i][j] - 1) / dim;
                colIdeal = board[i][j] - (rowIdeal * dim) - 1;

                // either row or col out-of-place and do not count 0
                if ((rowIdeal != i || colIdeal != j) && board[i][j] != 0) {
                    stepToIdeal = Math.abs(i - rowIdeal) + Math.abs(j - colIdeal);
                    mahattanDistance += stepToIdeal;
                }
            }
        }
        return mahattanDistance;
    }


    // is this board the goal board?
    public boolean isGoal() {
        int idealTile;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i == dim - 1 && j == dim - 1) idealTile = 0;
                else idealTile = (j + 1) + (i) * dim;
                // any element violates
                if (idealTile != board[i][j]) return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (this.board[i][j] != that.board[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    private Board createNeigborFromRowColSwap(int rowFrom, int colFrom, int rowTo, int colTo) {
        int tmp;
        // copy of the old board
        Board neighbor = new Board(this.board);
        tmp = neighbor.board[rowFrom][colFrom];
        neighbor.board[rowFrom][colFrom] = neighbor.board[rowTo][colTo];
        neighbor.board[rowTo][colTo] = tmp;

        return neighbor;
    }

    public Iterable<Board> neighbors() {
        // index of row and col of zero (space)
        int i0 = dim; // will cause run-time error if never change
        int j0 = dim;
        Queue<Board> q = new Queue<Board>();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (this.board[i][j] == 0) {
                    i0 = i;
                    j0 = j;
                    break;
                }
            }
        }
        // 2 neighbors = corner
        if (i0 == 0 && j0 == 0) {
            // swap place for zero
            // down
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0 + 1, j0));
            // right
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0, j0 + 1));
        }
        else if (i0 == 0 && j0 == (dim - 1)) {
            // down
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0 + 1, j0));
            // left
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0, j0 - 1));
        }
        else if (i0 == (dim - 1) && j0 == 0) {
            // up
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0 - 1, j0));
            // right
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0, j0 + 1));
        }
        else if (i0 == (dim - 1) && j0 == (dim - 1)) {
            // up
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0 - 1, j0));
            // left
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0, j0 - 1));
        }

        // 3 neighbors = edges but not corner
        // top
        else if (i0 == 0) {
            // down
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0 + 1, j0));
            // left
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0, j0 - 1));
            // right
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0, j0 + 1));

        }
        // bottom
        else if (i0 == (dim - 1)) {
            // up
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0 - 1, j0));
            // left
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0, j0 - 1));
            // right
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0, j0 + 1));
        }
        // left edge
        else if (j0 == 0) {
            // right
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0, j0 + 1));
            // up
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0 - 1, j0));
            // down
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0 + 1, j0));
        }
        // right edge
        else if (j0 == (dim - 1)) {
            // left
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0, j0 - 1));
            // up
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0 - 1, j0));
            // down
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0 + 1, j0));
        }
        // 4 neightbors = interior
        else {
            // left
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0, j0 - 1));
            // right
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0, j0 + 1));
            // up
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0 - 1, j0));
            // down
            q.enqueue(createNeigborFromRowColSwap(i0, j0, i0 + 1, j0));
        }

        return q;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // make sure you not exchanging place of zero
        int i1, i2, j1, j2;
        // index of row and col of zero (space)
        int i0 = dim; // will cause run-time error if never change
        int j0 = dim;

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] == 0) {
                    i0 = i;
                    j0 = j;
                    break;
                }
            }
        }
        // i1 = edu.princeton.cs.algs4.StdRandom.uniformInt(dim);
        // j1 = edu.princeton.cs.algs4.StdRandom.uniformInt(dim);
        //
        // // the first point must not be zero
        // while (i1 == i0 && j1 == j0) {
        //     i1 = edu.princeton.cs.algs4.StdRandom.uniformInt(dim);
        //     j1 = edu.princeton.cs.algs4.StdRandom.uniformInt(dim);
        // }
        //
        // i2 = edu.princeton.cs.algs4.StdRandom.uniformInt(dim);
        // j2 = edu.princeton.cs.algs4.StdRandom.uniformInt(dim);
        //
        // // the second point must not be at 0 or same point as (i1,j1)
        // while ((i2 == i0 && j2 == j0) || (i2 == i1 && j2 == j1)) {
        //     i1 = edu.princeton.cs.algs4.StdRandom.uniformInt(dim);
        //     j1 = edu.princeton.cs.algs4.StdRandom.uniformInt(dim);
        // }

        if (i0 == 0) i1 = i0 + 1;
        else i1 = i0 - 1;

        if (j0 == 0) j1 = j0 + 1;
        else j1 = j0 - 1;

        return createNeigborFromRowColSwap(i1, j0, i1, j1);

    }

    // public int[][] getBoard() {
    //     return this.board;
    // }
    //
    // public int getHammingDistance() {
    //     return this.hammingDistance;
    // }
    //
    // public int getMahattanDistance() {
    //     return this.mahattanDistance;
    // }

    // unit testing (not graded)
    public static void main(String[] args) {
        for (String filename : args) {
            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }
            Board initial = new Board(tiles);
            System.out.println(initial.toString());
            System.out.println("Hamming: " + initial.hamming());
            System.out.println("Manhattan: " + initial.manhattan());

            Iterable<Board> q = initial.neighbors();
            for (Board b : q) {
                System.out.println(b.toString());
            }

            System.out.println("Twin: " + initial.twin());

        }
    }

}
