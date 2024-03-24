package com.google.ar.core.examples.java.persistentcloudanchor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class NavigationManager {
    // insert code load data from firebase and then store them to anchors

    // method to get the input fields of source and destination

    private Map<String, Map<String, Float>> graph;
    private String Source;
    private String Destination;

    private List<AnchorItem> anchors;


    public NavigationManager(String Source, String Destination){
        this.Source = Source;
        this.Destination = Destination;
        this.graph = convertToGraphMap(/*parameter to pass anchors that were caught from firebase*/ anchors);
    }

    public static Map<String, Map<String, Float>> convertToGraphMap(List<AnchorItem> anchors){
        Map<String, Map<String,Float>> graphMap = new HashMap<>();

        for(AnchorItem anchor: anchors){
            String source = anchor.getAnchorId();
            Map<String, Float> distances = anchor.getEdges();
            graphMap.put(source, distances);
        }
        return graphMap;
    }

    public List<String> findShortestPath(String start, String Dest){
        Map<String, Float> distances = new HashMap<>();
        Map<String, String> previousNodes = new HashMap<>();
        for (String node : graph.keySet()) {
            distances.put(node, Float.MAX_VALUE);  // gotta change this
        }
        distances.put(start, 0f);
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        queue.add(start);
        while (!queue.isEmpty()) {
            String currentNode = queue.poll();

            // Visit neighbors of the current node.
            Map<String, Float> neighbors = graph.get(currentNode);
            if (neighbors != null) {
                for (String neighbor : neighbors.keySet()) {
                    float distance = distances.get(currentNode) + neighbors.get(neighbor);
                    if (distance < distances.getOrDefault(neighbor, Float.MAX_VALUE)) {
                        distances.put(neighbor, distance);
                        previousNodes.put(neighbor, currentNode);
                        queue.add(neighbor);
                    }
                }
            }
        }

        // Reconstruct the shortest path.
        List<String> shortestPath = new ArrayList<>();
        String currentNode = Dest;
        while (currentNode != null) {
            shortestPath.add(currentNode);
            currentNode = previousNodes.get(currentNode);
        }
        Collections.reverse(shortestPath);

        return shortestPath;
    }


}

