package com.example.space68;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.space68.data.Storage;
import com.example.space68.ui.QuartersActivity;
import com.example.space68.ui.SimulatorActivity;
import com.example.space68.ui.MissionControlActivity;
import com.example.space68.ui.RecruitActivity;
import com.example.space68.ui.StatisticsActivity;
import com.example.space68.ui.CasinoActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Storage.getInstance().loadFromDisk(this);

        CardView btnRecruit = findViewById(R.id.btnRecruit);
        CardView btnQuarters = findViewById(R.id.btnQuarters);
        CardView btnSimulator = findViewById(R.id.btnSimulator);
        CardView btnMissionControl = findViewById(R.id.btnMissionControl);
        CardView btnStatistics = findViewById(R.id.btnStatistics);
        CardView btnCasino = findViewById(R.id.btnCasino);

        btnRecruit.setOnClickListener(v ->
                startActivity(new Intent(this, RecruitActivity.class)));

        btnQuarters.setOnClickListener(v ->
                startActivity(new Intent(this, QuartersActivity.class)));

        btnSimulator.setOnClickListener(v ->
                startActivity(new Intent(this, SimulatorActivity.class)));

        btnMissionControl.setOnClickListener(v ->
                startActivity(new Intent(this, MissionControlActivity.class)));

        btnStatistics.setOnClickListener(v ->
                startActivity(new Intent(this, StatisticsActivity.class)));

        btnCasino.setOnClickListener(v ->
                startActivity(new Intent(this, CasinoActivity.class)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Storage.getInstance().saveToDisk(this);
    }
}