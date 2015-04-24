import java.util.*;

public class DeluxeBFSPavel {
    private static final int INFINITY = Integer.MAX_VALUE;
    private Map<VertexSource, Boolean> marked;
    private Map<VertexSource, Integer> edgeTo;
    private Map<VertexSource, Integer> distTo;

    private List<VertexSource> oldValues;

    private int bestAncestor;
    private int bestAncestorLength;

    public DeluxeBFSPavel() {
    }

    public DeluxeBFSPavel getInstance(Digraph G, Iterable<Integer> a, Iterable<Integer> b) {
        init(G, a, b);
        return this;
    }

    private void init(Digraph G, Iterable<Integer> a, Iterable<Integer> b) {

        bestAncestor = -1;
        bestAncestorLength = INFINITY;

        if (marked == null) {
            marked = new HashMap<>();
            distTo = new HashMap<>();
            edgeTo = new HashMap<>();

            oldValues = new ArrayList<>();

            for (int v = 0; v < G.V(); v++) {
                marked.put(new VertexSource(v, 0), false);
                distTo.put(new VertexSource(v, 0), INFINITY);
                edgeTo.put(new VertexSource(v, 0), 0);

                marked.put(new VertexSource(v, 1), false);
                distTo.put(new VertexSource(v, 1), INFINITY);
                edgeTo.put(new VertexSource(v, 1), 0);
            }

            bfs(G, a, b);

        } else {
            for (VertexSource val : oldValues) {
                marked.put(val, false);
                distTo.put(val, INFINITY);
                edgeTo.put(val, 0);
            }

            oldValues = new ArrayList<>();

            bfs(G, a, b);
        }
    }

    // BFS from single source
    private void bfs(Digraph G, Iterable<Integer> a, Iterable<Integer> b) {

        for (int iA : a) {
            for (int iB : b) {
                if (iA == iB) {
                    bestAncestor = iA;
                    bestAncestorLength = 0;
                    return;
                }
            }
        }

        Queue<VertexSource> q = new Queue<>();

        VertexSource vA;
        VertexSource vB;

        for (int iA : a) {
            vA = new VertexSource(iA, 0);
            oldValues.add(vA);
            marked.put(vA, true);
            distTo.put(vA, 0);
            q.enqueue(vA);
        }

        for (int iB : b) {
            vB = new VertexSource(iB, 1);
            oldValues.add(vB);
            marked.put(vB, true);
            distTo.put(vB, 0);
            q.enqueue(vB);
        }

        while (!q.isEmpty()) {

            VertexSource v = q.dequeue();

            for (int w : G.adj(v.getVertex())) {

                VertexSource vW = new VertexSource(w, v.getSource());

                if (!marked.get(vW)) {
                    edgeTo.put(vW, v.getVertex());
                    distTo.put(vW, distTo.get(v) + 1);
                    marked.put(vW, true);
                    oldValues.add(vW);
                    q.enqueue(vW);

                    int otherSource = (v.getSource() == 1) ? 0 : 1;
                    VertexSource otherVertex = new VertexSource(w, otherSource);

                    if (marked.get(otherVertex)) {
                        int d = distTo.get(otherVertex) + distTo.get(vW);
                        if (d < bestAncestorLength) {
                            bestAncestorLength = d;
                            bestAncestor = w;
                        }
                    }
                }
            }
        }
    }

    public int getBestAncestorLength() {
        return bestAncestorLength;
    }

    public int getBestAncestor() {
        return bestAncestor;
    }

    private class VertexSource {
        int vertex;
        int source;

        public VertexSource(int vertex, int source) {
            this.vertex = vertex;
            this.source = source;
        }

        public int getVertex() {
            return vertex;
        }

        public void setVertex(int vertex) {
            this.vertex = vertex;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            VertexSource that = (VertexSource) o;

            if (source != that.source) return false;
            if (vertex != that.vertex) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = vertex;
            result = 31 * result + source;
            return result;
        }

        @Override
        public String toString() {
            return "VertexSource{" +
                    "vertex=" + vertex +
                    ", source=" + source +
                    '}';
        }
    }

    /**
     * Is there a directed path from the source <tt>s</tt> (or sources) to vertex <tt>v</tt>?
     *
     * @param v the vertex
     * @return <tt>true</tt> if there is a directed path, <tt>false</tt> otherwise
     */
    public boolean hasPathTo(int v) {
        return marked.get(new VertexSource(v, 0));
    }

    /**
     * Returns the number of edges in a shortest path from the source <tt>s</tt>
     * (or sources) to vertex <tt>v</tt>?
     *
     * @param v the vertex
     * @return the number of edges in a shortest path
     */
    public int distTo(int v) {
        return distTo.get(new VertexSource(v, 0));
    }

    /**
     * Returns a shortest path from <tt>s</tt> (or sources) to <tt>v</tt>, or
     * <tt>null</tt> if no such path.
     *
     * @param v the vertex
     * @return the sequence of vertices on a shortest path, as an Iterable
     */
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = v; distTo.get(new VertexSource(x, 0)) != 0; x = edgeTo.get(new VertexSource(x, 0)))
            path.push(x);
        path.push(x);
        return path;
    }

    /**
     * Unit tests the <tt>BreadthFirstDirectedPaths</tt> data type.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        // StdOut.println(G);

        int a = Integer.parseInt(args[1]);
        int b = Integer.parseInt(args[2]);

        DeluxeBFSPavel bfs = new DeluxeBFSPavel();
//        bfs = bfs.getInstance(G, a, b);

//        for (int v = 0; v < G.V(); v++) {
//            if (bfs.hasPathTo(v)) {
//                StdOut.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
//                for (int x : bfs.pathTo(v)) {
//                    if (x == s) StdOut.print(x);
//                    else StdOut.print("->" + x);
//                }
//                StdOut.println();
//            } else {
//                StdOut.printf("%d to %d (-):  not connected\n", s, v);
//            }
//
//        }
    }


}
