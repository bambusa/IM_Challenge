package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BSD on 10.07.2015.
 */
public class Edge {

    private final int id; // Departure ID + Arrival ID
    private final Vertex departure;
    private final Vertex arrival;
    private Map<Integer, ArrayList<int[]>> trips; // <departure, [departure, arrival, length, line]>

    public Edge (int id, Vertex departure, Vertex arrival) {
        this.id = id;
        this.departure = departure;
        this.arrival = arrival;
        trips = new HashMap<>();
    }

    /**
     * Trip Array should be [departure, arrival, length, line]
     */
    public boolean addTrip(int[] trip) {
        if (trip.length != 4) {
            System.out.println("trip Array should consist of [departure, arrival, length, line], " + trip.length + " given");
            return false;
        }
        else {
            ArrayList<int[]> tripArray = null;
            if (trips.containsKey(trip[0])) {
               tripArray = trips.get(trip[0]);
            } else {
               tripArray = new ArrayList<>();
            }
            tripArray.add(new int[]{trip[0], trip[1], trip[2], trip[3]});
            trips.put(trip[0], tripArray);
            return true;
        }
    }

    public boolean containsTrip(int departure) {
        return trips.containsKey(departure);
    }

    /**
     * Search for the next trip within an hour from departure
     */
    public ArrayList<int[]> getNextTrips(int departure) {
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

    public int getId() {
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
