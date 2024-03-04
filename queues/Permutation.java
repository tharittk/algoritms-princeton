/* *****************************************************************************
 *  Name: Tharit T.
 *  Date: March 3, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        String s;
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            s = StdIn.readString();
            rq.enqueue(s);
        }

        for (int i = 0; i < n; i++) {
            s = rq.dequeue();
            StdOut.println(s);
        }


    }
}
