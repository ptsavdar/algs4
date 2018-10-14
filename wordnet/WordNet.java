package wordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WordNet {
    private final String CSV_DELIMITER = ",";
    private final String NOUN_DELIMITER = " ";
    private final Map<String, HashSet<Integer>> nounMap;
    private final Map<Integer, String> synsetMap;
    private final Digraph graph;
    private final SAP sap;

    /**
     * Constructor, takes as arguments two CSV filenames and builds a DAG (WordNet)
     *
     * @param synsets (String) a CSV file containing all synsets
     * @param hypernyms (String) a CSV file containing the DAG hypernym relationships
     */
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        // Build noun map and sysnet map
        nounMap = new HashMap<>();
        synsetMap = new HashMap<>();
        In in = new In(synsets);
        while (in.hasNextLine()) {
            String[] chunks = in.readLine().split(CSV_DELIMITER);
            int id = Integer.parseInt(chunks[0]);
            synsetMap.put(id, chunks[1]);
            String[] nouns = chunks[1].split(NOUN_DELIMITER);
            for(String noun : nouns) {
                if (!nounMap.containsKey(noun)) nounMap.put(noun, new HashSet<>());
                nounMap.get(noun).add(id);
            }
        }
        // Build wordnet DAG
        graph = new Digraph(synsetMap.size());
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            int[] ids = Arrays.stream(in.readLine().split(CSV_DELIMITER)).mapToInt(Integer::parseInt).toArray();
            for (int i = 1; i < ids.length; i++) {
                graph.addEdge(ids[0], ids[i]);
            }
        }
        sap = new SAP(graph);
        // Test if graph has root
        int roots = 0;
        for (int i = 0; i < graph.V(); i++) {
            if (graph.outdegree(i) == 0 && graph.indegree(i) > 0) roots++;
        }
        if (roots == 0 || roots > 1) throw new IllegalArgumentException();
    }

    /**
     * Returns all nouns of the sysnet
     *
     * @return List of nouns in wordnet
     */
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }

    /**
     * Checks if a noun is part of the wordnet
     *
     * @param word (String) the word to be tested
     * @return boolean
     */
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounMap.containsKey(word);
    }

    /**
     * Calculates shortest distance between nounA and nounB
     *
     * @param nounA first word
     * @param nounB second word
     * @return shortest distance between 2 words
     */
    public int distance(String nounA, String nounB) {
       if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

       return sap.length(nounMap.get(nounA), nounMap.get(nounB));
    }

    /**
     * Calculates ancestor between nounA and nounB
     *
     * @param nounA first word
     * @param nounB second word
     * @return shortest common ancestor between 2 words
     */
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        int id = sap.ancestor(nounMap.get(nounA), nounMap.get(nounB));

        return id == -1 ? "" : synsetMap.get(id);
    }
}
