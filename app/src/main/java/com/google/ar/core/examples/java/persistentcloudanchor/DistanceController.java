package com.google.ar.core.examples.java.persistentcloudanchor;

import static com.google.ar.core.examples.java.persistentcloudanchor.ResolveAnchorsLobbyActivity.retrieveStoredAnchors;

import android.content.SharedPreferences;

import java.util.List;

public class DistanceController {
    private List<AnchorItem> anchors;

    private SharedPreferences sharedPreferences;

    public void DistanceSave(String SourceName, String DestName, Float distance){
        // retrieveing the stored anchors
        anchors = retrieveStoredAnchors(sharedPreferences);
        AnchorItem found;
        for(AnchorItem anchor: anchors) {
            if (anchor.getAnchorName().equals(SourceName)) {
                //ithenthannn brain not workinggggg
                //call a fn that updates the shit
                anchor.DistanceUpdate(DestName, distance);
            }
            if (anchor.getAnchorName().equals(DestName)){
                anchor.DistanceUpdate(SourceName, distance);
            }

        }
    }


}
