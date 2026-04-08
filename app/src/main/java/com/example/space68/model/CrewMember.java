package com.example.space68.model;

import com.example.space68.data.Location;

public abstract class CrewMember {
    protected String name;
    protected String specialization;
    protected int skill;
    protected int resilience;
    protected int experience;
    protected int energy;
    protected int maxEnergy;
    protected Location location;

    public CrewMember(String name, String specialization, int skill, int resilience, int maxEnergy) {
        this.name = name;
        this.specialization = specialization;
        this.skill = skill;
        this.resilience = resilience;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
        this.experience = 0;
        this.location = Location.QUARTERS;
    }

    // act = attack, returns damage to deal
    public int atk() {
        return getSkillPower();
    }

    // defend = take damage, reduced by resilience
    public void defend(int damage) {
        int actualDamage = damage - resilience;
        if (actualDamage < 0) actualDamage = 0;
        energy -= actualDamage;
        if (energy < 0) energy = 0;
    }

    // skill power scales with experience (spec: XP=2 means skill +2)
    public int getSkillPower() {
        return skill + experience;
    }

    public void gainExperience(int amount) {
        experience += amount;
    }

    public void restoreEnergy() {
        energy = maxEnergy;
    }

    public boolean isAlive() {
        return energy > 0;
    }

    // getters
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public int getSkill() { return skill; }
    public int getResilience() { return resilience; }
    public int getExperience() { return experience; }
    public int getEnergy() { return energy; }
    public int getMaxEnergy() { return maxEnergy; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
}