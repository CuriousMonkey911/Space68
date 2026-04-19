package com.example.space68.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.space68.R;
import com.example.space68.model.CrewMember;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewViewHolder> {

    private List<CrewMember> crewList;
    private Set<Integer> selectedPositions = new HashSet<>();

    public CrewAdapter(List<CrewMember> crewList) {
        this.crewList = crewList;
    }

    // creates a new row view (called when RecyclerView needs a new row)
    @NonNull
    @Override
    public CrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_crew, parent, false);
        return new CrewViewHolder(view);
    }

    // fills a row with data for a specific crew member
    @Override
    public void onBindViewHolder(@NonNull CrewViewHolder holder, int position) {
        CrewMember cm = crewList.get(position);
        holder.textName.setText(cm.getName() + " (" + cm.getSpecialization() + ")");
        holder.textStats.setText(
                "Skill: " + cm.getSkillPower() +
                        "  Res: " + cm.getResilience() +
                        "  XP: " + cm.getExperience() +
                        "  Energy: " + cm.getEnergy() + "/" + cm.getMaxEnergy()
        );

        androidx.cardview.widget.CardView card = (androidx.cardview.widget.CardView) holder.itemView;
        card.setCardBackgroundColor(
                selectedPositions.contains(position) ? 0xFF2D1B6B : 0xFF13132A
        );

        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (selectedPositions.contains(pos)) {
                selectedPositions.remove(pos);
            } else {
                selectedPositions.add(pos);
            }
            notifyItemChanged(pos);
        });
    }

    @Override
    public int getItemCount() {
        return crewList.size();
    }

    // returns the crew members the user has tapped/selected
    public List<CrewMember> getSelectedCrew() {
        List<CrewMember> selected = new java.util.ArrayList<>();
        for (Integer pos : selectedPositions) {
            selected.add(crewList.get(pos));
        }
        return selected;
    }

    public void clearSelection() {
        selectedPositions.clear();
        notifyDataSetChanged();
    }

    // refreshes the list (called when data changes)
    public void updateData(List<CrewMember> newList) {
        this.crewList = newList;
        selectedPositions.clear();
        notifyDataSetChanged();
    }

    // holds references to the views inside one row
    static class CrewViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textStats;

        CrewViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textStats = itemView.findViewById(R.id.textStats);
        }
    }
}