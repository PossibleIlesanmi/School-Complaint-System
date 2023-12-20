package com.example.studentcomplaintsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ComplaintAdapter complaintAdapter;
    private DatabaseReference databaseReference;
    private FloatingActionButton fabSubmitComplaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize Firebase authentication
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Check if the user is authenticated
        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Update the path to use the provided Firebase Realtime Database URL
            databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://angelachioma-4f431-default-rtdb.firebaseio.com/complaints");

            // Set up RecyclerView
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Set up FloatingActionButton
            fabSubmitComplaint = findViewById(R.id.fabSubmitComplaint);
            fabSubmitComplaint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle FloatingActionButton click to open SubmitComplaintActivity
                    Intent intent = new Intent(DashboardActivity.this, SubmitComplaintActivity.class);
                    startActivity(intent);
                }
            });

            // Fetch and display complaints from the database
            fetchComplaintsFromDatabase();
        }
    }

    private void fetchComplaintsFromDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Complaint> complaints = new ArrayList<>();
                for (DataSnapshot complaintSnapshot : dataSnapshot.getChildren()) {
                    Complaint complaint = complaintSnapshot.getValue(Complaint.class);
                    if (complaint != null) {
                        complaints.add(complaint);
                    }
                }

                Log.d("DashboardActivity", "Number of complaints: " + complaints.size());
                // ... Your existing code

// Inside fetchComplaintsFromDatabase method
                complaintAdapter = new ComplaintAdapter(DashboardActivity.this, complaints, new ComplaintAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(String complaintId) {
                        Log.d("DashboardActivity", "Clicked item with ID: " + complaintId);

                        // Handle item click, you can add code to show details if needed
                        Intent intent = new Intent(DashboardActivity.this, ComplaintDetailActivity.class);
                        intent.putExtra("complaintId", complaintId);
                        startActivity(intent);
                    }
                });

                // Set the adapter to the RecyclerView
                recyclerView.setAdapter(complaintAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DashboardActivity", "Error fetching data: " + databaseError.getMessage());
            }
        });
    }
}
