import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.util.Arrays;
import java.util.HashSet;

public class BoggleSolver {
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    private TST<Integer> tries;

    public BoggleSolver(String[] dictionary) {
        tries = new TST<Integer>();
        for (String s : dictionary) {
            tries.put(s, s.length());
        }

    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int nrow = board.rows();
        int ncol = board.cols();
        boolean[] marked = new boolean[nrow * ncol];
        HashSet<String> acc = new HashSet<String>();

        for (int i = 0; i < nrow; i++) {
            for (int j = 0; j < ncol; j++) {
                marked[i * ncol + j] = false;
            }
        }
        for (int i = 0; i < nrow; i++) {
            for (int j = 0; j < ncol; j++) {
                char c = board.getLetter(i, j);
                dfs(Character.toString(c), i, j, marked, board, acc);
            }
        }
        return acc;
    }

    // dfs and add the valid word as you find it, stop otherwise
    private void dfs(String s, int row, int col, boolean[] marked, BoggleBoard board,
                     HashSet<String> acc) {
        // System.out.println("Exploring: " + s);
        marked[row * board.cols() + col] = true;
        if (s.length() <= 2) { // down more
            if (row > 0) { // above
                boolean[] m1 = Arrays.copyOf(marked, marked.length);
                dfs(s + board.getLetter(row - 1, col), row - 1, col, m1, board, acc);
            }
            if (row < board.rows() - 1) { // below
                boolean[] m2 = Arrays.copyOf(marked, marked.length);
                dfs(s + board.getLetter(row + 1, col), row + 1, col, m2, board, acc);
            }
            if (col > 0) { // left
                boolean[] m3 = Arrays.copyOf(marked, marked.length);
                dfs(s + board.getLetter(row, col - 1), row, col - 1, m3, board, acc);
            }
            if (col < board.cols() - 1) { // right
                boolean[] m4 = Arrays.copyOf(marked, marked.length);
                dfs(s + board.getLetter(row, col + 1), row, col + 1, m4, board, acc);
            }
            if (row > 0 && col > 0) { // upper-left corner
                boolean[] m5 = Arrays.copyOf(marked, marked.length);
                dfs(s + board.getLetter(row - 1, col - 1), row - 1, col - 1, m5, board, acc);
            }
            if (row < board.rows() - 1 && col > 0) { // lower-left corner
                boolean[] m6 = Arrays.copyOf(marked, marked.length);
                dfs(s + board.getLetter(row + 1, col - 1), row + 1, col - 1, m6, board, acc);
            }
            if (row > 0 && col < board.cols() - 1) { // upper-right corner
                boolean[] m7 = Arrays.copyOf(marked, marked.length);
                dfs(s + board.getLetter(row - 1, col + 1), row - 1, col + 1, m7, board, acc);
            }
            if (row < board.rows() - 1 && col < board.cols() - 1) { // lower-right corner
                boolean[] m8 = Arrays.copyOf(marked, marked.length);
                dfs(s + board.getLetter(row + 1, col + 1), row + 1, col + 1, m8, board, acc);
            }
        }
        else if (this.tries.contains(s)) { // length > 2
            acc.add(s);
            if (row > 0) { // above
                boolean[] m1 = Arrays.copyOf(marked, marked.length);
                dfs(s + board.getLetter(row - 1, col), row - 1, col, m1, board, acc);
            }
            if (row < board.rows() - 1) { // below
                boolean[] m2 = Arrays.copyOf(marked, marked.length);
                dfs(s + board.getLetter(row + 1, col), row + 1, col, m2, board, acc);
            }
            if (col > 0) { // left
                boolean[] m3 = Arrays.copyOf(marked, marked.length);
                dfs(s + board.getLetter(row, col - 1), row, col - 1, m3, board, acc);
            }
            if (col < board.cols() - 1) { // right
                boolean[] m4 = Arrays.copyOf(marked, marked.length);
                dfs(s + board.getLetter(row, col + 1), row, col + 1, m4, board, acc);
            }
            if (row > 0 && col > 0) { // upper-left corner
                boolean[] m5 = Arrays.copyOf(marked, marked.length);
                dfs(s + board.getLetter(row - 1, col - 1), row - 1, col - 1, m5, board, acc);
            }
            if (row < board.rows() - 1 && col > 0) { // lower-left corner
                boolean[] m6 = Arrays.copyOf(marked, marked.length);
                dfs(s + board.getLetter(row + 1, col - 1), row + 1, col - 1, m6, board, acc);
            }
            if (row > 0 && col < board.cols() - 1) { // upper-right corner
                boolean[] m7 = Arrays.copyOf(marked, marked.length);
                dfs(s + board.getLetter(row - 1, col + 1), row - 1, col + 1, m7, board, acc);
            }
            if (row < board.rows() - 1 && col < board.cols() - 1) { // lower-right corner
                boolean[] m8 = Arrays.copyOf(marked, marked.length);
                dfs(s + board.getLetter(row + 1, col + 1), row + 1, col + 1, m8, board, acc);
            }
        } // stop dfs otherwise (prefix no longer matches)
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (tries.contains(word)) {
            int length = word.length();
            int points;
            switch (length) {
                case 3:
                    points = 1;
                    break;
                case 4:
                    points = 1;
                    break;
                case 5:
                    points = 2;
                    break;
                case 6:
                    points = 3;
                    break;
                case 7:
                    points = 5;
                    break;
                default:
                    points = 11;
                    break;
            }
            return points;
        }
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
