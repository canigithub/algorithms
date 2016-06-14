import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordnet;

    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
        if (wordnet == null) {
            throw new NullPointerException("null wordnet");
        }
        this.wordnet = wordnet;
    }


    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
        if (nouns == null) {
            throw new NullPointerException("null nouns");
        }

        int max_dist = Integer.MIN_VALUE;
        String ret = "";

        for (int i = 0; i < nouns.length; ++i) {

            int sum_dist = 0;
//            StdOut.print(nouns[i] + ":\t");
            for (int j = 0; j < nouns.length; ++j) {

                int dist = wordnet.distance(nouns[i], nouns[j]);
                sum_dist += dist;
//                StdOut.print(nouns[j] + "-" + dist + " ");
            }
//            StdOut.println("\tsum:" + sum_dist);

            if (sum_dist >= max_dist) {
                max_dist = sum_dist;
                ret = nouns[i];
            }
        }

        return ret;
    }


    public static void main(String[] args)  // see test client below
    {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
