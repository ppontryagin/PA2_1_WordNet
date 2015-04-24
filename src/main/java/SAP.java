import java.util.*;

public class SAP {

    private Digraph g;
    private DeluxeBFSPavel bfs = new DeluxeBFSPavel();
    private Map<Pair, Result> cache;

    private static final int INFINITY = Integer.MAX_VALUE;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new NullPointerException("Input graph is null!");

        g = new Digraph(G);

        cache = new HashMap<>();
    }

    private static class Result {
        private int ancestor, length;

        private Result(int a, int l) {
            this.ancestor = a;
            this.length = l;
        }

        @Override
        public String toString() {
            return "Result{" + "ancestor=" + ancestor + ", length=" + length + '}';
        }

        public int getAncestor() {
            return ancestor;
        }

        public int getLength() {
            return length;
        }
    }

    private static class Pair {
        private int v, w;

        private Pair(int v, int w) {
            this.v = v;
            this.w = w;
        }

        public int getV() {
            return v;
        }

        public int getW() {
            return w;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            if (v != pair.v || w != pair.w) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = v;
            result = 31 * result + w;
            return result;
        }

        @Override
        public String toString() {
            return "Pair{" + "v=" + v + ", w=" + w + '}';
        }
    }



    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {

        Pair p = new Pair(v, w);

        if (!cache.containsKey(p))
            putPairToCache(p);

        return cache.get(p).getAncestor();
    }

    private void putPairToCache(Pair p) {

        if (p.getV() == p.getW()) {
            cache.put(p, new Result(p.getV(), 0));
            return;
        }

        int ancestor = -1;
        int l;

        List<Integer> vI = new ArrayList<>(1);
        List<Integer> wI = new ArrayList<>(1);

        vI.add(p.getV());
        wI.add(p.getW());

        bfs = bfs.getInstance(g, vI, wI);

        ancestor = bfs.getBestAncestor();
        l = bfs.getBestAncestorLength();

        l = (l == INFINITY ? -1 : l);

        cache.put(p, new Result(ancestor, l));
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Pair p = new Pair(v, w);

        if (!cache.containsKey(p))
            putPairToCache(p);

        return cache.get(p).getLength();
    }

    // length of shortest ancestral path between any vertex in v
    // and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {

        if (v == null || w == null)
            throw new NullPointerException("Input vertices list is null!");

        bfs = bfs.getInstance(g, v, w);

        int l = bfs.getBestAncestorLength();;

         l = (l == INFINITY ? -1 : l);

        return l == INFINITY ? -1 : l;
    }

    // a common ancestor that participates in
    // shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {

        checkParams(v);
        checkParams(w);

        if (v == null || w == null)
            throw new NullPointerException("Input vertices list is null!");

        bfs = bfs.getInstance(g, v, w);

        return bfs.getBestAncestor();
    }

    private void checkParams(Iterable<Integer> params) {
        for (int param : params) {
            if (param < 0 || param > g.V()) {
                throw new IndexOutOfBoundsException();
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}