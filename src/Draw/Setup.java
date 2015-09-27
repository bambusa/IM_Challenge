package Draw;

import Models.Edge;
import Models.Vertex;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Tristan on 24.09.2015.
 */

public class Setup<T> {

    ArrayList<ArrayList<Integer>> coords = new ArrayList<>();
    ArrayList<ArrayList<Integer>> lines = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<ArrayList<Integer>> bestLines = new ArrayList<>();
    ArrayList<String> color = new ArrayList<>();
    int gWidth = 0;
    int gHeight = 0;
    int xMaxValue = 0;
    int xMinValue = 10000000;
    int yMaxValue = 0;
    int yMinValue = 10000000;

    public Setup() {}

    public void setGraph(Map<String, Vertex> vertices, Map<String, Edge> edges, ArrayList<Edge> bestRoute){
        calculateMinAndMax(vertices);
        setCoords(vertices);
        setLines(edges);
        setNames(vertices);
        setBestLines(bestRoute);
        setColor();
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

            int yCheck = Integer.parseInt(vertex.getGeoY().replace(".", ""));
            if (yMaxValue < yCheck) {
                yMaxValue = yCheck;
            }
            if (yCheck < yMinValue) {
                yMinValue = yCheck;
            }

            gWidth = xMaxValue - xMinValue;
            gHeight = yMaxValue - yMinValue;
        }
    }

    public void setCoords(Map<String, Vertex> vertices) {
        List<Vertex> coordinates = new ArrayList<>(vertices.values());

        int i = 0;

        for(Vertex vertex : coordinates) {
            ArrayList<Integer> inner = new ArrayList<>();
            inner.add(0, null);
            inner.add(1, null);
            int j = 0;
            Double xCoordD = ((Integer.parseInt(vertex.getGeoX().replace(".", "")) - xMinValue) / 22.5 + 20 + 10);
            int xCoordI = xCoordD.intValue();
            inner.set(j, xCoordI);
            j++;
            Double yCoordD = ((Integer.parseInt(vertex.getGeoY().replace(".", "")) - yMinValue) / 22.5 + 20 + 10) * -1 + (gHeight / 22.5 + 40 + 30);
            int yCoordI = yCoordD.intValue();
            inner.set(j, yCoordI);
            coords.add(i, inner);
            i++;
        }
    }

    public void setLines(Map<String, Edge> edges) {
        List<Edge> coordinates = new ArrayList<>(edges.values());

        int i = 0;

        for(Edge edge : coordinates) {
            ArrayList<Integer> inner = new ArrayList<>();
            inner.add(0, null);
            inner.add(1, null);
            inner.add(2, null);
            inner.add(3, null);
            Double xCoordDA = ((Integer.parseInt(edge.getDeparture().getGeoX().replace(".", "")) - xMinValue) / 22.5 + 20 + 10);
            int xCoordIA = xCoordDA.intValue();
            inner.set(0, xCoordIA);
            Double yCoordDA = ((Integer.parseInt(edge.getDeparture().getGeoY().replace(".", "")) - yMinValue) / 22.5 + 20 + 10) * -1 + (gHeight / 22.5 + 40 + 30);
            int yCoordIA = yCoordDA.intValue();
            inner.set(1, yCoordIA);
            Double xCoordDB = ((Integer.parseInt(edge.getArrival().getGeoX().replace(".", "")) - xMinValue) / 22.5 + 20 + 10);
            int xCoordIB = xCoordDB.intValue();
            inner.set(2, xCoordIB);
            Double yCoordDB = ((Integer.parseInt(edge.getArrival().getGeoY().replace(".", "")) - yMinValue) / 22.5 + 20 + 10) * -1 + (gHeight / 22.5 + 40 + 30);
            int yCoordIB = yCoordDB.intValue();
            inner.set(3, yCoordIB);
            lines.add(i, inner);
            i++;
        }
    }

    public void setNames(Map<String, Vertex> vertices) {
        List<Vertex> name = new ArrayList<>(vertices.values());

        int i = 0;

        for(Vertex vertex : name) {
            String nameX = vertex.getName();
            names.add(i, nameX);
            i++;
        }
    }

    public void setBestLines(ArrayList<Edge> bestRoute) {
        List<Edge> bestLine = bestRoute;

        int i = 0;

        for(Edge edge : bestLine) {
            ArrayList<Integer> inner = new ArrayList<>();
            inner.add(0, null);
            inner.add(1, null);
            inner.add(2, null);
            inner.add(3, null);
            inner.add(4, null);
            Double xCoordDA = ((Integer.parseInt(edge.getDeparture().getGeoX().replace(".", "")) - xMinValue) / 22.5 + 20 + 10);
            int xCoordIA = xCoordDA.intValue();
            inner.set(0, xCoordIA);
            Double yCoordDA = ((Integer.parseInt(edge.getDeparture().getGeoY().replace(".", "")) - yMinValue) / 22.5 + 20 + 10) * -1 + (gHeight / 22.5 + 40 + 30);
            int yCoordIA = yCoordDA.intValue();
            inner.set(1, yCoordIA);
            Double xCoordDB = ((Integer.parseInt(edge.getArrival().getGeoX().replace(".", "")) - xMinValue) / 22.5 + 20 + 10);
            int xCoordIB = xCoordDB.intValue();
            inner.set(2, xCoordIB);
            Double yCoordDB = ((Integer.parseInt(edge.getArrival().getGeoY().replace(".", "")) - yMinValue) / 22.5 + 20 + 10) * -1 + (gHeight / 22.5 + 40 + 30);
            int yCoordIB = yCoordDB.intValue();
            inner.set(3, yCoordIB);
            int col = edge.getActiveTrip().getLine();
            inner.set(4, col);
            bestLines.add(i, inner);
            i++;
        }
    }

    public void setColor() {
        for(int i = 0; i <= 13; i++) {
            color.add(i, "238,28,40");
            color.add(i, "240,89,55");
            color.add(i, "236,17,99");
            color.add(i, "205,21,39");
            color.add(i, "0,0,0");
            color.add(i, "255,220,1");
            color.add(i, "157,1,56");
            color.add(i, "37,150,66");
            color.add(i, "135,199,101");
            color.add(i, "250,175,24");
            color.add(i, "180,219,174");
            color.add(i, "0,114,73");
            color.add(i, "254,194,16");
        }
    }

    public ArrayList<String> getColor() { return color; }

    public ArrayList<ArrayList<Integer>> getBestLines() { return bestLines; }

    public ArrayList<String> getNames() { return names; }

    public ArrayList<ArrayList<Integer>> getLines() {
        return lines;
    }

    public ArrayList<ArrayList<Integer>> getCoords() {
        return coords;
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