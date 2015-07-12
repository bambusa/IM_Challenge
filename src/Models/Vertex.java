package Models;

/**
 * Created by BSD on 10.07.2015.
 */
public class Vertex {

    private final int id;
    private final String name;
    private final String geoX;
    private final String geoY;

    public Vertex(int id, String name, String geoX, String geoY) {
        this.id = id;
        this.name = name;
        this.geoX = geoX;
        this.geoY = geoY;
    }

    public int getId() {
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
}
