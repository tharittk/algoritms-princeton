import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("null graph exception");
        }
        this.G = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v >= this.G.V() || w >= this.G.V()) {
            throw new IllegalArgumentException("vertex out of range exception");
        }
        int[] distAndId;
        distAndId = findAncestor(v, w);
        return distAndId[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        if (v >= this.G.V() || w >= this.G.V()) {
            throw new IllegalArgumentException("vertex out of range exception");
        }
        int[] distAndId;
        distAndId = findAncestor(v, w);
        return distAndId[1];
    }

    private int[] findAncestor(int v, int w) {

        int[] distAndId = new int[2];
        if (v == w) {
            distAndId[0] = 0;
            distAndId[1] = v;
            return distAndId;
        }

        boolean[] vMarked = new boolean[G.V()];
        int[] vDistTo = new int[G.V()];
        // int[] vEdgeTo = new int[G.V()];

        boolean[] wMarked = new boolean[G.V()];
        int[] wDistTo = new int[G.V()];
        // int[] wEdgeTo = new int[G.V()];

        for (int x = 0; x < G.V(); x++) {
            vDistTo[x] = Integer.MAX_VALUE;
            wDistTo[x] = Integer.MAX_VALUE;
        }

        // alternating bfs
        Queue<Integer> vq = new Queue<Integer>();
        Queue<Integer> wq = new Queue<Integer>();
        vMarked[v] = true;
        wMarked[w] = true;
        vDistTo[v] = 0;
        wDistTo[w] = 0;

        vq.enqueue(v);
        wq.enqueue(w);

        // bfs of v
        while (!vq.isEmpty()) {
            int vnext = vq.dequeue();
            for (int x : G.adj(vnext)) {
                if (!vMarked[x]) {
                    // vEdgeTo[x] = vnext;
                    vDistTo[x] = vDistTo[vnext] + 1;
                    vMarked[x] = true;
                    vq.enqueue(x);
                }
            }
        }
        // bfs of w
        while (!wq.isEmpty()) {
            int wnext = wq.dequeue();
            for (int x : G.adj(wnext)) {
                if (!wMarked[x]) {
                    // wEdgeTo[x] = wnext;
                    wDistTo[x] = wDistTo[wnext] + 1;
                    wMarked[x] = true;
                    wq.enqueue(x);
                }
            }
        }

        // find smallest sum
        int smallestSum = Integer.MAX_VALUE;
        int ancestor = -1;
        int cd;
        for (int i = 0; i < G.V(); i++) {
            if (vMarked[i] && wMarked[i]) {
                cd = vDistTo[i] + wDistTo[i];
                if (cd < smallestSum) {
                    smallestSum = cd;
                    ancestor = i;
                }
            }
        }

        if (smallestSum == Integer.MAX_VALUE) {
            distAndId[0] = -1;
            distAndId[1] = -1;
        }
        else {
            distAndId[0] = smallestSum;
            distAndId[1] = ancestor;
        }

        return distAndId;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in
    // w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("null vertex exception");
        }

        int[] dd = findAncestorMany(v, w);
        return dd[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such
    // path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("null vertex exception");
        }

        int[] dd = findAncestorMany(v, w);
        return dd[1];
    }

    private int[] findAncestorMany(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("null vertex exception");
        }
        int[] distAndId = new int[2];
        int shortestLength = Integer.MAX_VALUE;
        int shortestAncestor = Integer.MAX_VALUE;
        int currentLength;
        int currentAncestor;
        int[] did;
        for (Integer iv : v) {
            for (Integer iw : w) {
                if (iv == null || iw == null) {
                    throw new IllegalArgumentException("null vertex exception");
                }
                did = findAncestor(iv, iw);
                currentLength = did[0];
                currentAncestor = did[1];
                if (currentLength < shortestLength) {
                    shortestLength = currentLength;
                    shortestAncestor = currentAncestor;
                }
            }
        }
        if (shortestLength == Integer.MAX_VALUE) { // no common ancestor
            distAndId[0] = -1;
            distAndId[1] = -1;
        }
        else {
            distAndId[0] = shortestLength;
            distAndId[1] = shortestAncestor;
        }
        return distAndId;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

}
