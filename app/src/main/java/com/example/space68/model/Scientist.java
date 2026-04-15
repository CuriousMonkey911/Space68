package com.example.space68.model;

public class Scientist extends CrewMember {
    public Scientist(String name) {
        super(name, "Scientist", 8, 1, 17);
    }

    @Override
    public int atkAgainst(Threat threat) {
        if (threat.getName().equals("Solar Flare")) {
            return atk() + 3; // scientists understand radiation
        }
        return atk();
    }
}