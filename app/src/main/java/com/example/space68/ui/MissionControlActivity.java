package com.example.space68.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.space68.R;
import com.example.space68.data.Location;
import com.example.space68.data.MissionManager;
import com.example.space68.data.Storage;
import com.example.space68.model.CrewMember;
import com.example.space68.model.Threat;

import java.util.List;

public class MissionControlActivity extends AppCompatActivity {

    private CrewAdapter adapter;
    private MissionManager mission;

    private LinearLayout selectionPanel, combatPanel;
    private TextView textThreat, textCrewA, textCrewB, textCurrentTurn, textLog;
    private ScrollView scrollLog;
    private CardView btnAttack, btnDefend, btnSpecial, btnEndMission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_control);

        RecyclerView recycler = findViewById(R.id.recyclerCrew);
        CardView btnLaunch = findViewById(R.id.btnLaunch);
        CardView btnToQuarters = findViewById(R.id.btnToQuarters);
        selectionPanel = findViewById(R.id.selectionPanel);

        combatPanel = findViewById(R.id.combatPanel);
        textThreat = findViewById(R.id.textThreat);
        textCrewA = findViewById(R.id.textCrewA);
        textCrewB = findViewById(R.id.textCrewB);
        textCurrentTurn = findViewById(R.id.textCurrentTurn);
        textLog = findViewById(R.id.textLog);
        scrollLog = findViewById(R.id.scrollLog);
        btnAttack = findViewById(R.id.btnAttack);
        btnDefend = findViewById(R.id.btnDefend);
        btnSpecial = findViewById(R.id.btnSpecial);
        btnEndMission = findViewById(R.id.btnEndMission);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CrewAdapter(Storage.getInstance().getCrewByLocation(Location.MISSION_CONTROL));
        recycler.setAdapter(adapter);

        btnLaunch.setOnClickListener(v -> launchMission());
        btnToQuarters.setOnClickListener(v -> moveSelectedToQuarters());
        btnAttack.setOnClickListener(v -> doAction(MissionManager.Action.ATTACK));
        btnDefend.setOnClickListener(v -> doAction(MissionManager.Action.DEFEND));
        btnSpecial.setOnClickListener(v -> doAction(MissionManager.Action.SPECIAL));
        btnEndMission.setOnClickListener(v -> endMission());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        List<CrewMember> crew = Storage.getInstance().getCrewByLocation(Location.MISSION_CONTROL);
        adapter.updateData(crew);
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

    private void launchMission() {
        List<CrewMember> selected = adapter.getSelectedCrew();
        if (selected.size() != 2) {
            Toast.makeText(this, "Select exactly 2 crew", Toast.LENGTH_SHORT).show();
            return;
        }
        Threat threat = Storage.getInstance().generateThreat();
        mission = new MissionManager(selected.get(0), selected.get(1), threat);

        selectionPanel.setVisibility(View.GONE);
        combatPanel.setVisibility(View.VISIBLE);
        btnEndMission.setVisibility(View.GONE);
        showActionButtons(true);

        textLog.setText("=== MISSION: " + threat.getName() + " ===\n\n");
        updateCombatUI();
    }

    private void doAction(MissionManager.Action action) {
        String result = mission.processAction(action);
        textLog.append(result + "\n");
        scrollLog.post(() -> scrollLog.fullScroll(View.FOCUS_DOWN));
        updateCombatUI();

        if (mission.getState() != MissionManager.MissionState.IN_PROGRESS) {
            showActionButtons(false);
            btnEndMission.setVisibility(View.VISIBLE);
            handleMissionEnd();
        }
    }

    private void updateCombatUI() {
        Threat t = mission.getThreat();
        CrewMember a = mission.getCrewA();
        CrewMember b = mission.getCrewB();

        textThreat.setText("THREAT: " + t.getName() + "  energy: " + t.getEnergy() + "/" + t.getMaxEnergy());
        textCrewA.setText(a.getSpecialization() + "(" + a.getName() + ")  skill " + a.getSkillPower() + "  energy " + a.getEnergy() + "/" + a.getMaxEnergy());
        textCrewB.setText(b.getSpecialization() + "(" + b.getName() + ")  skill " + b.getSkillPower() + "  energy " + b.getEnergy() + "/" + b.getMaxEnergy());

        CrewMember current = mission.getCurrentCrew();
        if (current != null) {
            textCurrentTurn.setText("Round " + mission.getRound() + " — " + current.getName() + "'s turn");
        } else {
            textCurrentTurn.setText("");
        }
    }

    private void handleMissionEnd() {
        if (mission.getState() == MissionManager.MissionState.VICTORY) {
            Storage.getInstance().incrementMissionsCompleted();
        }
        if (!mission.getCrewA().isAlive()) {
            Storage.getInstance().sendToMedbay(mission.getCrewA());
        }
        if (!mission.getCrewB().isAlive()) {
            Storage.getInstance().sendToMedbay(mission.getCrewB());
        }
    }

    private void showActionButtons(boolean show) {
        int v = show ? View.VISIBLE : View.GONE;
        btnAttack.setVisibility(v);
        btnDefend.setVisibility(v);
        btnSpecial.setVisibility(v);
    }

    private void endMission() {
        combatPanel.setVisibility(View.GONE);
        selectionPanel.setVisibility(View.VISIBLE);
        mission = null;
        refreshList();
    }
}