package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BSD on 11.07.2015.
 */
public class Graph {
    private Map<Integer, Vertex> vertexMap;
    private Map<Integer, Edge> edgeMap;
    private Map<Integer, Transfer> transferMap;
    private Vertex startVertex;

    public Graph() {
        vertexMap = new HashMap<>();
        edgeMap = new HashMap<>();
        transferMap = new HashMap<>();
    }

    public Graph(Map<Integer, Vertex> vertexMap, Map<Integer, Edge> edgeMap, Map<Integer, Transfer> transferMap, Vertex startVertex) {
        this.vertexMap = vertexMap;
        this.edgeMap = edgeMap;
        this.transferMap = transferMap;
        this.startVertex = startVertex;
    }

    public Vertex getStartVertex() {
        return startVertex;
    }

    public void setStartVertex(Vertex startVertex) {
        this.startVertex = startVertex;
    }

    public Map<Integer, Vertex> getVerteces() {
        return vertexMap;
    }

    public Map<Integer, Edge> getEdges() {
        return edgeMap;
    }

    public Edge getEdgesBetween(Vertex dVertex, Vertex aVertex) {
//        log("getEdgesBetween | Checking edges in between for in total " + edges.size() + " edges");
        int eID = Integer.parseInt(dVertex.getName()+aVertex.getName());
        if (edgeMap.containsKey(eID)) {
            Edge edgeBetween = edgeMap.get(eID);
            if (edgeBetween.getDeparture() == dVertex && edgeBetween.getArrival() == aVertex) {
                return edgeBetween;
            }
            else {
                log("ERROR: EdgeBetween ID and verteces not corresponding:, ID: " + eID + ", verteces: " + dVertex.getId() + "-" + aVertex.getId());
            }
        }
        else {
            log("ERROR: No edge found between " + dVertex.getName() + " and " + aVertex.getName());
        }
        return null;
    }

    /*public Edge searchNextShortestEdgeWithout(Vertex currentVertex, List<Edge> whithout, int departure) {
        // Remove excluded edges
        log("searchNextShortestEdgeWithout | From " + currentVertex.getName() + " without " + whithout.size() + " edges");
        Edge shortestEdge = null;
        List<Edge> remainingEdges = new ArrayList<>(edgeMap.values());
        remainingEdges.removeAll(whithout);

        // Remove edges with other departure verteces than current vertex
//        log("searchNextShortestEdgeWithout | Edge sum: " + remainingEdges.size());
        List<Edge> removeEdges = new ArrayList<>();
        for (Edge edge : remainingEdges) {
            if (edge.getDeparture() != currentVertex) {
//                log("remove edge " + edge.getId());
                removeEdges.add(edge);
            }
        }
        remainingEdges.removeAll(removeEdges);

        // Find shortest edge
//        log("searchNextShortestEdgeWithout | " + remainingEdges.size() + " potential edges");
        if (remainingEdges.size() > 0) {
            for (Edge edge : remainingEdges) {
                ArrayList<int[]> length = edge.getNextTrips(departure);
                if (shortestEdge == null || edge.getLength() < shortestEdge.getLength()) {
                    shortestEdge = edge;
                }
            }
        }
        else {
            return null;
        }

        return shortestEdge;
    }

    public List<Edge> searchNextVertexWithUnvisited(Vertex currentVertex, List<Edge> unvisited) {
        List<Vertex> potentialVerteces = new ArrayList<>();
        for (Edge edge : unvisited) {
            if (!potentialVerteces.contains(edge.getSource())) {
                potentialVerteces.add(edge.getSource());
            }
        }
        log("searchNextVertexWithUnvisited | From " + currentVertex.getName() + ", " + potentialVerteces.size() + " remaining verteces with unvisited edges");

        Dijkstra dijkstra = new Dijkstra(this, currentVertex, potentialVerteces);
        return  dijkstra.getShortestPath();
    }*/



    private void log(String message) {
        System.out.println("[Graph] " + message);
    }
}
