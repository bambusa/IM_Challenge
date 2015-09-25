package Draw;

import Models.Edge;
import Models.Vertex;
import java.util.*;

/**
 * Created by Tristan on 24.09.2015.
 */

public class Setup<T> {

    ArrayList<Integer> inner = new ArrayList<>();
    ArrayList<ArrayList<Integer>> coords = new ArrayList<>();
    int gWidth = 0;
    int gHeight = 0;
    int xMaxValue = 0;
    int xMinValue = 10000000;
    int yMaxValue = 0;
    int yMinValue = 10000000;

    public Setup() {}

    public void setGraph(Map<String, Vertex> vertices, Map<String, Edge> edges){
        calculateMinAndMax(vertices);
        System.out.println("Width: " + gWidth + " | Height: " + gHeight);
        setCoords(vertices);
    }

    public void calculateMinAndMax(Map<String, Vertex> vertices){
        List<Vertex> calc = new ArrayList<>(vertices.values());
        for(Vertex vertex : calc) {
            int xCheck = Integer.parseInt(vertex.getGeoX().replace(".", ""));
            if (xMaxValue < xCheck) {
                xMaxValue = xCheck;
            }
            if (xCheck < xMinValue) {
                xMinValue = xCheck;
            }
            System.out.println("xMax: " + xMaxValue + " | xMin: " + xMinValue);
            int yCheck = Integer.parseInt(vertex.getGeoY().replace(".", ""));
            if (yMaxValue < yCheck) {
                yMaxValue = yCheck;
            }
            if (yCheck < yMinValue) {
                yMinValue = yCheck;
            }
            System.out.println("yMax: " + yMaxValue + " | yMin: " + yMinValue);
            gWidth = xMaxValue - xMinValue;
            gHeight = yMaxValue - yMinValue;
        }
    }

    public void setCoords(Map<String, Vertex> vertices) {
        List<Vertex> coordinates = new ArrayList<>(vertices.values());

        int i = 0;
        inner.add(0, null);
        inner.add(1, null);

        for(Vertex vertex : coordinates) {
            int j = 0;
            int xCoord = Integer.parseInt(vertex.getGeoX().replace(".", ""));
            inner.set(j, xCoord);
            j++;
            int yCoord = Integer.parseInt(vertex.getGeoY().replace(".", ""));
            inner.set(j, yCoord);
            coords.add(i, inner);
            i++;
        }

        for(int k = 0; k < coords.size(); k++) {
            int l = 0;
            System.out.println("x: " + coords.get(k).get(l));
            l++;
            System.out.println("y: " + coords.get(k).get(l));
        }
    }

    public int getWidth(){
        return gWidth;
    }

    public int getHeight() {
        return gHeight;
    }

    public int getXMinValue() {
        return xMinValue;
    }

    public int getYMinValue() {
        return yMinValue;
    }

}