public class SAP {

    private Digraph g;
    private BreadthFirstDirectedPaths bfdpV;
    private BreadthFirstDirectedPaths bfdpW;

    private static final int INFINITY = Integer.MAX_VALUE;



    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new NullPointerException("Input graph is null!");

        g = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {

        bfdpV = new BreadthFirstDirectedPaths(g, v);
        bfdpW = new BreadthFirstDirectedPaths(g, w);

        int ancestor = -1;
        int length = INFINITY;

        for (int vPoint : bfdpV.pathTo(0)) {
            if (bfdpW.hasPathTo(vPoint)) {
                int tempLength = bfdpW.distTo(vPoint) + bfdpV.distTo(vPoint);
                if (tempLength < length) {
                    length = tempLength;
                    ancestor = vPoint;
                }
            }
        }

        return length == INFINITY ? -1 : length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        bfdpV = new BreadthFirstDirectedPaths(g, v);
        bfdpW = new BreadthFirstDirectedPaths(g, w);

        int ancestor = -1;
        int length = INFINITY;

        for (int vPoint : bfdpV.pathTo(0)) {
            if (bfdpW.hasPathTo(vPoint)) {
                int tempLength = bfdpW.distTo(vPoint) + bfdpV.distTo(vPoint);
                if (tempLength < length) {
                    length = tempLength;
                    ancestor = vPoint;
                }
            }
        }

        return ancestor;
    }

    // TODO length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;
    }

    // TODO a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}