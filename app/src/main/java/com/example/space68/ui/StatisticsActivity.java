package com.example.space68.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.space68.R;
import com.example.space68.data.Storage;
import com.example.space68.model.CrewMember;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        TextView textColonyStats = findViewById(R.id.textColonyStats);
        TextView textCrewStats = findViewById(R.id.textCrewStats);

        Storage storage = Storage.getInstance();

        int totalCrew = storage.getAllCrew().size();
        int totalMissions = storage.getMissionsCompleted();
        textColonyStats.setText(
                "Total crew: " + totalCrew + "\n" +
                        "Missions completed (colony): " + totalMissions
        );

        StringBuilder sb = new StringBuilder();
        for (CrewMember cm : storage.getAllCrew()) {
            sb.append(cm.getSpecialization()).append("(").append(cm.getName()).append(")\n");
            sb.append("  XP: ").append(cm.getExperience()).append("\n");
            sb.append("  Missions: ").append(cm.getMissionsCompleted())
                    .append("  Won: ").append(cm.getMissionsWon()).append("\n");
            sb.append("  Training sessions: ").append(cm.getTrainingSessions()).append("\n\n");
        }
        if (sb.length() == 0) sb.append("No crew yet.");
        textCrewStats.setText(sb.toString());
    }
}