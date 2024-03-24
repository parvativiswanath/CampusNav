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

import static com.google.ar.core.examples.java.persistentcloudanchor.ResolveAnchorsLobbyActivity.retrieveStoredAnchors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.ar.core.examples.java.common.helpers.DisplayRotationHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/** Main Navigation Activity for the Persistent Cloud Anchor Sample. */
public class MainLobbyActivity extends AppCompatActivity {

  private static final String TAG = "MainLobbyActivity";
  private DisplayRotationHelper displayRotationHelper;
  private SharedPreferences anchorPreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FirebaseApp.initializeApp(this);
    setContentView(R.layout.main_lobby);
    displayRotationHelper = new DisplayRotationHelper(this);
    MaterialButton hostButton = findViewById(R.id.host_button);
    hostButton.setOnClickListener((view) -> onHostButtonPress());
    MaterialButton addDistanceButton = findViewById(R.id.get_distance_text);
    addDistanceButton.setOnClickListener((view) -> onAddDistanceButtonPress());
    Button firebaseButton = findViewById(R.id.firebase_button_text);
    firebaseButton.setOnClickListener((view) -> onFirebaseButtonPress());
    MaterialButton resolveButton = findViewById(R.id.begin_resolve_button);
    resolveButton.setOnClickListener((view) -> onResolveButtonPress());
    MaterialButton navigateButton = findViewById(R.id.begin_navigate_button);
    navigateButton.setOnClickListener((view) -> onNavigateButtonPress());
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

  private void onHostButtonPress() {
    Intent intent = CloudAnchorActivity.newHostingIntent(this);
    startActivity(intent);
  }

  private void onAddDistanceButtonPress() {
    Intent intent = AddDistanceActivity.newIntent(this);
    startActivity(intent);
  }

  /** Callback function invoked when the Resolve Button is pressed. */
  private void onResolveButtonPress() {
    Intent intent = ResolveAnchorsLobbyActivity.newIntent(this);
    startActivity(intent);
  }

  private void onNavigateButtonPress() {
    Log.d(TAG, "Navigate button pressed");
    Intent intent = NavigateLobbyActivity.newIntent(this);
    startActivity(intent);
  }

  private void onFirebaseButtonPress() {

    anchorPreferences = this.getSharedPreferences(CloudAnchorActivity.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
    //List<AnchorItem> anchors = new ArrayList<>();

    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

    String hostedAnchorIds = anchorPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_IDS, "");
    String hostedAnchorNames =
            anchorPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_NAMES, "");
    String hostedAnchorMinutes =
            anchorPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_MINUTES, "");
    String hostedAnchorEdges = anchorPreferences.getString(CloudAnchorActivity.HOSTED_ANCHOR_DISTANCES, "");
    if (!hostedAnchorIds.isEmpty()) {
      String[] anchorIds = hostedAnchorIds.split(";", -1);
      String[] anchorNames = hostedAnchorNames.split(";", -1);
      String[] anchorMinutes = hostedAnchorMinutes.split(";", -1);
      String[] jsonStrings = hostedAnchorEdges.split(";");
      for (int i = 0; i < anchorIds.length - 1; i++) {
        long timeSinceCreation =
                TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis())
                        - Long.parseLong(anchorMinutes[i]);
        if (timeSinceCreation < 24 * 60) {
          Gson gson = new Gson();
          Map<String, Float> edges = gson.fromJson(jsonStrings[i], new TypeToken<Map<String, Float>>() {}.getType());
          AnchorItem anchor = new AnchorItem(anchorIds[i], anchorNames[i], timeSinceCreation);
          anchor.setEdges(edges);
          databaseRef.child("myanchors").child(anchorIds[i]).setValue(anchor);
        }
      }
    }
  }
}
