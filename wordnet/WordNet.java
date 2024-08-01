import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;

public class WordNet {
    private BST<String, Integer> bst;
    private HashMap<Integer, String> hmap;
    private Digraph G;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Null input file");
        }

        In inSyn = new In(synsets);
        In inHyp = new In(hypernyms);

        // Use BST (k,v) = (noun, vertex id) for O(log n) search
        // and hmap for search noun for a given vertex number
        bst = new BST<String, Integer>();
        hmap = new HashMap<Integer, String>();

        String[] fields;
        int count = 0;
        while (inSyn.hasNextLine()) {
            String line = inSyn.readLine();
            fields = line.split(",");
            bst.put(fields[1], Integer.parseInt(fields[0]));
            hmap.put(Integer.parseInt(fields[0]), fields[1]);
            count++;
        }
        // Build Direct Graph from vertices
        G = new Digraph(count);
        while (inHyp.hasNextLine()) {
            String line = inHyp.readLine();
            fields = line.split(",");
            int v = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int w = Integer.parseInt(fields[i]);
                G.addEdge(v, w);
            }
        }

        // check cycle
        DirectedCycle dc = new DirectedCycle(G);
        if (dc.hasCycle()) throw new IllegalArgumentException("G has a cycle");
        // check one root
        int countRoot = 0;
        for (int i = 0; i < count; i++) {
            if (G.outdegree(i) == 0) countRoot++;
        }
        if (countRoot > 1) throw new IllegalArgumentException("More than one root");

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return bst.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return (bst.get(word) != null);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return "test";
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String synsets = args[0];
        String hypernyms = args[1];
        WordNet wn = new WordNet(synsets, hypernyms);

    }
}
