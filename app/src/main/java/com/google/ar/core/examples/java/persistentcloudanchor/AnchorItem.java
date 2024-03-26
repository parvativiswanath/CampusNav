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

  public HashMap<String, Object> asMap() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("anchorId", anchorId);
    map.put("anchorName", anchorName);
    map.put("minutesSinceCreation", minutesSinceCreation);
    map.put("edges", edges);

    return map;
  }

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
