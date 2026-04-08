package com.example.space68.data;

import com.example.space68.model.CrewMember;
import com.example.space68.model.Threat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
}