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

    public Map<String, Vertex> getVertices() {
        return vertexMap;
    }

    public Map<String, Edge> getEdges() {
        return edgeMap;
    }

    public ArrayList<Edge> getEdgesBetween(Vertex dVertex, Vertex aVertex) {
        log("getEdgesBetween | " + dVertex.toString() + " and " + aVertex.toString());
        ArrayList<Edge> edges = new ArrayList<>();
        String[] eIDs = new String[] {dVertex.getId() + aVertex.getId(), aVertex.getId() + dVertex.getId()};
        for (String eID : eIDs) {
            if (edgeMap.containsKey(eID)) {
                log("getEdgesBetween | found " + edgeMap.get(eID).toString());
                edges.add(edgeMap.get(eID));
            }
            else {
                log("getEdgesBetween | WARNING: Edge " + eID + " not found");
            }
        }
        return edges;
    }

    public Edge searchNextShortestEdgeWith(Edge lastEdge, ArrayList<Edge> with) {
        /*** Remove visited edges ***/
        log("searchNextShortestEdgeWith | From " + lastEdge.getArrival().getName() + " with " + with.size() + " unvisited edges at " + timeFromSeconds(lastEdge.getActiveTrip().getArrivalTime()));
        Edge shortestEdge = null;
        Trip lastTrip = lastEdge.getActiveTrip();
        ArrayList<Edge> unvisited = new ArrayList<>(with);

        /*** Remove edges with other departure vertices than current vertex ***/
        ArrayList<Edge> removeEdges = new ArrayList<>();
        for (Edge edge : unvisited) {
            if (edge.getDeparture() != lastEdge.getArrival()) {
                removeEdges.add(edge);
            }
        }
        unvisited.removeAll(removeEdges);
//        log("searchNextShortestEdgeWith | " + unvisited.size() + " edges remaining");

        /*** Find next departing trip ***/
        if (unvisited.size() > 0) {
            for (Edge edge : unvisited) {
//                log("searchNextShortestEdgeWith | Checking " + edge.toString());
                Trip thisTrip = findNextShortestTrip(edge, lastTrip, -1);
                if (shortestEdge == null || thisTrip.getDepartureTime() < shortestEdge.getActiveTrip().getDepartureTime()) {
                    log("searchNextShortestEdgeWith | Found better trip at " + timeFromSeconds(thisTrip.getDepartureTime()) + " on line " + thisTrip.getLine() + " | " + edge.getDeparture().getName() + " -> " + edge.getArrival().getName());
                    edge.setActiveTrip(thisTrip);
                    shortestEdge = edge;
                }
            }
            if (shortestEdge != null && shortestEdge.getActiveTrip() != null) {
//                log("searchNextShortestEdgeWith | Found shortest Edge to " + shortestEdge.getArrival().getName());
            }
            else {
//                log("searchNextShortestEdgeWith | ERROR: Found no trip");
            }
        }
        else {
//            log("searchNextShortestEdgeWith | Found no remaining edges");
        }

        return shortestEdge;
    }

    public Trip findNextShortestTrip(Edge edge, Trip activeTrip, int maxWait) {
        if (maxWait == -1) {
            maxWait = 3600;
        }
        Trip bestTrip = null;
        int arrival = activeTrip.getArrivalTime();
        int line = activeTrip.getLine();
        String ahsb = activeTrip.getArrivalHSB();
        int wait = 0;
        ArrayList<Trip> possibleTrips = new ArrayList<>();
//        log("findNextShortestTrip | " + edge.toString() + ", coming from " + ahsb + " at " + timeFromSeconds(arrival));

        /*** Find possible trips, including transfer times, limited time window ***/
        while (wait <= 86400) {
            int thisDeparture = arrival + wait;
            if (edge.containsTrips(thisDeparture)) {
//                log("findNextShortestTrip | Waiting " + wait + " seconds until " + timeFromSeconds(thisDeparture));
                for (Trip trip : edge.getTrips(thisDeparture)) {
//                    log("findNextShortestTrip | Checking " + trip.toString());

                    /*** Same train, cancel furhter search ***/
                    if (line == trip.getLine() && arrival == trip.getDepartureTime()) {
//                        log("findNextShortestTrip | Same train is continuing on edge, cancel search");
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
//        log("findNextShortestTrip | Comparing " + possibleTrips.size() + " possible trips");
        for (Trip trip : possibleTrips) {
            if (bestTrip == null || trip.getDepartureTime() < bestTrip.getDepartureTime()) {
                bestTrip = trip;
            }
        }

        if (bestTrip != null) {
//            log("findNextShortestTrip | Best " + bestTrip.toString());
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
        log("searchNextVertexWithUnvisited | From " + lastEdge.getArrival().getName() + ", " + potentialVerteces.size() + " remaining vertices with unvisited edges");

        Dijkstra dijkstra = new Dijkstra(this, lastEdge, potentialVerteces);
        return  dijkstra.getShortestPath();
    }

    public Map<String, Transfer> getTransferMap() {
        return transferMap;
    }

    public String timeFromSeconds(int seconds) {
        int minutes = (int) Math.ceil(seconds / 60);
        int hours = (int) Math.floor(minutes / 60);
        String remainingMinutes = String.format("%02d", (minutes - (hours * 60)));
        return hours + ":" + remainingMinutes + " (" + seconds + ")";
    }



    private void log(String message) {
        System.out.println("[Graph] " + message);
    }
}
