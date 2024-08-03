import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    
    private final BST<String, ArrayList<Integer>> bst;
    private final HashMap<Integer, String> hmap;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Null input file");
        }

        In inSyn = new In(synsets);
        In inHyp = new In(hypernyms);

        // Use BST (k,v) = (noun, vertex id) for O(log n) search
        // and hmap for search noun for a given vertex number
        bst = new BST<String, ArrayList<Integer>>();
        hmap = new HashMap<Integer, String>();

        String[] fields;
        int count = 0;
        while (inSyn.hasNextLine()) {
            String line = inSyn.readLine();
            fields = line.split(",");

            String[] inSynset = fields[1].split("\\s+");
            for (String word : inSynset) {
                if (bst.contains(word)) {
                    bst.get(word).add(Integer.parseInt(fields[0]));
                }
                else {
                    ArrayList<Integer> arr = new ArrayList<Integer>();
                    arr.add(Integer.parseInt(fields[0]));
                    bst.put(word, arr);
                }
            }
            hmap.put(Integer.parseInt(fields[0]), fields[1]);
            count++;
        }
        // Build Direct Graph from vertices
        Digraph g = new Digraph(count);
        while (inHyp.hasNextLine()) {
            String line = inHyp.readLine();
            fields = line.split(",");
            int v = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int w = Integer.parseInt(fields[i]);
                g.addEdge(v, w);
            }
        }

        sap = new SAP(g);

        // check cycle
        DirectedCycle dc = new DirectedCycle(g);
        if (dc.hasCycle())
            throw new IllegalArgumentException("G has a cycle");
        // check one root
        int countRoot = 0;
        for (int i = 0; i < count; i++) {
            if (g.outdegree(i) == 0)
                countRoot++;
        }
        if (countRoot > 1)
            throw new IllegalArgumentException("More than one root");

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return bst.keys();
        // return keys;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("null word input");
        }
        return (bst.get(word) != null);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("null word input");
        }

        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("noun is not in universe");
        }

        return this.sap.length(bst.get(nounA), bst.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA
    // and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("null word input");
        }

        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("noun is not in universe");
        }

        return hmap.get(this.sap.ancestor(bst.get(nounA), bst.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // String synsets = args[0];
        // String hypernyms = args[1];
        // WordNet wn = new WordNet(synsets, hypernyms);
        // Unit test

        // // wordNet.isNoun: non-valid input
        // assert (!wn.isNoun("zz"));
        // // wordNet.isNoun: valid input
        // assert (wn.isNoun("a"));
        // // wordNet.distance: null input
        // try {
        //     wn.distance(null, "a");
        //     System.out.println("Should not be here: nouns is null");
        // }
        // catch (IllegalArgumentException e) {
        // }
        // // wordNet.distance: input outside range
        // try {
        //     wn.distance("zz", "a");
        //     System.out.println("Should not be here: nouns is not in graph");
        // }
        // catch (IllegalArgumentException e) {
        // }
        // // wordNet.distance: valid input
        // assert (wn.distance("n", "i") == 3);
        // // wordNet.sap: null input
        // try {
        //     wn.sap(null, "a");
        //     System.out.println("Should not be here: nouns is null");
        // }
        // catch (IllegalArgumentException e) {
        // }
        // // wordNet.sap: input outside range
        // try {
        //     wn.sap("zz", "a");
        //     System.out.println("Should not be here: nouns is not in graph");
        // }
        // catch (IllegalArgumentException e) {
        // }
        // // wordNet.sap: valid input
        // assert (wn.sap("r", "m").equals("f"));
        //
        // // sap.length: input outside range
        // try {
        //     wn.sap.length(27, 2);
        //     System.out.println("Should not be here: vertex is outside of range");
        // }
        // catch (IllegalArgumentException e) {
        // }
        // // sap.length: valid input
        // assert (wn.sap.length(13, 16) == 4);
        // // sap.length: same input
        // assert (wn.sap.length(5, 5) == 0);
        // // sap.length: direct parent input
        // assert (wn.sap.length(5, 2) == 1);
        // // sap.ancestor: input outside range
        // try {
        //     wn.sap.ancestor(30, 2);
        //     System.out.println("Should not be here: vertex is outside of range");
        // }
        // catch (IllegalArgumentException e) {
        // }
        // // sap.ancestor: valid input
        // assert (wn.sap.ancestor(17, 6) == 2);
        // // sap.ancestor: same input
        // assert (wn.sap.ancestor(5, 5) == 5);
        // // sap.ancestor: direct parent input
        // assert (wn.sap.ancestor(4, 1) == 1);
        // // sap with iterable inputs
        // int k1, k2;
        // Integer[] aa = { 13, 23, 24 };
        // Integer[] bb = { 6, 16, 17 };
        // Iterable<Integer> aa2 = Arrays.asList(aa);
        // Iterable<Integer> bb2 = Arrays.asList(bb);
        // k1 = wn.sap.ancestor(aa2, bb2);
        // k2 = wn.sap.length(aa2, bb2);
        // assert (k1 == 3);
        // assert (k2 == 4);
    }
}
