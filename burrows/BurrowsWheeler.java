import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;

public class BurrowsWheeler {
    private static CircularSuffixArray csa;
    private static String s;
    private static final int N_ASCII = 256;

    public static void transform() {
        s = BinaryStdIn.readString();
        csa = new CircularSuffixArray(s);
        writeFirstIndexAndLastColumnOfSortSuffixArray();
    }

    private static void writeFirstIndexAndLastColumnOfSortSuffixArray() {
        int first;
        StringBuilder t = new StringBuilder();

        for (int i = 0; i < csa.length(); i++) {
            int idxi = csa.index(i);
            if (idxi == 0) {
                t.append(s.charAt(csa.length() - 1));
                first = i;
                BinaryStdOut.write(first);
            }
            else {
                t.append(s.charAt(idxi - 1));
            }
        }
        BinaryStdOut.write(t.toString());
        BinaryStdOut.close();
    }

    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();

        int[] count = new int[N_ASCII + 1];
        int[] next = new int[t.length()];

        ArrayList<ArrayList<Integer>> charPositionLists = initializeCharacterPositionsLists();

        countCharFrequencyAndStorePositions(t, count, charPositionLists);

        turnDiscreteCountToAccumulate(count);

        char[] tSorted = getSortedCharOfString(t, count);

        buildNextArray(next, charPositionLists);

        invertForOriginalText(first, next, tSorted);
    }


    private static ArrayList<ArrayList<Integer>> initializeCharacterPositionsLists() {
        ArrayList<ArrayList<Integer>> charPositionLists = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < N_ASCII + 1; i++) {
            charPositionLists.add(new ArrayList<Integer>());
        }
        return charPositionLists;
    }

    private static void countCharFrequencyAndStorePositions(String t, int[] count,
                                                            ArrayList<ArrayList<Integer>> charPositionLists) {
        for (int i = 0; i < t.length(); i++) {
            int c = t.charAt(i);
            count[c + 1]++;
            charPositionLists.get(c + 1).add(i);
        }
    }

    private static void turnDiscreteCountToAccumulate(int[] count) {
        for (int r = 0; r < N_ASCII; r++) {
            count[r + 1] += count[r];
        }
    }

    private static char[] getSortedCharOfString(String t, int[] count) {
        char[] tSorted = new char[t.length()];
        for (int i = 0; i < t.length(); i++) {
            tSorted[count[t.charAt(i)]++] = t.charAt(i);
        }
        return tSorted;
    }

    private static void buildNextArray(int[] next,
                                       ArrayList<ArrayList<Integer>> charPositionLists) {
        int i = 0;
        for (ArrayList<Integer> positions : charPositionLists) {
            for (int position : positions) {
                next[i] = position;
                i++;
            }
        }
    }

    private static void invertForOriginalText(int first, int[] next, char[] tSorted) {
        StringBuilder it = new StringBuilder();
        int j = first;
        // There's some kind of bug in stars.txt where next[first] = first
        // so only 1 star is retrieved
        if (next[first] == first) {
            for (int k = 0; k < tSorted.length; k++) {
                it.append(tSorted[k]);
            }
        }
        else {
            while (next[j] != first) {
                it.append(tSorted[j]);
                j = next[j];
            }
            it.append(tSorted[j]); // last character
        }
        BinaryStdOut.write(it.toString());
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
    }
}

