package com.google.ar.core.examples.java.persistentcloudanchor;

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
        List<AnchorItem> anchors = AnchorDataStore.getDataFromSharedPreferences(sharedPreferences);

        for (AnchorItem anchor: anchors) {
            if (anchor.getAnchorName().equals(anchorName)) {
                Log.d("PRINT", anchorName + " : " + anchor.getEdges().toString());
                break;
            }
        }
    }
    // The function that is called when the Distance Update button is pressed.
    //Gets the sourcec name and dest name and then, updates the edges
    public int DistanceSave(String SourceName, String DestName, Float distance){
        // retrieveing the stored anchors
        sharedPreferences =
                context.getSharedPreferences(CloudAnchorActivity.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
//        List<AnchorItem> anchors = ResolveAnchorsLobbyActivity.retrieveStoredAnchors(sharedPreferences);
        List<AnchorItem> anchors = AnchorDataStore.getDataFromSharedPreferences(sharedPreferences);
        String SourceId = "";
        String DestId= "";

        //Get Anchor Ids of the source and destination nodes
        for(AnchorItem anchor: anchors) {
            if (anchor.getAnchorName().equals(SourceName)) {
                SourceId = anchor.getAnchorId();
            }
            if (anchor.getAnchorName().equals(DestName)){
                DestId = anchor.getAnchorId();
            }
        }
        if (SourceId=="" || DestId=="")
            return 0;
        //Update the edges
        for(AnchorItem anchor: anchors) {
            if (anchor.getAnchorName().equals(SourceName)) {
                //ithenthannn brain not workinggggg
                //call a fn that updates the shit
                anchor.DistanceUpdate(DestId, distance);
            }
            if (anchor.getAnchorName().equals(DestName)){
                anchor.DistanceUpdate(SourceId, distance);
            }

        }
        AnchorDataStore.storeToSharedPreferences(anchors, sharedPreferences);
        return 1;
    }

//    public void setPreferences(List<AnchorItem> anchors){
//        //String hostedAnchorId = sharedPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_DISTANCES, "");
//        //connecting to the shared preferences file that has the data
//        //not sure about the context thing
//        sharedPreferences =
//                context.getSharedPreferences(CloudAnchorActivity.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
//        String hostedAnchorIds = "";
//        String hostedAnchorNames = "";
//
//        StringBuilder hostedAnchorIdsBuilder = new StringBuilder();
//        // travering each array and appending it to a json string, hopefully this makes navigation easier
//        //look at pieces for the how to load part
//        for(AnchorItem anchor : anchors){
//            Gson gson = new Gson();
//            String jSonString = gson.toJson(anchor.getEdges());
//            hostedAnchorIdsBuilder.append(jSonString).append(";");
//            hostedAnchorIds += anchor.getAnchorId() + ";" ;
//            hostedAnchorNames += anchor.getAnchorName() + ";" ;
//
//
//
//        }
//
//        String hostedAnchorId = hostedAnchorIdsBuilder.toString();
//        //removing the last semicolon
//        if (!hostedAnchorId.isEmpty()) {
//            hostedAnchorId = hostedAnchorId.substring(0, hostedAnchorId.length() - 1);
//        }
//        //Finalllly, setting the string in the shared prefs
//        sharedPreferences.edit().putString(CloudAnchorActivity.HOSTED_ANCHOR_DISTANCES, hostedAnchorId).apply();
//        sharedPreferences.edit().putString(CloudAnchorActivity.HOSTED_ANCHOR_IDS, hostedAnchorIds).apply();
//        sharedPreferences.edit().putString(CloudAnchorActivity.HOSTED_ANCHOR_NAMES, hostedAnchorNames).apply();
////        sharedPreferences.edit().putString(CloudAnchorActivity.HOSTED_ANCHOR_MINUTES, hostedAnchorMinutes).apply();
//        String toprint = sharedPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_DISTANCES,"DEFYOUFUNYN");
//        Log.d(TAG,toprint);
//        String toprint1 = sharedPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_IDS,"ids kittila");
//        Log.d(TAG,"ids :" + toprint1);
//        String toprint2 = sharedPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_NAMES,"names kittila");
//        Log.d(TAG,"names :" + toprint2);
//
//
//    }


}
