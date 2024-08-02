import java.util.Arrays;

public class Outcast {
    // constructor takes a WordNet object

    private WordNet wn;

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

        for (String src : nouns) {
            assert wn.isNoun(src);

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

    }
}