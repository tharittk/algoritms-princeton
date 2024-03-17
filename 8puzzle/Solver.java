/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private int moveSoFar;
    private Board prevBoard;
    public MinPQ <Board> PQ;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){

        PQ = new MinPQ<Board>();


        // insert initial to empty priority queue

        // current = deuque Root
        // move = 1 // wrong if not monotonically go deeper (no in PQ)

        // while current is not goal board

        // constructing neighbors from current

        // insert neighbors to PQ

        // save curent to prev

        // current search = dequeue min PQ


    }

    // class for seach node
    private class SearchNode implements Comparable<SearchNode> {
        private Board previousBoard, currentBoard;
        private int moveSofar, manhattan, hamming, priorityManhattan, priorityHamming;
        // constructor
        private SearchNode (Board prev, Board current, int move){
            previousBoard = prev;
            currentBoard = current;
            moveSofar = move;
            hamming = current.getHammingDistance();
            manhattan = current.getMahattanDistance();

            priorityHamming = moveSoFar + priorityManhattan;
            priorityManhattan = moveSoFar + manhattan;

        }
        public int compareTo(SearchNode that){
            return this.priorityHamming - that.priorityHamming;
        }


    }

    // is the initial board solvable? (see below)
    public boolean isSolvable(){}

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves()

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution()

    // test client (see below)
    public static void main(String[] args)

}
