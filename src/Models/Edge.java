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

    public Edge (String id, Vertex departure, Vertex arrival) {
        this.id = id;
        this.departure = departure;
        this.arrival = arrival;
        trips = new HashMap<>();
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
        trips.put(trip.getDeparture(), tripArray);
    }

    public boolean containsTrip(int departure) {
        return trips.containsKey(departure);
    }

    /**
     * Search for the next trip within an hour from departure
     */
    public ArrayList<Trip> getNextTrips(int departure) {
        int i = 0;
        while (!trips.containsKey(departure)) {
            departure += 60;
            i++;
            if (i == 60) {
                log("No trip found in the next hour from " + departure);
                return null;
            }
        }

        return trips.get(departure);
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
}
