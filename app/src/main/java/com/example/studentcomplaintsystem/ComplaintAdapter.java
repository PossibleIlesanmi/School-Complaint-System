package com.example.studentcomplaintsystem;

import static com.example.studentcomplaintsystem.Complaint.STATUS_IN_PROGRESS;
import static com.example.studentcomplaintsystem.Complaint.STATUS_PENDING;
import static com.example.studentcomplaintsystem.Complaint.STATUS_RESOLVED;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// ComplaintAdapter.java



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ViewHolder> {
    private List<Complaint> complaints;
    private final OnItemClickListener clickListener;
    private final Context context;  // Add this variable

    public interface OnItemClickListener {
        void onItemClick(String complaintId);
    }

    public ComplaintAdapter(Context context, List<Complaint> complaints, OnItemClickListener clickListener) {
        this.complaints = complaints;
        this.clickListener = clickListener;
        this.context = context;  // Store the context
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complaint, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Complaint currentComplaint = complaints.get(position);

        holder.textListName.setText(currentComplaint.getTitle());
        holder.statusTextView.setText("Status: " + currentComplaint.getStatus());

        // Set background color based on status
        int statusColor = getStatusColor(currentComplaint.getStatus());
        holder.materialCardView.setCardBackgroundColor(ContextCompat.getColor(context, statusColor));

        // Set click listener for the item
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(currentComplaint.getId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return complaints.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textListName;
        private final TextView statusTextView;
        private final MaterialCardView materialCardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textListName = itemView.findViewById(R.id.textListName);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            materialCardView = itemView.findViewById(R.id.materialCardView);
        }
    }

    private int getStatusColor(String status) {
        Log.d("ComplaintAdapter", "Status: " + status);

        switch (status) {
            case STATUS_PENDING:
                Log.d("ComplaintAdapter", "Color: Pending");
                return R.color.colorPending; // Define colorPending in your resources
            case STATUS_IN_PROGRESS:
                Log.d("ComplaintAdapter", "Color: In Progress");
                return R.color.colorInProgress; // Define colorInProgress in your resources
            case STATUS_RESOLVED:
                Log.d("ComplaintAdapter", "Color: Resolved");
                return R.color.colorResolved; // Define colorResolved in your resources
            default:
                Log.d("ComplaintAdapter", "Color: Default");
                return R.color.colorDefault; // Define a default color or handle accordingly
        }
    }

}

