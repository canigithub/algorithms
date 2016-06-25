
import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class WordNet {

    private final List<String> noun_list = new ArrayList<>();
    private final HashMap<String, List<Integer>> noun_id = new HashMap<>();
    private Digraph wordnet_graph;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null) {
            throw new NullPointerException("null input file");
        }

        In synset = new In(synsets);

        while (synset.hasNextLine()) {

            String[] line = synset.readLine().split(",");

            noun_list.add(line[1]);
            String[] words = line[1].split(" ");

            for (int i = 0; i < words.length; ++i) {

                List<Integer> temp;
                if (noun_id.containsKey(words[i])) {
                    temp = noun_id.get(words[i]);
                } else {
                    temp = new LinkedList<>();
                }

                temp.add(Integer.parseInt(line[0]));
                noun_id.put(words[i], temp);
            }
        }

        wordnet_graph = new Digraph(noun_list.size());

        In hypernym = new In(hypernyms);

        while (hypernym.hasNextLine()) {

            String[] line = hypernym.readLine().split(",");
            int v = Integer.parseInt(line[0]);

            for (int i = 1; i < line.length; ++i) {
                int w = Integer.parseInt(line[i]);
                wordnet_graph.addEdge(v, w);
            }
        }

        DirectedCycle dircir = new DirectedCycle(wordnet_graph);
        if (dircir.hasCycle()) {
            throw new IllegalArgumentException("input not DAG");
        }

        int root = 0;
        for (int i = 0; i < wordnet_graph.V(); ++i) {
            if (!wordnet_graph.adj(i).iterator().hasNext()) {
                ++root;
            }
        }
        if (root > 1) {
            throw new IllegalArgumentException("input not rooted");
        }

        sap = new SAP(wordnet_graph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return noun_id.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {

        if (word == null) {
            throw new NullPointerException("null word");
        }

        return noun_id.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {

        if (nounA == null || nounB == null) {
            throw new NullPointerException("null noun");
        }

        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("noun not in wordnet");
        }

        return sap.length(noun_id.get(nounA), noun_id.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of
    // nounA and nounB in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {

        if (nounA == null || nounB == null) {
            throw new NullPointerException("null noun");
        }

        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("noun not in wordnet");
        }

        return noun_list.get(sap.ancestor(noun_id.get(nounA), noun_id.get(nounB)));
    }


    // do unit testing of this class
    public static void main(String[] args) {

        WordNet wn = new WordNet(args[0], args[1]);
        StdOut.println(wn.noun_list.get(30230));
        StdOut.println(wn.noun_list.get(30231));
    }
}
