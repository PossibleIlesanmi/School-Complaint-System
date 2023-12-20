package com.example.studentcomplaintsystem;

import com.google.firebase.database.FirebaseDatabase;
public class Complaint {
    private String id;
    private String title;
    private String category;
    private String date;
    private String description;
    private String status;  // Add status field

    // Define statuses as constants
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_RESOLVED = "Resolved";

    // Default (no-argument) constructor required for Firebase
    public Complaint() {
        // Default constructor required for Firebase
    }

    public Complaint(String title, String category, String date, String description, String status) {
        // Generate a unique ID for the complaint
        this.id = FirebaseDatabase.getInstance().getReference("complaints").push().getKey();
        this.title = title;
        this.category = category;
        this.date = date;
        this.description = description;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Add this method to get color based on the status
    public int getStatusColor() {
        switch (status) {
            case STATUS_PENDING:
                return R.color.colorPending; // Define colorPending in your resources
            case STATUS_IN_PROGRESS:
                return R.color.colorInProgress; // Define colorInProgress in your resources
            case STATUS_RESOLVED:
                return R.color.colorResolved; // Define colorResolved in your resources
            default:
                return R.color.colorDefault; // Define a default color or handle accordingly
        }
    }

    @Override
    public String toString() {
        return "Complaint{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
