/* *****************************************************************************
 *  Name: Tharit T.
 *  Date: March 3, 2024
 *  Description:
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size;
    private Item[] q;

    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("cannot add null to deque !");


    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Cannot sample, queue is empty !");

        // q[picked] = null;
        return;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Cannot sample, queue is empty !");
        return;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private int i = 0;
        private int arraySize = size;
        private Item[] qq = q;

        public boolean hasNext() {
            return i < arraySize;
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "Method removed() is not supported by an iterator");
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more items in iteration");
            }
            Item item;

            item = qq[i];
            i++;
            // in case the value is already randomly dequeued
            while (item == null) {
                item = qq[i];
                i++;
            }
            return item;
        }

    }

    // unit testing (required)
    public static void main(String[] args) {

    }

}
