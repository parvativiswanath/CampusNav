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

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.ar.core.examples.java.common.helpers.DisplayRotationHelper;

/** Main Navigation Activity for the Persistent Cloud Anchor Sample. */
public class MainLobbyActivity extends AppCompatActivity {

  private DisplayRotationHelper displayRotationHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FirebaseApp.initializeApp(this);
    setContentView(R.layout.main_lobby);
    displayRotationHelper = new DisplayRotationHelper(this);
    MaterialButton hostButton = findViewById(R.id.host_button);
    hostButton.setOnClickListener((view) -> onHostButtonPress());
    MaterialButton resolveButton = findViewById(R.id.begin_resolve_button);
    resolveButton.setOnClickListener((view) -> onResolveButtonPress());
    Button firebaseButton = findViewById(R.id.firebase_button_text);
    firebaseButton.setOnClickListener((view) -> onFirebaseButtonPress());
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

  /** Callback function invoked when the Resolve Button is pressed. */
  private void onResolveButtonPress() {
    Intent intent = ResolveAnchorsLobbyActivity.newIntent(this);
    startActivity(intent);
  }

  private void onFirebaseButtonPress(){
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    // Assuming you have a data model class named "User" and an instance of that class named "user"
    databaseRef.child("users").child("userId").setValue("user");
    databaseRef.setValue("Hello, World!");
  }
}
