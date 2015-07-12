package Models;

import java.util.List;

/**
 * Created by BSD on 10.07.2015.
 */
public class Edge {

    private final String id;
    private final Vertex source;
    private final Vertex destination;
    private int cost;

    public Edge (String id, Vertex source, Vertex destination, int cost) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.cost = cost;
    }

    public String getId() {
        return id;
    }
    public Vertex getSource() {
        return source;
    }
    public Vertex getDestination() {
        return destination;
    }
    public int getCost() {
        return cost;
    }
}
