package Models;

import Calculation.Dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by BSD on 11.07.2015.
 */
public class Graph {
    private final List<Vertex> verteces;
    private final List<Edge> edges;
    private Vertex startVertex;

    public Graph() {
        verteces = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public Graph(List<Vertex> verteces, List<Edge> edges, Vertex startVertex) {
        this.verteces = verteces;
        this.edges = edges;
        this.startVertex = startVertex;
    }

    public Vertex getStartVertex() {
        return startVertex;
    }

    public void setStartVertex(Vertex startVertex) {
        this.startVertex = startVertex;
    }

    public List<Vertex> getVerteces() {
        return verteces;
    }

    public List<Edge> getEdges() {
        return edges;
    }



    public List<Edge> getEdgesBetween(Vertex vertex1, Vertex vertex2) {
//        log("getEdgesBetween | Checking edges in between for in total " + edges.size() + " edges");
        List<Edge> edgesBetween = new ArrayList<>();
        for (Edge edge : edges) {
//            log("getEdgesBetween | Checking in between for " + edge.getId());
            if ((edge.getSource() == vertex1 && edge.getDestination() == vertex2) || (edge.getDestination() == vertex1 && edge.getSource() == vertex2)) {
//                log("getEdgesBetween | " + edge.getId() + " is in between");
                edgesBetween.add(edge);
            }
        }
        return edgesBetween;
    }

    public Edge searchNextShortestEdgeWithout(Vertex currentVertex, List<Edge> whithout) {
        log("searchNextShortestEdgeWithout | From " + currentVertex.getName() + " without " + whithout.size() + " edges in total " + edges.size() + " edges");
        Edge shortestEdge = null;
        List<Edge> remainingEdges = new ArrayList<>(edges);
        remainingEdges.removeAll(whithout);

//        log("searchNextShortestEdgeWithout | Edge sum: " + remainingEdges.size());
        List<Edge> removeEdges = new ArrayList<>();
        for (Edge edge : remainingEdges) {
            if (edge.getSource() != currentVertex) {
//                log("remove edge " + edge.getId());
                removeEdges.add(edge);
            }
        }
        remainingEdges.removeAll(removeEdges);

        log("searchNextShortestEdgeWithout | " + remainingEdges.size() + " potential edges");
        if (remainingEdges.size() > 0) {
            for (Edge edge : remainingEdges) {
                if (shortestEdge == null || edge.getCost() < shortestEdge.getCost()) {
                    shortestEdge = edge;
                }
            }
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
    }



    private void log(String message) {
        System.out.println("[GRAPH] " + message);
    }
}
