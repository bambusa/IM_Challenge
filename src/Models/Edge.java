package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BSD on 10.07.2015.
 */
public class Edge {

    private final String id; // Departure ID + Arrival ID
    private final Vertex departure;
    private final Vertex arrival;
    private Map<Integer, ArrayList<Trip>> trips;
    private Trip activeTrip = null;

    public Edge (String id, Vertex departure, Vertex arrival) {
        this.id = id;
        this.departure = departure;
        this.arrival = arrival;
        trips = new HashMap<>();
    }

    public Edge (Edge lastEdge) {
        id = lastEdge.getId();
        departure = lastEdge.getDeparture();
        arrival = lastEdge.getArrival();
        trips = new HashMap<>(lastEdge.getTripMap());
        activeTrip = lastEdge.getActiveTrip();
    }

    /**
     * Trip Array should be [departure, arrival, length, line]
     */
    public void addTrip(Trip trip) {
        ArrayList<Trip> tripArray = null;
        if (trips.containsKey(trip.getDeparture())) {
           tripArray = trips.get(trip.getDeparture());
        } else {
           tripArray = new ArrayList<>();
        }
        tripArray.add(trip);
        trips.put(trip.getDepartureTime(), tripArray);
    }

    public boolean containsTrips(int departure) {
        return trips.containsKey(departure);
    }
    public ArrayList<Trip> getTrips(int departure) {
        return trips.get(departure);
    }
    public Map<Integer, ArrayList<Trip>> getTripMap() {
        return trips;
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

    private void log(String message) {
        System.out.println("[Edge] " + message);
    }

    public Trip getActiveTrip() {
        return activeTrip;
    }

    public void setActiveTrip(Trip activeTrip) {
        this.activeTrip = activeTrip;
    }

    public String toString() {
        return "[EDGE " + departure.getId() + " -> " + arrival.getId() + " | " + departure.getName() + " -> " + arrival.getName() + "]";
    }
}
