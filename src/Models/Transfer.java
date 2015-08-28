package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BSD on 10.07.2015.
 */
public class Transfer {

    private String id; // Departure ID + Arrival ID
    private Vertex departure;
    private Vertex arrival;
    private int time;

    public Transfer (String id, Vertex departure, Vertex arrival, int time) {
        this.id = id;
        this.departure = departure;
        this.arrival = arrival;
        this.time = time;
    }

    public String getId() {
        return id;
    }
    public Vertex getDeparture() {
        return departure;
    }
    public Vertex getArrival() {
        return arrival;
    }
    public int getTime() {
        return time;
    }
}
