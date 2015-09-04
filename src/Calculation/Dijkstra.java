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
    private List<Vertex> searching;
    private HashMap<Vertex, Edge> tripTo;
    private List<Vertex> visitedVertices;
    private Edge startEdge;

    public Dijkstra(Graph graph, Edge lastEdge, List<Vertex> searching) {
        this.graph = graph;
        this.startEdge = lastEdge;
        this.searching = searching;
        tripTo = new HashMap<>();
        visitedVertices = new ArrayList<>();

        // Start
        visitedVertices.add(startEdge.getArrival());
        log("Init Dijkstra, searching for " + searching.size() + " vertices");
    }

    public List<Edge> getShortestPath() {
        long startTime = System.currentTimeMillis();

        while (!visitedVertices.containsAll(searching)) {
            /*** Filter visited vertices with unvisited neighbors ***/
            List<Edge> edgesToUnvisitedNeighbors = new ArrayList<>();
            for (Vertex vertex : visitedVertices) {
                for (Edge edge : graph.getEdges().values()) {
                    if (edge.getDeparture() == vertex && !visitedVertices.contains(edge.getArrival())) {
                        edgesToUnvisitedNeighbors.add(edge);
                    }
                }
            }
//            log("getShortestPath | Iteration | Found " + edgesToUnvisitedNeighbors.size() + " edges to neighbors from " + visitedVertices.size() + " visited vertices");

            /*** Find best trips on edges to unvisited neighbors ***/
            ArrayList<Edge> compareEdgeTrips = new ArrayList<>();
            for (Edge edge : edgesToUnvisitedNeighbors) {
                Trip lastTrip = null;
                if (edge.getDeparture() == startEdge.getArrival()) {
                    lastTrip = startEdge.getActiveTrip();
                }
                else if (tripTo.containsKey(edge.getDeparture())) {
                    lastTrip = tripTo.get(edge.getDeparture()).getActiveTrip();
                }
                else {
                    log("getShortestPath | ERROR: Previous trip not found");
                    return null;
                }

                Trip bestTrip = graph.findNextShortestTrip(edge, lastTrip, 86400);
                if (bestTrip != null) {
//                    log("getShortestPath | New edge for comparison: " + edge.toString());
                    edge.setActiveTrip(bestTrip);
                    compareEdgeTrips.add(edge);
                }
                else {
                    log("getShortestPath | WARNING: No trip for edge " + edge.toString());
                }
            }

            /*** Find trip with next arrival time and add to visited vertices ***/
            Edge bestEdgeTrip = null;
            if (compareEdgeTrips.size() > 0) {
                for (Edge edge : compareEdgeTrips) {
                    if (bestEdgeTrip == null || edge.getActiveTrip().getArrivalTime() < bestEdgeTrip.getActiveTrip().getArrivalTime()) {
                        bestEdgeTrip = edge;
                    }
                }
            }
            else {
                ArrayList<Vertex> remaining = new ArrayList<>(searching);
                remaining.removeAll(visitedVertices);
                String remainingString = "";
                for (Vertex vertex : remaining) {
                    remainingString += vertex.getName() + ", ";
                }
                log("getShortestPath | WARNING: No trips found for comparison, remaining: " + remainingString);
                visitedVertices.addAll(remaining);
            }

//            log("getShortestPath | Selecting best " + bestEdgeTrip.toString()  + " - " + bestEdgeTrip.getActiveTrip().toString());
            if (bestEdgeTrip != null) {
                tripTo.put(bestEdgeTrip.getArrival(), bestEdgeTrip);
                visitedVertices.add(bestEdgeTrip.getArrival());
            }
        }

        /*** Backwards calculate shortest route to nearest target vertex ***/
        Edge bestTrip = null;
        for (Edge edge : tripTo.values()) {
            if (searching.contains(edge.getArrival())) {
                if (bestTrip == null || edge.getActiveTrip().getArrivalTime() < bestTrip.getActiveTrip().getArrivalTime()) {
                    bestTrip = edge;
                }
            }
        }

        ArrayList<Edge> route = new ArrayList<>();
        if (bestTrip != null) {
            log("getShortestPath | Backwards calculating route to nearest target vertex " + bestTrip.getArrival().toString());
            Vertex tripToVertex = bestTrip.getArrival();
            while (tripToVertex != startEdge.getArrival()) {
                Edge thisEdge = tripTo.get(tripToVertex);
                route.add(thisEdge);
                tripToVertex = thisEdge.getDeparture();
            }
            Collections.reverse(route);

            String routeString = "getShortestPath | Final route: " + startEdge.getArrival().getName();
            for (Edge edge : route) {
                routeString += " -> " + edge.getArrival().getName();
            }
            log(routeString);
        }
        else {
            log("getShortestPath | ERROR: No trip to target vertex found");
        }
        measureTime(startTime, "Dijkstra");
        return route;
    }



    private void log(String message) {
        System.out.println("[Dijkstra] " + message);
    }

    private void measureTime(long startTime, String task) {
        double elapsedTime = Math.round((System.currentTimeMillis() - startTime) / 1000);
        log("<<< Finished " + task + " in " + elapsedTime + " seconds >>>");
        log("<<< ___________________________________________________________________ >>>");
        log("");
    }
}
