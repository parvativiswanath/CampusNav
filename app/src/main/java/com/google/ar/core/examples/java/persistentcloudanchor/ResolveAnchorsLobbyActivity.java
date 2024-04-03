/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ar.core.examples.java.persistentcloudanchor;

import static com.google.ar.core.examples.java.persistentcloudanchor.AnchorDataStore.getDataFromSharedPreferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.ar.core.examples.java.common.helpers.DisplayRotationHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/** Lobby activity for resolving anchors in the Persistent Cloud Anchor Sample. */
public class ResolveAnchorsLobbyActivity extends AppCompatActivity {
  private Spinner spinner;
  private SharedPreferences sharedPreferences;
  List<AnchorItem> selectedAnchors;

//  public static List<AnchorItem> retrieveStoredAnchors(SharedPreferences anchorPreferences) {
//    List<AnchorItem> anchors = new ArrayList<>();
//    String hostedAnchorIds = anchorPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_IDS, "");
//    String hostedAnchorDistances = anchorPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_DISTANCES,"");
//    String hostedAnchorNames =
//        anchorPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_NAMES, "");
//    String hostedAnchorMinutes =
//        anchorPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_MINUTES, "");
//    if (!hostedAnchorIds.isEmpty()) {
//      String[] anchorIds = hostedAnchorIds.split(";", -1);
//      String[] anchorNames = hostedAnchorNames.split(";", -1);
//      String[] anchorMinutes = hostedAnchorMinutes.split(";", -1);
//      String[] anchorDistances = hostedAnchorDistances.split(";");
//
//      //type handling to initiate gson -> map change
//      Type type = new TypeToken<Map<String, Float>>() {}.getType();
//      Gson gson = new Gson();
//      String gsonString;
//      for (int i = 0; i < anchorIds.length - 1; i++) {
//        long timeSinceCreation =
//            TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis())
//                - Long.parseLong(anchorMinutes[i]);
//        if (timeSinceCreation < 24 * 60) {
//          //edges = parseDistances(anchorDistances[i]);
//          gsonString = anchorDistances[i];
//          //Map<String, String> tempEdges = gson.fromJson(gsonString, type);
//          Map<String, Float> edges = gson.fromJson(gsonString, type);
//
//          // Convert String values to Float
////          for (Map.Entry<String, String> entry : tempEdges.entrySet()) {
////            edges.put(entry.getKey(), Float.parseFloat(entry.getValue()));
////          }
////          edges = gson.fromJson(gsonString, type);
//          AnchorItem anchor = new AnchorItem(anchorIds[i], anchorNames[i], timeSinceCreation);
//          anchor.setEdges(edges);
//          anchors.add(anchor);
//
//        }
//      }
//    }
//    return anchors;
//  }

//  private static Map<String, Float> parseDistances(String distancesString){
//    Map<String, Float>  edges = new HashMap<>();
//    String[] distancesArray = distancesString.split(",", -1);
//    for(String distanceEntry : distancesArray){
//      String[] parts = distanceEntry.split(":",-1);
//      String neighbor = parts[0];
//      Float distance = Float.parseFloat(parts[1]);
//      edges.put(neighbor, distance);
//    }
//
//    Type type = new TypeToken<Map<String, String>>(){}.getType();
//
//    return edges;
//  }



  private DisplayRotationHelper displayRotationHelper;
  /** Callback function invoked when the Host Button is pressed. */
  static Intent newIntent(Context packageContext) {
    return new Intent(packageContext, ResolveAnchorsLobbyActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.resolve_anchors_lobby);
    displayRotationHelper = new DisplayRotationHelper(this);
    MaterialButton resolveButton = findViewById(R.id.resolve_button);
    resolveButton.setOnClickListener((view) -> onResolveButtonPress());
    sharedPreferences =
        getSharedPreferences(CloudAnchorActivity.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
    selectedAnchors = AnchorDataStore.getDataFromSharedPreferences(sharedPreferences);
    spinner = (Spinner) findViewById(R.id.select_anchors_spinner);
    MultiSelectItem adapter = new MultiSelectItem(this, 0, selectedAnchors, spinner);
    spinner.setAdapter(adapter);
  }

  @Override
  protected void onResume() {
    super.onResume();
    displayRotationHelper.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    displayRotationHelper.onPause();
  }

  /** Callback function invoked when the Resolve Button is pressed. */
  private void onResolveButtonPress() {
    ArrayList<String> anchorsToResolve = new ArrayList<>();
    //gets the anchors tobe resolved
    // change it to the list obtained from the navigation process
    for (AnchorItem anchorItem : selectedAnchors) {
      if (anchorItem.isDestination()) {
        anchorsToResolve.add(anchorItem.getAnchorId());
      }
    }
    EditText enteredAnchorIds = (EditText) findViewById(R.id.anchor_edit_text);
    String[] idsList = enteredAnchorIds.getText().toString().trim().split(",", -1);
    for (String anchorId : idsList) {
      if (anchorId.isEmpty()) {
        continue;
      }
      anchorsToResolve.add(anchorId);
    }

    // here starts resolving
    Intent intent = CloudAnchorActivity.newResolvingIntent(this, anchorsToResolve);
    startActivity(intent);
  }
}
