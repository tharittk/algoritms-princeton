import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wn;

    public Outcast(WordNet wordnet) {
        this.wn = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int sumDistMax = Integer.MIN_VALUE;
        int sumDist = 0;
        int d;
        String smax = null;

        // check argument contains null first
        for (String n : nouns) {
            if (n == null) {
                throw new IllegalArgumentException("null string in input");
            }
        }
        // main loop
        for (String src : nouns) {
            for (String dst : nouns) {
                d = wn.distance(src, dst);
                sumDist += d;
            }
            if (sumDist > sumDistMax) {
                sumDistMax = sumDist;
                smax = src;
            }
            sumDist = 0; // reset for the next src
        }
        return smax;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
