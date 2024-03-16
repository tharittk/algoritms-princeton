/* *****************************************************************************
 *  Name: Tharit T
 *  Date: March 16, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

public class Board {
    private int[][] board;
    private int dim;
    private int hammingDistance;
    private int mahattanDistance;

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
        return dim;
    }

    // number of tiles out of place
    public int hamming() {
        int idealTile;
        hammingDistance = 0;
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

        mahattanDistance = 0;
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
        return true;
    }

    // does this board equal y?
    // public boolean equals(Object y) {}

    // all neighboring boards
    // public Iterable<Board> neighbors()

    // a board that is obtained by exchanging any pair of tiles
    // public Board twin()

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

        }
    }

}
