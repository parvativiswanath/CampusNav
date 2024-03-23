package com.google.ar.core.examples.java.persistentcloudanchor;

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
import java.util.List;
import java.util.concurrent.TimeUnit;
import android.util.Log;

public class NavigateLobbyActivity extends AppCompatActivity {

    private static final String TAG = "NavigateLobbyActivity";
    private DisplayRotationHelper displayRotationHelper;

    static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, NavigateLobbyActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigate_lobby);
        displayRotationHelper = new DisplayRotationHelper(this);
        MaterialButton findPathButton = findViewById(R.id.find_path_button);
        findPathButton.setOnClickListener((view) -> onFindPathButtonPress());

    }

    private void onFindPathButtonPress() {
        Log.d(TAG, "Find path button pressed");
        EditText start = (EditText) findViewById(R.id.anchor_start);
        EditText dest = (EditText) findViewById(R.id.anchor_dest);
        //ShortestPath.findShortestPath(start,dest);
    }
}
