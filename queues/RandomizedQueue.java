import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    /* @citation Adapted from:
     * https://algs4.cs.princeton.edu/13stacks/ResizingArrayQueue.java.
     * Accessed 9/14/2023.
     */

    // initial capacity of array
    private static final int INIT_CAPACITY = 8;
    // array of items
    private Item[] items;
    // index of first element
    private int firstIndex;
    // index of next available slot
    private int lastIndex;
    // number of items in array
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[INIT_CAPACITY];
        firstIndex = 0;
        lastIndex = 0;
        size = 0;
    }

    // is the randomized queue empty?
    // @return true if empty
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    // @return size of queue
    public int size() {
        return size;
    }

    // resize the underlying array
    private void resize(int capacity) {
        assert capacity >= size;
        Item[] copy = (Item[]) new Object[capacity];
        // copy items in first --> last order
        for (int i = 0; i < size; i++) {
            copy[i] = items[(firstIndex + i) % items.length];
        }
        items = copy;
        firstIndex = 0;
        lastIndex = size;
    }

    // add item to end of queue
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null items");
        }
        // double size of array if necessary and recopy to front of array
        if (size == items.length) resize(2 * items.length);
        items[lastIndex++] = item;
        // wrap-around
        if (lastIndex == items.length) lastIndex = 0;
        size++;
    }

    // remove and return a random item
    // @return random item after removing
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        // generate random index
        int randomIndex = (StdRandom.uniformInt(size) + firstIndex) % items.length;
        Item item = items[randomIndex];
        // swap random item with first item
        items[randomIndex] = items[firstIndex];
        // avoid loitering
        items[firstIndex] = null;
        size--;
        firstIndex++;
        // wrap-around
        if (firstIndex == items.length) firstIndex = 0;
        // shrink size of array if necessary
        if (size > 0 && size == items.length / 4) resize(items.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    // @return random item without removing
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        int randomIndex = (StdRandom.uniformInt(size) + firstIndex) % items.length;
        Item item = items[randomIndex];
        return item;
    }

    // return an independent iterator over items in random order
    // @return iterator for randomized queue
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // array iterator in random order
    private class RandomizedQueueIterator implements Iterator<Item> {

        // copy of underlying array, arranged in first-->last order
        private Item[] copy;
        // index of next item to be returned
        private int index;

        // construct iterator for randomized queue
        private RandomizedQueueIterator() {
            copy = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                copy[i] = items[(firstIndex + i) % items.length];
            }
            // shuffle array
            StdRandom.shuffle(copy);
            index = 0;
        }

        // @return true if iterator has next element, false otherwise
        public boolean hasNext() {
            return (index < size);
        }

        // @return next item
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Reached end of queue");
            }
            Item item = copy[index];
            index++;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> test = new RandomizedQueue<String>();
        System.out.println("empty: " + test.isEmpty());
        test.enqueue("one");
        test.enqueue("two");
        test.enqueue("three");
        System.out.println("sample: " + test.sample());
        System.out.println("size: " + test.size());
        test.dequeue();
        test.dequeue();
        System.out.println("empty: " + test.isEmpty());
        System.out.println("size: " + test.size());
        for (String s : test) {
            System.out.println(s);
        }
    }
}

