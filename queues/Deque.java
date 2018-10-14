package queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node first;
    private Node last;

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        checkNull(item);
        Node tmp = first;
        first = new Node(item, null, tmp);
        if (tmp != null) tmp.prev = first;
        if (last == null) last = first;
        size++;
    }

    public void addLast(Item item) {
        checkNull(item);
        Node nl = new Node(item, last, null);
        if (last != null) last.next = nl;
        last = nl;
        if (first == null) first = last;
        size++;
    }

    public Item removeFirst() {
        checkEmpty();
        Item item = first.item;
        first = first.next;
        if (first != null) first.prev = null;
        else last = null;
        size--;

        return item;
    }

    public Item removeLast() {
        checkEmpty();
        Item item = last.item;
        last = last.prev;
        if (last != null) last.next = null;
        else first = null;
        size--;

        return item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private void checkEmpty() {
        if (size == 0) throw new NoSuchElementException("Deque is empty");
    }

    private void checkNull(Item item) {
        if (item == null) throw new IllegalArgumentException("Cannot insert null item");
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;

            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Node {
        Item item;
        Node next, prev;

        private Node(Item item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    public static void main(String args[]) {
        Deque<Integer> rq = new Deque<>();
        rq.addLast(1);
        System.out.println(rq.removeLast());
        rq.addLast(3);
        rq.addLast(4);
        rq.addFirst(5);
        System.out.println(rq.removeFirst());
        System.out.println(rq.removeLast());
        System.out.println(rq.removeLast());


        for (Integer nb : rq) {
            System.out.println(nb);
        }
    }
}
