package com.example.space68;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
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

        // load saved crew on app start
        Storage.getInstance().loadFromDisk(this);

        Button btnRecruit = findViewById(R.id.btnRecruit);
        Button btnQuarters = findViewById(R.id.btnQuarters);
        Button btnSimulator = findViewById(R.id.btnSimulator);
        Button btnMissionControl = findViewById(R.id.btnMissionControl);
        Button btnStatistics = findViewById(R.id.btnStatistics);

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

        Button btnCasino = findViewById(R.id.btnCasino);
        btnCasino.setOnClickListener(v ->
                startActivity(new Intent(this, CasinoActivity.class)));
    }
    @Override
    protected void onPause() {
        super.onPause();
        // auto-save when leaving the app
        Storage.getInstance().saveToDisk(this);
    }
}