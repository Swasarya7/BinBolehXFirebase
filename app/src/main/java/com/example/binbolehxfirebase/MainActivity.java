package com.example.binbolehxfirebase;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Read data from Firebase
        readDataFromFirebase();
    }

    private void readDataFromFirebase() {
        // Get a reference to the "data" node in your Firebase database
        DatabaseReference dataRef = mDatabase.child("data");

        // Add a listener for Firebase database changes
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    Long weight = (Long) snapshot.child("weight").getValue();

                    Log.d(TAG, "ID: " + id + ", Weight: " + weight);

                    // Print the data to the console/logcat
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
}
