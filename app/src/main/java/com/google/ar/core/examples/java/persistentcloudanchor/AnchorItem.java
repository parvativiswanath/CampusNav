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

import android.util.Log;

import com.google.ar.core.Anchor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/** Container class holding identifying information for an Anchor to be resolved. */
public class AnchorItem extends Anchor {

  public static final String TAG = "AnchorItem";
  public final String anchorId;
  public final String anchorName;
  public final long minutesSinceCreation;
  private boolean selected;
  public Map<String, Float> edges;


  public AnchorItem(String anchorId, String anchorName, long minutesSinceCreation) {
    this.anchorId = anchorId;
    this.anchorName = anchorName;
    this.minutesSinceCreation = minutesSinceCreation;
    this.selected = false;
    this.edges = new HashMap<>();
  }

  public void DistanceUpdate(String anchorId, Float distance){
    this.edges.put(anchorId, distance);
    Log.d(TAG,"Edges updated for id:"+anchorId);
    for (Map.Entry<String, Float> entry : edges.entrySet()) {
      String key = entry.getKey();
      Float value = entry.getValue();
      Log.d(TAG,"Key: " + key + ", Value: " + value);
    }
  }
  public HashMap<String, Object> asMap() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("anchorId", anchorId);
    map.put("anchorName", anchorName);
    map.put("minutesSinceCreation", minutesSinceCreation);
    map.put("edges", edges);

    return map;
  }

  public AnchorItem(DataSnapshot snapshot){
    this.edges = new HashMap<String, Float>();
    this.anchorId = snapshot.child("anchorId").getValue(String.class);
    this.anchorName = snapshot.child("anchorName").getValue(String.class);
    DataSnapshot minutesSinceCreationSnapshot = snapshot.child("minutesSinceCreation");
    if (minutesSinceCreationSnapshot.exists()) {
      this.minutesSinceCreation = minutesSinceCreationSnapshot.getValue(Integer.class);
    } else {
      // Handle the case when minutesSinceCreation is null or missing
      this.minutesSinceCreation = 0; // Or any default value that makes sense in your context
    }
    this.edges = snapshot.child("edges").getValue(new GenericTypeIndicator<Map<String, Float>>() {});
    //ERRORRRRRRRRRRRRRRR(LINE 77)
//    HashMap<String, Float> edges = new HashMap<>();
//    this.edges = new HashMap<>();
//    DataSnapshot edgesSnapshot = snapshot.child("edges");
//    if(edgesSnapshot.exists()){
//      for(DataSnapshot edgeSnapshot : edgesSnapshot.getChildren()){
//        String key = edgeSnapshot.getKey();
//        Float value = edgeSnapshot.getValue(Float.class);
//        if(key!= null && value != null){
//          this.edges.put(key,value);
//        }
//      }

    }




  public String getAnchorName() {
    return anchorName;
  }

  public void setEdges(Map<String, Float> edges) {
    this.edges = edges;
  }
  public Map<String, Float> getEdges(){return edges;}

  public String getAnchorId() {
    return anchorId;
  }

  public String getMinutesSinceCreation() {
    if (minutesSinceCreation < 60) {
      return minutesSinceCreation + "m ago";
    } else {
      return (int) minutesSinceCreation / 60 + "hr ago";
    }
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }
}
