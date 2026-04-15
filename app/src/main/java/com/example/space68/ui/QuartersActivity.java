package com.example.space68.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.space68.R;
import com.example.space68.data.Location;
import com.example.space68.data.Storage;
import com.example.space68.model.CrewMember;

import java.util.List;

public class QuartersActivity extends AppCompatActivity {

    private CrewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quarters);

        RecyclerView recycler = findViewById(R.id.recyclerCrew);
        Button btnToSim = findViewById(R.id.btnToSimulator);
        Button btnToMission = findViewById(R.id.btnToMissionControl);

        // set up RecyclerView
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CrewAdapter(Storage.getInstance().getCrewByLocation(Location.QUARTERS));
        recycler.setAdapter(adapter);

        btnToSim.setOnClickListener(v -> moveSelectedTo(Location.SIMULATOR));
        btnToMission.setOnClickListener(v -> moveSelectedTo(Location.MISSION_CONTROL));

        Button btnToCasino = findViewById(R.id.btnToCasino);
        btnToCasino.setOnClickListener(v -> moveSelectedTo(Location.CASINO));
    }

    // refresh list whenever we come back to this screen
    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        List<CrewMember> crew = Storage.getInstance().getCrewByLocation(Location.QUARTERS);
        adapter.updateData(crew);
    }

    private void moveSelectedTo(Location target) {
        List<CrewMember> selected = adapter.getSelectedCrew();
        if (selected.isEmpty()) {
            Toast.makeText(this, "Select crew first", Toast.LENGTH_SHORT).show();
            return;
        }
        for (CrewMember cm : selected) {
            Storage.getInstance().moveCrew(cm, target);
        }
        Toast.makeText(this, "Moved " + selected.size() + " crew", Toast.LENGTH_SHORT).show();
        refreshList();
    }
}