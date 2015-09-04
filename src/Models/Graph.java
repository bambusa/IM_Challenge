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
        /*** Remove excluded edges ***/
        log("searchNextShortestEdgeWithout | " + lastEdge.getDeparture().getName() + " -> " + lastEdge.getArrival().getName() + ", without " + route.size() + " edges");
        List<Edge> remainingEdges = new ArrayList<>(edgeMap.values());
        remainingEdges.removeAll(route);
        Edge shortestEdge = null;

        /*** Remove edges with other departure verteces than current vertex ***/
        List<Edge> removeEdges = new ArrayList<>();
        for (Edge edge : remainingEdges) {
            if (edge.getDeparture() != lastEdge.getArrival()) {
                removeEdges.add(edge);
            }
        }
        remainingEdges.removeAll(removeEdges);
//        log("searchNextShortestEdgeWithout | Removed edges where departure is not " + lastEdge.getArrival().getName() + ", " + remainingEdges.size() + " remaining");

        /*** Find next departing trip ***/
        if (remainingEdges.size() > 0) {
            for (Edge edge : remainingEdges) {
                log("searchNextShortestEdgeWithout | Checking " + edge.toString());
                Trip lastTrip = null;
                if (shortestEdge != null) {
                    lastTrip = shortestEdge.getActiveTrip();
                }
                else {
                    lastTrip = lastEdge.getActiveTrip();
                }
                Trip thisTrip = findNextShortestTrip(edge, lastTrip);
                if (shortestEdge == null || thisTrip.getDepartureTime() < shortestEdge.getActiveTrip().getDepartureTime()) {
                    log("searchNextShortestEdgeWithout | Found better trip at " + timeFromSeconds(thisTrip.getDepartureTime()) + " on line " + thisTrip.getLine() + " | " + edge.getDeparture().getName() + " -> " + edge.getArrival().getName());
                    if (shortestEdge != null) {
                        log("searchNextShortestEdgeWithout | Previous trip was at " + timeFromSeconds(shortestEdge.getActiveTrip().getDepartureTime()) + " on line " + shortestEdge.getActiveTrip().getLine() + " to " + shortestEdge.getArrival().getName());
                    }
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
        int arrival = activeTrip.getArrivalTime();
        int line = activeTrip.getLine();
        String ahsb = activeTrip.getArrivalHSB();
        int wait = 0;
        ArrayList<Trip> possibleTrips = new ArrayList<>();
        log("findNextShortestTrip | " + edge.toString() + ", coming from " + ahsb + " at " + timeFromSeconds(arrival));

        /*** Find possible trips, including transfer times, limited time window ***/
        while (wait <= 3600) {
            int thisDeparture = arrival + wait;
            if (edge.containsTrips(thisDeparture)) {
                log("findNextShortestTrip | Waiting " + wait + " seconds until " + timeFromSeconds(thisDeparture));
                for (Trip trip : edge.getTrips(thisDeparture)) {
                    log("findNextShortestTrip | Checking " + trip.toString());

                    /*** Same train, cancel furhter search ***/
                    if (line == trip.getLine() && arrival == trip.getDepartureTime()) {
                        log("findNextShortestTrip | Same train is continuing on edge, cancel search");
                        return trip;
                    }

                    else {
                        String transferId = ahsb + trip.getDepartureHSB();
                        int transferTime = 0;
                        if (transferMap.containsKey(transferId)) {
                            transferTime = transferMap.get(transferId).getTime();
                        }
                        else {
                            log("WARNING: No transfer time for " + ahsb + " -> " + trip.getDepartureHSB() + " on edge " + edge.getDeparture().getName() + " -> " + edge.getArrival().getName());
                        }

                        if (trip.getDepartureTime() >= (arrival + transferTime)) {
                            possibleTrips.add(trip);
                        }
                    }
                }
            }
            wait += 60;
        }

        /*** Find next trip in possible trips ***/
        log("findNextShortestTrip | Comparing " + possibleTrips.size() + " possible trips");
        for (Trip trip : possibleTrips) {
            if (bestTrip == null || trip.getDepartureTime() < bestTrip.getDepartureTime()) {
                log("Setting better trip");
                bestTrip = trip;
            }
        }

        if (bestTrip != null) {
            log("findNextShortestTrip | Best " + bestTrip.toString());
            return bestTrip;
        }
        else {
            log("ERROR: Found no next trip");
            return null;
        }
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

    public String timeFromSeconds(int seconds) {
        int minutes = (int) Math.ceil(seconds / 60);
        int hours = (int) Math.floor(minutes / 60);
        int remainingMinutes = minutes - (hours * 60);
        return hours + ":" + remainingMinutes;
    }



    private void log(String message) {
        System.out.println("[Graph] " + message);
    }
}
