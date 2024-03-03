/* *****************************************************************************
 *  Name: Tharit T.
 *  Date: March 3, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

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
        Deque deque = new Deque();
        String s, snext, sout;
        while (!StdIn.isEmpty()) {
            s = StdIn.readString();
            if (s.equals(">")) {
                snext = StdIn.readString();
                deque.addFirst(snext);
            }
            if (s.equals(")")) {
                sout = (String) deque.removeFirst();
                System.out.println("Pop first: " + sout);
            }
            if (s.equals("<")) {
                snext = StdIn.readString();
                deque.addLast(snext);
            }
            if (s.equals("(")) {
                sout = (String) deque.removeLast();
                System.out.println("Pop last: " + sout);
            }

        }

    }

}
