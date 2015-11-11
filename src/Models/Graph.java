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
    private HashMap<String, Vertex> vertexMap;
    private HashMap<String, Edge> edgeMap;
    private HashMap<String, Transfer> transferMap;
    private ArrayList<List<Edge>> longWaitingTime;
    private ArrayList<List<Edge>> unnecessaryTrips;
    private ArrayList<String> terminalStops;

    public Graph() {
        vertexMap = new HashMap<>();
        edgeMap = new HashMap<>();
        transferMap = new HashMap<>();
        longWaitingTime = new ArrayList<>();
        unnecessaryTrips = new ArrayList<>();
    }

    public Graph(HashMap<String, Vertex> vertexMap, HashMap<String, Edge> edgeMap, HashMap<String, Transfer> transferMap, ArrayList<String> terminalStops) {
        this.vertexMap = vertexMap;
        this.edgeMap = edgeMap;
        this.transferMap = transferMap;
        longWaitingTime = new ArrayList<>();
        unnecessaryTrips = new ArrayList<>();
        this.terminalStops = terminalStops;
        log(this.terminalStops.size()+" terminal stops");
    }

    public Graph(Graph graph) {
        this.vertexMap = new HashMap<>(graph.getVertices());
        this.edgeMap = new HashMap<>(graph.getEdges());
        this.transferMap = new HashMap<>(graph.getTransferMap());
        this.longWaitingTime = new ArrayList<>(graph.getLongWaitingTime());
        this.unnecessaryTrips = new ArrayList<>(graph.getUnnecessaryTrips());
        this.terminalStops = new ArrayList<>(graph.getTerminalStops());
    }

    public void resetGraph() {
        longWaitingTime = new ArrayList<>();
        unnecessaryTrips = new ArrayList<>();
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
                Trip thisTrip = findNextShortestTrip(edge, lastEdge, -1);
                if (thisTrip != null && (shortestEdge == null || thisTrip.getDepartureTime() < shortestEdge.getActiveTrip().getDepartureTime())) {
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

        if (shortestEdge != null) {
//            log("searchNextShortestEdgeWith | Waiting Time: " + (shortestEdge.getActiveTrip().getDepartureTime() - lastEdge.getActiveTrip().getArrivalTime()));
            if ((shortestEdge.getActiveTrip().getDepartureTime() - lastEdge.getActiveTrip().getArrivalTime()) >= 300) {
                ArrayList<Edge> list = new ArrayList<>();
                list.add(new Edge(lastEdge));
                list.add(new Edge(shortestEdge));
                longWaitingTime.add(list);
            }
        }
        return shortestEdge;
    }

    public Trip findNextShortestTrip(Edge newEdge, Edge lastEdge, int maxWait) {
        if (maxWait == -1) {
            maxWait = 3600;
        }
        Trip bestTrip = null;
        Trip activeTrip = lastEdge.getActiveTrip();
        int arrival = activeTrip.getArrivalTime();
        int line = activeTrip.getLine();
        String ahsb = activeTrip.getArrivalHSB();
        boolean terminalStop = false;


        int wait = 0;
        ArrayList<Trip> possibleTrips = new ArrayList<>();
//        log("findNextShortestTrip | " + edge.toString() + ", coming from " + ahsb + " at " + timeFromSeconds(arrival));

        /*** Find possible trips, including transfer times, limited time window ***/
        while (wait <= maxWait) {
            int thisDeparture = arrival + wait;
            if (newEdge.containsTrips(thisDeparture)) {
//                log("findNextShortestTrip | Waiting " + wait + " seconds until " + timeFromSeconds(thisDeparture));
                for (Trip trip : newEdge.getTrips(thisDeparture)) {
//                    log("findNextShortestTrip | Checking " + trip.toString());

                    /*** Same train, cancel further search, tolerate waiting time up to 10 minutes ***/
                    if (line == trip.getLine() && trip.getDepartureTime() - arrival < 600) {
                        if (lastEdge.getDeparture() != newEdge.getArrival()) {
//                            log("findNextShortestTrip | Same train is continuing on edge, cancel search");
                            return trip;
                        }
                    }

                    /*** Check transfer time ***/
                    // else
                    String transferId = ahsb + trip.getDepartureHSB();
                    int transferTime = 0;
                    if (transferMap.containsKey(transferId)) {
                        transferTime = transferMap.get(transferId).getTime();
                    }
                    else {
                        log("WARNING: No transfer time for " + ahsb + " -> " + trip.getDepartureHSB() + " on edge " + newEdge.getDeparture().getName() + " -> " + newEdge.getArrival().getName());
                    }

                    if (trip.getDepartureTime() >= (arrival + transferTime)) {
                        possibleTrips.add(trip);
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
        List<Edge> route = dijkstra.getShortestPath();
        List<Edge> list = new ArrayList<>();
        list.add(new Edge(lastEdge));
        for (Edge edge : route) {
            list.add(new Edge(edge));
        }
        unnecessaryTrips.add(list);
        return route;
    }

    public Map<String, Transfer> getTransferMap() {
        return transferMap;
    }

    public ArrayList<List<Edge>> getLongWaitingTime() {
        return longWaitingTime;
    }

    public ArrayList<List<Edge>> getUnnecessaryTrips() {
        return unnecessaryTrips;
    }

    public ArrayList<String> getTerminalStops() {
        return terminalStops;
    }



    public String timeFromSeconds(int seconds) {
        int minutes = (int) Math.ceil(seconds / 60);
        int hours = (int) Math.floor(minutes / 60);
        String remainingMinutes = String.format("%02d", (minutes - (hours * 60)));
        return hours + ":" + remainingMinutes + " (" + seconds + ")";
    }

    private void log(String message) {
//        System.out.println("[Graph] " + message);
    }
}
