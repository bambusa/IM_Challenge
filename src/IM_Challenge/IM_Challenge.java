package IM_Challenge;

import Models.*;
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
    public static int Ab_HSTB_Nr = 0;
    public static int Ab_HST_Nr = 1;
    public static int Ab_HST_Name = 2;
    public static int ABFAHRT = 3;
    public static int LINIE = 4;
    public static int An_HSTB_Nr = 5;
    public static int An_HST_Nr = 6;
    public static int An_HST_Name = 7;
    public static int ANKUNFT = 8;
    public static int DAUER = 9;
    public static int Ab_X = 10;
    public static int Ab_Y = 11;
    public static int An_X = 12;
    public static int An_Y = 13;
    // Transfers Columns
    public static int VHSBNR = 0;
    public static int VNR = 1;
    public static int VNAME = 2;
    public static int NHSBNR = 3;
    public static int NNR = 4;
    public static int NNAME = 5;
    public static int ZEIT = 6;

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
        Map<String, Vertex> vertexMap = new HashMap<>();
        Map<String, Edge> edgeMap = new HashMap<>();
        long startTime = System.currentTimeMillis();
        int rows = 0;
        int tripCount = 0;
        log("Mapping trips...");

        String csvFilename = "C://Users/beny-/IdeaProjects/IM_Challenge/PHP Database/IM_Trips.csv";
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
                String dID = String.format("%03d", Integer.parseInt(row[Ab_HST_Nr]));
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
                String aID = String.format("%03d", Integer.parseInt(row[An_HST_Nr]));
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
                String edgeID = String.format("%03d", Integer.parseInt(row[Ab_HST_Nr])) + String.format("%03d", Integer.parseInt(row[An_HST_Nr]));
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
                String departureHSB = String.format("%05d", Integer.parseInt(row[An_HSTB_Nr]));
                String arrivalHSB = String.format("%05d", Integer.parseInt(row[Ab_HSTB_Nr]));
                int length = Integer.parseInt(row[DAUER]);
                int line = Integer.parseInt(row[LINIE]);
                edge.addTrip(new Trip(departure, arrival, departureHSB, arrivalHSB, length, line));
                added += "Added Trip " + departure + ", ";
                tripCount++;

                edgeMap.put(edgeID, edge);
                //log(added);
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
        Map<String, Transfer> transferMap = new HashMap<>();
        rows = 0;
        log("Mapping transfers...");

        csvFilename = "C://Users/beny-/IdeaProjects/IM_Challenge/PHP Database/IM_Transfers.csv";
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
                String vhsbnr = String.format("%05d", Integer.parseInt(row[VHSBNR]));
                String nhsbnr = String.format("%05d", Integer.parseInt(row[NHSBNR]));
                String tID = vhsbnr + nhsbnr;
                if (!transferMap.containsKey(tID)) {
                    Transfer transfer = new Transfer(tID, vhsbnr, nhsbnr, Integer.parseInt(row[ZEIT]));
                    transferMap.put(tID, transfer);
                }
                else {
                    log("ERROR: Transfer already existing: " + tID);
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
        log("Mapped transfers with " + transferMap.size() + " transfers from " + rows + " lines");

        measureTime(startTime, "mapping graph");
        if (!vertexMap.containsKey(133)) {
            log("Vertex 133 not existing");
        }
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
