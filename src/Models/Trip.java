package Models;

/**
 * Created by BSZ Thinkpad on 28.08.2015.
 */
public class Trip {

    private int departure;
    private int arrival;
    private String departureHSB;
    private String arrivalHSB;
    private int length;
    private int line;

    public Trip (int departure, int arrival, String departureHSB, String arrivalHSB, int length, int line) {
        this.departure = departure;
        this.arrival = arrival;
        this.departureHSB = departureHSB;
        this.arrivalHSB = arrivalHSB;
        this.length = length;
        this.line = line;
    }

    public int getDeparture() {
        return departure;
    }

    public int getArrival() {
        return arrival;
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
}
