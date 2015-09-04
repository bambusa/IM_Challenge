package Models;

/**
 * Created by BSD on 10.07.2015.
 */
public class Vertex {

    private final String id;
    private final String name;
    private final String geoX;
    private final String geoY;

    public Vertex(String id, String name, String geoX, String geoY) {
        this.id = id;
        this.name = name;
        this.geoX = geoX;
        this.geoY = geoY;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getGeoX() {
        return geoX;
    }
    public String getGeoY() {
        return geoY;
    }

    public String toString() {
        return "[VERTEX " + name + " | " + geoX + ", " + geoY + "]";
    }
}
