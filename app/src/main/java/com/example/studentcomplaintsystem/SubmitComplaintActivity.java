package com.example.studentcomplaintsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// SubmitComplaintActivity.java
public class SubmitComplaintActivity extends AppCompatActivity {

    private TextInputEditText titleEditText, descriptionEditText;
    private TextInputLayout titleInputLayout, descriptionInputLayout;
    private DatabaseReference databaseReference;

    // Add this variable to store the selected category
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_complaint);

        // Initialize views
        titleInputLayout = findViewById(R.id.titleInputLayout);
        titleEditText = findViewById(R.id.titleEditText);
        descriptionInputLayout = findViewById(R.id.descriptionInputLayout);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        databaseReference = FirebaseDatabase.getInstance().getReference("complaints");

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button in the Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Set up the RadioGroup
        RadioGroup categoryRadioGroup = findViewById(R.id.categoryRadioGroup);

        // Set click listener for the Submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComplaint();
            }
        });

        // Set up click listener for RadioGroup
        categoryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onRadioButtonClicked(findViewById(checkedId));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle the back button press in the Toolbar
        onBackPressed();
        return true;
    }

    private void submitComplaint() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        // Validate inputs
        if (title.isEmpty() || description.isEmpty() || selectedCategory.isEmpty()) {
            showSnackbar("Please fill in all fields");
            return;
        }

        // Get the current date
        String currentDate = getCurrentDate();

        // Create a new Complaint object
        Complaint complaint = new Complaint(title, selectedCategory, currentDate, description, Complaint.STATUS_PENDING);

        // Save the complaint to the database
        saveComplaintToDatabase(complaint);
    }

    private String getCurrentDate() {
        // Get the current date in the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    // Implement this method to get the selected category
    private String getSelectedCategory() {
        // Return the selected category
        return selectedCategory != null ? selectedCategory : "";
    }

    // Add this method to set the selected category when a radio button is clicked
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        int id = view.getId();

        if (id == R.id.categoryGradingDisputes && checked) {
            selectedCategory = "Grading Disputes";
        } else if (id == R.id.categoryTeachingMethods && checked) {
            selectedCategory = "Ineffective Teaching Methods";
        } else if (id == R.id.categoryResources && checked) {
            selectedCategory = "Lack of Resources for Learning";
        } else if (id == R.id.categoryAdminDecisions && checked) {
            selectedCategory = "Unfair Administrative Decisions";
        } else if (id == R.id.categoryTuitionFees && checked) {
            selectedCategory = "Issues with Tuition and Fees";
        }
        // Add else-if statements for other radio buttons as needed
    }

    private void saveComplaintToDatabase(Complaint complaint) {
        // Replace this with your Firebase Realtime Database URL
        databaseReference = FirebaseDatabase.getInstance().getReference("complaints");

        // Save the complaint to the database
        databaseReference.child(complaint.getId()).setValue(complaint)
                .addOnSuccessListener(aVoid -> {
                    // Clear text fields
                    clearTextFields();

                    // Show success message
                    showSnackbar("Complaint submitted successfully");
                })
                .addOnFailureListener(e -> {
                    // Show error message
                    showSnackbar("Failed to submit complaint. Please try again.");
                });
    }

    private void clearTextFields() {
        titleEditText.setText("");
        descriptionEditText.setText("");
        // Clear any additional fields if needed
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
