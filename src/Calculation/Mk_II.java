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
 * Algorithmus Mk II
 * Für echten Graphen
 *
 * 1. Startpunkt Südvorstadt
 * 2. Suche nächsten, ausgehenden, unbesuchten Trip vom aktuellen Knoten (Minimierung Wartezeit)
 * * Wiederhole 2. bis keine ausgehende, unbesuchte Kante mehr übrig
 * 3. Suche nächsten Knoten mit nächsten, unbesuchten, ausgehenden Trip (Minimierung Fahrtzeit + Wartezeit)
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

    public ArrayList<Edge> start() {
        /*** 1. Startpunkt Südvorstadt ***/
        Edge startEdge = null;
        long algoTime = System.currentTimeMillis();
        Edge lastEdge = new Edge("INIT", startVertex, startVertex);
        lastEdge.setActiveTrip(new Trip(0, startTime, startHSB, startHSB, startVertex.getName(), startVertex.getName(), 0, startLine));
        ArrayList<Edge> unvisited = new ArrayList<>(graph.getEdges().values());
        ArrayList<Edge> route = new ArrayList<>();
        log("Starting Mk II with " + unvisited.size() + " edges");

        int iteration = 0;
        while (unvisited.size() > 0) {

            /*** 2. Suche nächsten, ausgehenden, unbesuchten Trip vom aktuellen Knoten (Minimierung Wartezeit) ***/
            iteration++;
            long stepTime = System.currentTimeMillis();
            Edge newEdge = graph.searchNextShortestEdgeWith(lastEdge, unvisited);

            if (newEdge != null && newEdge.getActiveTrip() != null) {
                if (startEdge == null) {
                    startEdge = newEdge;
                }
                lastEdge = newEdge;
                Edge addEdge = new Edge(newEdge);
                route.add(addEdge);
                Edge checkEdge = route.get(route.size()-1);
                log("Added " + checkEdge.toString() + ", " + checkEdge.getActiveTrip().toString());
                unvisited.removeAll(graph.getEdgesBetween(lastEdge.getDeparture(), lastEdge.getArrival()));

                double elapsedTime = Math.round((System.currentTimeMillis() - startTime) / 10) * 100;
//                measureTime(iterationTime, "iteration");
                log("<<< New Trip " + lastEdge.toString() + ", " + lastEdge.getActiveTrip().toString() + ", " + unvisited.size() + " edges remaining >>>");
                log("<<< ---------------------------------------------------------------------------------------------------------------------------------------- >>>");
            }

            else {
                /*** 3. Suche nächsten Knoten mit nächsten, unbesuchten, ausgehenden Trip (Minimierung Fahrtzeit + Wartezeit) ***/
                log("No unvisited edge in " + lastEdge.getArrival().getName() + " remaining");
                List<Edge> routeToVertex = graph.searchNextVertexWithUnvisited(lastEdge, unvisited);

                for (Edge edge : routeToVertex) {
                    lastEdge = edge;
                    Edge addEdge = new Edge(lastEdge);
                    route.add(addEdge);
                    unvisited.removeAll(graph.getEdgesBetween(lastEdge.getDeparture(), lastEdge.getArrival()));
                }
            }
        }



        if (unvisited.size() == 0) {
            log("");
            log("<<< ___________________________________________________________________ >>>");
            log("<<< Success! Found complete route, starting from " +
                        startEdge.getDeparture().getName() + " - " + timeFromSeconds(startEdge.getActiveTrip().getDepartureTime()) +
                        " finishing at " + lastEdge.getDeparture().getName() + " - " + timeFromSeconds(lastEdge.getActiveTrip().getArrivalTime()) +
                        ", taking all in all " + timeFromSeconds(lastEdge.getActiveTrip().getArrivalTime() - startEdge.getActiveTrip().getDepartureTime()));
            String edgeString = "";
            for (Edge edge : route) {
                edgeString += " -> " + edge.getDeparture().getName() + " | " + edge.getActiveTrip().getLine();
            }
            log("<<< Route:" + edgeString);
            log("<<< ___________________________________________________________________ >>>");
        }
        measureTime(algoTime, "Mk II");
        return route;
    }

    public String timeFromSeconds(int seconds) {
        int minutes = (int) Math.ceil(seconds / 60);
        int hours = (int) Math.floor(minutes / 60);
        String remainingMinutes = String.format("%02d", (minutes - (hours * 60)));
        return hours + ":" + remainingMinutes + " (" + seconds + ")";
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
