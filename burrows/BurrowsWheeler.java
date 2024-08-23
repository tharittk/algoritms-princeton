import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;

public class BurrowsWheeler {
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        StringBuilder t = new StringBuilder();
        int first;

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

        char[] tSorted = new char[t.length()];

        // building next
        int nAscii = 256; // extended ASCII
        int nChar = t.length();
        int[] count = new int[nAscii + 1];
        int[] next = new int[nChar];

        ArrayList<ArrayList<Integer>> charPositionLists = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < nAscii + 1; i++) {
            charPositionLists.add(new ArrayList<Integer>());
        }

        // count frequency and store the list of index in t where char is found
        for (int i = 0; i < nChar; i++) {
            int c = t.charAt(i);
            count[c + 1]++;
            charPositionLists.get(c + 1).add(i);
            // System.out.println("char: " + c + " adding position" + i);

        }

        for (int r = 0; r < nAscii; r++) {
            count[r + 1] += count[r];
        }

        for (int i = 0; i < nChar; i++) {
            tSorted[count[t.charAt(i)]++] = t.charAt(i);
        }

        // building next
        int i = 0;
        for (ArrayList<Integer> positions : charPositionLists) {
            for (int position : positions) {
                next[i] = position;
                // System.out.println("i: " + i + " next[i]:: " + next[i]);
                i++;
            }
        }

        // inverting
        StringBuilder it = new StringBuilder();
        int j = first;
        // There's some kind of bug in stars.txt where next[first] = first
        // so only 1 star is retrieved
        if (next[first] == first) {
            for (int k = 0; k < nChar; k++) {
                it.append(tSorted[k]);
            }
        }
        else {
            while (next[j] != first) {
                // System.out.println("here: " + next[j]);
                it.append(tSorted[j]);
                j = next[j];
            }
            it.append(tSorted[j]); // last elem
        }

        BinaryStdOut.write(it.toString());
        BinaryStdOut.close();
    }


    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
    }

}

