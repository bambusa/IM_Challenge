package IM_Challenge;

import Calculation.Mk_II;
import Models.*;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    // Files
    private static final String tripCSV = "C:\\Users\\BSD\\IdeaProjects\\IM_Challenge\\PHP Database\\IM_Trips.csv";
    private static final String transferCSV = "C:\\Users\\BSD\\IdeaProjects\\IM_Challenge\\PHP Database\\IM_Transfers.csv";
    private static final String resultCSV = "C:\\Users\\BSD\\IdeaProjects\\IM_Challenge\\PHP Database\\IM_Result.csv";
    private static final String waitingCSV = "C:\\Users\\BSD\\IdeaProjects\\IM_Challenge\\PHP Database\\IM_Long_Waiting_Time.csv";
    private static final String unnecessaryCSV = "C:\\Users\\BSD\\IdeaProjects\\IM_Challenge\\PHP Database\\IM_Unnecessary_Trips.csv";

    public static void main(String[] args) {
        long overallTime = System.currentTimeMillis();
        Graph graph = mapGraph();

        ArrayList<Edge> bestRoute = null;
        Graph bestGraph = null;
        int bestTime = 999999;
        for (int time = 20340; time < 43200; time += 600) {
            graph.resetGraph();
            graph = new Graph(graph);
            Mk_II mk_ii = new Mk_II(graph, graph.getVertices().get("133"), time, "13301", 8);
            ArrayList<Edge> newR = mk_ii.start();
            if (newR != null) {
                ArrayList<Edge> route = new ArrayList<>(newR);
                if (route != null && route.size() > 0) {
                    log("Found new route for Iteration " + timeFromSeconds(time) + " | From " +
                            route.get(0).getDeparture().getName() + " " + timeFromSeconds(route.get(0).getActiveTrip().getDepartureTime()) +
                            " to " + route.get(route.size() - 1).getArrival().getName() + " " + timeFromSeconds(route.get(route.size() - 1).getActiveTrip().getArrivalTime()));
                    int thisTime = route.get(route.size() - 1).getActiveTrip().getArrivalTime() - route.get(0).getActiveTrip().getDepartureTime();
                    if (thisTime < bestTime && check(graph, route)) {
                        bestRoute = route;
                        bestGraph = graph;
                        bestTime = thisTime;
                    }
                }
            }
            else {
                log("WARNING: Found no route for Iteration " + timeFromSeconds(time));
            }
        }

        if (bestRoute != null && bestRoute.size() > 0) {
            writeCSV(bestRoute);
            printWaitingTime(bestGraph.getLongWaitingTime());
            printRoutes(bestGraph.getUnnecessaryTrips());
        }
        else {
            log("ERROR: Found no final route");
        }
        measureTime(overallTime, "Complete Calculation");
    }

    /**
     * Read and map the CSV file
     */
    private static Graph mapGraph() {
        /*
        Mapping Trips
         */
        HashMap<String, Vertex> vertexMap = new HashMap<>();
        HashMap<String, Edge> edgeMap = new HashMap<>();
        long startTime = System.currentTimeMillis();
        int rows = 0;
        int tripCount = 0;
        log("Mapping trips...");

        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(tripCSV), ';');
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
                int departureTime = Integer.parseInt(row[ABFAHRT]);
                int arrivalTime = Integer.parseInt(row[ANKUNFT]);
                String departureHSB = String.format("%05d", Integer.parseInt(row[Ab_HSTB_Nr]));
                String arrivalHSB = String.format("%05d", Integer.parseInt(row[An_HSTB_Nr]));
                String departure = row[Ab_HST_Name];
                String arrival = row[An_HST_Name];
                int length = Integer.parseInt(row[DAUER]);
                int line = Integer.parseInt(row[LINIE]);
                edge.addTrip(new Trip(departureTime, arrivalTime, departureHSB, arrivalHSB, departure, arrival, length, line));
                added += "Added Trip " + departureTime + ", ";
                tripCount++;

                edgeMap.put(edgeID, edge);
