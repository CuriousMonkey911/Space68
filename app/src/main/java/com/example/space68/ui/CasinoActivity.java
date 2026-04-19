package com.example.space68.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.space68.R;
import com.example.space68.data.CasinoManager;
import com.example.space68.data.Location;
import com.example.space68.data.Storage;
import com.example.space68.model.CrewMember;

import java.util.List;

public class CasinoActivity extends AppCompatActivity {

    private CrewAdapter adapter;
    private TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casino);

        RecyclerView recycler = findViewById(R.id.recyclerCrew);
        CardView btnGamble = findViewById(R.id.btnGamble);
        CardView btnToQuarters = findViewById(R.id.btnToQuarters);
        textResult = findViewById(R.id.textResult);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CrewAdapter(Storage.getInstance().getCrewByLocation(Location.CASINO));
        recycler.setAdapter(adapter);

        btnGamble.setOnClickListener(v -> gambleSelected());
        btnToQuarters.setOnClickListener(v -> moveSelectedToQuarters());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        List<CrewMember> crew = Storage.getInstance().getCrewByLocation(Location.CASINO);
        adapter.updateData(crew);
    }

    private void gambleSelected() {
        List<CrewMember> selected = adapter.getSelectedCrew();
        if (selected.isEmpty()) {
            Toast.makeText(this, "Select crew first", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (CrewMember cm : selected) {
            CasinoManager.Result result = CasinoManager.gamble(cm);
            sb.append(cm.getName()).append(": ");
            switch (result) {
                case WIN: sb.append("DOUBLED XP! Now ").append(cm.getExperience()).append("\n"); break;
                case LOSE: sb.append("LOST HALF! Now ").append(cm.getExperience()).append("\n"); break;
                case NO_XP: sb.append("no XP to gamble\n"); break;
            }
        }
        textResult.setText(sb.toString());
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
        refreshList();
    }
}