package com.example.space68.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.space68.R;
import com.example.space68.data.Storage;
import com.example.space68.model.CrewMember;
import com.example.space68.model.Engineer;
import com.example.space68.model.Medic;
import com.example.space68.model.Pilot;
import com.example.space68.model.Scientist;
import com.example.space68.model.Soldier;

public class RecruitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit);

        EditText editName = findViewById(R.id.editName);
        RadioGroup radioGroup = findViewById(R.id.radioGroupSpec);
        Button btnCreate = findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
            }

            // create the right subclass based on which radio is selected
            CrewMember newMember = createCrewByRadio(radioGroup.getCheckedRadioButtonId(), name);

            Storage.getInstance().addCrewMember(newMember);

            Toast.makeText(this, name + " recruited!", Toast.LENGTH_SHORT).show();
            finish(); // close screen, back to main
        });
    }

    // returns the right CrewMember subclass for the selected radio button
    private CrewMember createCrewByRadio(int checkedId, String name) {
        if (checkedId == R.id.radioEngineer) return new Engineer(name);
        if (checkedId == R.id.radioMedic) return new Medic(name);
        if (checkedId == R.id.radioScientist) return new Scientist(name);
        if (checkedId == R.id.radioSoldier) return new Soldier(name);
        return new Pilot(name); // default
    }
}