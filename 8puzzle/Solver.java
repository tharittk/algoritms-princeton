/* *****************************************************************************
 *  Name: Tharit T.
 *  Date: March 17, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private SearchNode rootSearch, terminalSearch;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        SearchNode prevSearch, currentSearch;
        MinPQ<SearchNode> PQ = new MinPQ<SearchNode>();

        // insert root search to empty priority queue
        rootSearch = new SearchNode(null, initial);
        PQ.insert(rootSearch);

        // Need to simutenously implement twin ( || Twin.isGoal)

        prevSearch = rootSearch;
        currentSearch = PQ.delMin();
        while (!currentSearch.associatedBoard.isGoal()) {

            // constructing neighbors from current
            Iterable<Board> neighborBoards = currentSearch.associatedBoard.neighbors();

            // insert neighbors to PQ
            for (Board neighbor : neighborBoards) {
                // optimization: not insert the prev.board
                if (neighbor != prevSearch.associatedBoard) {
                    PQ.insert(new SearchNode(currentSearch, neighbor));
                }
            }
            // current search = dequeue min PQ
            prevSearch = currentSearch;
            currentSearch = PQ.delMin();

        }
        // save solution
        terminalSearch = currentSearch;

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
        return true;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        // not yet implement twins
        return terminalSearch.moveSoFar;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        SearchNode currentSearch;
        Stack<Board> solutionPath = new Stack<Board>();
        currentSearch = terminalSearch;
        while (currentSearch.prevSearch != null) {
            solutionPath.push(currentSearch.associatedBoard);
            currentSearch = currentSearch.prevSearch;
        }
        // push to the root search node
        solutionPath.push(rootSearch.associatedBoard);
        return solutionPath;
    }

    // test client (see below)
    public static void main(String[] args) {
        System.out.println(">> Start solver");
    }

}
