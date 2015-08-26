package IM_Challenge;

import Models.Edge;
import Models.Graph;
import Models.Transfer;
import Models.Vertex;
import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BSD on 23.08.2015.
 */
public class IM_Challenge {

    // Trips Columns
    public static int Ab_HST_Nr = 0;
    public static int Ab_HST_Name = 1;
    public static int ABFAHRT = 2;
    public static int LINIE = 3;
    public static int An_HST_Nr = 4;
    public static int An_HST_Name = 5;
    public static int ANKUNFT = 6;
    public static int DAUER = 7;
    public static int Ab_X = 8;
    public static int Ab_Y = 9;
    public static int An_X = 10;
    public static int An_Y = 11;
    // Transfers Columns
    public static int VNR = 0;
    public static int VNAME = 1;
    public static int NNR = 2;
    public static int NNAME = 3;
    public static int ZEIT = 4;

    public static void main(String[] args) {
        Graph graph = mapGraph();
    }

    /**
     * Read and map the CSV file
     */
    private static Graph mapGraph() {
        /*
        Mapping Trips
         */
        Map<Integer, Vertex> vertexMap = new HashMap<>();
        Map<Integer, Edge> edgeMap = new HashMap<>();
        long startTime = System.currentTimeMillis();
        int rows = 0;
        int tripCount = 0;
        log("Mapping trips...");

        String csvFilename = "C://xampp/htdocs/IM_Trips.csv";
        CSVReader csvReader = null;

        try {
            csvReader = new CSVReader(new FileReader(csvFilename), ';');
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String[] row = null;
        try {
            csvReader.readNext(); // Header
            while((row = csvReader.readNext()) != null) {
                rows++;
                String added = "";
                /*
                Check Verteces
                 */

                // Departure Vertex
                Vertex dVertex = null;
                int dID = Integer.parseInt(row[Ab_HST_Nr]);
                if (!vertexMap.containsKey(dID)) {
                    dVertex = new Vertex(dID, row[Ab_HST_Name], row[Ab_X], row[Ab_Y]);
                    vertexMap.put(dID, dVertex);
                    added += "Added Vertex " + row[Ab_HST_Name] + ", ";
                }
                else {
                    dVertex = vertexMap.get(dID);
                }

                // Arrival Vertex
                Vertex aVertex = null;
                int aID = Integer.parseInt(row[An_HST_Nr]);
                if (!vertexMap.containsKey(aID)) {
                    aVertex = new Vertex(aID, row[An_HST_Name], row[An_X], row[An_Y]);
                    vertexMap.put(aID, aVertex);
                    added += "Added Vertex " + row[An_HST_Name] + ", ";
                }
                else {
                    aVertex = vertexMap.get(aID);
                }

                /*
                Check Edge
                 */

                // Edge Mapping
                Edge edge = null;
                int edgeID = Integer.parseInt(row[Ab_HST_Nr] + row[An_HST_Nr]);
                if (!edgeMap.containsKey(edgeID)) {
                    edge = new Edge(edgeID, dVertex, aVertex);
                    added += "Added Edge " + edgeID;
                }
                else {
                    edge = edgeMap.get(edgeID);
                }

                // Edge Trips
                int departure = Integer.parseInt(row[ABFAHRT]);
                int arrival = Integer.parseInt(row[ANKUNFT]);
                int length = Integer.parseInt(row[DAUER]);
                int line = Integer.parseInt(row[LINIE]);
                edge.addTrip(new int[] {departure, arrival, length, line});
                added += "Added Trip " + departure + ", ";
                tripCount++;

                edgeMap.put(edgeID, edge);
//                log(added);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log("Mapped trips with " + vertexMap.size() + " verteces, " + edgeMap.size() + " edges and overall " + tripCount + " trips from " + rows + " lines");



        /*
        Mapping Transfers
         */
        Map<Integer, Transfer> transferMap = new HashMap<>();
        rows = 0;
        log("Mapping transfers...");

        csvFilename = "C://xampp/htdocs/IM_Transfers.csv";
        csvReader = null;

        try {
            csvReader = new CSVReader(new FileReader(csvFilename), ';');
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

       row = null;
        try {
            csvReader.readNext(); // Header
            while((row = csvReader.readNext()) != null) {
                rows++;
                int dID = Integer.parseInt(row[VNR]);
                int aID = Integer.parseInt(row[NNR]);
                if (edgeMap.containsKey(dID) && edgeMap.containsKey(aID)) {
                    int edgeID = Integer.parseInt("" + dID + aID);
                    Transfer transfer = new Transfer(edgeID, vertexMap.get(dID), vertexMap.get(aID), Integer.parseInt(row[ZEIT]));
                } else {
                    log("ERROR: Vertex not found for transfer: " + dID + aID);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log("Mapped transfers with " + vertexMap.size() + " transfers from " + rows + " lines");

        measureTime(startTime, "mapping graph");
        return new Graph(vertexMap, edgeMap, transferMap, vertexMap.get(133));
    }

    private static void measureTime(long startTime, String task) {
        double elapsedTime = Math.round((System.currentTimeMillis() - startTime) / 1000);
        log("Finished " + task + " in " + elapsedTime + " seconds");
    }

    private static void log(String message) {
        System.out.println("[IM_Challenge] " + message);
    }

}
