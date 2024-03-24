package com.google.ar.core.examples.java.persistentcloudanchor;

import static com.google.ar.core.examples.java.persistentcloudanchor.ResolveAnchorsLobbyActivity.retrieveStoredAnchors;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import android.content.ContextWrapper;
import android.util.Log;

import java.util.List;

public class DistanceController {

    private static final String TAG = "DistanceController";
    private final Context context;

    public DistanceController(Context context) {
        this.context = context;
    }

    private SharedPreferences sharedPreferences;

    public void printDistances(String anchorName) {
        sharedPreferences =
                context.getSharedPreferences(CloudAnchorActivity.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        List<AnchorItem> anchors = retrieveStoredAnchors(sharedPreferences);

        for (AnchorItem anchor: anchors) {
            if (anchor.getAnchorName().equals(anchorName)) {
                Log.d("PRINT", anchorName + " : " + anchor.getEdges().toString());
                break;
            }
        }
    }

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

        }
        setPreferences(anchors);
    }
    public void setPreferences(List<AnchorItem> anchors){
        //String hostedAnchorId = sharedPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_DISTANCES, "");
        //connecting to the shared preferences file that has the data
        //not sure about the context thing
        sharedPreferences =
                context.getSharedPreferences(CloudAnchorActivity.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        String hostedAnchorIds = "";
        String hostedAnchorNames = "";
        String hostedAnchorMinutes = "";
        StringBuilder hostedAnchorIdsBuilder = new StringBuilder();
        // travering each array and appending it to a json string, hopefully this makes navigation easier
        //look at pieces for the how to load part
        for(AnchorItem anchor : anchors){
            Gson gson = new Gson();
            String jSonString = gson.toJson(anchor.getEdges());
            hostedAnchorIdsBuilder.append(jSonString).append(";");
            hostedAnchorIds += anchor.getAnchorId() + ";" ;
            hostedAnchorNames += anchor.getAnchorName() + ";" ;
            hostedAnchorMinutes += anchor.getMinutesSinceCreation() + ";" ;


        }

        String hostedAnchorId = hostedAnchorIdsBuilder.toString();
        //removing the last semicolon
        if (!hostedAnchorId.isEmpty()) {
            hostedAnchorId = hostedAnchorId.substring(0, hostedAnchorId.length() - 1);
        }
        //Finalllly, setting the string in the shared prefs
        sharedPreferences.edit().putString(CloudAnchorActivity.HOSTED_ANCHOR_DISTANCES, hostedAnchorId).apply();
        sharedPreferences.edit().putString(CloudAnchorActivity.HOSTED_ANCHOR_IDS, hostedAnchorIds).apply();
        sharedPreferences.edit().putString(CloudAnchorActivity.HOSTED_ANCHOR_NAMES, hostedAnchorNames).apply();
        sharedPreferences.edit().putString(CloudAnchorActivity.HOSTED_ANCHOR_MINUTES, hostedAnchorMinutes).apply();
        String toprint = sharedPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_DISTANCES,"DEFYOUFUNYN");
        Log.d(TAG,toprint);
        toprint = sharedPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_IDS,"ids kittila");
        Log.d(TAG,"ids :" + toprint);
        toprint = sharedPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_NAMES,"names kittila");
        Log.d(TAG,"names :" + toprint);
        toprint = sharedPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_MINUTES,"minutes kittila");
        Log.d(TAG,"minutes :" + toprint);

    }


}