//                 log(added);
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
        HashMap<String, Transfer> transferMap = new HashMap<>();
        rows = 0;
        log("Mapping transfers...");

        csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(transferCSV), ';');
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
                if (!transferMap.containsKey(tID) || transferMap.get(tID).getTime() < Integer.parseInt(row[ZEIT])) {
                    Transfer transfer = new Transfer(tID, vhsbnr, nhsbnr, Integer.parseInt(row[ZEIT]));
                    transferMap.put(tID, transfer);
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
        return new Graph(vertexMap, edgeMap, transferMap);
    }

    private static boolean check(Graph graph, ArrayList<Edge> route) {
        Edge lastEdge = null;
        HashMap<String, Edge> allEdges = new HashMap<>(graph.getEdges());
        for (Edge edge : route) {
            if (allEdges.containsKey(edge.getId())) {
                allEdges.remove(edge.getId());
            }
            String reverseEdgeId = edge.getArrival().getId() + edge.getDeparture().getId();
            if (allEdges.containsKey(reverseEdgeId)) {
                allEdges.remove(reverseEdgeId);
            }

            if (lastEdge != null) {
                if (graph.getEdges().containsKey(edge.getId())) {
                    if (graph.getEdges().get(edge.getId()).containsTrips(edge.getActiveTrip().getDepartureTime()) && graph.getEdges().get(edge.getId()).getTrips(edge.getActiveTrip().getDepartureTime()).contains(edge.getActiveTrip())) {
                        if (lastEdge.getActiveTrip().getLine() == edge.getActiveTrip().getLine()) {
                            lastEdge = edge;
                        }
                        else {
                            Trip lastTrip = lastEdge.getActiveTrip();
                            Trip newTrip = edge.getActiveTrip();
                            String transferId = lastTrip.getArrivalHSB() + newTrip.getDepartureHSB();
                            if (graph.getTransferMap().containsKey(transferId)) {
                                int newTime = lastTrip.getArrivalTime() + graph.getTransferMap().get(transferId).getTime();
                                if (newTime <= newTrip.getDepartureTime()) {
                                    lastEdge = edge;
                                }
                                else {
                                    log("check | ERROR: Transfer time " + graph.getTransferMap().get(transferId).getTime() + " - " + lastTrip.toString() + " -> " + newTrip.toString());
                                    return false;
                                }
                            }
                            else {
                                log("check | ERROR: Transfer doesn't exist " + transferId + " - " + lastEdge.toString() + " -> " + edge.toString());
                                return false;
                            }
                        }
                    }
                    else {
                        log("check | ERROR: Trip doesn't exist " + edge.toString() + " - " + edge.getActiveTrip().toString());
                        return false;
                    }
                }
                else {
                    log("check | ERROR: Edge doesn't exist " + edge.toString());
                    return false;
                }
            }
            else {
                if (graph.getEdges().containsKey(edge.getId())) {
                    if (graph.getEdges().get(edge.getId()).containsTrips(edge.getActiveTrip().getDepartureTime()) && graph.getEdges().get(edge.getId()).getTrips(edge.getActiveTrip().getDepartureTime()).contains(edge.getActiveTrip())) {
                        lastEdge = edge;
                    }
                    else {
                        log("check | ERROR: Trip doesn't exist " + edge.toString() + " - " + edge.getActiveTrip().toString());
                        return false;
                    }
                }
                else {
                    log("check | ERROR: Edge doesn't exist " + edge.toString());
                    return false;
                }
            }
        }

        if (allEdges.size() == 0) {
            log("check | Route check successful");
            return true;
        }
        else {
            log("check | ERROR: " + allEdges.size() + " edges not visited");
            return false;
        }
    }

    private static void writeCSV(List<Edge> route){
        deleteIfExists(resultCSV);

        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(resultCSV), ';');
        } catch (IOException e) {
            e.printStackTrace();
        }
