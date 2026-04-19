package com.example.space68.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.space68.R;
import com.example.space68.data.Location;
import com.example.space68.data.Storage;
import com.example.space68.model.CrewMember;

import java.util.List;

public class SimulatorActivity extends AppCompatActivity {

    private CrewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulator);

        RecyclerView recycler = findViewById(R.id.recyclerCrew);
        CardView btnTrain = findViewById(R.id.btnTrain);
        CardView btnToQuarters = findViewById(R.id.btnToQuarters);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CrewAdapter(Storage.getInstance().getCrewByLocation(Location.SIMULATOR));
        recycler.setAdapter(adapter);

        btnTrain.setOnClickListener(v -> trainSelected());
        btnToQuarters.setOnClickListener(v -> moveSelectedToQuarters());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        List<CrewMember> crew = Storage.getInstance().getCrewByLocation(Location.SIMULATOR);
        adapter.updateData(crew);
    }

    private void trainSelected() {
        List<CrewMember> selected = adapter.getSelectedCrew();
        if (selected.isEmpty()) {
            Toast.makeText(this, "Select crew first", Toast.LENGTH_SHORT).show();
            return;
        }
        for (CrewMember cm : selected) {
            cm.gainExperience(1);
            cm.recordTraining();
        }
        Toast.makeText(this, "Trained " + selected.size() + " crew", Toast.LENGTH_SHORT).show();
        refreshList();
    }

    private void moveSelectedToQuarters() {
        List<CrewMember> selected = adapter.getSelectedCrew();
        if (selected.isEmpty()) {
            Toast.makeText(this, "Select crew first", Toast.LENGTH_SHORT).show();
            return;
        }
        for (CrewMember cm : selected) {
            Storage.getInstance().moveCrew(cm, Location.QUARTERS);
        }
        Toast.makeText(this, "Sent " + selected.size() + " to quarters", Toast.LENGTH_SHORT).show();
        refreshList();
    }
}