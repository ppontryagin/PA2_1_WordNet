import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordNet {

    private Digraph hypernymsGraph;
    private Map<String, Set<Integer>> synsetsDict;
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
                if (synsetsDict.containsKey(noun))
                    synsetsDict.get(noun).add(id);
                else {
                    Set<Integer> s = new HashSet<>();
                    s.add(id);
                    synsetsDict.put(noun, s);
                }
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

        if (!singleRooted())
            throw new IllegalArgumentException("Not single rooted graph!");

        DirectedCycle cycle = new DirectedCycle(hypernymsGraph);
        if (cycle.hasCycle())
            throw new IllegalArgumentException("Cycles found!");

        //SAP init
        sap = new SAP(hypernymsGraph);
    }

    private boolean singleRooted() {
        int out = 0;
        for (int v = 0; v < hypernymsGraph.V(); v++) {
            if (hypernymsGraph.outdegree(v) == 0) {
                out++;
            }
        }

        return (out == 1);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsetsDict.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new NullPointerException("isNoun");

        return synsetsDict.keySet().contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new NullPointerException("distance");

        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        return sap.length(synsetsDict.get(nounA), synsetsDict.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new NullPointerException("sap");

        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        int idAncestor = sap.ancestor(synsetsDict.get(nounA), synsetsDict.get(nounB));
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
            return "Synset{" + "id=" + id + ", gloss='" + gloss + '\'' + '}';
        }
    }
}