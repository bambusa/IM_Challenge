/*
package Tests;

import Calculation.Mk_I;
import Models.Edge;
import Models.Graph;
import Models.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

*/
/**
 * Created by BSD on 11.07.2015.
 *//*

public class Test {

    public static void main(String[] args) {
        log("Starting test class");
        Mk_I_Test_I();
    }

    private static void Mk_I_Test_I() {
        log("Testing Mk_I with Test Graph I");

        Mk_I mk_i_test = new Mk_I(getTestGraphI());
        mk_i_test.start();
    }

    private static Graph getTestGraphI() {
        long startTime = System.currentTimeMillis();

        Vertex a = new Vertex(1, "A", "", "");
        Vertex b = new Vertex(2, "B", "", "");
        Vertex c = new Vertex(3, "C", "", "");
        Vertex d = new Vertex(3, "D", "", "");

        Edge ab = new Edge("AB", a, b, 1, 0, 0, 0);
        Edge ba = new Edge("BA", b ,a, 1, 0, 0, 0);
        Edge ad = new Edge("AD", a, d, 2, 0, 0, 0);
        Edge da = new Edge("DA", d, a, 2, 0, 0, 0);

        Edge bc = new Edge("BC", b, c, 1, 0, 0, 0);
        Edge cb = new Edge("CB", c, b, 1, 0, 0, 0);
        Edge bd = new Edge("BD", b, d, 3, 0, 0, 0);
        Edge db = new Edge("DB", d, b, 3, 0, 0, 0);

        Edge cd = new Edge("CD", c, d, 3, 0, 0, 0);
        Edge dc = new Edge("DC", d, c, 3, 0, 0, 0);

        List<Vertex> verteces = new ArrayList<>();
        verteces.addAll(Arrays.asList(new Vertex[]{a, b, c, d}));
        List<Edge> edges = new ArrayList<>();
        edges.addAll(Arrays.asList(new Edge[] {ab, ba, ad, da, bc, cb, bd, db, cd, dc}));
        Graph graph = new Graph(verteces, edges, a);

        double elapsedTime = Math.round((System.currentTimeMillis() - startTime) / 10) * 100;
        log("Created Test Graph I with " + verteces.size() + " verteces and " + edges.size() + " edges in " + elapsedTime + " seconds");
        return graph;
    }

    private static void log(String message) {
        System.out.println("[TEST] " + message);
    }
}
*/
