import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    /* @citation Adapted from:
     * https://algs4.cs.princeton.edu/13stacks/LinkedStack.java.
     * Accessed 9/14/2023.
     */

    // top of deque
    private Node first;
    // end of deque
    private Node last;
    // number of items in deque
    private int size;

    // helper linked list class
    private class Node {
        // item
        private Item item;
        // link to next node
        private Node next;
        // link to previous node
        private Node previous;
    }

    // construct an empty deque
    public Deque() {
        size = 0;
        first = null;
        last = null;
    }

    // is the deque empty?
    // @return true if empty
    public boolean isEmpty() {
        return first == null || last == null;
    }

    // return the number of items on the deque
    // @return size of deque
    public int size() {
        return size;
    }

    // add the item to the front
    // @param item to be added
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null items");
        }
        Node oldFirst = first;
        // create new node
        first = new Node();
        // set node attributes
        first.item = item;
        first.next = oldFirst;

        if (isEmpty()) last = first;
        else oldFirst.previous = first;
        // increment size
        size++;
    }

    // add the item to the back
    // @param item to be added
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null items");
        }
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.previous = oldLast;

        if (isEmpty()) first = last;
        else oldLast.next = last;
        size++;
    }

    // remove and return the item from the front
    // @return first item
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Stack Underflow");
        }
        Item firstItem = first.item;
        first = first.next;

        if (isEmpty()) last = null;
        else first.previous = null;
        size--;
        return firstItem;
    }

    // remove and return the item from the back
    // @return last item
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Stack Underflow");
        }
        Item lastItem = last.item;
        last = last.previous;

        if (isEmpty()) first = null;
        else last.next = null;
        size--;
        return lastItem;
    }

    // return an iterator over items in order from front to back
    // @return iterator for deque
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // iterator for linked list in first-->last order
    private class DequeIterator implements Iterator<Item> {
        // next node to return
        private Node currentNode = first;

        // @return true if iterator has next element, false otherwise
        public boolean hasNext() {
            return currentNode != null;
        }

        // @return next item in iterator
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Reached end of deque");
            }
            Item item = currentNode.item;
            currentNode = currentNode.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> test = new Deque<String>();
        System.out.println("empty: " + test.isEmpty());
        test.addFirst("two");
        test.addFirst("one");
        test.removeFirst();
        test.addLast("three");
        System.out.println("size: " + test.size());
        test.removeLast();
        System.out.println("empty: " + test.isEmpty());
        System.out.println("size: " + test.size());
        for (String s : test) {
            System.out.println(s);
        }
    }
}
