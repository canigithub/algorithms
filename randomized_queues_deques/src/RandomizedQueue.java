import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {


    private Item[] queue;
    private int size;


    public RandomizedQueue()                 // construct an empty randomized queue
    {
        size = 0;
        queue = (Item[]) new Object[1];
    }


    public boolean isEmpty()                 // is the queue empty?
    {
        return size == 0;
    }


    public int size()                        // return the number of items on the queue
    {
        return size;
    }


    private void resize(int capacity)
    {
        Item[] new_queue = (Item[]) new Object[capacity];

        for (int i = 0; i < size; ++i) {
            new_queue[i] = queue[i];
        }

        queue = new_queue;
    }


    public void enqueue(Item item)           // add the item
    {
        if (item == null) {
            throw new NullPointerException("add null item.");
        }

        if (size == queue.length) {
            resize(queue.length*2);
        }

        queue[size++] = item;
    }


    public Item dequeue()                    // remove and return a random item
    {
        if (isEmpty()) {
            throw new NoSuchElementException("remove from empty queue.");
        }

        int r_idx = StdRandom.uniform(size);
        Item ret = queue[r_idx];

        queue[r_idx] = queue[size-1];
        queue[--size] = null;

        if (size > 0 && size == queue.length/4) {
            resize(queue.length/2);
        }

        return ret;
    }


    public Item sample()                     // return (but do not remove) a random item
    {
        if (isEmpty()) {
            throw new NoSuchElementException("remove from empty queue.");
        }

        int r_idx = StdRandom.uniform(size);

        return queue[r_idx];
    }


    private class RandomQueueIterator implements Iterator<Item>
    {
        Item[] r_queue;
        int cur_index = 0;

        public RandomQueueIterator() {

            r_queue = (Item[]) new Object[size];

            for(int i = 0; i < r_queue.length; ++i) {
                r_queue[i] = queue[i];
            }

            StdRandom.shuffle(r_queue);
        }

        @Override
        public boolean hasNext() {
            return cur_index < r_queue.length;
        }

        @Override
        public Item next() {

            if (!hasNext()) {
                throw new NoSuchElementException("has no next.");
            }

            return r_queue[cur_index++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove not supported in iterator.");
        }
    }


    @Override
    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new RandomQueueIterator();
    }


    public static void main(String[] args)   // unit testing
    {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        // test
        rq.enqueue(9);
        StdOut.println(rq.dequeue());
        rq.enqueue(27);

//        for (Integer i : rq) {
//            StdOut.print(i + " ");
//        }
    }
}
