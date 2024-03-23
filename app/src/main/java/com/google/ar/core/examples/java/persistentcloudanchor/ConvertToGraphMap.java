package com.google.ar.core.examples.java.persistentcloudanchor;

import com.google.ar.core.Anchor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertToGraphMap {
    public static Map<String, Map<String, Float>> convertToGraphMap(List<AnchorItem> anchors){
        Map<String, Map<String,Float>> graphMap = new HashMap<>();

        for(AnchorItem anchor: anchors){
            String source = anchor.getAnchorId();
            Map<String, Float> distances = anchor.getEdges();
            graphMap.put(source, distances);
        }
        return graphMap;
    }

}
