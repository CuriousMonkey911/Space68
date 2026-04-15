package com.example.space68.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.space68.model.CrewMember;
import com.example.space68.model.Engineer;
import com.example.space68.model.Medic;
import com.example.space68.model.Pilot;
import com.example.space68.model.Scientist;
import com.example.space68.model.Soldier;
import com.example.space68.model.Threat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

// central manager for all crew and mission state
// singleton — one shared instance across the whole app
public class Storage {
    private static Storage instance;

    private HashMap<Integer, CrewMember> crewMembers;
    private int nextId;
    private int missionsCompleted;

    private Storage() {
        crewMembers = new HashMap<>();
        nextId = 1;
        missionsCompleted = 0;
    }

    // returns the shared instance, creates it on first call
    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    // adds new crew, assigns and returns a unique id
    public int addCrewMember(CrewMember member) {
        int id = nextId++;
        crewMembers.put(id, member);
        return id;
    }

    public CrewMember getCrewMember(int id) {
        return crewMembers.get(id);
    }

    public void removeCrewMember(int id) {
        crewMembers.remove(id);
    }

    // returns all crew currently in a given location (used by RecyclerViews)
    public List<CrewMember> getCrewByLocation(Location location) {
        List<CrewMember> result = new ArrayList<>();
        for (CrewMember cm : crewMembers.values()) {
            if (cm.getLocation() == location) {
                result.add(cm);
            }
        }
        return result;
    }

    public List<CrewMember> getAllCrew() {
        return new ArrayList<>(crewMembers.values());
    }

    // moves a crew member to a new location
    // returning to quarters auto-restores energy
    public void moveCrew(CrewMember member, Location newLocation) {
        member.setLocation(newLocation);
        if (newLocation == Location.QUARTERS) {
            member.restoreEnergy();
        }
    }

    // sends a defeated crew to medbay — restores energy but wipes XP as penalty
    public void sendToMedbay(CrewMember member) {
        member.setLocation(Location.MEDBAY);
        member.restoreEnergy();
        member.resetExperience();
    }

    // creates a new threat with stats scaled by missions completed
    public Threat generateThreat() {
        String[] names = {
                "Asteroid Storm", "Alien Swarm", "Solar Flare",
                "Reactor Breach", "Hull Fracture", "Plasma Leak"
        };
        Random rand = new Random();
        String name = names[rand.nextInt(names.length)];
        int skill = 4 + missionsCompleted;
        int resilience = 1 + missionsCompleted / 2;
        int energy = 20 + missionsCompleted * 3;
        return new Threat(name, skill, resilience, energy);
    }

    public int getMissionsCompleted() {
        return missionsCompleted;
    }

    public void incrementMissionsCompleted() {
        missionsCompleted++;
    }

    public void removeCrewMember(CrewMember member) {
        Integer keyToRemove = null;
        for (java.util.Map.Entry<Integer, CrewMember> entry : crewMembers.entrySet()) {
            if (entry.getValue() == member) {
                keyToRemove = entry.getKey();
                break;
            }
        }
        if (keyToRemove != null) {
            crewMembers.remove(keyToRemove);
        }
    }


// ===== save/load =====

    private static final String PREFS_NAME = "space68_save";
    private static final String KEY_CREW = "crew_data";
    private static final String KEY_MISSIONS = "missions_completed";

    public void saveToDisk(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> crewSet = new HashSet<>();
        for (CrewMember cm : crewMembers.values()) {
            crewSet.add(cm.serialize());
        }
        prefs.edit()
                .putStringSet(KEY_CREW, crewSet)
                .putInt(KEY_MISSIONS, missionsCompleted)
                .apply();
    }

    public void loadFromDisk(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> crewSet = prefs.getStringSet(KEY_CREW, new HashSet<>());
        missionsCompleted = prefs.getInt(KEY_MISSIONS, 0);
        crewMembers.clear();
        nextId = 1;
        for (String data : crewSet) {
            CrewMember cm = deserialize(data);
            if (cm != null) {
                crewMembers.put(nextId++, cm);
            }
        }
    }

    // turn saved string back into a CrewMember
    private CrewMember deserialize(String data) {
        String[] parts = data.split("\\|");
        if (parts.length < 8) return null;
        String spec = parts[0];
        String name = parts[1];
        int xp = Integer.parseInt(parts[2]);
        int energy = Integer.parseInt(parts[3]);
        Location loc = Location.valueOf(parts[4]);
        int missions = Integer.parseInt(parts[5]);
        int won = Integer.parseInt(parts[6]);
        int training = Integer.parseInt(parts[7]);

        CrewMember cm;
        switch (spec) {
            case "Engineer": cm = new Engineer(name); break;
            case "Medic": cm = new Medic(name); break;
            case "Scientist": cm = new Scientist(name); break;
            case "Soldier": cm = new Soldier(name); break;
            default: cm = new Pilot(name); break;
        }

        // restore saved state
        for (int i = 0; i < xp; i++) cm.gainExperience(1);
        cm.setEnergy(energy);
        cm.setLocation(loc);
        for (int i = 0; i < missions - won; i++) cm.recordMission(false);
        for (int i = 0; i < won; i++) cm.recordMission(true);
        for (int i = 0; i < training; i++) cm.recordTraining();

        return cm;
    }
}