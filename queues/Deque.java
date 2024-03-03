/* *****************************************************************************
 *  Name: Tharit T.
 *  Date: March 3, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int size;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (first == null && last == null && size == 0);
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("cannot add null to deque !");

        if (isEmpty()) {
            first = new Node();
            first.item = item;
            first.next = null;
            first.prev = null;
            last = first;
        }
        else {
            Node oldFirst = first;
            first = new Node();
            first.item = item;
            first.next = oldFirst;
            oldFirst.prev = first;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("cannot add null to deque !");

        if (isEmpty()) {
            last = new Node();
            last.item = item;
            last.next = null;
            last.prev = null;
            first = last;
        }
        else {
            Node oldLast = last;
            last = new Node();
            last.item = item;
            oldLast.next = last;
            last.prev = oldLast;
        }
        size++;

    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Cannot remove, deque is already empty !");
        Item item;
        if (size == 1) {
            item = first.item;
            first = first.next; // null
            last = null;
        }
        else {
            item = first.item;
            first = first.next;
            first.prev = null;
        }
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Cannot remove, deque is already empty !");
        Item item;
        if (size == 1) {
            item = last.item;
            last = last.prev;
            first = null;
        }
        else {
            item = last.item;
            last = last.prev;
            last.next = null;

        }
        size--;
        return item;

    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "Method removed() is not supported by an iterator");
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more items in iteration");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

    }

    // unit testing (required)
    public static void main(String[] args) {

        int testOut;
        // Test 1: isEmpty

        System.out.println("======== Test 1: isEmpty =======");
        Deque<Integer> deque1 = new Deque<Integer>();

        if (deque1.isEmpty()) System.out.println("deck is empty (expected)");
        else System.out.println("Something wrong, deck should be empty");

        deque1.addFirst(1);
        if (deque1.isEmpty()) System.out.println("Something wrong, deck should NOT be empty\"");
        else System.out.println("OK deck not empty");

        // Test 2: addFirst from empty
        System.out.println("======== Test 2: addFirst =======");
        Deque<Integer> deque2 = new Deque<Integer>();
        deque2.addFirst(1);
        testOut = deque2.removeFirst();

        System.out.println("Expected 1: Printout: " + testOut);
        if (deque2.isEmpty()) System.out.println("deck is empty (expected)");
        else System.out.println("Something wrong, deck should be empty");

        // System.out.println("Should throw error from popping empty");
        // testOut = (int) deque.removeFirst();

        deque2.addFirst(1);
        deque2.addFirst(2);
        testOut = deque2.removeFirst();
        System.out.println("Expected 2: Printout: " + testOut);

        // Test 3: addLast from empty
        System.out.println("======== Test 3: addLast =======");
        Deque<Integer> deque3 = new Deque<Integer>();
        deque3.addLast(2);
        testOut = deque3.removeLast();
        System.out.println("Expected 2: Printout: " + testOut);

        if (deque3.isEmpty()) System.out.println("deck is empty (expected)");
        else System.out.println("Something wrong, deck should be empty");

        deque3.addFirst(3);
        deque3.addLast(4);
        testOut = deque3.removeLast();
        System.out.println("Expected 4: Printout: " + testOut);

        System.out.println("======== Test 4: removeFirst =======");
        Deque<Integer> deque4 = new Deque<Integer>();
        deque4.addFirst(1);
        deque4.addFirst(0);
        deque4.addLast(2);
        testOut = deque4.removeFirst();
        System.out.println("Expected 0: Printout: " + testOut);
        testOut = deque4.removeFirst();
        System.out.println("Expected 1: Printout: " + testOut);
        testOut = deque4.removeFirst();
        System.out.println("Expected 2: Printout: " + testOut);

        System.out.println("======== Test 5: removeLast =======");
        Deque<Integer> deque5 = new Deque<Integer>();
        deque5.addLast(1);
        deque5.addLast(2);
        deque5.addFirst(0);

        testOut = deque5.removeLast();
        System.out.println("Expected 2: Printout: " + testOut);
        testOut = deque5.removeLast();
        System.out.println("Expected 1: Printout: " + testOut);
        testOut = deque5.removeLast();
        System.out.println("Expected 0: Printout: " + testOut);

        System.out.println("======== Test 6: size =======");
        Deque<Integer> deque6 = new Deque<Integer>();
        testOut = deque6.size();
        System.out.println("Init size expected 0: Printout: " + testOut);
        deque6.addFirst(1);
        testOut = deque6.size();
        System.out.println("size expected 1: Printout: " + testOut);
        deque6.addLast(2);
        testOut = deque6.size();
        System.out.println("size expected 2: Printout: " + testOut);
        deque6.removeFirst();
        testOut = deque6.size();
        System.out.println("size expected 1: Printout: " + testOut);
        deque6.addLast(2);
        deque6.removeLast();
        System.out.println("size expected 1: Printout: " + testOut);

        System.out.println("======== Test 7: Iterator =======");
        Deque<Integer> deque7 = new Deque<Integer>();
        deque7.addFirst(3);
        deque7.addFirst(2);
        deque7.addLast(4);

        Iterator<Integer> it = deque7.iterator();

        System.out.println("Expect 2 3 4:");
        while (it.hasNext()) {
            int i = it.next();
            StdOut.println(i);
        }


    }

}
