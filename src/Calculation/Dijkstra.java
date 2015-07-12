package Calculation;

import Models.Edge;
import Models.Graph;
import Models.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by BSD on 11.07.2015.
 */
public class Dijkstra {

    private Graph graph;
    private Vertex startVertex;
    private List<Vertex> Q;
    private List<Vertex> searching;
    private HashMap<Vertex, Vertex> predecessor;
    private HashMap<Vertex, Integer> distance;
    private HashMap<Vertex, Edge> preEdge;



    public Dijkstra(Graph graph, Vertex startVertex, List<Vertex> searching) {
        this.graph = graph;
        this.startVertex = startVertex;

        if (searching == null) {
            this.searching = new ArrayList<>();
            this.searching.addAll(graph.getVerteces());
        }
        else {
            this.searching = searching;
        }
    }

    public List<Edge> getShortestPath() {

        // Start Algorithm
        log("Starting from " + startVertex.getName() + " searching for " + searching.size() + " verteces");
        initialize();
        while (Q.size() > 0) {

            // Nearest vertex
            Vertex thisVertex = null;
            int thisDistance = 1000000000;
            for (Vertex vertex : Q) {
                if (distance.get(vertex) < thisDistance) {
                    thisVertex = vertex;
                    thisDistance = distance.get(vertex);
                }
            }
            log("Selecting vertex " + thisVertex.getName());

            // Remove vertex
            Q.remove(thisVertex);

            // Check Neighbors
            for (Edge edge : graph.getEdges()) {
                if (edge.getSource() == thisVertex) {
                    updateDistance(thisVertex, edge);
                }
            }
        }

        Vertex routeTo = null;
        int routeLength = 1000000000;
        for (Vertex vertex : searching) {
            if (routeTo == null || distance.get(vertex) < routeLength) {
                routeTo = vertex;
                routeLength = distance.get(vertex);
            }
        }
        log("Found nearest vertex " + routeTo.getName() + " at cost " + routeLength);
        return createRoute(routeTo);

    }

    private void initialize() {
        this.Q = new ArrayList<>();
        Q.addAll(graph.getVerteces());
        predecessor = new HashMap<>();
        distance = new HashMap<>();
        preEdge = new HashMap<>();

        for (Vertex vertex : Q) {
            predecessor.put(vertex, null);
            distance.put(vertex, 1000000000);
            preEdge.put(vertex, null);
        }
        distance.put(startVertex, 0);
    }

    private void updateDistance(Vertex thisVertex, Edge neighborEdge) {
        int thisDistance = distance.get(thisVertex) + neighborEdge.getCost();
        if (thisDistance < distance.get(neighborEdge.getDestination())) {
            log("Updating distance from " + thisVertex.getName() + " to " + neighborEdge.getDestination().getName() + ": " + neighborEdge.getCost());
            distance.put(neighborEdge.getDestination(), thisDistance);
            predecessor.put(neighborEdge.getDestination(), thisVertex);
            preEdge.put(neighborEdge.getDestination(), neighborEdge);
        }
    }

    private List<Edge> createRoute(Vertex destination) {
        List<Edge> route = new ArrayList<>();
        route.add(preEdge.get(destination));
        Vertex thisVertex = destination;
        while (predecessor.get(thisVertex) != startVertex) {
            thisVertex = predecessor.get(thisVertex);
            route.add(preEdge.get(thisVertex));
        }

        Collections.reverse(route);
        String message = "Found route";
        for (Edge edge : route) {
            message += " " + edge.getId();
        }
        log(message);
        return route;
    }



    private void log(String message) {
        System.out.println("[DIJKSTRA] " + message);
    }

}
