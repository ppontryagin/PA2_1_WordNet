import org.junit.Test;

import java.util.Random;

/**
 * Created by Pavel.Pontryagin on 16.04.2015.
 */
public class SAPTest {

    @Test
    public void timingTest() {
        In in = new In("digraph-wordnet.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        long started = System.currentTimeMillis();
        int n = 10000;

        Random r = new Random();
        int v;
        int w;

        for (int i = 0; i < n; i++) {
            v = r.nextInt(82192);
            w = r.nextInt(82192);
            sap.length(v, w);
            sap.ancestor(v, w);
        }

        long duration = (System.currentTimeMillis() - started);
        System.out.printf("N: %d, running time: %d, rate: %d\n", n, duration, (long)n / duration);
    }

}
