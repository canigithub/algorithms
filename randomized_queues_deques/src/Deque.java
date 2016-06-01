import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {


    private Node first;
    private Node last;
    private int  size;


    private class Node
    {
        Item       value;
        Node    prev;
        Node    next;

        public Node(Item value, Node prev, Node next) {
            this.value = value;
            this.prev  = prev;
            this.next  = next;
        }

        public Node(Item value) {
            this(value, null, null);
        }
    }

    public Deque()                          // construct an empty deque
    {
        first = null;
        last  = null;
        size  = 0;
    }


    public boolean isEmpty()                // is the deque empty?
    {
        return size() == 0;
    }


    public int size()                       // return the number of items on the deque
    {
        return size;
    }


    public void addFirst(Item item)            // add the item to the front
    {
        if (item == null) {
            throw new NullPointerException("add null item.");
        }

        if (isEmpty()) {
            first = new Node(item);
            last = first;
        } else {
            Node old_first = first;
            first = new Node(item, null, old_first);
            old_first.prev = first;
        }

        ++size;
    }


    public void addLast(Item item)             // add the item to the end
    {
        if (item == null) {
            throw new NullPointerException("add null item.");
        }

        if (isEmpty()) {
            first = new Node(item);
            last = first;
        } else {
            Node old_last = last;
            last = new Node(item, old_last, null);
            old_last.next = last;
        }

        ++size;
    }


    public Item removeFirst()                  // remove and return the item from the front
    {
        if (isEmpty()) {
            throw new NoSuchElementException("remove from empty queue.");
        }

        Item ret = first.value;
        --size;

        if (isEmpty()) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.prev = null;
        }

        return ret;
    }


    public Item removeLast()                   // remove and return the item from the end
    {
        if (isEmpty()) {
            throw new NoSuchElementException("remove from empty queue.");
        }

        Item ret = last.value;
        --size;

        if (isEmpty()) {
            first = null;
            last = null;
        } else {
            last = last.prev;
            last.next = null;
        }

        return ret;
    }


    private class DequeIterator implements Iterator<Item>
    {
        Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {

            if (!hasNext()) {
                throw new NoSuchElementException("has no next.");
            }

            Item ret = current.value;
            current = current.next;
            return ret;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove not supported in iterator.");
        }

    }


    @Override
    public Iterator<Item> iterator()           // return an iterator over items in order from front to end
    {
        return new DequeIterator();
    }


    public static void main(String[] args)   // unit testing
    {
        Deque<String> dq = new Deque<>();

        dq.addFirst("AA");
        dq.addFirst("AB");
        dq.addFirst("AC");
        dq.addLast("ZZ");
        dq.addLast("ZY");
        dq.addLast("ZX");
        dq.removeFirst();
        dq.removeLast();
        dq.removeLast();

        for (String i : dq) {
            StdOut.print(i + " ");
        }
    }
}
