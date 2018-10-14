package queues;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private Item[] rq;

    public RandomizedQueue() {
        size = 0;
        rq = (Item[]) new Object[2];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Item must be not null");
        if (size == rq.length) resize(2 * size);
        rq[size++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Stack overflow");
        int idx = StdRandom.uniform(size);
        Item item = rq[idx];
        rq[idx] = rq[size - 1];
        rq[--size] = null;
        if (size > 0 && size == rq.length/4) resize(2 * size);

        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Stack overflow");
        return rq[StdRandom.uniform(size)];
    }

    private void resize(int newSize) {
        Item[] newRq = (Item[]) new Object[newSize];
        for (int i = 0; i < Math.min(newSize, rq.length); i++) {
            newRq[i] = rq[i];
        }

        rq = newRq;
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] trq;
        private int tsize;

        public RandomizedQueueIterator() {
            trq = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                trq[i] = rq[i];
            }
            tsize = size;
        }

        @Override
        public boolean hasNext() {
            return tsize > 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("Stack Overflow");
            int idx = StdRandom.uniform(tsize);
            Item item = trq[idx];
            trq[idx] = trq[tsize - 1];
            trq[--tsize] = null;

            return item;
        }

        @Override
        public void remove() { throw new UnsupportedOperationException("Remove method not supported"); }
    }

    public static void main(String args[]) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);
        rq.enqueue(5);
        rq.enqueue(6);

        for (Integer nb : rq) {
            System.out.println(nb);
        }
    }
}
