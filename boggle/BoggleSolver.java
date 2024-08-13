import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashSet;

public class BoggleSolver {
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    private final CustomTST<Integer> tries;

    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException("Empty dictionary");
        tries = new CustomTST<Integer>();
        for (String s : dictionary) {
            tries.put(s, s.length());
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) throw new IllegalArgumentException("Empty board !");
        int nrow = board.rows();
        int ncol = board.cols();
        HashSet<String> acc = new HashSet<String>();


        for (int i = 0; i < nrow; i++) {
            for (int j = 0; j < ncol; j++) {
                boolean[] marked = getNewAllocatedMarked(nrow, ncol);
                dfs("", i, j, marked, board, acc);
            }
        }
        return acc;
    }

    private boolean[] getNewAllocatedMarked(int nrow, int ncol) {
        boolean[] marked = new boolean[nrow * ncol]; // new marked for every new first char
        for (int ir = 0; ir < nrow; ir++) {
            for (int ic = 0; ic < ncol; ic++) {
                marked[ir * ncol + ic] = false;
            }
        }
        return marked;
    }

    // dfs and add the valid word as you find it, stop otherwise
    private void dfs(String orig, int i, int j, boolean[] marked, BoggleBoard board,
                     HashSet<String> acc) {
        // System.out.println("Exploring: " + s);
        int r = board.rows();
        int c = board.cols();
        marked[i * c + j] = true;
        boolean doDFS = false;
        char ch = board.getLetter(i, j);
        String s = orig + ch;
        if (ch == 'Q') {
            s = s + 'U';
            // System.out.println(s);
        }

        if (s.length() <= 2) { // down more
            doDFS = true;
        }
        else if (this.tries.isPrefixOfAnyWord(s)) { // length > 2

            if (this.tries.contains(s)) acc.add(s);
            doDFS = true;
        }
        // if (i == r - 1 && j == c - 1) System.out.println("dsf: " + s);

        if (doDFS) {
            // System.out.println("dsf: " + s);
            if (i > 0 && !marked[(i - 1) * c + j]) { // above
                boolean[] m1 = Arrays.copyOf(marked, marked.length);
                dfs(s, i - 1, j, m1, board, acc);
            }
            if (i < r - 1 && !marked[(i + 1) * c + j]) { // below
                boolean[] m2 = Arrays.copyOf(marked, marked.length);
                dfs(s, i + 1, j, m2, board, acc);
            }
            if (j > 0 && !marked[i * c + j - 1]) { // left
                boolean[] m3 = Arrays.copyOf(marked, marked.length);
                dfs(s, i, j - 1, m3, board, acc);
            }
            if (j < c - 1 && !marked[i * c + j + 1]) { // right
                boolean[] m4 = Arrays.copyOf(marked, marked.length);
                dfs(s, i, j + 1, m4, board, acc);
            }
            if (i > 0 && j > 0 && !marked[(i - 1) * c + j - 1]) { // upper-left corner
                boolean[] m5 = Arrays.copyOf(marked, marked.length);
                dfs(s, i - 1, j - 1, m5, board, acc);
            }
            if (i < r - 1 && j > 0 && !marked[(i + 1) * c + j - 1]) { // lower-left corner
                boolean[] m6 = Arrays.copyOf(marked, marked.length);
                dfs(s, i + 1, j - 1, m6, board, acc);
            }
            if (i > 0 && j < c - 1 && !marked[(i - 1) * c + j + 1]) { // upper-right corner
                boolean[] m7 = Arrays.copyOf(marked, marked.length);
                dfs(s, i - 1, j + 1, m7, board, acc);
            }
            if (i < r - 1 && j < c - 1 && !marked[(i + 1) * c + j + 1]) { // lower-right corner
                boolean[] m8 = Arrays.copyOf(marked, marked.length);
                dfs(s, i + 1, j + 1, m8, board, acc);
            }
        } // stop dfs otherwise (prefix no longer matches)
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) throw new IllegalArgumentException("NULL word");
        if (tries.contains(word)) {
            int length = word.length();
            int points;
            if (length < 3) return 2;
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
        int count = 0;
        for (String word : solver.getAllValidWords(board)) {
            // StdOut.println(word);
            score += solver.scoreOf(word);
            count++;
        }
        StdOut.println("Score = " + score);
        StdOut.println("Total word = " + count);

    }

    // Customized Trie - https://algs4.cs.princeton.edu/52trie/TST.java.html
    private class CustomTST<Value> {
        private int n;              // size
        private Node<Value> root;   // root of TST

        private class Node<Value> {
            private char c;                        // character
            private Node<Value> left, mid, right;  // left, middle, and right subtries
            private Value val;                     // value associated with string
        }

        /**
         * Initializes an empty string symbol table.
         */
        public CustomTST() {
        }

        /**
         * Does this symbol table contain the given key?
         *
         * @param key the key
         * @return {@code true} if this symbol table contains {@code key} and
         * {@code false} otherwise
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public boolean contains(String key) {
            if (key == null) {
                throw new IllegalArgumentException("argument to contains() is null");
            }
            return get(key) != null;
        }

        /**
         * Returns the value associated with the given key.
         *
         * @param key the key
         * @return the value associated with the given key if the key is in the symbol table
         * and {@code null} if the key is not in the symbol table
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public Value get(String key) {
            if (key == null) {
                throw new IllegalArgumentException("calls get() with null argument");
            }
            if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
            Node<Value> x = get(root, key, 0);
            if (x == null) return null;
            return x.val;
        }

        // return subtrie corresponding to given key
        private Node<Value> get(Node<Value> x, String key, int d) {
            if (x == null) return null;
            if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
            char c = key.charAt(d);
            if (c < x.c) return get(x.left, key, d);
            else if (c > x.c) return get(x.right, key, d);
            else if (d < key.length() - 1) return get(x.mid, key, d + 1);
            else return x;
        }

        /**
         * Inserts the key-value pair into the symbol table, overwriting the old value
         * with the new value if the key is already in the symbol table.
         * If the value is {@code null}, this effectively deletes the key from the symbol table.
         *
         * @param key the key
         * @param val the value
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public void put(String key, Value val) {
            if (key == null) {
                throw new IllegalArgumentException("calls put() with null key");
            }
            if (!contains(key)) n++;
            else if (val == null) n--;       // delete existing key
            root = put(root, key, val, 0);
        }

        private Node<Value> put(Node<Value> x, String key, Value val, int d) {
            char c = key.charAt(d);
            if (x == null) {
                x = new Node<Value>();
                x.c = c;
            }
            if (c < x.c) x.left = put(x.left, key, val, d);
            else if (c > x.c) x.right = put(x.right, key, val, d);
            else if (d < key.length() - 1) x.mid = put(x.mid, key, val, d + 1);
            else x.val = val;
            return x;
        }


        // check if the given prefix is a prefix of any word in trie
        private boolean isPrefixOfAnyWord(String prefix) {
            if (prefix == null) {
                throw new IllegalArgumentException("calls isPrefixOfAnyWord() with null argument");
            }
            if (prefix.length() == 0)
                throw new IllegalArgumentException("prefix must have length >= 1");
            Node<Value> x = nodePrefixTerminate(root, prefix, 0);
            if (x == null) return false;
            return true;
        }

        // check if the given prefix is a prefix of any word in trie
        private Node<Value> nodePrefixTerminate(Node<Value> x, String prefix, int d) {
            if (x == null) return null;
            if (prefix.length() == 0)
                throw new IllegalArgumentException("prefix must have length >= 1");
            char c = prefix.charAt(d);
            if (c < x.c) return nodePrefixTerminate(x.left, prefix, d);
            else if (c > x.c) return nodePrefixTerminate(x.right, prefix, d);
            else if (d < prefix.length() - 1) return nodePrefixTerminate(x.mid, prefix, d + 1);
            else return x;
        }

    }

}
