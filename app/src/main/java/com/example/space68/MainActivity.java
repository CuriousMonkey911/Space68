package com.example.space68;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.space68.ui.QuartersActivity;
import com.example.space68.ui.SimulatorActivity;
import com.example.space68.ui.MissionControlActivity;
import com.example.space68.ui.RecruitActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRecruit = findViewById(R.id.btnRecruit);
        Button btnQuarters = findViewById(R.id.btnQuarters);
        Button btnSimulator = findViewById(R.id.btnSimulator);
        Button btnMissionControl = findViewById(R.id.btnMissionControl);

        btnRecruit.setOnClickListener(v ->
                startActivity(new Intent(this, RecruitActivity.class)));

        btnQuarters.setOnClickListener(v ->
                startActivity(new Intent(this, QuartersActivity.class)));

        btnSimulator.setOnClickListener(v ->
                startActivity(new Intent(this, SimulatorActivity.class)));

        btnMissionControl.setOnClickListener(v ->
                startActivity(new Intent(this, MissionControlActivity.class)));
    }
}