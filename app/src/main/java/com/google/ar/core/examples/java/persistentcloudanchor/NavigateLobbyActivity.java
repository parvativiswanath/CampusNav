package com.google.ar.core.examples.java.persistentcloudanchor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.ar.core.examples.java.common.helpers.DisplayRotationHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NavigateLobbyActivity extends AppCompatActivity {

    private static final String TAG = "NavigateLobbyActivity";
    private DisplayRotationHelper displayRotationHelper;

    private final List<AnchorItem> firebaseAnchors = new ArrayList<>();

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

        loadAnchorsFromFirebase();
    }

    private void onFindPathButtonPress() {
        if (firebaseAnchors.isEmpty()) {
            Toast.makeText(this, "Please wait, loading anchors", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Find path button pressed");
        EditText start = (EditText) findViewById(R.id.anchor_start);
        EditText dest = (EditText) findViewById(R.id.anchor_dest);
        NavigationManager findpath = new NavigationManager(start.getText().toString(), dest.getText().toString(), firebaseAnchors);
        //change to class member so as to access it in visualisation part
        List<String> path = findpath.findShortestPath();
        String pathToPrint = path.toString();
        Log.d("Navigation Result", pathToPrint);

    }

    public void loadAnchorsFromFirebase() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("myanchors");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AnchorItem anchor = snapshot.getValue(AnchorItem.class);
                    Log.d(TAG, "firebase values edges: " + anchor);
                    if (anchor != null) {
                        firebaseAnchors.add(anchor);
                    }
                }
                Log.d(TAG, "loaded " + firebaseAnchors.size() + " anchors from firebase");
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                //firebase listener not working
                Log.d(TAG, "Firebase Listener Error");
            }
        });
    }
}
