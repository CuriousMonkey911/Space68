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
    protected int missionsCompleted;
    protected int missionsWon;
    protected int trainingSessions;

    public CrewMember(String name, String specialization, int skill, int resilience, int maxEnergy) {
        this.name = name;
        this.specialization = specialization;
        this.skill = skill;
        this.resilience = resilience;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
        this.experience = 0;
        this.location = Location.QUARTERS;
        this.missionsCompleted = 0;
        this.missionsWon = 0;
        this.trainingSessions = 0;
    }

    // act = attack, returns damage to deal
    public int atk() {
        return getSkillPower();
    }

    // damage against a specific threat — subclasses override for bonuses
    public int atkAgainst(Threat threat) {
        return atk(); // default: no bonus
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

    public void resetExperience() {
        experience = 0;
    }

    public void restoreEnergy() {
        energy = maxEnergy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public boolean isAlive() {
        return energy > 0;
    }

    // records mission outcome — called by MissionManager at end of mission
    public void recordMission(boolean won) {
        missionsCompleted++;
        if (won) missionsWon++;
    }

    // called by SimulatorActivity each time crew is trained
    public void recordTraining() {
        trainingSessions++;
    }

    public int getMissionsCompleted() { return missionsCompleted; }
    public int getMissionsWon() { return missionsWon; }
    public int getTrainingSessions() { return trainingSessions; }

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

    // serialize to a simple pipe-separated string for saving
    public String serialize() {
        return specialization + "|" + name + "|" + experience + "|" + energy
                + "|" + location.name() + "|" + missionsCompleted + "|" + missionsWon
                + "|" + trainingSessions;
    }
}
