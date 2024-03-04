/* *****************************************************************************
 *  Name: Tharit T.
 *  Date: March 3, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int currentCapacity;
    private int itemCount;
    private int qTail;
    private Item[] q;

    // construct an empty randomized queue
    public RandomizedQueue() {
        currentCapacity = 1;
        itemCount = 0;
        qTail = 0;
        q = (Item[]) new Object[1];
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        qTail = 0;
        for (int i = 0; i < currentCapacity; i++) {
            if (q[i] != null) {
                copy[qTail] = q[i];
                qTail++;
            }
        }
        q = copy;
        currentCapacity = capacity;
        itemCount = qTail;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (itemCount == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return itemCount;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("cannot add null to endeque !");

        if (qTail == currentCapacity) resize(2 * currentCapacity);
        q[qTail++] = item;
        itemCount++;

    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Cannot sample, queue is empty !");
        int indexChosen;
        Item item;
        indexChosen = edu.princeton.cs.algs4.StdRandom.uniformInt(currentCapacity);

        while (q[indexChosen] == null) {
            indexChosen = edu.princeton.cs.algs4.StdRandom.uniformInt(currentCapacity);
        }
        item = q[indexChosen];
        q[indexChosen] = null;
        itemCount--;
        if (itemCount > 0 && itemCount == currentCapacity / 4) resize(currentCapacity / 2);

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Cannot sample, queue is empty !");
        int indexChosen;
        indexChosen = edu.princeton.cs.algs4.StdRandom.uniformInt(currentCapacity);
        while (q[indexChosen] == null) {
            indexChosen = edu.princeton.cs.algs4.StdRandom.uniformInt(currentCapacity);
        }
        return q[indexChosen];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private int i = 0;
        // private int arraySize = currentCapacity;
        private int maxItem = itemCount;
        private int counter = 0;
        private Item[] qq = q;

        public boolean hasNext() {
            return counter < maxItem;
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "Method removed() is not supported by an iterator");
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more items in iteration");
            }
            Item item = null;
            // in case the value is already randomly dequeued
            while (item == null && hasNext()) {
                // System.out.println("Check with i: " + i + " qq[i]: " + qq[i]);
                item = qq[i];
                i++;
            }
            counter++;
            return item;
        }

    }

    // unit testing (required)
    public static void main(String[] args) {

        System.out.println("======== Test 1: isEmpty =======");
        RandomizedQueue<Integer> rq1 = new RandomizedQueue<Integer>();
        if (rq1.isEmpty()) System.out.println("rq is empty (expected)");
        else System.out.println("something wrong, rq must be empty");

        rq1.enqueue(1);
        if (rq1.isEmpty()) System.out.println("something wrong, rq is NOT empty");
        else System.out.println("rq is not empty (expected)");


        System.out.println("======== Test 2: size =======");
        int testOut;
        RandomizedQueue<Integer> rq2 = new RandomizedQueue<Integer>();

        testOut = rq2.size();
        System.out.println("expected size: 0, get " + testOut);
        rq2.enqueue(1);
        testOut = rq2.size();
        System.out.println("expected size: 1, get " + testOut);
        testOut = rq2.dequeue();
        testOut = rq2.size();
        System.out.println("expected size: 0, get " + testOut);


        System.out.println("======== Test 3: enque =======");
        RandomizedQueue<Integer> rq3 = new RandomizedQueue<Integer>();
        rq3.enqueue(1);
        testOut = rq3.currentCapacity;
        System.out.println("expected capacity: 1, get " + testOut);

        rq3.enqueue(2);
        testOut = rq3.currentCapacity;
        System.out.println("expected capacity: 2, get " + testOut);

        rq3.enqueue(3);
        testOut = rq3.currentCapacity;
        System.out.println("expected capacity: 4, get " + testOut);

        rq3.enqueue(4);
        rq3.enqueue(5);
        testOut = rq3.currentCapacity;
        System.out.println("expected capacity: 8, get " + testOut);

        System.out.println("======== Test 4: deque =======");
        int cap;
        int sz;
        RandomizedQueue<Integer> rq4 = new RandomizedQueue<Integer>();
        rq4.enqueue(1);
        rq4.enqueue(2);
        rq4.enqueue(3);
        rq4.enqueue(4);
        rq4.enqueue(5);

        cap = rq4.currentCapacity;
        System.out.println("expected capacity: 8, get " + cap);
        testOut = rq4.dequeue();
        cap = rq4.currentCapacity;
        System.out.println("Random out " + testOut +
                                   "expected capacity: 8, get " + cap);

        testOut = rq4.dequeue();
        cap = rq4.currentCapacity;
        System.out.println("Random out " + testOut +
                                   "expected capacity: 8, get " + cap);
        sz = rq4.size();
        System.out.println("expected size: 3, get " + sz);

        testOut = rq4.dequeue();
        cap = rq4.currentCapacity;
        System.out.println("expected capacity: 4, get " + cap);

        testOut = rq4.dequeue();
        cap = rq4.currentCapacity;
        sz = rq4.size();
        System.out.println("expected capacity: 2, get " + cap);
        System.out.println("expected size: 1, get " + sz);

        testOut = rq4.dequeue();
        cap = rq4.currentCapacity;
        sz = rq4.size();
        System.out.println("expected capacity: 2, get " + cap);
        System.out.println("expected size: 0, get " + sz);

        System.out.println("======== Test 5: sample =======");
        RandomizedQueue<Integer> rq5 = new RandomizedQueue<Integer>();
        rq5.enqueue(1);
        testOut = rq5.sample();
        sz = rq5.size();
        System.out.println("expected out: 1, get " + testOut);
        System.out.println("expected size: 1, get " + sz);

        rq5.enqueue(2);
        rq5.enqueue(3);
        rq5.enqueue(4);
        rq5.enqueue(5);
        int testOut1 = rq5.sample();
        int testOut3 = rq5.sample();
        int testOut2 = rq5.sample();

        System.out.println("expected size: 5, get " + sz);
        System.out.println("Random outs: " + testOut1 +
                                   ", " + testOut2 + ", " + testOut3);

        System.out.println("======== Test 6: Iterator =======");
        RandomizedQueue<Integer> rq6 = new RandomizedQueue<Integer>();
        rq6.enqueue(1);
        rq6.enqueue(2);
        rq6.enqueue(3);
        rq6.enqueue(4);
        rq6.enqueue(7);
        testOut1 = rq6.dequeue();
        testOut2 = rq6.dequeue();

        StdOut.println("The randomly ousted:");
        StdOut.println(testOut1);
        StdOut.println(testOut2);

        Iterator<Integer> it = rq6.iterator();

        System.out.println("Expect 1,2,3,4,7:");
        while (it.hasNext()) {

            // int i = it.next();
            StdOut.println(it.next());
        }


    }

}
