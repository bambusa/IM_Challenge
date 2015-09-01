package Calculation;

import Models.Edge;
import Models.Graph;
import Models.Trip;
import Models.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 /* Created by BSD on 23.08.2015.
 *
 * Algorythmus Mk II
 * Für echten Graphen
 *
 * 1. Startpunkt Südvorstadt
 * 2. Suche kürzeste, ausgehende, unbesuchte Kante
 * * Wiederhole 2. bis keine ausgehende, unbesuchte Kante
 * 3. Suche nähesten Knoten mit unbesuchter, ausgehender Kante
 * * Wiederhole 2. - 3. bis alle Kanten besucht sind
 */



public class Mk_II {

    private Graph graph;
    private Vertex startVertex;
    private int startTime;
    private String startHSB;
    private int startLine;

    public Mk_II(Graph graph, Vertex startVertex, int startTime, String startHSB, int startLine) {
        this.graph = graph;
        this.startVertex = startVertex;
        this.startTime = startTime;
        this.startHSB = startHSB;
        this.startLine = startLine;
    }

    public void start() {

        // 1. Startpunkt Südvorstadt
        long iterationTime = System.currentTimeMillis();
        log("Starting Mk II");

        Edge lastEdge = new Edge("INIT", null, startVertex);
        lastEdge.setActiveTrip(new Trip(0, startTime, "", startHSB, 0, startLine));
        ArrayList<Edge> unvisited = new ArrayList<>(graph.getEdges().values());
        ArrayList<Edge> route = new ArrayList<>();


        int iteration = 0;
        while (unvisited.size() > 0) {

            // 2. Suche kürzeste, ausgehende, unbesuchte Kante
            iteration++;
            long stepTime = System.currentTimeMillis();
            lastEdge = graph.searchNextShortestEdgeWithout(lastEdge, route);
            if (lastEdge != null && lastEdge.getActiveTrip() != null) {
                log("New Trip " + lastEdge.getActiveTrip().getLine() + " | " + lastEdge.getDeparture().getName() + " -> " + lastEdge.getArrival().getName() + " | " + lastEdge.getActiveTrip().getDeparture() + " -> " + lastEdge.getActiveTrip().getArrival() + ", " + unvisited.size() + " edges remaining");
                route.add(lastEdge);

                unvisited.remove(graph.getEdgesBetween(lastEdge.getDeparture(), lastEdge.getArrival()));

                double elapsedTime = Math.round((System.currentTimeMillis() - startTime) / 10) * 100;
//                measureTime(iterationTime, "iteration");
            }

            else {

                // 3. Suche nähesten Knoten mit unbesuchter, ausgehender Kante
                log("No unvisited edge in " + lastEdge.getArrival().getName() + " remaining");
                List<Edge> routeToVertex = graph.searchNextVertexWithUnvisited(lastEdge, unvisited);
                route.addAll(routeToVertex);

                for (Edge edge : routeToVertex) {
                    unvisited.remove(graph.getEdgesBetween(lastEdge.getDeparture(), lastEdge.getArrival()));
                }
            }
        }



        if (unvisited.size() == 0) {
            log("");
            log("<<< ___________________________________________________________________ >>>");
            log("<<< Success! Found complete route");
            String edgeString = "";
            for (Edge edge : route) {
                edgeString += " " + edge.getId();
            }
            log("<<< Route:" + edgeString);
            log("<<< ___________________________________________________________________ >>>");
            log("");

        }
        double elapsedTime = Math.round((System.currentTimeMillis() - startTime) / 10) * 100;
        log("Finished Mk II in " + elapsedTime + " seconds");
    }

    private void log(String message) {
        System.out.println("[Mk_II] " + message);
    }

    private void measureTime(long startTime, String task) {
        double elapsedTime = Math.round((System.currentTimeMillis() - startTime) / 1000);
        log("<<< Finished " + task + " in " + elapsedTime + " seconds >>>");
        log("<<< ___________________________________________________________________ >>>");
        log("");
    }
}
