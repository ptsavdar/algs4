package queues;

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String args[]) {
        int k = Integer.parseInt(args[0]);
        if (k < 0) throw new IllegalArgumentException("k needs to be bigger than 0 and less than n");
        if (k == 0) return;
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            rq.enqueue(StdIn.readString());
        }
        for (String w : rq) {
            System.out.println(w);
            k--;
            if (k == 0) return;
        }
    }
}