import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {
    private static final int N_ASCII = 256;

    public static void encode() {
        LinkedList<Integer> dl = constructLinkedListOfChars();
        runEncodeMoveToFrontWhileInputExists(dl);
    }

    private static LinkedList<Integer> constructLinkedListOfChars() {
        LinkedList<Integer> dl = new LinkedList<Integer>();
        for (int r = 0; r < N_ASCII; r++) {
            dl.add(r);
        }
        return dl;
    }

    private static void runEncodeMoveToFrontWhileInputExists(LinkedList<Integer> dl) {
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int pos = dl.indexOf((int) c);
            dl.remove(pos);
            dl.addFirst((int) c);
            BinaryStdOut.write((char) pos);
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        LinkedList<Integer> dl = constructLinkedListOfChars();
        runDecodeMoveToFrontWhileInputExists(dl);
    }

    private static void runDecodeMoveToFrontWhileInputExists(LinkedList<Integer> dl) {
        while (!BinaryStdIn.isEmpty()) {
            int pos = BinaryStdIn.readChar();
            int c = dl.get(pos);
            BinaryStdOut.write((char) c);
            dl.remove(pos);
            dl.addFirst(c);
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
    }
}
