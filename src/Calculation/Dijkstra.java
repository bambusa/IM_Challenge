package Calculation;

import Models.Edge;
import Models.Graph;
import Models.Trip;
import Models.Vertex;

import java.util.*;

/*
 * Created by BSD on 11.07.2015.
 */

public class Dijkstra {

    private Graph graph;
    private Vertex startVertex;
    private int startTime;
    private int startLine;
    // private List<Vertex> searching;

    private List<Vertex> Q;
    private List<Vertex> searching;
    private HashMap<Vertex, Edge> predecessor;
    private HashMap<Vertex, Integer> distance;
    // private HashMap<Vertex, Edge> preEdge;



    public Dijkstra(Graph graph, Edge lastEdge, List<Vertex> searching) {
        this.graph = graph;
        this.startVertex = lastEdge.getArrival();
        this.startTime = lastEdge.getActiveTrip().getArrivalTime();
        this.startLine = lastEdge.getActiveTrip().getLine();
        this.searching = searching;

        if (searching == null) {
            Q = new ArrayList<>();
            Q.addAll(graph.getVerteces().values());
        }
        else {
            Q = new ArrayList<>(searching);
            if (Q.contains(startVertex)) {
            }
        }
    }

    public List<Edge> getShortestPaths() {
        // Initialize
        log("Getting shortest path for " + Q.size() + " verteces");
        distance.put(startVertex, 0);
        predecessor.put(startVertex, null);
        for (Vertex vertex : Q) {
            distance.put(vertex, 999999999);
            predecessor.put(vertex, null);
        }

        // Distance calculation
        while (Q.size() > 0) {
            Vertex nearestVertex = getNearestVertex();
            Q.remove(nearestVertex);

            for (Edge edge : graph.getEdges().values()) { // for each neighbor, which is still in Q
                if (edge.getDeparture() == nearestVertex && Q.contains(edge.getArrival())) {
                    Trip activeTrip = null;
                    if (predecessor.get(edge.getDeparture()) != null && predecessor.get(edge.getDeparture()).getActiveTrip() != null) {
                        activeTrip = predecessor.get(edge.getDeparture()).getActiveTrip();
                    }
                    else {
                        activeTrip = new Trip(-1, startTime, "", "", "", "", -1, startLine);
                    }

                    edge.setActiveTrip(graph.findNextShortestTrip(edge, activeTrip));
                    if (edge.getActiveTrip().getArrivalTime() < distance.get(edge.getArrival())) {
                        distance.put(edge.getArrival(), edge.getActiveTrip().getArrivalTime());
                        predecessor.put(edge.getArrival(), edge);
                    }
                }
            }
        }

        // Get shortest path
        Vertex nearest = null;
        for (Vertex vertex : searching) {
            if (nearest == null || distance.get(vertex) < distance.get(nearest)) {
                nearest = vertex;
            }
        }
        Vertex current = nearest;
        List<Edge> route = new ArrayList<>();
        while (current != startVertex) {
            route.add(predecessor.get(current));
            current = predecessor.get(current).getDeparture();
        }

        Collections.reverse(route);
        return route;
    }

    private Vertex getNearestVertex() {
        Vertex bestVertex = null;
        int bestDistance = 999999;

        for (Vertex vertex : Q) {
            if (bestVertex == null || distance.get(vertex) < bestDistance) {
                bestVertex = vertex;
                bestDistance = distance.get(vertex);
            }
        }

        return bestVertex;
    }



    private void log(String message) {
        System.out.println("[Dijkstra] " + message);
    }

}
