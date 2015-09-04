package Models;

/**
 * Created by BSZ Thinkpad on 28.08.2015.
 */
public class Trip {

    private int departureTime;
    private int arrivalTime;
    private String departureHSB;
    private String arrivalHSB;
    private String departure;
    private String arrival;
    private int length;
    private int line;

    public Trip (int departureTime, int arrivalTime, String departureHSB, String arrivalHSB, String departure, String arrival, int length, int line) {
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureHSB = departureHSB;
        this.arrivalHSB = arrivalHSB;
        this.departure = departure;
        this.arrival = arrival;
        this.length = length;
        this.line = line;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public String getDepartureHSB() {
        return departureHSB;
    }

    public String getArrivalHSB() {
        return arrivalHSB;
    }

    public int getLength() {
        return length;
    }

    public int getLine() {
        return line;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public String timeFromSeconds(int seconds) {
        int minutes = (int) Math.ceil(seconds / 60);
        int hours = (int) Math.floor(minutes / 60);
        String remainingMinutes = String.format("%02d", (minutes - (hours * 60)));
        return hours + ":" + remainingMinutes + " (" + seconds + ")";
    }

    public String toString() {
        return "[TRIP " + departureHSB + " -> " + arrivalHSB + " | " + timeFromSeconds(departureTime) + " -> " + timeFromSeconds(arrivalTime) + " | " + line + "]";
    }
}
