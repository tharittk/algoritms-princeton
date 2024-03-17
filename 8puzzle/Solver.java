/* *****************************************************************************
 *  Name: Tharit T.
 *  Date: March 17, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private SearchNode rootSearch, rootSearchTwin, terminalSearch;
    private boolean originalWins;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        // Original
        SearchNode prevSearch, currentSearch;
        MinPQ<SearchNode> PQ = new MinPQ<SearchNode>();
        // Twin
        SearchNode prevSearchTwin, currentSearchTwin;
        MinPQ<SearchNode> PQTwin = new MinPQ<SearchNode>();

        // insert root search to empty priority queue
        rootSearch = new SearchNode(null, initial);
        PQ.insert(rootSearch);

        // twin
        Board initalTwin = initial.twin();
        rootSearchTwin = new SearchNode(null, initalTwin);
        PQTwin.insert(rootSearchTwin);

        // GameTree
        prevSearch = null;
        currentSearch = PQ.delMin();

        prevSearchTwin = null;
        currentSearchTwin = PQTwin.delMin();

        while (!currentSearch.associatedBoard.isGoal()
                && !currentSearchTwin.associatedBoard.isGoal()) {

            // constructing neighbors from current
            Iterable<Board> neighborBoards = currentSearch.associatedBoard.neighbors();
            Iterable<Board> neighborBoardsTwin = currentSearchTwin.associatedBoard.neighbors();

            // insert neighbors to PQ
            for (Board neighbor : neighborBoards) {
                // optimization: not insert the prev.board
                if (prevSearch == null || !neighbor.equals(prevSearch.associatedBoard)) {
                    PQ.insert(new SearchNode(currentSearch, neighbor));
                }
            }
            // twin
            for (Board neighbor : neighborBoardsTwin) {
                // optimization: not insert the prev.board
                if (prevSearch == null || !neighbor.equals(prevSearchTwin.associatedBoard)) {
                    PQTwin.insert(new SearchNode(currentSearchTwin, neighbor));
                }
            }

            // current search = dequeue min PQ
            prevSearch = currentSearch;
            currentSearch = PQ.delMin();

            prevSearchTwin = currentSearchTwin;
            currentSearchTwin = PQTwin.delMin();
        }
        // save solution

        // original or twin
        if (currentSearch.associatedBoard.isGoal()) {
            terminalSearch = currentSearch;
            originalWins = true;
        }
        else {
            terminalSearch = currentSearchTwin;
            originalWins = false;
        }

    }

    // class for search node
    private class SearchNode implements Comparable<SearchNode> {
        private SearchNode prevSearch;
        private Board associatedBoard;
        private int moveSoFar;
        private int priorityManhattan;
        private int priorityHamming;

        // constructor
        private SearchNode(SearchNode prevNode, Board currentBaord) {

            prevSearch = prevNode;
            associatedBoard = currentBaord;

            if (prevSearch == null) moveSoFar = 0; // root
            else moveSoFar = prevNode.moveSoFar + 1;

            // pre-compute for caching
            int hamming = associatedBoard.hamming();
            int manhattan = associatedBoard.manhattan();

            priorityHamming = moveSoFar + hamming;
            priorityManhattan = moveSoFar + manhattan;

        }

        public int compareTo(SearchNode that) {
            // use caching in construction
            return this.priorityManhattan - that.priorityManhattan;
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        // which one wins, twin or
        return originalWins;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        // not yet implement twins
        if (!this.isSolvable()) return -1;
        else return terminalSearch.moveSoFar;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {

        if (!this.isSolvable()) return null;

        SearchNode currentSearch;
        Stack<Board> solutionStack = new Stack<Board>();
        Queue<Board> solutionPath = new Queue<Board>();

        currentSearch = terminalSearch;
        while (currentSearch.prevSearch != null) {
            solutionStack.push(currentSearch.associatedBoard);
            currentSearch = currentSearch.prevSearch;
        }
        // push to the root search node
        solutionStack.push(rootSearch.associatedBoard);

        while (!solutionStack.isEmpty()) {
            solutionPath.enqueue(solutionStack.pop());
        }

        return solutionPath;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
