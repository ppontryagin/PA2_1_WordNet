public class Outcast {

    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {

        int maxDist = 0;
        String outcast = "";

        for (String wordNetNoun: nouns) {
            int currDist = 0;

            for (String noun : nouns) {
                currDist += wordNet.distance(noun, wordNetNoun);
            }

            if (currDist > maxDist) {
                maxDist = currDist;
                outcast = wordNetNoun;
            }
        }

        return outcast;
    }

    // see test client below
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