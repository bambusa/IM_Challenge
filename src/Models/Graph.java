package Models;

import Calculation.Dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BSD on 11.07.2015.
 */
public class Graph {
    private Map<String, Vertex> vertexMap;
    private Map<String, Edge> edgeMap;
    private Map<String, Transfer> transferMap;

    public Graph() {
        vertexMap = new HashMap<>();
        edgeMap = new HashMap<>();
        transferMap = new HashMap<>();
    }

    public Graph(Map<String, Vertex> vertexMap, Map<String, Edge> edgeMap, Map<String, Transfer> transferMap) {
        this.vertexMap = vertexMap;
        this.edgeMap = edgeMap;
        this.transferMap = transferMap;
    }

    public Map<String, Vertex> getVerteces() {
        return vertexMap;
    }

    public Map<String, Edge> getEdges() {
        return edgeMap;
    }

    public ArrayList<Edge> getEdgesBetween(Vertex dVertex, Vertex aVertex) {
        ArrayList<Edge> edges = new ArrayList<>();
        String[] eIDs = new String[] {dVertex.getId() + aVertex.getId(), aVertex.getId() + dVertex.getId()};
        for (String eID : eIDs) {
            if (edgeMap.containsKey(eID)) {
                edges.add(edgeMap.get(eID));
            }
        }
        return edges;
    }

    public Edge searchNextShortestEdgeWithout(Edge lastEdge, ArrayList<Edge> route) {
        // Remove excluded edges
//        log("searchNextShortestEdgeWithout | From " + lastEdge.getArrival().getName() + " without " + route.size() + " edges");
        List<Edge> remainingEdges = new ArrayList<>(edgeMap.values());
        remainingEdges.removeAll(route);
        Edge shortestEdge = null;

        // Remove edges with other departure verteces than current vertex
        List<Edge> removeEdges = new ArrayList<>();
        for (Edge edge : remainingEdges) {
            if (edge.getDeparture() != lastEdge.getArrival()) {
                removeEdges.add(edge);
            }
        }
        remainingEdges.removeAll(removeEdges);
//        log("searchNextShortestEdgeWithout | Removed edges where departure is not " + lastEdge.getArrival().getName() + ", " + remainingEdges.size() + " remaining");

        // Find shortest trip, limit search to 1 hour
        if (remainingEdges.size() > 0) {
            for (Edge edge : remainingEdges) {
//                log("searchNextShortestEdgeWithout | Checking edge " + edge.getId());
                Trip thisTrip = findNextShortestTrip(edge, lastEdge.getActiveTrip());
                if (shortestEdge == null || thisTrip.getDeparture() < shortestEdge.getActiveTrip().getDeparture()) {
                    edge.setActiveTrip(thisTrip);
                    shortestEdge = edge;
                }
            }
            if (shortestEdge != null && shortestEdge.getActiveTrip() != null) {
//                log("searchNextShortestEdgeWithout | Found shortest Edge to " + shortestEdge.getArrival().getName());
            }
            else {
                log("searchNextShortestEdgeWithout | ERROR: Found no trip");
            }
        }
        else {
            log("searchNextShortestEdgeWithout | ERROR: Found no remaining edeges");
        }

        return shortestEdge;
    }

    public Trip findNextShortestTrip(Edge edge, Trip activeTrip) {
        Trip bestTrip = null;
        int departure = activeTrip.getArrival();
        int wait = 0;
//        log("findNextShortestTrip | Starting search at " + departure);
        while (wait <= 3600) {
//            log("findNextShortestTrip | Waiting " + wait + " seconds...");
            int thisDeparture = departure + wait;
            if (edge.containsTrips(thisDeparture)) {
                for (Trip trip : edge.getTrips(thisDeparture)) {
//                    log("findNextShortestTrip | Checking trip at " + thisDeparture + " on line " + trip.getLine());
                    if (bestTrip == null) {
                        log("findNextShortestTrip | Starting with first trip at " + thisDeparture + " on line " + trip.getLine());
                        bestTrip = trip;
                    }
                    else if (activeTrip.getLine() == 0 || (trip.getLine() == activeTrip.getLine() && trip.getDeparture() == thisDeparture)) {
                        if (bestTrip.getArrival() > trip.getArrival()) {
                            log("findNextShortestTrip | Found better trip on same line " + trip.getLine());
                            bestTrip = trip;
                        }
                    }
                    else {
                        String transferID = activeTrip.getArrivalHSB() + trip.getDepartureHSB();
                        if (transferMap.containsKey(transferID) &&
                                (thisDeparture + transferMap.get(transferID).getTime()) <= trip.getDeparture() ) {
                            if (bestTrip.getArrival() > trip.getArrival()) {
                                log("findNextShortestTrip | Found better trip, switching to line " + trip.getLine());
                                bestTrip = trip;
                            }
                        }
                    }
                }
            }
            wait += 60;
        }
//        log("findNextShortestTrip | Best trip is at " + bestTrip.getDeparture() + " on line " + bestTrip.getLine());
        return bestTrip;
    }

    public List<Edge> searchNextVertexWithUnvisited(Edge lastEdge, List<Edge> unvisited) {
        List<Vertex> potentialVerteces = new ArrayList<>();
        for (Edge edge : unvisited) {
            if (!potentialVerteces.contains(edge.getDeparture())) {
                potentialVerteces.add(edge.getDeparture());
            }
        }
        log("searchNextVertexWithUnvisited | From " + lastEdge.getArrival().getName() + ", " + potentialVerteces.size() + " remaining verteces with unvisited edges");

        Dijkstra dijkstra = new Dijkstra(this, lastEdge, potentialVerteces);
        return  dijkstra.getShortestPaths();
    }



    private void log(String message) {
        System.out.println("[Graph] " + message);
    }
}
