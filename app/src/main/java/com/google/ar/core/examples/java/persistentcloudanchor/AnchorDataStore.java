package com.google.ar.core.examples.java.persistentcloudanchor;

import static com.google.ar.core.examples.java.persistentcloudanchor.CloudAnchorActivity.HOSTED_ANCHOR_DETAILS;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class AnchorDataStore {

    // anchorItem -> Json String
    // get the stored data from shared preferences ad then append new anchorItem, and then store it again

    public static void appendToExistingAnchorData(AnchorItem anchor, SharedPreferences anchorPreferences){

        // to be continued.......
        Gson gson  = new Gson();
        List<AnchorItem> anchorsList = getDataFromSharedPreferences(anchorPreferences);

        anchorsList.add(anchor);

        storeToSharedPreferences(anchorsList,anchorPreferences);


    }

    public static void storeToSharedPreferences(List<AnchorItem> anchorsList, SharedPreferences anchorPreferences) {
        Gson gson  = new Gson();
        String hostedAnchorIds = gson.toJson(anchorsList);

        anchorPreferences.edit().putString(HOSTED_ANCHOR_DETAILS, hostedAnchorIds).apply();
    }

    //Retrieve Json string from SharedPreferences and return anchors

    public static List<AnchorItem> getDataFromSharedPreferences(SharedPreferences anchorPreferences){
        Gson gson = new Gson();
        Type type = new TypeToken<List<AnchorItem>>() {}.getType();
        String hostedAnchorDetails = anchorPreferences.getString(HOSTED_ANCHOR_DETAILS, "");
        if(!hostedAnchorDetails.isEmpty())
        {
            return gson.fromJson(hostedAnchorDetails,type );
        }
        else {
            return new ArrayList<>();
            }
        }

    //updating the distance
    //public static

    public static int getNumCloudAnchors(SharedPreferences sharedPreferences) {
        return getDataFromSharedPreferences(sharedPreferences).size();
    }

}