package Calculation;

import Models.Edge;
import Models.Graph;
import Models.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BSD on 11.07.2015.
 *
 * Rundkurs Mk I
 * Für statischen (Test-)Graphen
 *
 * 1. Startpunkt Südvorstadt
 * 2. Suche kürzeste, ausgehende, unbesuchte Kante
 * * Wiederhole 2. bis keine ausgehende, unbesuchte Kante
 * 3. Suche nähesten Knoten mit unbesuchter, ausgehender Kante
 * * Wiederhole 2. - 3. bis alle Kanten besucht sind
 */
public class Mk_I {

    Graph graph;

    public Mk_I(Graph graph) {
        this.graph = graph;
    }

    public void start() {

        // 1. Startpunkt Südvorstadt
        long startTime = System.currentTimeMillis();
        log("Starting Mk I");

        int costs = 0;
        Vertex currentVertex = graph.getStartVertex();
        List<Edge> unvisited = new ArrayList<>(graph.getEdges());
        List<Edge> route = new ArrayList<>();
        List<Edge> without = new ArrayList<>();

        int iteration = 0;
        while (unvisited.size() > 0 && iteration < 10) {

            // 2. Suche kürzeste, ausgehende, unbesuchte Kante
            iteration++;
            long stepTime = System.currentTimeMillis();
            Edge nextEdge = graph.searchNextShortestEdgeWithout(currentVertex, without);
            if (nextEdge != null) {
                currentVertex = nextEdge.getDestination();
                log("Driving to " + currentVertex.getName() + " at costs: " + nextEdge.getCost());
                costs += nextEdge.getCost();
                route.add(nextEdge);
                String edgeString = "";
                for (Edge edge : route) {
                    edgeString += " " + edge.getId();
                }
                log("Current route:" + edgeString);

                List<Edge> edgesBetween = graph.getEdgesBetween(currentVertex, nextEdge.getSource());
                        /*edgeString = "";
                        for (Edge edge : edgesBetween) {
                            edgeString += " " + edge.getId();
                        }
                        log("edges between:" + edgeString);*/
                without.addAll(edgesBetween);
                        /*edgeString = "";
                        for (Edge edge : without) {
                            edgeString += " " + edge.getId();
                        }
                        log("Current without:" + edgeString);*/
                unvisited.removeAll(edgesBetween);
                        /*edgeString = "";
                        for (Edge edge : unvisited) {
                            edgeString += " " + edge.getId();
                        }
                        log("Current unvisited:" + edgeString);*/

                double elapsedTime = Math.round((System.currentTimeMillis() - startTime) / 10) * 100;
                log("Finished Iteration " + iteration + " in " + elapsedTime + " seconds");
                log("___________________________________________________________________");
            }

            else {

                // 3. Suche nähesten Knoten mit unbesuchter, ausgehender Kante
                log("No unvisited edge in " + currentVertex.getName() + " remaining");
                List<Edge> routeToVertex = graph.searchNextVertexWithUnvisited(currentVertex, unvisited);
                String edgeString = "";
                for (Edge edge : routeToVertex) {
                    edgeString += " " + edge.getId();
                }
                log("Add route:" + edgeString);
                route.addAll(routeToVertex);
                for (Edge edge : routeToVertex) {
                    costs += edge.getCost();
                    if (unvisited.contains(edge)) {
                        log("Added route contained unvisited edge " + edge.getId());
                        List<Edge> edgesBetween = graph.getEdgesBetween(edge.getDestination(), edge.getSource());
                        without.addAll(edgesBetween);
                        unvisited.removeAll(edgesBetween);
                    }
                }
                currentVertex = routeToVertex.get(routeToVertex.size()-1).getDestination();
            }
        }



        if (unvisited.size() == 0) {
            log("");
            log("<<< ___________________________________________________________________ >>>");
            log("<<< Success! Found complete route at costs: " + costs);
            String edgeString = "";
            for (Edge edge : route) {
                edgeString += " " + edge.getId();
            }
            log("<<< Route:" + edgeString);
            log("<<< ___________________________________________________________________ >>>");
            log("");

        }
        double elapsedTime = Math.round((System.currentTimeMillis() - startTime) / 10) * 100;
        log("Finished Mk I in " + elapsedTime + " seconds");
    }

    private void log(String message) {
        System.out.println("[Mk_I] " + message);
    }
}
