import edu.princeton.cs.algs4.In;

import java.util.Arrays;

public class CircularSuffixArray {
    private final String s;
    private final CircularSuffix[] csArray;

    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("null string input !");

        this.s = s;
        csArray = new CircularSuffix[s.length()];

        for (int i = 0; i < s.length(); i++) {
            csArray[i] = new CircularSuffix(i, s);
        }

        Arrays.sort(csArray);
    }

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private final int pos;
        private int offset;
        private final String s;

        private CircularSuffix(int pos, String sref) {
            this.pos = pos;
            this.offset = 0;
            this.s = sref;
        }

        public int compareTo(CircularSuffix other) {
            // exactly same suffix
            if (this.offset == s.length() && other.offset == s.length()) {
                this.offset = 0;
                other.offset = 0;
                return 0;
            }

            int cmp = Character.compare(this.s.charAt((this.pos + this.offset) % s.length()),
                                        other.s.charAt((other.pos + other.offset) % s.length()));
            if (cmp == 0) {
                this.offset++;
                other.offset++;
                return compareTo(other);
            }
            else {
                this.offset = 0;
                other.offset = 0;
                return cmp;
            }
        }
    }

    public int length() {
        return s.length();
    }

    public int index(int i) {
        if (i < 0 || i >= this.length())
            throw new IllegalArgumentException("i must be between 0 and n-1");
        return csArray[i].pos;
    }

    public static void main(String[] args) {
        In file = new In(args[0]);
        String allString = file.readAll();
        System.out.println(allString);
        CircularSuffixArray csa = new CircularSuffixArray(allString);
        System.out.println("Length: " + csa.length());
        System.out.println("Index: ");
        for (int i = 0; i < csa.length(); i++) {
            System.out.println("i: " + i + " index[i] = " + csa.index(i));
        }
    }

}