//        String[] values = {"AB_ZEIT","AN_ZEIT","AB_HST_NAME","AN_HST_NAME","AB_HSTBEREICH_NR","AN_HSTBEREICH_NR","TRANSPORTMITTEL","LINIE"}; Lehrstuhl
        String[] values = {"AB_ZEIT","AN_ZEIT","AB_HST_NAME","AN_HST_NAME","AB_HSTBEREICH_NR","AN_HSTBEREICH_NR","TRANSPORTMITTEL","LINIE","AB_X","AB_Y","AN_X","AN_Y"};
        writer.writeNext(values);

        for (Edge edge : route) {
//            log("writeCSV | Writing " + edge.toString() + " , " + edge.getActiveTrip().toString());
            /*values = new String[]{edge.getActiveTrip().getDepartureTime()/60+"", edge.getActiveTrip().getArrivalTime()/60+"", edge.getDeparture().getName(), edge.getArrival().getName(),
                                    edge.getActiveTrip().getDepartureHSB(), edge.getActiveTrip().getArrivalHSB(), "TRAM", edge.getActiveTrip().getLine()+""};*/
            values = new String[]{timeFromSeconds(edge.getActiveTrip().getDepartureTime()), timeFromSeconds(edge.getActiveTrip().getArrivalTime()), edge.getDeparture().getName(), edge.getArrival().getName(),
                                    edge.getActiveTrip().getDepartureHSB(), edge.getActiveTrip().getArrivalHSB(), "TRAM", edge.getActiveTrip().getLine()+"",
                                    edge.getDeparture().getGeoX(), edge.getDeparture().getGeoY(), edge.getArrival().getGeoX(), edge.getArrival().getGeoY()};
            writer.writeNext(values);
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printRoutes(ArrayList<List<Edge>> unnecessaryTrips) {
        deleteIfExists(unnecessaryCSV);

        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(unnecessaryCSV), ';');
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] values = {"AB_ZEIT","AN_ZEIT","AB_HST_NAME","AN_HST_NAME","TRANSPORTMITTEL","LINIE"};
        writer.writeNext(values);

        for (List<Edge> trip : unnecessaryTrips) {
            Edge lastEdge = trip.get(0);
            values = new String[] {"","","","","",""};
            writer.writeNext(values);
            values = new String[] {"Last","Trip","","","",""};
            writer.writeNext(values);
            values = new String[] {timeFromSeconds(lastEdge.getActiveTrip().getDepartureTime()),timeFromSeconds(lastEdge.getActiveTrip().getArrivalTime()),
                    lastEdge.getDeparture().getName(),lastEdge.getArrival().getName(),"TRAM",lastEdge.getActiveTrip().getLine()+""};
            writer.writeNext(values);
            values = new String[] {"Next","Trips","","","Taking:",timeFromSeconds(trip.get(trip.size()-1).getActiveTrip().getArrivalTime() - lastEdge.getActiveTrip().getDepartureTime())};
            writer.writeNext(values);

            for (int i = 1; i < trip.size(); i++) {
                lastEdge = trip.get(i);
                values = new String[]{timeFromSeconds(lastEdge.getActiveTrip().getDepartureTime()),timeFromSeconds(lastEdge.getActiveTrip().getArrivalTime()),
                        lastEdge.getDeparture().getName(),lastEdge.getArrival().getName(),"TRAM",lastEdge.getActiveTrip().getLine()+""};
                writer.writeNext(values);
            }
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printWaitingTime(ArrayList<List<Edge>> longWaitingTime) {
        deleteIfExists(waitingCSV);

        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(waitingCSV), ';');
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] values = {"AB_ZEIT","AN_ZEIT","AB_HST_NAME","AN_HST_NAME","TRANSPORTMITTEL","LINIE"};
        writer.writeNext(values);

        for (List<Edge> trip : longWaitingTime) {
            Edge lastEdge = trip.get(0);
            Edge newEdge = trip.get(1);
            values = new String[] {"","","","","",""};
            writer.writeNext(values);
            values = new String[] {"Last","Trip","","","",""};
            writer.writeNext(values);
            values = new String[] {timeFromSeconds(lastEdge.getActiveTrip().getDepartureTime()),timeFromSeconds(lastEdge.getActiveTrip().getArrivalTime()),
                    lastEdge.getDeparture().getName(),lastEdge.getArrival().getName(),"TRAM",lastEdge.getActiveTrip().getLine()+""};
            writer.writeNext(values);
            values = new String[] {"Next","Trip","","","Waiting:",timeFromSeconds(newEdge.getActiveTrip().getDepartureTime() - lastEdge.getActiveTrip().getArrivalTime())};
            writer.writeNext(values);
            values = new String[] {timeFromSeconds(newEdge.getActiveTrip().getDepartureTime()),timeFromSeconds(newEdge.getActiveTrip().getArrivalTime()),
                    newEdge.getDeparture().getName(),newEdge.getArrival().getName(),"TRAM",newEdge.getActiveTrip().getLine()+""};
            writer.writeNext(values);
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deleteIfExists(String resultCSV) {
        try{
            File fileTemp = new File(resultCSV);
            if (fileTemp.exists()){
                fileTemp.delete();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    private static void measureTime(long startTime, String task) {
        int elapsedTime = Math.round((System.currentTimeMillis() - startTime) / 1000);
        log("Finished " + task + " in " + timeFromSeconds(elapsedTime));
    }

    public static String timeFromSeconds(int seconds) {
        int minutes = (int) Math.ceil(seconds / 60);
        int hours = (int) Math.floor(minutes / 60);
        String remainingMinutes = String.format("%02d", (minutes - (hours * 60)));
        return hours + ":" + remainingMinutes + " (" + seconds + ")";
    }

    private static void log(String message) {
        System.out.println("[IM_Challenge] " + message);
    }

}
