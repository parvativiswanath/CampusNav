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
import com.google.ar.core.Anchor;
import com.google.ar.core.examples.java.common.helpers.DisplayRotationHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddDistanceActivity extends AppCompatActivity {
    private static final String TAG = "AddDistanceActivity";
    private DistanceController distanceController = new DistanceController(this);;
    private DisplayRotationHelper displayRotationHelper;

    private final List<AnchorItem> firebaseAnchors = new ArrayList<>();

    static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, AddDistanceActivity.class);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_distance_lobby);
        displayRotationHelper = new DisplayRotationHelper(this);
        MaterialButton updateButton = findViewById(R.id.find_path_button);
        updateButton.setOnClickListener((view) -> onUpdateButtonPress());
        MaterialButton updateFirebaseButton = findViewById(R.id.find_path_button3);
        updateFirebaseButton.setOnClickListener((view) -> onUpdateFirebaseButtonPress());
    }

    private void onUpdateButtonPress() {
        float distance;
        Log.d(TAG, "Update button pressed");

        EditText anchor1 = (EditText) findViewById(R.id.anchor1);
        EditText anchor2 = (EditText) findViewById(R.id.anchor2);
        EditText dist = (EditText) findViewById(R.id.dist);
        if (dist.getText().toString().trim().isEmpty()){
            distance= 1f;
        }
        else{
            distance = Float.parseFloat(dist.getText().toString());
        }

        //Get data entered by the user
        String anchor1Name = anchor1.getText().toString();
        String anchor2Name = anchor2.getText().toString();
        int res = distanceController.DistanceSave(anchor1Name, anchor2Name, distance);

        // Clear the text fields
        anchor1.setText("");
        anchor2.setText("");
        assert dist != null;
        dist.setText("");

        // Show a notification message
        String successMessage = "Distance updated";
        String errorMessage = "Please enter correct anchor names and retry";
        if(res==1)
            Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();

        distanceController.printDistances(anchor1Name);
        distanceController.printDistances(anchor2Name);
    }

    private void onUpdateFirebaseButtonPress() {
        Log.d("firebase", "Update with Firebase button pressed");

        EditText anchor1 = (EditText) findViewById(R.id.anchor1);
        EditText anchor2 = (EditText) findViewById(R.id.anchor2);
        EditText dist = (EditText) findViewById(R.id.dist);

        //Get data entered by the user
        String anchor1Name = anchor1.getText().toString();
        String anchor2Name = anchor2.getText().toString();
        float distance = Float.parseFloat(dist.getText().toString());
        loadAnchorsFromFirebase(anchor1Name,anchor2Name,distance);

        // Clear the text fields
        anchor1.setText("");
        anchor2.setText("");
        dist.setText("");
    }
    public void loadAnchorsFromFirebase(String anchor1Name, String anchor2Name, float distance) {
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

                int res = distanceController.DistanceSaveWithFirebase(anchor1Name, anchor2Name, distance, firebaseAnchors);

                // Show a notification message
                String successMessage = "Distance updated";
                String errorMessage = "Please enter correct anchor names and retry";
                if(res==1)
                    Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                //firebase listener not working
                Log.d(TAG, "Firebase Listener Error");
            }
        });
    }
}
