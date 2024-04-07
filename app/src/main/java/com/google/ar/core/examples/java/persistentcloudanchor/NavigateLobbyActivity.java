package com.google.ar.core.examples.java.persistentcloudanchor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.stream.Collectors;

public class NavigateLobbyActivity extends AppCompatActivity {

    private static final String TAG = "NavigateLobbyActivity";
    private Spinner sourceSpinner;
    private Spinner destSpinner;
    private DisplayRotationHelper displayRotationHelper;
    private  static String start = "";
    private static String dest = "";

    private final List<AnchorItem> firebaseAnchors = new ArrayList<>();
    private List<String> filteredAnchors = new ArrayList<>();

    static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, NavigateLobbyActivity.class);
    }

    public interface OnGetDataListener {
        public void onStart();
        public void onSuccess();
        public void onFailed(DatabaseError databaseError);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigate_lobby);
        displayRotationHelper = new DisplayRotationHelper(this);
        sourceSpinner = (Spinner) findViewById(R.id.select_source);
        destSpinner = (Spinner) findViewById(R.id.select_destination);
        loadAnchorsFromFirebase(new OnGetDataListener() {
            @Override
            public void onStart() {
                Toast.makeText(NavigateLobbyActivity.this, "Please wait, loading anchors", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess() {
                Toast.makeText(NavigateLobbyActivity.this, "Load Successful", Toast.LENGTH_SHORT).show();
                MaterialButton findPathButton = findViewById(R.id.find_path_button);
                findPathButton.setOnClickListener((view) -> onFindPathButtonPress());
                for(AnchorItem anchor : firebaseAnchors){
            if (anchor.getIsDestination()){
                    filteredAnchors.add(anchor.getAnchorName());
            }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        NavigateLobbyActivity.this,
                        android.R.layout.simple_spinner_item,
                        filteredAnchors
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                sourceSpinner.setAdapter(adapter);
                sourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Call your method when source item is selected
                        NavigateLobbyActivity.start =parent.getItemAtPosition(position).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(NavigateLobbyActivity.this, "Choose a starting point", Toast.LENGTH_LONG).show();
                    }
                });



                ArrayAdapter<String> destAdapter = new ArrayAdapter<>(
                        NavigateLobbyActivity.this,
                        android.R.layout.simple_spinner_item,
                        filteredAnchors
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                destSpinner.setAdapter(destAdapter);
                destSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        NavigateLobbyActivity.dest= parent.getItemAtPosition(position).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Implement this method to handle when nothing is selected in sourceSpinner
                        Toast.makeText(NavigateLobbyActivity.this, "Choose a destination", Toast.LENGTH_LONG).show();

                    }
                });



            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                Toast.makeText(NavigateLobbyActivity.this, "error" + databaseError, Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void onFindPathButtonPress() {
        if (firebaseAnchors.isEmpty()) {
            Toast.makeText(this, "Please wait, loading anchors", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Find path button pressed");
//        EditText start = (EditText) findViewById(R.id.anchor_start);
//        EditText dest = (EditText) findViewById(R.id.anchor_dest);

        AnchorItem startAnchor = getFirebaseAnchorByName(start);
        if (startAnchor == null) {
            Toast.makeText(this, "Invalid start anchor name", Toast.LENGTH_LONG).show();
            return;
        }

        AnchorItem destAnchor = getFirebaseAnchorByName(dest);
        if (destAnchor == null) {
            Toast.makeText(this, "Invalid destination anchor name", Toast.LENGTH_LONG).show();
            return;
        }

        NavigationManager findPath = new NavigationManager(startAnchor.anchorId, destAnchor.anchorId, firebaseAnchors);
        //change to class member so as to access it in visualisation part
        List<AnchorItem> path = findPath
                .findShortestPath()
                .stream()
                .map(this::getFirebaseAnchorById)
                .collect(Collectors.toList());

        List<String> anchorNames = path
                .stream()
                .map(anchor -> anchor.anchorName)
                .collect(Collectors.toList());
        Log.d("Navigation Result", anchorNames.toString());

        List<String> anchorIds = path
                .stream()
                .map(anchor -> anchor.anchorId)
                .collect(Collectors.toList());

        if(anchorIds.size() == 1 && anchorIds.contains(destAnchor.anchorId)){
            Toast.makeText(this, "Mapping Inadequate to perform navigation", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent = CloudAnchorActivity.newResolvingIntent(this,new ArrayList<>(anchorIds));
        startActivity(intent);

    }

    AnchorItem getFirebaseAnchorByName(String name) {
        for (AnchorItem anchor : firebaseAnchors) {
            if (anchor.anchorName.equals(name)) {
                return anchor;
            }
        }
        return null;
    }

    AnchorItem getFirebaseAnchorById(String id) {
        for (AnchorItem anchor : firebaseAnchors) {
            if (anchor.anchorId.equals(id)) {
                return anchor;
            }
        }
        return null;
    }

    public void loadAnchorsFromFirebase( final OnGetDataListener listener) {
        listener.onStart();
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
                listener.onSuccess();
                Log.d(TAG, "loaded " + firebaseAnchors.size() + " anchors from firebase");
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                //firebase listener not working
                Log.d(TAG, "Firebase Listener Error");
                listener.onFailed(databaseError);
            }
        });
    }

}
