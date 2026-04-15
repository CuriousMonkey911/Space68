package com.example.space68.model;

public class Medic extends CrewMember {
    public Medic(String name) {
        super(name, "Medic", 7, 2, 18);
    }

    @Override
    public int atkAgainst(Threat threat) {
        return atk() + 1; // medics are versatile, small bonus everywhere
    }
}