import java.util.HashMap;
import java.util.Map;

public class WordNet {

    private Digraph hypernymsGraph;
    private Map<String, Synset> synsetsDict;
    private Map<Integer, String> idSynsetIndex;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        //synsets - build symbol table
        In in = new In(synsets);
        synsetsDict = new HashMap<>();
        idSynsetIndex = new HashMap<>();
        int vertexCount = 0;

        while (in.hasNextLine()) {
            String[] tokLine = in.readLine().split(",");

            Integer id = Integer.valueOf(tokLine[0]);
            String[] nouns = tokLine[1].split("\\s+");

            for (String noun : nouns) {
                synsetsDict.put(noun, new Synset(id, tokLine[2]));
            }

            idSynsetIndex.put(id, tokLine[1]);

            vertexCount++;
        }

        in.close();

        //hypernyms - graph build
        hypernymsGraph = new Digraph(vertexCount);
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] tokLine = in.readLine().split(",");
            int v = Integer.valueOf(tokLine[0]);
            for (int i = 1; i < tokLine.length; i++) {
                int w = Integer.valueOf(tokLine[i]);
                hypernymsGraph.addEdge(v, w);
            }
        }

        in.close();

        //SAP init
        sap = new SAP(hypernymsGraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsetsDict.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return synsetsDict.keySet().contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return sap.length(synsetsDict.get(nounA).getId(), synsetsDict.get(nounB).getId());
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        int idAncestor = sap.ancestor(synsetsDict.get(nounA).getId(), synsetsDict.get(nounB).getId());
        return idSynsetIndex.get(idAncestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {

        WordNet wordNet = new WordNet(args[0], args[1]);

        while (!StdIn.isEmpty()) {
            String w1 = StdIn.readString();
            String w2 = StdIn.readString();

            int length = wordNet.distance(w1, w2);
            String ancestor = wordNet.sap(w1, w2);

            StdOut.printf("length = %d, ancestor = %s\n", length, ancestor);
        }

    }

    private class Synset {
        private Integer id;
        private String gloss;

        public Synset(Integer id, String gloss) {
            this.id = id;
            this.gloss = gloss;
        }

        public Integer getId() {
            return id;
        }

        public String getGloss() {
            return gloss;
        }

        @Override
        public String toString() {
            return "Synset{" +
                    "id=" + id +
                    ", gloss='" + gloss + '\'' +
                    '}';
        }
    }
}