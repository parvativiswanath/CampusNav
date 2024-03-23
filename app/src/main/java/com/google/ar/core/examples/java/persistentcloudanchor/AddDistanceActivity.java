package com.google.ar.core.examples.java.persistentcloudanchor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.ar.core.examples.java.common.helpers.DisplayRotationHelper;

public class AddDistanceActivity extends AppCompatActivity {
    private static final String TAG = "NavigateLobbyActivity";
    private DisplayRotationHelper displayRotationHelper;
    static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, AddDistanceActivity.class);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_distance_lobby);
        displayRotationHelper = new DisplayRotationHelper(this);
        MaterialButton updateButton = findViewById(R.id.find_path_button);
        updateButton.setOnClickListener((view) -> onUpdateButtonPress());
    }

    private void onUpdateButtonPress() {
        Log.d(TAG, "Update button pressed");
    }
}
