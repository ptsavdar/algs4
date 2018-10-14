package wordnet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordNet;

    public Outcast(WordNet wordNet) {
        if (wordNet == null) throw new IllegalArgumentException();
        this.wordNet = wordNet;
    }

    public String outcast(String[] nouns) {
        int[] dist = new int[nouns.length];
        int maxDist = Integer.MIN_VALUE;
        int maxNoun = -1;
        for(int i = 0; i < nouns.length; i++) {
            for(int j = i + 1; j < nouns.length; j++) {
                int d = wordNet.distance(nouns[i], nouns[j]);
                dist[i] += d;
                dist[j] += d;
            }
            if (dist[i] > maxDist) {
                maxDist = dist[i];
                maxNoun = i;
            }
        }

        return nouns[maxNoun];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
