package com.google.ar.core.examples.java.persistentcloudanchor;

import static com.google.ar.core.examples.java.persistentcloudanchor.ResolveAnchorsLobbyActivity.retrieveStoredAnchors;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import android.content.ContextWrapper;

import java.util.List;

public class DistanceController {
    private Context context;

    public DistanceController(Context context) {
        this.context = context;
    }

    private SharedPreferences sharedPreferences;

    public void DistanceSave(String SourceName, String DestName, Float distance){
        // retrieveing the stored anchors
        sharedPreferences =
                context.getSharedPreferences(CloudAnchorActivity.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        List<AnchorItem> anchors = retrieveStoredAnchors(sharedPreferences);

        for(AnchorItem anchor: anchors) {
            if (anchor.getAnchorName().equals(SourceName)) {
                //ithenthannn brain not workinggggg
                //call a fn that updates the shit
                anchor.DistanceUpdate(DestName, distance);
            }
            if (anchor.getAnchorName().equals(DestName)){
                anchor.DistanceUpdate(SourceName, distance);
            }
            setPreferences(anchors);
        }
    }
    public void setPreferences(List<AnchorItem> anchors){
        //String hostedAnchorId = sharedPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_DISTANCES, "");
        //connecting to the shared preferences file that has the data
        //not sure about the context thing
        sharedPreferences =
                context.getSharedPreferences(CloudAnchorActivity.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        StringBuilder hostedAnchorIdsBuilder = new StringBuilder();
        // travering each array and appending it to a json string, hopefully this makes navigation easier
        //look at pieces for the how to load part
        for(AnchorItem anchor : anchors){
            Gson gson = new Gson();
            String jSonString = gson.toJson(anchor.getEdges());
            hostedAnchorIdsBuilder.append(jSonString).append(";");
        }

        String hostedAnchorIds = hostedAnchorIdsBuilder.toString();
        //removing the last semicolon
        if (!hostedAnchorIds.isEmpty()) {
            hostedAnchorIds = hostedAnchorIds.substring(0, hostedAnchorIds.length() - 1);
        }
        //Finalllly, setting the string in the shared prefs
        sharedPreferences.edit().putString(CloudAnchorActivity.HOSTED_ANCHOR_DISTANCES, hostedAnchorIds).apply();


    }


}
