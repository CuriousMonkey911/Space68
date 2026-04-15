package com.example.space68.model;

public class Soldier extends CrewMember {
    public Soldier(String name) {
        super(name, "Soldier", 9, 0, 16);
    }

    @Override
    public int atkAgainst(Threat threat) {
        if (threat.getName().equals("Alien Swarm")) {
            return atk() + 3; // soldiers crush aliens
        }
        return atk();
    }
}