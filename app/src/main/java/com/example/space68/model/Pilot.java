package com.example.space68.model;

public class Pilot extends CrewMember {
    public Pilot(String name) {
        super(name, "Pilot", 5, 4, 20);
    }

    @Override
    public int atkAgainst(Threat threat) {
        if (threat.getName().equals("Asteroid Storm")) {
            return atk() + 3; // pilots dodge asteroids
        }
        return atk();
    }
}