package com.example.space68.model;

public class Engineer extends CrewMember {
    public Engineer(String name) {
        super(name, "Engineer", 6, 3, 19);
    }

    @Override
    public int atkAgainst(Threat threat) {
        String n = threat.getName();
        if (n.equals("Reactor Breach") || n.equals("Plasma Leak") || n.equals("Hull Fracture")) {
            return atk() + 3; // engineers fix machines
        }
        return atk();
    }
}